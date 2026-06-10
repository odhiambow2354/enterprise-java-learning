package menu;

import log.TransactionLogger;
import model.Employee;
import model.EmployeeFactory;
import repository.EmployeeRepository;
import service.AuthService;
import utility.ConsoleUI;
import utility.InputHelper;
import utility.Role;

import java.util.List;

/** Administration menu: manage staff accounts and view logs. */
public class AdminMenu {

    private final AuthService        authService;
    private final EmployeeRepository employeeRepo;
    private final TransactionLogger  logger = TransactionLogger.getInstance();

    public AdminMenu(AuthService authService, EmployeeRepository employeeRepo) {
        this.authService  = authService;
        this.employeeRepo = employeeRepo;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUI.printHeader("ADMINISTRATION");
            System.out.println("  1. Add Staff Account");
            System.out.println("  2. Deactivate Staff Account");
            System.out.println("  3. List All Staff");
            System.out.println("  4. View Login History");
            System.out.println("  5. View Transaction Log");
            System.out.println("  6. Change My Password");
            System.out.println("  0. Back");
            ConsoleUI.printDivider();

            int choice = InputHelper.readInt("  Choice: ");
            switch (choice) {
                case 1 -> addStaff();
                case 2 -> deactivateStaff();
                case 3 -> listStaff();
                case 4 -> viewLoginHistory();
                case 5 -> logger.printAll();
                case 6 -> changePassword();
                case 0 -> back = true;
                default -> ConsoleUI.printError("Invalid option.");
            }
        }
    }

    private void addStaff() {
        ConsoleUI.printSubHeader("ADD STAFF ACCOUNT");

        System.out.println("  Roles:");
        Role[] roles = Role.values();
        for (int i = 0; i < roles.length; i++) {
            System.out.printf("    %d. %s%n", i + 1, roles[i].getDisplayName());
        }
        int roleIdx = InputHelper.readInt("  Select role: ") - 1;
        if (roleIdx < 0 || roleIdx >= roles.length) {
            ConsoleUI.printError("Invalid role selection.");
            return;
        }
        Role role = roles[roleIdx];

        String firstName = InputHelper.readString("  First Name : ");
        String lastName  = InputHelper.readString("  Last Name  : ");
        String phone     = InputHelper.readString("  Phone      : ");
        String email     = InputHelper.readString("  Email      : ");
        String dob       = InputHelper.readString("  DOB        : ");
        String address   = InputHelper.readString("  Address    : ");
        String username  = InputHelper.readString("  Username   : ");
        String password  = InputHelper.readString("  Password   : ");

        if (employeeRepo.usernameExists(username)) {
            ConsoleUI.printError("Username already taken: " + username);
            InputHelper.pressEnterToContinue();
            return;
        }

        Employee emp = EmployeeFactory.create(role, firstName, lastName, phone, email, dob, address, username, password);
        employeeRepo.save(emp);
        ConsoleUI.printSuccess("Staff account created: " + emp.getEmployeeId() + " | " + emp.getUsername());
        InputHelper.pressEnterToContinue();
    }

    private void deactivateStaff() {
        ConsoleUI.printSubHeader("DEACTIVATE STAFF");
        String empId = InputHelper.readString("  Employee ID : ");
        employeeRepo.findById(empId).ifPresentOrElse(emp -> {
            emp.setActive(false);
            employeeRepo.update(emp);
            ConsoleUI.printSuccess("Account deactivated: " + emp.getUsername());
        }, () -> ConsoleUI.printError("Employee not found: " + empId));
        InputHelper.pressEnterToContinue();
    }

    private void listStaff() {
        List<Employee> staff = employeeRepo.findAll();
        ConsoleUI.printSubHeader("ALL STAFF (" + staff.size() + ")");
        System.out.printf("  %-10s | %-15s | %-20s | %-18s | %s%n",
                "ID", "Username", "Full Name", "Role", "Active");
        System.out.println("  " + "-".repeat(80));
        staff.forEach(e -> System.out.printf("  %-10s | %-15s | %-20s | %-18s | %s%n",
                e.getEmployeeId(), e.getUsername(), e.getFullName(), e.getRole(), e.isActive()));
        InputHelper.pressEnterToContinue();
    }

    private void viewLoginHistory() {
        List<String> history = authService.getLoginHistory();
        ConsoleUI.printSubHeader("LOGIN HISTORY (" + history.size() + " entries)");
        if (history.isEmpty()) {
            ConsoleUI.printInfo("No login history yet.");
        } else {
            history.forEach(h -> System.out.println("  " + h));
        }
        InputHelper.pressEnterToContinue();
    }

    private void changePassword() {
        new AuthMenu(authService).showChangePassword();
    }
}