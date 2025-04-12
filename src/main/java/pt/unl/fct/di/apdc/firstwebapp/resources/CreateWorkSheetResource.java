package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.cloud.datastore.*;
import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import pt.unl.fct.di.apdc.firstwebapp.enums.Adjudicated;
import pt.unl.fct.di.apdc.firstwebapp.enums.Roles;
import pt.unl.fct.di.apdc.firstwebapp.util.CreateWorkSheetData;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/createWorksheet")
public class CreateWorkSheetResource {

    private static final Logger LOG = Logger.getLogger(CreateWorkSheetResource.class.getName());
    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    private final Gson g = new Gson();

    public CreateWorkSheetResource() {}

    @POST
    @Path("/createWorksheet")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createWorksheet(CreateWorkSheetData data){

        LOG.fine("Attempt to create Worksheet: " + data.toString());

        Transaction txn = datastore.newTransaction();

        try {

            Key worksheetKey = datastore.newKeyFactory().setKind("Worksheet").newKey(data.workID);
            Entity worksheet = txn.get(worksheetKey);

            Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.token);
            Entity token = txn.get(tokenKey);
            String role = token.getString("user_role");
            String username = token.getString("username");

            if (worksheet == null) {
                if (!role.equals(Roles.BACKOFFICE.getDescription())) {
                    txn.rollback();
                    return Response.status(Status.FORBIDDEN).entity("Only BACKOFFICE can create worksheets.").build();
                }

                if (data.adjudication.equals(Adjudicated.ADJUDICATED.getDescription())) {
                    if (Objects.equals(data.adjudicationDate, "") || Objects.equals(data.beginningDate, "") || Objects.equals(data.conclusionDate, "") ||
                            Objects.equals(data.entityAccount, "") || Objects.equals(data.adjudicationEntity, "") || Objects.equals(data.companyNIF, "") ||
                            Objects.equals(data.workState, "")) {
                        txn.rollback();
                        return Response.status(Status.BAD_REQUEST)
                                .entity("Missing adjudication-related fields.").build();
                    }
                }

                    Entity.Builder newWorkSheet = Entity.newBuilder(worksheetKey)
                            .set("work_id", data.workID)
                            .set("description", data.description)
                            .set("type", data.type)
                            .set("adjudication", data.adjudication);

                    if (data.adjudication.equals(Adjudicated.ADJUDICATED.getDescription())) {
                        newWorkSheet.set("adjudication_date", data.adjudicationDate)
                                .set("beginning_date", data.beginningDate)
                                .set("conclusion_date", data.conclusionDate)
                                .set("entity_account", data.entityAccount)
                                .set("adjudication_entity", data.adjudicationEntity)
                                .set("company_nif", data.companyNIF)
                                .set("work_state", data.workState)
                                .set("observations", !Objects.equals(data.observations, "") ? data.observations : "");
                    }

                    txn.put(newWorkSheet.build());
                    txn.commit();
                    LOG.info("Worksheet registered " + data.workID);
                    return Response.ok().entity("Worksheet has been registered.").build();

                } else {

                    String currentAdjudication = worksheet.getString("adjudication");
                    String entityAccount = worksheet.getString("entity_account");

                    if(role.equals(Roles.PARTNER.getDescription())) {
                        if(!currentAdjudication.equals(Adjudicated.ADJUDICATED.getDescription())
                        || entityAccount == null || !entityAccount.equals(username)) {

                            txn.rollback();
                            return Response.status(Status.FORBIDDEN)
                                    .entity("People with PARTNER Role type can not update this worksheet.").build();
                        }

                        Entity updated = Entity.newBuilder(worksheet)
                                .set("work_state", data.workState)
                                .set("observations", !Objects.equals(data.observations, "") ? data.observations : "")
                                .build();

                        txn.put(updated);
                        txn.commit();
                        LOG.info("Worksheet state updated by PARTNER: " + data.workID);
                        return Response.ok().entity("Worksheet has been updated.").build();
                    }

                if (!role.equals(Roles.BACKOFFICE.getDescription())) {
                    txn.rollback();
                    return Response.status(Status.FORBIDDEN)
                            .entity("Only BACKOFFICE can update worksheet details.").build();
                }

                Entity.Builder builder = Entity.newBuilder(worksheet)
                        .set("description", data.description)
                        .set("type", data.type)
                        .set("adjudication", data.adjudication);

                if (data.adjudication.equals(Adjudicated.ADJUDICATED.getDescription())) {
                    builder.set("adjudication_date", data.adjudicationDate)
                            .set("beginning_date", data.beginningDate)
                            .set("conclusion_date", data.conclusionDate)
                            .set("entity_account", data.entityAccount)
                            .set("adjudication_entity", data.adjudicationEntity)
                            .set("company_nif", data.companyNIF)
                            .set("work_state", data.workState)
                            .set("observations", data.observations != null ? data.observations : "");
                }

                txn.put(builder.build());
                txn.commit();
                LOG.info("Worksheet updated by BACKOFFICE: " + data.workID);
                return Response.ok().entity("Worksheet was updated.").build();
            }

        } catch (Exception e) {

            LOG.log(Level.ALL, e.toString());

            if (txn.isActive()) {
                txn.rollback();
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();

        } finally {
            if (txn.isActive())
                txn.rollback();
        }

    }
}
