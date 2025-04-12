package pt.unl.fct.di.apdc.firstwebapp.util;

import pt.unl.fct.di.apdc.firstwebapp.enums.Adjudicated;

public class CreateWorkSheetData {
    public String token;
    public String workID;
    public String description;
    public String type;
    public String adjudication;
    public String adjudicationDate;
    public String beginningDate;
    public String conclusionDate;
    public String entityAccount;
    public String adjudicationEntity;
    public String companyNIF;
    public String workState;
    public String observations;

    public CreateWorkSheetData () {}

    public CreateWorkSheetData(String token, String workID, String description, String type, String adjudication,
                               String adjudicationDate, String beginningDate, String conclusionDate,
                               String entityAccount, String adjudicationEntity, String companyNIF,
                               String workState, String observations) {

        this.token = token;
        this.workID = workID;
        this.description = description;
        this.type = type;
        this.adjudication = adjudication;

        if(adjudication.equals(Adjudicated.ADJUDICATED.getDescription())){
            this.adjudicationDate = adjudicationDate;
            this.beginningDate = beginningDate;
            this.conclusionDate = conclusionDate;
            this.entityAccount = entityAccount;
            this.adjudicationEntity = adjudicationEntity;
            this.companyNIF = companyNIF;
            this.workState = workState;
            this.observations = observations;
        }
        else {
            this.adjudicationDate = "";
            this.beginningDate = "";
            this.conclusionDate = "";
            this.entityAccount = "";
            this.adjudicationEntity = "";
            this.companyNIF = "";
            this.workState = "";
            this.observations = "";
        }
    }
}
