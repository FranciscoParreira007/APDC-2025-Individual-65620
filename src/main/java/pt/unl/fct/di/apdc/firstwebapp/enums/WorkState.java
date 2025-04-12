package pt.unl.fct.di.apdc.firstwebapp.enums;

public enum WorkState {

    NOT_INITIATED("NÃO INICIADO"),
    ON_COURSE("EM CURSO"),
    CONCLUDED("CONCLUÍDO");

    private final String description;

    WorkState(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
