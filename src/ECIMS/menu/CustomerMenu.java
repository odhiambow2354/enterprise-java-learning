package ECIMS.menu;

import exception.CustomerNotFoundException;
import ECIMS.model.Customer;
import service.CustomerService;
import utility.ConsoleUI;
import utility.InputHelper;

import java.util.List;

/** Console UI for all customer management operations. */
public class CustomerMenu {

    private final CustomerService customerService;

    public CustomerMenu(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUI.printHeader("CUSTOMER MANAGEMENT");
            System.out.println("  1. Create Customer");
            System.out.println("  2. Update Customer");
            System.out.println("  3. Delete Customer");
            System.out.println("  4. Search Customer");
            System.out.println("  5. Display All Customers");
            System.out.println("  0. Back");
            ConsoleUI.printDivider();

            int choice = InputHelper.readInt("  Choice: ");
            switch (choice) {
                case 1 -> createCustomer();
                case 2 -> updateCustomer();
                case 3 -> deleteCustomer();
                case 4 -> searchCustomer();
                case 5 -> displayAllCustomers();
                case 0 -> back = true;
                default -> ConsoleUI.printError("Invalid option.");
            }
        }
    }

    private void createCustomer() {
        ConsoleUI.printSubHeader("NEW CUSTOMER");
        String nid       = InputHelper.readString("  National ID   : ");
        String firstName = InputHelper.readString("  First Name    : ");
        String lastName  = InputHelper.readString("  Last Name     : ");
        String phone     = InputHelper.readString("  Phone         : ");
        String email     = InputHelper.readString("  Email         : ");
        String dob       = InputHelper.readString("  Date of Birth : ");
        String address   = InputHelper.readString("  Address       : ");

        try {
            Customer c = customerService.createCustomer(nid, firstName, lastName,
                    phone, email, dob, address);
            ConsoleUI.printSuccess("Customer created. ID: " + c.getCustomerId());
        } catch (Exception e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void updateCustomer() {
        ConsoleUI.printSubHeader("UPDATE CUSTOMER");
        String id = InputHelper.readString("  Customer ID : ");
        try {
            Customer existing = customerService.getCustomerById(id);
            ConsoleUI.printInfo("Current: " + existing.getFullName() + " | " + existing.getPhoneNumber());
            System.out.println("  (Leave blank to keep current value)");

            String phone   = InputHelper.readString("  New Phone   : ");
            String email   = InputHelper.readString("  New Email   : ");
            String address = InputHelper.readString("  New Address : ");

            customerService.updateCustomer(id, phone, email, address);
            ConsoleUI.printSuccess("Customer updated.");
        } catch (CustomerNotFoundException e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void deleteCustomer() {
        ConsoleUI.printSubHeader("DELETE CUSTOMER");
        String id = InputHelper.readString("  Customer ID : ");
        try {
            Customer c = customerService.getCustomerById(id);
            if (InputHelper.confirm("  Delete " + c.getFullName() + "?")) {
                customerService.deleteCustomer(id);
                ConsoleUI.printSuccess("Customer deleted.");
            } else {
                ConsoleUI.printInfo("Deletion cancelled.");
            }
        } catch (CustomerNotFoundException e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void searchCustomer() {
        ConsoleUI.printSubHeader("SEARCH CUSTOMER");
        System.out.println("  1. By Customer ID");
        System.out.println("  2. By National ID");
        System.out.println("  3. By Name");
        int choice = InputHelper.readInt("  Choice: ");

        try {
            switch (choice) {
                case 1 -> {
                    String id = InputHelper.readString("  Customer ID : ");
                    printCustomerDetail(customerService.getCustomerById(id));
                }
                case 2 -> {
                    String nid = InputHelper.readString("  National ID : ");
                    printCustomerDetail(customerService.getCustomerByNationalId(nid));
                }
                case 3 -> {
                    String name = InputHelper.readString("  Name query  : ");
                    List<Customer> results = customerService.searchByName(name);
                    if (results.isEmpty()) {
                        ConsoleUI.printInfo("No customers found matching: " + name);
                    } else {
                        printCustomerTable(results);
                    }
                }
                default -> ConsoleUI.printError("Invalid option.");
            }
        } catch (CustomerNotFoundException e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void displayAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        ConsoleUI.printSubHeader("ALL CUSTOMERS (" + customers.size() + ")");
        if (customers.isEmpty()) {
            ConsoleUI.printInfo("No customers registered.");
        } else {
            printCustomerTable(customers);
        }
        InputHelper.pressEnterToContinue();
    }

    private void printCustomerTable(List<Customer> list) {
        System.out.println();
        System.out.printf("| %-8s | %-15s | %-20s | %-12s | %-25s | %-12s |%n",
                "ID", "National ID", "Full Name", "Phone", "Email", "Registered");
        System.out.println("-".repeat(110));
        list.forEach(System.out::println);
    }

    private void printCustomerDetail(Customer c) {
        System.out.println();
        System.out.println("  Customer ID    : " + c.getCustomerId());
        System.out.println("  National ID    : " + c.getNationalId());
        System.out.println("  Name           : " + c.getFullName());
        System.out.println("  Phone          : " + c.getPhoneNumber());
        System.out.println("  Email          : " + c.getEmail());
        System.out.println("  Date of Birth  : " + c.getDateOfBirth());
        System.out.println("  Address        : " + c.getAddress());
        System.out.println("  Registered     : " + c.getRegistrationDate());
    }
}