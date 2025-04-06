package pt.unl.fct.di.apdc.firstwebapp.enums;

public enum Roles {

    ENDUSER("ENDUSER"),
    BACKOFFICE("BACKOFFICE"),
    ADMIN("ADMIN"),
    PARTNER("PARTNER"),;

    private final String description;

    private Roles(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
