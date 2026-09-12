package io.kairos.delivery_system.core.utilities;

public class ValidatorUtil {
    private ValidatorUtil() {}

    public static boolean notInteger(String value) {
        try {
            Integer.parseInt(value);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }


    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }

        return phone.matches("^\\+?[0-9]{8,15}$");
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        return email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        );
    }
}
