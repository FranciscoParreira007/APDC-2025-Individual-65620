package pt.unl.fct.di.apdc.firstwebapp.util;

import pt.unl.fct.di.apdc.firstwebapp.enums.Roles;

public class ChangeRoleData {

    public String targetUsername;
    public String requesterUsername;
    public Roles role;

    public ChangeRoleData() {}

    public ChangeRoleData(String targetUsername, String requesterUsername,
                           Roles role) {
        this.targetUsername = targetUsername;
        this.requesterUsername = requesterUsername;
        this.role = role;
    }
 }
