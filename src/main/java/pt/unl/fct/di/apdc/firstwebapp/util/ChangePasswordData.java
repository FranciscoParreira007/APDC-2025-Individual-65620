package pt.unl.fct.di.apdc.firstwebapp.util;

public class ChangePasswordData {

    public String token;
    public String newPassword;
    public String currentPassword;
    public String confirmPassword;

    public ChangePasswordData() {}

    public ChangePasswordData(String token, String currentPwd,
                              String newPassword, String confirmPassword) {
        this.token = token;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
        this.currentPassword = currentPwd;
    }
}
