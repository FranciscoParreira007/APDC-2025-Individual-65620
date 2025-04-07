package pt.unl.fct.di.apdc.firstwebapp.util;

import pt.unl.fct.di.apdc.firstwebapp.enums.Roles;

public class ChangeRoleData {

    public String targetUsername;
    public String token;
    public Roles role;

    public ChangeRoleData() {}

    public ChangeRoleData(String targetUsername, String token,
                           Roles role) {
        this.targetUsername = targetUsername;
        this.token = token;
        this.role = role;
    }
 }
