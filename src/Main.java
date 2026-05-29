package src;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Principal ($1K-1M): ");
        int principal = input.nextInt();
        while (principal < 1000 || principal > 1000000) {
            System.out.print("Enter Principal ($1K-1M): ");
            principal = input.nextInt();

        }
        System.out.print("Enter Annual Interest Rate (> 0 <=30): ");
        double rate = input.nextDouble();
        while (rate <1 || rate > 30){
            System.out.print("Enter Annual Interest Rate (> 0 <=30): ");
            rate = input.nextDouble();
        }
        System.out.print("Enter Period (1-30 Years): ");
        double period = input.nextDouble();
        while (period < 1 || period > 30) {
            System.out.print("Enter Period (1-30 Years): ");
            period = input.nextDouble();
        }

        double monthlyInterestRate = (rate/12/100);
        double periodsInMonths = (period * 12);

        double numerator = monthlyInterestRate * Math.pow(1 + monthlyInterestRate, periodsInMonths);
        double denominator = Math.pow(1 + monthlyInterestRate, periodsInMonths) - 1;

        double mortgage = principal * (numerator / denominator);
        System.out.println("Your monthly Mortgage is $" + Math.round(mortgage));;
    }
}