package pt.unl.fct.di.apdc.firstwebapp.util;

import pt.unl.fct.di.apdc.firstwebapp.enums.AccountStatus;

public class ChangeStateData {

    public AccountStatus state;
    public String targetUsername;
    public String token;

    public ChangeStateData() {}


    public ChangeStateData(String targetUsername, String token,
                           AccountStatus state) {
        this.targetUsername = targetUsername;
        this.token = token;
        this.state = state;
    }

}
