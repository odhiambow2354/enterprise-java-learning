package menu;

import model.Payment;
import service.PaymentService;
import utility.ConsoleUI;
import utility.InputHelper;

import java.util.List;

/** Console UI for payment operations. */
public class PaymentMenu {

    private final PaymentService paymentService;

    public PaymentMenu(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUI.printHeader("PREMIUM PAYMENTS");
            System.out.println("  1. Receive Premium");
            System.out.println("  2. View Payment History (by Policy)");
            System.out.println("  3. View Payment History (by Customer)");
            System.out.println("  4. Calculate Outstanding Balance");
            System.out.println("  0. Back");
            ConsoleUI.printDivider();

            int choice = InputHelper.readInt("  Choice: ");
            switch (choice) {
                case 1 -> receivePremium();
                case 2 -> historyByPolicy();
                case 3 -> historyByCustomer();
                case 4 -> outstandingBalance();
                case 0 -> back = true;
                default -> ConsoleUI.printError("Invalid option.");
            }
        }
    }

    private void receivePremium() {
        ConsoleUI.printSubHeader("RECEIVE PREMIUM");
        String policyNumber = InputHelper.readString("  Policy Number : ");
        double amount       = InputHelper.readDouble("  Amount (KES)  : ");

        try {
            Payment payment = paymentService.receivePremium(policyNumber, amount);
            System.out.println();
            System.out.println(payment.toReceipt());
            ConsoleUI.printSuccess("Payment recorded.");
        } catch (Exception e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void historyByPolicy() {
        ConsoleUI.printSubHeader("PAYMENT HISTORY — BY POLICY");
        String policyNumber = InputHelper.readString("  Policy Number : ");
        List<Payment> history = paymentService.getHistoryByPolicy(policyNumber);
        printPaymentTable(history);
        InputHelper.pressEnterToContinue();
    }

    private void historyByCustomer() {
        ConsoleUI.printSubHeader("PAYMENT HISTORY — BY CUSTOMER");
        String customerId = InputHelper.readString("  Customer ID : ");
        List<Payment> history = paymentService.getHistoryByCustomer(customerId);
        printPaymentTable(history);
        InputHelper.pressEnterToContinue();
    }

    private void outstandingBalance() {
        ConsoleUI.printSubHeader("OUTSTANDING BALANCE");
        String policyNumber = InputHelper.readString("  Policy Number : ");
        try {
            double balance = paymentService.getOutstandingBalance(policyNumber);
            System.out.printf("%n  Outstanding Balance for %s : KES %.2f%n", policyNumber, balance);
        } catch (Exception e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void printPaymentTable(List<Payment> list) {
        if (list.isEmpty()) { ConsoleUI.printInfo("No payments found."); return; }
        System.out.println();
        System.out.printf("| %-10s | %-10s | %10s | %-20s | %-12s |%n",
                "Receipt", "Policy", "Amount", "Processed By", "Date");
        System.out.println("-".repeat(75));
        list.forEach(System.out::println);
        double total = list.stream().mapToDouble(Payment::getAmount).sum();
        System.out.printf("%nTotal: KES %.2f%n", total);
    }
}