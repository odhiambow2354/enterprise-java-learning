//compares primitive values
package controlFlow;
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
    }
}