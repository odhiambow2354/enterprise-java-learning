//compares primitive values
package controlFlow;

import java.util.Scanner;

public class Comparison{
    //boolean expression
    public static void main(String[] args){
        int x = 1;
        int y = 1;
        //logical expression
        int temperature = 31;
        boolean isHot = temperature >20 && temperature<25;
        boolean hasHighIncome = false;
        boolean hasGoodCredit = true;
        boolean hasCriminalRecord = false;
        boolean isEligible = (hasHighIncome || hasGoodCredit) && !hasCriminalRecord;
        //if statement
//        If tem is greater than 30:
//        It's a hot day
//    drink plenty of water
//        othewise, if  temp is between 20 and 30
//    it's a nice day
//            otherwise
//                    it's cold'
        if (temperature > 30){
            System.out.println("It's a hot day \nDrink plenty of water");
        }else if (temperature > 20 && temperature<30){
            System.out.println("It's a nice day");
        }else{
            System.out.println("It's a cold day");
        }
        System.out.println(isHot);
        System.out.println(x==y);
        System.out.println(isEligible);
        //switch statements
        String role = "admin";
        switch (role){
            case "admin":
                System.out.println("you're an administrator");
                break;
            case "moderator":
                System.out.println("you're a moderator");
                break;
                default:
                    System.out.println("you're a guest");
        }


        //QUIZ=> FizzBuzz

        //If the number is divisible by 5 we get Buzz,
        // if divisible by 3 we get
        //if the number is divisible by both 3 and 5 we get FizzBuzz
        //if not divisible by either we get the same number printed in the terminal

        Scanner input = new Scanner(System.in);
        System.out.print("Enter your preferred number " );
        int number = Integer.parseInt(input.nextLine());
        if(number % 5 ==0 && number % 3 ==0){
            System.out.println("FizzBuzz");
        }else if (number % 3 == 0){
            System.out.println("Fizz");
        }else if (number % 5 ==0 ){
            System.out.println("Buzz");
        }else{
            System.out.println(number);
        }

    }
}