package mini_projects;

import java.sql.SQLOutput;
import java.util.Scanner;

public class MainClass{
    public static void main(String[] args){
        AtmOperationInterface op = new AtmOperationImplementation();
        int atmnumber=12345;
        int atmpin=123;
        Scanner in=new Scanner(System.in);
            System.out.println("===Welcome to ATM Machine===");
            System.out.print("Enter ATM number: ");
            int atmNumber =in.nextInt();
            System.out.print("Enter ATM Pin: ");
            int pin =in.nextInt();
            if((atmnumber==atmNumber)&&(atmpin==pin)){
                while(true){
                    System.out.println("===Main Menu===");
                    System.out.println("1. View Available Balance\n2. Withraw Amount\n3. Deposit Amount\n4. View MiniStatement\n5. Exit");
                    System.out.println("Enter your choice: ");
                    int choice=in.nextInt();
                    if(choice==1){
                        op.viewBalance();
                    }
                    else if(choice==2){
                        System.out.println("Enter Amount to Withdraw: ");
                        double withdrawAmount =in.nextDouble();
                        op.withdrawAmount(withdrawAmount);


                    }
                    else if(choice==3){
                        System.out.println("Enter Amount to Deposit :");
                        double depositAmount =in.nextDouble();
                        op.depositAmount(depositAmount);

                    }
                    else if(choice==4){
                        op.viewMiniStatement();

                    }
                    else if(choice==5){
                        System.out.println("Collect your ATM Card\n Thank you for using ATM Machine");
                        System.exit(0);

                    }
                    else{
                        System.out.println("Please enter valid choice ");
                    }




                }
            }
            else{
                System.out.println("Validation Failed");
                System.exit(0);
            }
        }
}