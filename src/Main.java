package src;

import java.util.Scanner;

public class Main {
    private static final int MONTHS_IN_YEAR = 12;
    private static final int PERCENT = 100;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        double principal = readNumber(input, "Principal ($1K-1M): ", 1_000, 1_000_000);
        double annualInterest = readNumber(input, "Annual Interest Rate (1-30): ", 1, 30);
        double years = readNumber(input, "Period (1-30 Years): ", 1, 30);

        input.close();

        double mortgage = calculateMortgage(principal, annualInterest, years);
        System.out.println("Your monthly Mortgage is $" + Math.round(mortgage));
        printAmortizationSchedule(principal, annualInterest, years);
    }

    /**
     * Reusable helper to read and validate user inputs within a specific range.
     */
    private static double readNumber(Scanner scanner, String prompt, double min, double max) {
        double value;
        while (true) {
            System.out.print("Enter " + prompt);
            if (scanner.hasNextDouble()) {
                value = scanner.nextDouble();
                value = scanner.nextDouble();
                if (value >= min && value <= max) {
                    break;
                }
            } else {
                scanner.next(); // Discard invalid non-numeric input
            }
            System.out.println("Invalid entry. Please enter a value between " + min + " and " + max + ".");
        }
        return value;
    }

    /**
     * Calculates the monthly mortgage payment based on standard financial formulas.
     */
    public static double calculateMortgage(double principal, double annualInterest, double years) {
        double monthlyInterestRate = annualInterest / MONTHS_IN_YEAR / PERCENT;
        double numberOfPayments = years * MONTHS_IN_YEAR;

        double compoundInterest = Math.pow(1 + monthlyInterestRate, numberOfPayments);

        return principal * (monthlyInterestRate * compoundInterest) / (compoundInterest - 1);
    }
    /**
     * Calculating the remaining balance after monthly payment until the balance is completed
     * B= L[(1 + C)^n-(1+c)^p]/[(1+c)^n-1]
     * l=>loan amount
     * c=>monthly interest
     * n=> number of payments(months)
     * p=>number of payments already made.
     **/
    public static void printAmortizationSchedule(double principal, double annualInterest, double years) {
        final int MONTHS_IN_YEAR = 12;
        final int PERCENT = 100;
        //Converting annual inputs to monthly units
        double monthlyInterestRate = annualInterest / MONTHS_IN_YEAR / PERCENT;
        double totalMonths = years * MONTHS_IN_YEAR;

        System.out.println("\n---Amortization Schedule---");

        //loop from month p=0 to totalMonths (n)
        for (int p=0; p<=totalMonths; p++) {
            double totalCompounded = Math.pow(1 + monthlyInterestRate, totalMonths);

            //calculate the current compound interest term: (1 +c)^n
            double currentCompounded = Math.pow(1 + monthlyInterestRate, p);
            double numerator = totalCompounded - currentCompounded;
            double denominator = totalCompounded - 1;
            double remainingBalance = principal *(numerator / denominator);
            if(remainingBalance < 0 || p == totalMonths) {
                remainingBalance = 0;

            }
           // System.out.println("Amortization of " + p + ": " + remainingBalance);
            System.out.println("Month " + p + ": Remaining Balance = $" + Math.round(remainingBalance));
        }

    }
}
