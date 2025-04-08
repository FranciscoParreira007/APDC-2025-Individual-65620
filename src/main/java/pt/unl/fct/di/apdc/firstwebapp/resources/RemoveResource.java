package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.cloud.datastore.*;
import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.apdc.firstwebapp.enums.Roles;
import pt.unl.fct.di.apdc.firstwebapp.util.RemoveData;

@Path("/remove")
public class RemoveResource {

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

    private final Gson g = new Gson();

    @POST
    @Path("/removeUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeUserRole(RemoveData data) {

        Transaction txn = datastore.newTransaction();

        if (data == null || data.token == null || data.target == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required data.").build();
        }

        Key tokenKey = datastore.newKeyFactory()
                .setKind("Token")
                .newKey(data.token);
        Entity tokenID = txn.get(tokenKey);

        if (tokenID == null) {
            txn.rollback();
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Requester not found.")
                    .build();
        }

        Entity target = null;
        Key targetKey = null;

        Key possibleKey = userKeyFactory.newKey(data.target);
        Entity possibleTarget = datastore.get(possibleKey);

        if (possibleTarget != null) {
            target = possibleTarget;
            targetKey = possibleKey;
        } else {
            Query<Entity> query = Query.newEntityQueryBuilder()
                    .setKind("User")
                    .setFilter(StructuredQuery.PropertyFilter.eq("user_email", data.target))
                    .build();

            QueryResults<Entity> results = datastore.run(query);
            if (results.hasNext()) {
                target = results.next();
                targetKey = target.getKey();
            }
        }

        if (target == null || targetKey == null) {
            txn.rollback();
            return Response.status(Response.Status.NOT_FOUND).entity("Target user not found.").build();
        }

        String requesterRole = tokenID.getString("user_role");
        String targetRole = target.getString("user_role");

        switch (requesterRole) {
            case "BACKOFFICE":
                if (!targetRole.equals(Roles.ENDUSER.getDescription())
                        && !targetRole.equals(Roles.PARTNER.getDescription())) {
                    txn.rollback();
                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Invalid target.")
                            .build();
                }
                break;

            case "ADMIN":
                break;

            default:
                txn.rollback();
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Unauthorized access.")
                        .build();
        }

        String userIDTarget = target.getString("user_name");

        txn.delete(targetKey);

        Query<Entity> tokenQuery = Query.newEntityQueryBuilder()
                .setKind("Token")
                .setFilter(StructuredQuery.PropertyFilter.eq("username", userIDTarget))
                .build();

        QueryResults<Entity> tokenResults = txn.run(tokenQuery);
        while (tokenResults.hasNext()) {
            Entity tokenEntity = tokenResults.next();
            txn.delete(tokenEntity.getKey());
        }

        txn.commit();

        return Response.ok()
                .entity("User has been removed.")
                .build();
    }
}
