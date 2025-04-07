package pt.unl.fct.di.apdc.firstwebapp.enums;

public enum AccountProfile {
    PUBLIC("público"),
    PRIVATE("privado");

    private final String description;

    AccountProfile(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
