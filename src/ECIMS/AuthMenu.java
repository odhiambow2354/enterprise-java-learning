package menu;

import exception.AuthenticationException;
import service.AuthService;
import utility.ConsoleUI;
import utility.InputHelper;

/**
 * Handles login, logout, and password-change screens.
 */
public class AuthMenu {

    private final AuthService authService;

    public AuthMenu(AuthService authService) {
        this.authService = authService;
    }

    public boolean showLogin() {
        ConsoleUI.printHeader("NATURESURF INSURANCE — LOGIN");
        String username = InputHelper.readString("  Username : ");
        String password = InputHelper.readString("  Password : ");

        try {
            authService.login(username, password);
            ConsoleUI.printSuccess("Welcome, " + authService.getCurrentUser().getFullName()
                    + " (" + authService.getCurrentUser().getRole() + ")");
            InputHelper.pressEnterToContinue();
            return true;
        } catch (AuthenticationException e) {
            ConsoleUI.printError(e.getMessage());
            InputHelper.pressEnterToContinue();
            return false;
        }
    }

    public void showChangePassword() {
        ConsoleUI.printHeader("CHANGE PASSWORD");
        String oldPwd  = InputHelper.readString("  Current password : ");
        String newPwd  = InputHelper.readString("  New password     : ");
        String confirm = InputHelper.readString("  Confirm new      : ");

        if (!newPwd.equals(confirm)) {
            ConsoleUI.printError("New passwords do not match.");
            InputHelper.pressEnterToContinue();
            return;
        }

        try {
            authService.changePassword(oldPwd, newPwd);
            ConsoleUI.printSuccess("Password changed successfully.");
        } catch (AuthenticationException e) {
            ConsoleUI.printError(e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }
}