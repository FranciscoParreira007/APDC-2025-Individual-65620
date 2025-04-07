package pt.unl.fct.di.apdc.firstwebapp.util;

import pt.unl.fct.di.apdc.firstwebapp.enums.AccountStatus;

public class ChangeStateData {

    public AccountStatus state;
    public String targetUsername;
    public String requesterUsername;

    public ChangeStateData() {}


    public ChangeStateData(String targetUsername, String requesterUsername,
                           AccountStatus state) {
        this.targetUsername = targetUsername;
        this.requesterUsername = requesterUsername;
        this.state = state;
    }

}
