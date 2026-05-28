package fundamentals;
//Challenge 3 — Financial Calculator
//
//Create:
//Input:

//base premium
//risk percentage
//tax percentage
//Calculate:

//risk amount
//tax amount
//final premium

//Use:

//casting
//formatting
//Math.round()

import java.util.Scanner;

public class InsurancePremiumCalculator {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter base premium ");
        int basePremium = input.nextInt();
        System.out.print("Enter tax rate ");
        double taxRate = input.nextDouble();
        System.out.print("Enter risk rate ");
        double riskRate = input.nextDouble();
double riskAmount = (basePremium * riskRate) / 100;
double taxAmount = ((basePremium + riskAmount) * taxRate)/ 100;
double finalPremium = basePremium + riskAmount + taxAmount;
        System.out.println("Risk Amount: " + riskAmount);
        System.out.println("Tax Amount: " + taxAmount);
        System.out.println("Final Premium " + Math.round(finalPremium));

    };
}