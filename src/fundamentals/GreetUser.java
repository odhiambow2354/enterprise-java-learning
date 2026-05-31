package fundamentals;

public class GreetUser {
    public static void main(String[] args) {
        String message = greetUser( "Wycliffe", "Omondi");

    }
    public static String greetUser(String firstName, String lastName) {
        return "Hello " + firstName + " " + lastName;
    }
}