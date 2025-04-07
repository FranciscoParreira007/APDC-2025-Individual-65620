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
import pt.unl.fct.di.apdc.firstwebapp.util.ChangeRoleData;

import java.util.logging.Logger;

@Path("/changeRole")
public class ChangeRoleResource {

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

    private final Gson g = new Gson();

    public ChangeRoleResource() {}

    @POST
    @Path("/role")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeUserRole(ChangeRoleData data) {

        if (data == null || data.token == null || data.targetUsername == null || data.role == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required data.").build();
        }

        Transaction txn = datastore.newTransaction();

        Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.token);
        Entity tokenID = txn.get(tokenKey);

        if (tokenID == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Requester not found.").build();
        }

        Key targetKey = userKeyFactory.newKey(data.targetUsername);
        Entity target = datastore.get(targetKey);
        if (target == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Target user not found.").build();
        }

        String requesterRole = tokenID.getString("user_role");
        String newRole = data.role.getDescription();

        switch (requesterRole) {
            case "BACKOFFICE":
                if (newRole.equals("ADMIN") || target.getString("user_role").equals("ADMIN")) {
                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity(Roles.BACKOFFICE.getDescription() + " cannot change roles who are " + Roles.ADMIN.getDescription()
                                    + " or to " + Roles.ADMIN.getDescription() + ".")
                            .build();
                }
                break;
            case "ENDUSER":
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Roles.ENDUSER.getDescription() + " cannot change any roles.")
                        .build();
            case "ADMIN":
                break;
            default:
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Unrecognized requester role, it should either be: " + Roles.ADMIN.getDescription() + ", "
                        + Roles.BACKOFFICE.getDescription() + ", " + Roles.ENDUSER.getDescription() + ", " + Roles.PARTNER.getDescription() + ".")
                        .build();
        }

        Entity updatedTarget = Entity.newBuilder(target)
                .set("user_role", newRole)
                .build();

        datastore.update(updatedTarget);
        txn.put(tokenID);
        txn.commit();

        return Response.ok(g.toJson(updatedTarget)).build();
    }
}
