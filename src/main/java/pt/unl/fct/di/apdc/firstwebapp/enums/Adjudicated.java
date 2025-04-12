package pt.unl.fct.di.apdc.firstwebapp.enums;

public enum Adjudicated {

    ADJUDICATED("ADJUDICADO"),
    NOT_ADJUDICATED("NÃO ADJUDICADO");

    private final String description;

    Adjudicated(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
