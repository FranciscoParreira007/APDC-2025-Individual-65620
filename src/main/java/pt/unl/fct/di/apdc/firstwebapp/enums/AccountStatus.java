package pt.unl.fct.di.apdc.firstwebapp.enums;

public enum AccountStatus {

    ACTIVE("ATIVADA"),
    SUSPENSE("SUSPENSA"),
    DEACTIVATED("DESATIVADA");

    private final String description;

    private AccountStatus(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
