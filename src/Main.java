package src;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter principal: ");
        double principal = input.nextDouble();
        System.out.print("Enter rate: ");
        double rate = input.nextDouble();
        System.out.print("Enter period: ");
        double period = input.nextDouble();

        double monthlyInterestRate = (rate/12/100);
        double periodsInMonths = (period * 12);

        double numerator = monthlyInterestRate * Math.pow(1 + monthlyInterestRate, periodsInMonths);
        double denominator = Math.pow(1 + monthlyInterestRate, periodsInMonths) - 1;

        double mortgage = principal * (numerator / denominator);
        System.out.println("Your monthly Mortgage is $" + mortgage);;
    }
}