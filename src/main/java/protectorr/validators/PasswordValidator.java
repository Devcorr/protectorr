package protectorr.validators;

public class PasswordValidator {

    private String password;

    public PasswordValidator(String sPassword) {
        password = sPassword;
    }

    public boolean isValidPassword() {
        if (this.getPassword() != null) {
            return !(this.getPassword().isEmpty() && this.getPassword().trim().contains(" "));
        }
        return false;
    }

    public String getPassword() {
        return password;
    }
}
