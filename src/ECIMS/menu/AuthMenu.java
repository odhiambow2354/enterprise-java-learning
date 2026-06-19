package ECIMS.menu;

import exception.AuthenticationException;
import service.AuthService;
import utility.ConsoleUI;
import utility.InputHelper;

/**
 * Handles authentication-related screens:
 * - Login
 * - Change Password
 */
public class AuthMenu {

    private final AuthService authService;

    /**
     * Constructor.
     *
     * @param authService Authentication service.
     */
    public AuthMenu(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Displays the login screen.
     *
     * @return true if login succeeds, otherwise false.
     */
    public boolean showLogin() {
        ConsoleUI.printHeader("NATURESURF INSURANCE — LOGIN");

        String username = InputHelper.readString("  Username : ");
        String password = InputHelper.readString("  Password : ");

        try {
            authService.login(username, password);

            var currentUser = authService.getCurrentUser();

            ConsoleUI.printSuccess(
                    String.format(
                            "Welcome, %s (%s)",
                            currentUser.getFullName(),
                            currentUser.getRole()
                    )
            );

            return true;

        } catch (AuthenticationException e) {
            ConsoleUI.printError(e.getMessage());
            return false;

        } finally {
            InputHelper.pressEnterToContinue();
        }
    }

    /**
     * Displays the change password screen.
     */
    public void showChangePassword() {
        ConsoleUI.printHeader("CHANGE PASSWORD");

        String currentPassword = InputHelper.readString("  Current password : ");
        String newPassword = InputHelper.readString("  New password     : ");
        String confirmPassword = InputHelper.readString("  Confirm new      : ");

        if (!validateNewPasswords(newPassword, confirmPassword)) {
            InputHelper.pressEnterToContinue();
            return;
        }

        try {
            authService.changePassword(currentPassword, newPassword);
            ConsoleUI.printSuccess("Password changed successfully.");

        } catch (AuthenticationException e) {
            ConsoleUI.printError(e.getMessage());

        } finally {
            InputHelper.pressEnterToContinue();
        }
    }

    /**
     * Validates that the new password and confirmation match.
     *
     * @param newPassword      The new password.
     * @param confirmPassword  The confirmation password.
     * @return true if they match, otherwise false.
     */
    private boolean validateNewPasswords(String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            ConsoleUI.printError("New passwords do not match.");
            return false;
        }
        return true;
    }
}