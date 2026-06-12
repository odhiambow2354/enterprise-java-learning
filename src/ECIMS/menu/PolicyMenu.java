package ECIMS.menu;

import exception.PolicyNotFoundException;
import model.Policy;
import service.PolicyService;
import utility.ConsoleUI;
import utility.InputHelper;
import utility.PolicyType;

import java.util.List;

/** Console UI for policy lifecycle operations. */
public class PolicyMenu {

    private final PolicyService policyService;

    public PolicyMenu(PolicyService policyService) {
        this.policyService = policyService;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUI.printHeader("POLICY MANAGEMENT");
            System.out.println("  1. Issue Policy");
            System.out.println("  2. Renew Policy");
            System.out.println("  3. Cancel Policy");
            System.out.println("  4. Suspend Policy");
            System.out.println("  5. Search Policy");
            System.out.println("  6. Display All Policies");
            System.out.println("  7. Policies by Customer");
            System.out.println("  0. Back");
            ConsoleUI.printDivider();

            int choice = InputHelper.readInt("  Choice: ");
            switch (choice) {
                case 1 -> issuePolicy();
                case 2 -> renewPolicy();
                case 3 -> cancelPolicy();
                case 4 -> suspendPolicy();
                case 5 -> searchPolicy();
                case 6 -> displayAllPolicies();
                case 7 -> policiesByCustomer();
                case 0 -> back = true;
                default -> ConsoleUI.printError("Invalid option.");
            }
        }
    }

    private void issuePolicy() {
        ConsoleUI.printSubHeader("ISSUE POLICY");
        String customerId = InputHelper.readString("  Customer ID     : ");
        PolicyType type   = selectPolicyType();
        double premium    = InputHelper.readDouble("  Annual Premium  : KES ");
        double coverage   = InputHelper.readDouble("  Coverage Amount : KES ");
        int months        = InputHelper.readInt("  Duration (months): ");

        try {
            Policy p = policyService.issuePolicy(customerId, type, premium, coverage, months);
            ConsoleUI.printSuccess("Policy issued: " + p.getPolicyNumber()
                    + " | Expires: " + p.getExpiryDate());
        } catch (Exception e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void renewPolicy() {
        ConsoleUI.printSubHeader("RENEW POLICY");
        String policyNumber = InputHelper.readString("  Policy Number : ");
        int months          = InputHelper.readInt("  Extend by (months): ");
        try {
            Policy p = policyService.renewPolicy(policyNumber, months);
            ConsoleUI.printSuccess("Policy renewed. New expiry: " + p.getExpiryDate());
        } catch (Exception e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void cancelPolicy() {
        ConsoleUI.printSubHeader("CANCEL POLICY");
        String policyNumber = InputHelper.readString("  Policy Number : ");
        try {
            if (InputHelper.confirm("  Cancel policy " + policyNumber + "?")) {
                policyService.cancelPolicy(policyNumber);
                ConsoleUI.printSuccess("Policy cancelled.");
            }
        } catch (Exception e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void suspendPolicy() {
        ConsoleUI.printSubHeader("SUSPEND POLICY");
        String policyNumber = InputHelper.readString("  Policy Number : ");
        try {
            policyService.suspendPolicy(policyNumber);
            ConsoleUI.printSuccess("Policy suspended.");
        } catch (Exception e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void searchPolicy() {
        ConsoleUI.printSubHeader("SEARCH POLICY");
        String policyNumber = InputHelper.readString("  Policy Number : ");
        try {
            printPolicyDetail(policyService.getPolicy(policyNumber));
        } catch (PolicyNotFoundException e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void displayAllPolicies() {
        List<Policy> policies = policyService.getAllPolicies();
        ConsoleUI.printSubHeader("ALL POLICIES (" + policies.size() + ")");
        printPolicyTable(policies);
        InputHelper.pressEnterToContinue();
    }

    private void policiesByCustomer() {
        ConsoleUI.printSubHeader("POLICIES BY CUSTOMER");
        String customerId = InputHelper.readString("  Customer ID : ");
        List<Policy> policies = policyService.getPoliciesByCustomer(customerId);
        if (policies.isEmpty()) {
            ConsoleUI.printInfo("No policies found for customer: " + customerId);
        } else {
            printPolicyTable(policies);
        }
        InputHelper.pressEnterToContinue();
    }

    private PolicyType selectPolicyType() {
        System.out.println("  Policy Types:");
        PolicyType[] types = PolicyType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("    %d. %s%n", i + 1, types[i].getDisplayName());
        }
        int idx = InputHelper.readInt("  Select type: ") - 1;
        if (idx < 0 || idx >= types.length) {
            ConsoleUI.printInfo("Invalid selection, defaulting to LIFE.");
            return PolicyType.LIFE;
        }
        return types[idx];
    }

    private void printPolicyTable(List<Policy> list) {
        if (list.isEmpty()) { ConsoleUI.printInfo("No policies to display."); return; }
        System.out.println();
        System.out.printf("| %-10s | %-8s | %-18s | %10s | %10s | %-12s | %-12s | %-12s |%n",
                "Policy No", "Cust ID", "Type", "Premium", "Coverage", "Issue", "Expiry", "Status");
        System.out.println("-".repeat(115));
        list.forEach(System.out::println);
    }

    private void printPolicyDetail(Policy p) {
        System.out.println();
        System.out.println("  Policy Number  : " + p.getPolicyNumber());
        System.out.println("  Customer ID    : " + p.getCustomerId());
        System.out.println("  Type           : " + p.getPolicyType().getDisplayName());
        System.out.printf( "  Premium        : KES %.2f%n", p.getPremium());
        System.out.printf( "  Coverage       : KES %.2f%n", p.getCoverage());
        System.out.println("  Issue Date     : " + p.getIssueDate());
        System.out.println("  Expiry Date    : " + p.getExpiryDate());
        System.out.println("  Status         : " + p.getStatus());
        System.out.printf( "  Total Paid     : KES %.2f%n", p.getTotalPaid());
        System.out.printf( "  Outstanding    : KES %.2f%n", p.getOutstandingBalance());
    }
}