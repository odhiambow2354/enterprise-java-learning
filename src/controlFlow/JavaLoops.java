package controlFlow;

import java.util.Scanner;

public class JavaLoops{
    public static void main(String[] args) {
        for (int i = 1; i <= 4; i++){
            System.out.println("Hello Wycliffe, welcome onboard");

        }
        //While loop
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals("quit")){
            System.out.print("Enter your preferred number ");
            input = scanner.nextLine().toLowerCase();
            if (input.equals("quit"))
                break;

            System.out.println(input);
        }
        //Do while loop
//        do{
//            System.out.print("Enter your preferred number ");
//            input = scanner.nextLine().toLowerCase();
//            System.out.println(input);
//            if (input.equals("quit"))
//                break;
//
//        }while(!input.equals("quit"));

        //For each loop=> iterate over array or collection
        //it has the following limitations=>
        // 1. it's always forward only, you can't iterate over array from the end
        // 2. No access to the index of each item

        String [] fruits = {"Apple", "Mango", "Orange", "Pear", "Watermelon"};
        for (String fruit : fruits){
            System.out.println(fruit);
        }
    }
}