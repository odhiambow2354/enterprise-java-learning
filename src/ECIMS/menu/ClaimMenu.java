package ECIMS.menu;

import model.Claim;
import service.ClaimService;
import utility.ClaimStatus;
import utility.ConsoleUI;
import utility.InputHelper;

import java.util.List;

/** Console UI for claim operations. */
public class ClaimMenu {

    private final ClaimService claimService;

    public ClaimMenu(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUI.printHeader("CLAIMS MANAGEMENT");
            System.out.println("  1. Lodge Claim");
            System.out.println("  2. Approve Claim");
            System.out.println("  3. Reject Claim");
            System.out.println("  4. Mark Claim as Paid");
            System.out.println("  5. Track Claim");
            System.out.println("  6. Display All Claims");
            System.out.println("  0. Back");
            ConsoleUI.printDivider();

            int choice = InputHelper.readInt("  Choice: ");
            switch (choice) {
                case 1 -> lodgeClaim();
                case 2 -> approveClaim();
                case 3 -> rejectClaim();
                case 4 -> markPaid();
                case 5 -> trackClaim();
                case 6 -> displayAllClaims();
                case 0 -> back = true;
                default -> ConsoleUI.printError("Invalid option.");
            }
        }
    }

    private void lodgeClaim() {
        ConsoleUI.printSubHeader("LODGE CLAIM");
        String policyNumber = InputHelper.readString("  Policy Number : ");
        String description  = InputHelper.readString("  Description   : ");
        double amount       = InputHelper.readDouble("  Claimed Amount (KES): ");

        try {
            Claim c = claimService.lodgeClaim(policyNumber, description, amount);
            ConsoleUI.printSuccess("Claim lodged. ID: " + c.getClaimId());
        } catch (Exception e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void approveClaim() {
        ConsoleUI.printSubHeader("APPROVE CLAIM");
        String claimId = InputHelper.readString("  Claim ID : ");
        String notes   = InputHelper.readString("  Notes    : ");
        try {
            claimService.approveClaim(claimId, notes);
            ConsoleUI.printSuccess("Claim " + claimId + " approved.");
        } catch (Exception e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void rejectClaim() {
        ConsoleUI.printSubHeader("REJECT CLAIM");
        String claimId = InputHelper.readString("  Claim ID : ");
        String reason  = InputHelper.readString("  Reason   : ");
        try {
            claimService.rejectClaim(claimId, reason);
            ConsoleUI.printSuccess("Claim " + claimId + " rejected.");
        } catch (Exception e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void markPaid() {
        ConsoleUI.printSubHeader("MARK CLAIM PAID");
        String claimId = InputHelper.readString("  Claim ID : ");
        try {
            claimService.markClaimPaid(claimId);
            ConsoleUI.printSuccess("Claim " + claimId + " marked as PAID.");
        } catch (Exception e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void trackClaim() {
        ConsoleUI.printSubHeader("TRACK CLAIM");
        String claimId = InputHelper.readString("  Claim ID : ");
        try {
            Claim c = claimService.getClaim(claimId);
            printClaimDetail(c);
        } catch (Exception e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void displayAllClaims() {
        List<Claim> claims = claimService.getAllClaims();
        ConsoleUI.printSubHeader("ALL CLAIMS (" + claims.size() + ")");
        printClaimTable(claims);
        InputHelper.pressEnterToContinue();
    }

    private void printClaimTable(List<Claim> list) {
        if (list.isEmpty()) { ConsoleUI.printInfo("No claims found."); return; }
        System.out.println();
        System.out.printf("| %-10s | %-10s | %12s | %-14s | %-12s |%n",
                "Claim ID", "Policy", "Amount", "Status", "Date");
        System.out.println("-".repeat(70));
        list.forEach(System.out::println);
    }

    private void printClaimDetail(Claim c) {
        System.out.println();
        System.out.println("  Claim ID      : " + c.getClaimId());
        System.out.println("  Policy No     : " + c.getPolicyNumber());
        System.out.println("  Customer ID   : " + c.getCustomerId());
        System.out.println("  Description   : " + c.getDescription());
        System.out.printf( "  Claimed       : KES %.2f%n", c.getClaimedAmount());
        System.out.println("  Lodged On     : " + c.getLodgeDate());
        System.out.println("  Status        : " + c.getStatus());
        if (c.getReviewNotes() != null) {
            System.out.println("  Review Notes  : " + c.getReviewNotes());
        }
        if (c.getResolutionDate() != null) {
            System.out.println("  Resolved On   : " + c.getResolutionDate());
        }
    }
}