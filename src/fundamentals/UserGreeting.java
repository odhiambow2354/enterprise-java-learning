import java.util.Scanner;  // Import the Scanner class to read input

public class UserGreeting {

    // Method to greet the user by name
    public static void greetUser(String name) {
        System.out.println("Hello, " + name + "! Nice to meet you.");
    }

    public static void main(String[] args) {
        // Create a Scanner object to read input from the terminal
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter their name
        System.out.print("Please enter your name: ");

        // Read the user's input (entire line)
        String userName = scanner.nextLine();

        // Call the greetUser method with the entered name
        greetUser(userName);

        // Close the scanner
        scanner.close();
    }
}