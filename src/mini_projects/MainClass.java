package mini_projects;

import java.util.Scanner;

/**
 * MainClass.java — THE ENTRY POINT & MENU CONTROLLER
 *
 * WHAT main() SHOULD AND SHOULDN'T DO:
 * The original code had ALL logic in main() — that's the "God method" anti-pattern.
 * Here, main() does ONLY three things:
 *   1. Bootstrap: create the Scanner and the implementation object.
 *   2. Authenticate: call login() and decide whether to continue.
 *   3. Route: show the menu, read a choice, call the right method.
 *
 * All actual work is delegated to the implementation.
 * This is the Controller layer in MVC architecture.
 *
 * NOTE ON SCANNER OWNERSHIP:
 * We create ONE Scanner here and pass it to the implementation via its constructor.
 * Never create multiple Scanners on System.in — they compete for input and cause bugs.
 */
public class MainClass {

    public static void main(String[] args) {

        // ── BOOTSTRAP ────────────────────────────────────────────────────────
        Scanner scanner = new Scanner(System.in);

        // We declare the variable as the INTERFACE type, not the implementation.
        // WHY? If we ever swap to a different implementation (e.g., DatabaseAtm),
        // we only change this one line — nothing else in main() needs to change.
        // The implementation is cast to access the helper read methods it exposes.
        AtmOperationImplementation impl = new AtmOperationImplementation(scanner);
        AtmOperationInterface atm = impl;  // Reference through the interface

        // ── WELCOME BANNER ───────────────────────────────────────────────────
        printBanner();

        // ── ADMIN CHECK ──────────────────────────────────────────────────────
        // Before normal login, offer admin mode entry.
        // In a real system this would be a hardware key or separate terminal.
        System.out.println("Press ENTER to log in as customer, or type 'admin' for admin mode:");
        String mode = scanner.nextLine().trim();

        if (mode.equalsIgnoreCase("admin")) {
            // Admin flow — completely separate from user session
            if (atm.adminLogin()) {
                atm.showAdminDashboard();
            }
            // After admin view, exit. Admin doesn't transact.
            scanner.close();
            return;
        }

        // ── USER LOGIN ───────────────────────────────────────────────────────
        // login() returns false if all 3 attempts failed (account locked)
        boolean loggedIn = atm.login();
        if (!loggedIn) {
            // Account is locked — program must exit
            scanner.close();
            System.exit(1);  // Exit code 1 = abnormal termination (not the same as user Exit)
        }

        // ── MAIN MENU LOOP ───────────────────────────────────────────────────
        // 'while (true)' is correct here: the ATM runs INDEFINITELY until the
        // user explicitly chooses option 7 (Exit). There's no natural end condition
        // that can be expressed in the loop header — the exit is event-driven.
        //
        // We break out via System.exit() on option 7 (or 'return' would also work).
        while (true) {
            printMenu();

            System.out.print("  Your choice: ");
            String choiceInput = scanner.nextLine().trim();

            // ── INPUT VALIDATION ─────────────────────────────────────────────
            // We read as String and parse manually.
            // If the user typed "abc", parseInt throws — we catch it and loop back.
            int choice;
            try {
                choice = Integer.parseInt(choiceInput);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a number between 1 and 7.\n");
                continue;  // 'continue' jumps back to the top of the while loop
            }

            // ── ROUTING ──────────────────────────────────────────────────────
            // A switch statement is cleaner than if-else chains for menu routing.
            // Each 'case' maps to one feature; 'default' handles invalid numbers.
            switch (choice) {

                case 1: // ── CHECK BALANCE ─────────────────────────────────
                    atm.viewBalance();
                    break;

                case 2: // ── DEPOSIT ───────────────────────────────────────
                    System.out.print("  Enter deposit amount (KES): ");
                    double depositAmt = impl.readDouble();
                    atm.deposit(depositAmt);
                    break;

                case 3: // ── WITHDRAW ──────────────────────────────────────
                    System.out.print("  Enter withdrawal amount (KES): ");
                    double withdrawAmt = impl.readDouble();
                    atm.withdraw(withdrawAmt);
                    break;

                case 4: // ── TRANSFER FUNDS ────────────────────────────────
                    System.out.print("  Enter recipient account number (10 digits): ");
                    String accountNumber = impl.readAccountNumber();
                    System.out.print("  Enter transfer amount (KES): ");
                    double transferAmt = impl.readDouble();
                    atm.transferFunds(accountNumber, transferAmt);
                    break;

                case 5: // ── MINI STATEMENT ────────────────────────────────
                    atm.printMiniStatement();
                    break;

                case 6: // ── CHANGE PIN ────────────────────────────────────
                    atm.changePin();
                    break;

                case 7: // ── EXIT ──────────────────────────────────────────
                    // Farewell message as required by spec
                    System.out.println("\n  Thank you for banking with SecureBank Kenya.");
                    System.out.println("  Please collect your card. Goodbye!\n");
                    scanner.close();  // Always close resources before exit
                    System.exit(0);   // Exit code 0 = successful, clean termination
                    break;            // Unreachable, but good practice to keep

                default: // ── INVALID CHOICE ────────────────────────────────
                    // Numbers outside 1–7 land here
                    System.out.println("  [!] Invalid choice. Please select 1–7.\n");
                    break;
            }
            // After each operation, the while(true) loop naturally returns here
            // and prints the menu again — that's the "return to menu" behaviour.
        }
    }

    // ─── PRIVATE STATIC HELPERS ──────────────────────────────────────────────
    // static because they belong to the class level, not to an instance.
    // private because nothing outside MainClass needs them.

    /**
     * printBanner() — displays the welcome screen.
     * Extracted to a method so main() stays clean and readable.
     */
    private static void printBanner() {
        System.out.println();
        System.out.println("  ╔════════════════════════════════════════════╗");
        System.out.println("  ║        SecureBank Kenya ATM System         ║");
        System.out.println("  ║         Your Trusted Banking Partner       ║");
        System.out.println("  ╚════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * printMenu() — displays the main options.
     * Extracted for the same reason: keeps the loop body focused on logic,
     * not on formatting strings.
     */
    private static void printMenu() {
        System.out.println("  ┌─────────────────────────────┐");
        System.out.println("  │     ===  MAIN MENU  ===     │");
        System.out.println("  ├─────────────────────────────┤");
        System.out.println("  │  1. Check Balance           │");
        System.out.println("  │  2. Deposit                 │");
        System.out.println("  │  3. Withdraw                │");
        System.out.println("  │  4. Transfer Funds          │");
        System.out.println("  │  5. Mini Statement          │");
        System.out.println("  │  6. Change PIN              │");
        System.out.println("  │  7. Exit                    │");
        System.out.println("  └─────────────────────────────┘");
    }
}