package ECIMS.menu;

import model.*;
import service.ReportService;
import service.ReportService.ReportSummary;
import utility.ConsoleUI;
import utility.InputHelper;

import java.util.List;

/** Console UI for all management reports. */
public class ReportMenu {

    private final ReportService reportService;

    public ReportMenu(ReportService reportService) {
        this.reportService = reportService;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUI.printHeader("REPORTS");
            System.out.println("  1. Active Policies");
            System.out.println("  2. Expired Policies");
            System.out.println("  3. Pending Claims");
            System.out.println("  4. Paid Claims");
            System.out.println("  5. Outstanding Premiums");
            System.out.println("  6. Registered Customers");
            System.out.println("  7. Daily Transactions");
            System.out.println("  8. System Summary Dashboard");
            System.out.println("  0. Back");
            ConsoleUI.printDivider();

            int choice = InputHelper.readInt("  Choice: ");
            switch (choice) {
                case 1 -> activePolicies();
                case 2 -> expiredPolicies();
                case 3 -> pendingClaims();
                case 4 -> paidClaims();
                case 5 -> outstandingPremiums();
                case 6 -> registeredCustomers();
                case 7 -> dailyTransactions();
                case 8 -> summaryDashboard();
                case 0 -> back = true;
                default -> ConsoleUI.printError("Invalid option.");
            }
        }
    }

    private void activePolicies() {
        List<Policy> list = reportService.getActivePolicies();
        ConsoleUI.printSubHeader("ACTIVE POLICIES — " + list.size() + " records");
        printPolicyList(list);
        InputHelper.pressEnterToContinue();
    }

    private void expiredPolicies() {
        List<Policy> list = reportService.getExpiredPolicies();
        ConsoleUI.printSubHeader("EXPIRED POLICIES — " + list.size() + " records");
        printPolicyList(list);
        InputHelper.pressEnterToContinue();
    }

    private void pendingClaims() {
        List<Claim> list = reportService.getPendingClaims();
        ConsoleUI.printSubHeader("PENDING CLAIMS — " + list.size() + " records");
        printClaimList(list);
        InputHelper.pressEnterToContinue();
    }

    private void paidClaims() {
        List<Claim> list = reportService.getPaidClaims();
        ConsoleUI.printSubHeader("PAID CLAIMS — " + list.size() + " records");
        printClaimList(list);
        InputHelper.pressEnterToContinue();
    }

    private void outstandingPremiums() {
        List<Policy> list = reportService.getPoliciesWithOutstandingPremiums();
        ConsoleUI.printSubHeader("OUTSTANDING PREMIUMS — " + list.size() + " policies");
        if (list.isEmpty()) {
            ConsoleUI.printInfo("All premiums are fully paid.");
        } else {
            System.out.println();
            System.out.printf("| %-10s | %-8s | %-18s | %12s | %12s |%n",
                    "Policy No", "Cust ID", "Type", "Premium", "Outstanding");
            System.out.println("-".repeat(75));
            for (Policy p : list) {
                System.out.printf("| %-10s | %-8s | %-18s | %12.2f | %12.2f |%n",
                        p.getPolicyNumber(), p.getCustomerId(),
                        p.getPolicyType().getDisplayName(),
                        p.getPremium(), p.getOutstandingBalance());
            }
        }
        InputHelper.pressEnterToContinue();
    }

    private void registeredCustomers() {
        List<Customer> list = reportService.getAllRegisteredCustomers();
        ConsoleUI.printSubHeader("REGISTERED CUSTOMERS — " + list.size() + " records");
        if (list.isEmpty()) {
            ConsoleUI.printInfo("No customers registered.");
        } else {
            System.out.println();
            System.out.printf("| %-8s | %-20s | %-12s | %-25s |%n",
                    "ID", "Full Name", "Phone", "Email");
            System.out.println("-".repeat(75));
            for (Customer c : list) {
                System.out.printf("| %-8s | %-20s | %-12s | %-25s |%n",
                        c.getCustomerId(), c.getFullName(), c.getPhoneNumber(), c.getEmail());
            }
        }
        InputHelper.pressEnterToContinue();
    }

    private void dailyTransactions() {
        List<Payment> list = reportService.getDailyTransactions();
        ConsoleUI.printSubHeader("TODAY'S TRANSACTIONS — " + list.size() + " records");
        if (list.isEmpty()) {
            ConsoleUI.printInfo("No transactions recorded today.");
        } else {
            System.out.printf("| %-10s | %-10s | %10s | %-15s |%n",
                    "Receipt", "Policy", "Amount", "Processed By");
            System.out.println("-".repeat(55));
            list.forEach(System.out::println);
            double total = list.stream().mapToDouble(Payment::getAmount).sum();
            System.out.printf("%nDay Total: KES %.2f%n", total);
        }
        InputHelper.pressEnterToContinue();
    }

    private void summaryDashboard() {
        ReportSummary s = reportService.getSummary();
        ConsoleUI.printHeader("SYSTEM SUMMARY DASHBOARD");
        System.out.printf("  Total Customers          : %d%n", s.totalCustomers());
        System.out.printf("  Total Policies           : %d%n", s.totalPolicies());
        System.out.printf("  Active Policies          : %d%n", s.activePolicies());
        System.out.printf("  Total Claims             : %d%n", s.totalClaims());
        System.out.printf("  Pending Claims           : %d%n", s.pendingClaims());
        System.out.printf("  Total Premiums Collected : KES %.2f%n", s.totalPremiumsCollected());
        InputHelper.pressEnterToContinue();
    }

    private void printPolicyList(List<Policy> list) {
        if (list.isEmpty()) { ConsoleUI.printInfo("No records."); return; }
        list.forEach(p -> System.out.printf("  %s | %-8s | %-18s | %-10s | %s%n",
                p.getPolicyNumber(), p.getCustomerId(),
                p.getPolicyType().getDisplayName(), p.getStatus(), p.getExpiryDate()));
    }

    private void printClaimList(List<Claim> list) {
        if (list.isEmpty()) { ConsoleUI.printInfo("No records."); return; }
        list.forEach(c -> System.out.printf("  %s | %-10s | KES %,.2f | %s%n",
                c.getClaimId(), c.getPolicyNumber(), c.getClaimedAmount(), c.getLodgeDate()));
    }
}