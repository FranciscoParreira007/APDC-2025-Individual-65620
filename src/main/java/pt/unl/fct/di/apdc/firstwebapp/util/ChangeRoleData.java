package pt.unl.fct.di.apdc.firstwebapp.util;

import pt.unl.fct.di.apdc.firstwebapp.enums.Roles;

public class ChangeRoleData {

    public String targetUsername;
    public String requesterUsername;
    public String password;
    public Roles role;

    public ChangeRoleData() {}

    public ChangeRoleData(String targetUsername, String requesterUsername,
                          String password, Roles role) {
        this.targetUsername = targetUsername;
        this.requesterUsername = requesterUsername;
        this.password = password;
        this.role = role;
    }
 }
