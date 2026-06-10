package ECIMS;

import log.TransactionLogger;
import menu.*;
import repository.*;
import service.*;
import utility.ConsoleUI;
import utility.DataSeeder;
import utility.InputHelper;

/**
 * Application entry point.
 * Wires up all repositories, services, and menus — the only place where
 * the dependency graph is assembled, keeping individual classes testable.
 */
public class Main {

    public static void main(String[] args) {

        // ── Repositories ──────────────────────────────────────────────────
        EmployeeRepository employeeRepo = new EmployeeRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        PolicyRepository   policyRepo   = new PolicyRepository();
        PaymentRepository  paymentRepo  = new PaymentRepository();
        ClaimRepository    claimRepo    = new ClaimRepository();

        // ── Services ──────────────────────────────────────────────────────
        AuthService     authService     = new AuthService(employeeRepo);
        CustomerService customerService = new CustomerService(customerRepo, authService);
        PolicyService   policyService   = new PolicyService(policyRepo, customerRepo, authService);
        PaymentService  paymentService  = new PaymentService(paymentRepo, policyRepo, authService);
        ClaimService    claimService    = new ClaimService(claimRepo, policyRepo, authService);
        ReportService   reportService   = new ReportService(customerRepo, policyRepo, paymentRepo, claimRepo);

        // ── Menus ─────────────────────────────────────────────────────────
        AuthMenu     authMenu     = new AuthMenu(authService);
        CustomerMenu customerMenu = new CustomerMenu(customerService);
        PolicyMenu   policyMenu   = new PolicyMenu(policyService);
        PaymentMenu  paymentMenu  = new PaymentMenu(paymentService);
        ClaimMenu    claimMenu    = new ClaimMenu(claimService);
        ReportMenu   reportMenu   = new ReportMenu(reportService);
        AdminMenu    adminMenu    = new AdminMenu(authService, employeeRepo);

        // ── Seed demo data ────────────────────────────────────────────────
        DataSeeder.seed(employeeRepo, customerRepo, policyRepo, paymentRepo, claimRepo, authService);

        // ── Main loop ─────────────────────────────────────────────────────
        boolean running = true;
        while (running) {

            // Force login before showing the main menu
            if (!authService.isLoggedIn()) {
                if (!authMenu.showLogin()) continue;
            }

            printMainMenu(authService.getCurrentUser().getFullName(),
                    authService.getCurrentUser().getRole());

            int choice = InputHelper.readInt("  Choice: ");
            switch (choice) {
                case 1 -> customerMenu.show();
                case 2 -> policyMenu.show();
                case 3 -> paymentMenu.show();
                case 4 -> claimMenu.show();
                case 5 -> reportMenu.show();
                case 6 -> adminMenu.show();
                case 7 -> {
                    authService.logout();
                    ConsoleUI.printSuccess("Logged out.");
                    InputHelper.pressEnterToContinue();
                }
                case 0 -> {
                    authService.logout();
                    ConsoleUI.printSuccess("Goodbye.");
                    running = false;
                }
                default -> ConsoleUI.printError("Invalid option. Please select 0–7.");
            }
        }
    }

    private static void printMainMenu(String username, String role) {
        ConsoleUI.clearScreen();
        System.out.println();
        System.out.println("  ╔═══════════════════════════════════════════════════╗");
        System.out.println("  ║     ENTERPRISE INSURANCE MANAGEMENT SYSTEM        ║");
        System.out.println("  ║         Naturesurf Insurance Company (NIC)        ║");
        System.out.println("  ╠═══════════════════════════════════════════════════╣");
        System.out.printf( "  ║  User: %-20s Role: %-13s║%n", username, role);
        System.out.println("  ╠═══════════════════════════════════════════════════╣");
        System.out.println("  ║  1. Customer Management                           ║");
        System.out.println("  ║  2. Policy Management                             ║");
        System.out.println("  ║  3. Premium Payments                              ║");
        System.out.println("  ║  4. Claims                                        ║");
        System.out.println("  ║  5. Reports                                       ║");
        System.out.println("  ║  6. Administration                                ║");
        System.out.println("  ║  7. Logout                                        ║");
        System.out.println("  ║  0. Exit                                          ║");
        System.out.println("  ╚═══════════════════════════════════════════════════╝");
    }
}