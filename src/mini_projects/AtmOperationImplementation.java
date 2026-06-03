package mini_projects;
import java.util.HashMap;
import java.util.Map;

public class AtmOperationImplementation implements AtmOperationInterface{
    ATMSimulator atm = new ATMSimulator();;
    Map<Double, String> miniStatement = new HashMap();

    @Override
    public void viewBalance() {
        System.out.println("Available Balance is: "+atm.getBalance());

    }

    @Override
    public void withdrawAmount(double withdrawAmount) {
        miniStatement.put(withdrawAmount, " Amount Withdrawn");
        if(withdrawAmount <= atm.getBalance()){

            System.out.println("clollect the cash: "+withdrawAmount);
            atm.setBalance(atm.getBalance()-withdrawAmount);
            viewBalance();

        }
        else {
            System.out.println("Insufficient funds in y0ur Account !!");
        }

    }

    @Override
    public void depositAmount(double depositAmount) {
        miniStatement.put(depositAmount, " Amount deposited");

        System.out.println(depositAmount+" Deposited Successfully");
        atm.setBalance(atm.getBalance()+depositAmount);
        viewBalance();

    }

    @Override
    public void viewMiniStatement() {
        for(Map.Entry<Double, String> entry:miniStatement.entrySet()){
            System.out.println(entry.getKey()+" "+entry.getValue());
        }

    }
}