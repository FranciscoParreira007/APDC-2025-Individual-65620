package pt.unl.fct.di.apdc.firstwebapp.resources;

import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.apdc.firstwebapp.util.ChangeAccountAttributesData;

import com.google.cloud.datastore.*;
import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

import java.util.Map;
import java.util.logging.Logger;


@Path("/changeAttributes")
public class ChangeAccountAttributesResource {

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());

    private final Gson g = new Gson();


    public ChangeAccountAttributesResource() {}


    @POST
    @Path("/changeAccountAttributes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ChangeAccountAttributes(ChangeAccountAttributesData data) {

        Transaction txn = datastore.newTransaction();

        if (data == null || data.requesterToken.isEmpty() || data.targetToken.isEmpty()
                || data.attributes == null) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Missing required data.")
                    .build();
        }

        try {
            Key requesterTokenKey = datastore.newKeyFactory()
                    .setKind("Token")
                    .newKey(data.requesterToken);
            Entity requesterTokenEntity = txn.get(requesterTokenKey);

            Key targetTokenKey = datastore.newKeyFactory()
                    .setKind("Token")
                    .newKey(data.targetToken);
            Entity targetTokenEntity = txn.get(targetTokenKey);

            if (requesterTokenEntity == null || targetTokenEntity == null) {
                txn.rollback();
                return Response.status(Status.UNAUTHORIZED)
                        .entity("Requester or target not found.")
                        .build();
            }

            String requesterUsername = requesterTokenEntity.getString("username");
            String targetUsername = targetTokenEntity.getString("username");

            Key targetUserKey = datastore.newKeyFactory()
                    .setKind("User")
                    .newKey(targetUsername);
            Entity targetUser = txn.get(targetUserKey);

            if (targetUser == null) {
                txn.rollback();
                return Response.status(Status.NOT_FOUND)
                        .entity("Target user not found.")
                        .build();
            }

            String requesterRole = requesterTokenEntity.getString("user_role");
            String targetRole = targetTokenEntity.getString("user_role");

            Entity.Builder updatedBuilder = Entity.newBuilder(targetUser);

            for (Map.Entry<String, String> entry : data.attributes.entrySet()) {
                String attributeKey = entry.getKey();
                String attributeValue = entry.getValue();

                switch (requesterRole) {
                    case "ENDUSER" -> {
                        if (requesterUsername.equals(targetUsername)) {
                            if (!attributeKey.equals("user_name") && !attributeKey.equals("user_email")
                                    && !attributeKey.equals("user_complete_name") && !attributeKey.equals("user_role")
                                    && !attributeKey.equals("user_status")) {
                                updatedBuilder.set(attributeKey, attributeValue);
                            } else {
                                LOG.warning("ENDUSER cannot change restricted attributes.");
                            }
                        } else {
                            LOG.warning("ENDUSER cannot change other users.");
                        }
                    }
                    case "BACKOFFICE" -> {
                        if (!requesterUsername.equals(targetUsername)) {
                            if (targetRole.equals("ENDUSER") || targetRole.equals("PARTNER")) {
                                if (!attributeKey.equals("user_name") && !attributeKey.equals("user_email")) {
                                    updatedBuilder.set(attributeKey, attributeValue);
                                } else {
                                    LOG.warning("BACKOFFICE cannot change user_name or user_email of others.");
                                }
                            } else {
                                LOG.warning("BACKOFFICE cannot change other BACKOFFICE users.");
                            }
                        } else {
                            if (!attributeKey.equals("user_name") && !attributeKey.equals("user_email")) {
                                updatedBuilder.set(attributeKey, attributeValue);
                            }
                        }
                    }
                    case "ADMIN" -> {
                        updatedBuilder.set(attributeKey, attributeValue);
                    }
                    default -> {
                        LOG.warning("Invalid role or insufficient permissions.");
                    }
                }
            }

            txn.put(updatedBuilder.build());
            txn.commit();
            return Response.ok().entity("User has been updated.").build();

        } catch (Exception e) {
            if (txn.isActive()) {
                txn.rollback();
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error updating user.").build();
        }
    }

}
