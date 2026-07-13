package cp2.cp2motorphpayroll;


import javax.swing.*;

public class Validation {

    //ACCOUNTS - add new accounts here:
    // username, password, role
    private static final String[][] ACCOUNTS = {
            {"employee", "12345", "employee"},
            {"payroll_staff", "12345", "payroll_staff"},
            {"group13", "12345", "employee"},
    };

    //VALIDATE PASSWORD
    static String ValidatePassword(String usernameInput,
                                   String passwordInput) {
        if (usernameInput == null || passwordInput == null) {
            return null;
        }
        for (String[] account : ACCOUNTS) {
            if (account[0].equals(usernameInput)
                    && account[1].equals(passwordInput)) {
                return account[2];
            }
        }
        return null;
    }

    // HELPER — check if a username exists in the registry
    static boolean usernameExists(String usernameInput) {
        for (String[] account : ACCOUNTS) {
            if (account[0].equals(usernameInput)) {
                return true;
            }
        }
        return false;
    }

    static boolean onlyIntegers(String content, int limit) {
        if (content.length() != limit && limit != 0) {
            return false;
        }
        if (!content.matches("\\d+")) {
            return false;
        }
        return true;
    }
    
    static boolean genericValidator(String input, String format, String separator) {
        String[] chunkedFormat = format.split(separator, -1);
        String[] chunkedInput = input.split(separator, -1);

        if (chunkedInput.length != chunkedFormat.length) {
            return false;
        }

        for (int i = 0; i < chunkedFormat.length; i++) {
            System.out.println("-----");
            System.out.println(chunkedInput[i]);
            System.out.println(chunkedFormat[i]);
            if (!(onlyIntegers(chunkedInput[i], chunkedFormat[i].length()))) {
                return false;
            }
        }
        return true;
    }


    // For SSS numbers: xx-xxxxxxx-x
    static boolean validateSSS(String input) {
        if (input == null || input.isEmpty()) return false;
        return input.matches("\\d{2}-\\d{7}-\\d{1}");
    }


    // For TIN numbers: xxxx-xxxx-xxxx
   static boolean validateTIN(String input) {
        if (input == null || input.isEmpty()) return false;
        return input.matches("\\d{3}-\\d{3}-\\d{3}-\\d{3}");
    }
    // FOr Pag-IBIG Numbers xxxx-xxxx-xxxx
    static boolean validatePagIbig(String input) {
        return onlyIntegers(input, 12);
    }
    // For PhilHealth numbers: 12 digits, no delimiter
    static boolean validatePhilHealth(String input) {
        return onlyIntegers(input, 12);
    }

    // For dates in MM/DD/YYYY format
    static boolean validateDate(String input) {
        return genericValidator(input, "xx/xx/xxxx", "/");
    }

    static String processJTextField(JTextField field) {
        return field.getText();
    }
    static boolean validatePhoneNumber(String input) {
        if (input == null || input.isEmpty()) return false;
        return input.matches("\\d{3}-\\d{3}-\\d{3}");
    }
    static boolean validateDouble(String input) {
        if (input == null || input.isEmpty()) return false;
        // Matches optional digits before dot, dot, up to 2 decimal places
        // Also accepts whole numbers with no decimal point
        return input.matches("\\d+(\\.\\d{1,2})?");
    }

}
