package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.cloud.datastore.*;
import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.apdc.firstwebapp.enums.AccountStatus;
import pt.unl.fct.di.apdc.firstwebapp.enums.Roles;
import pt.unl.fct.di.apdc.firstwebapp.util.ChangeRoleData;
import pt.unl.fct.di.apdc.firstwebapp.util.ChangeStateData;

import java.util.logging.Logger;

@Path("/changeState")
public class ChangeStateResource {

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

    public ChangeStateResource() {}

    @POST
    @Path("/state")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeUserState(ChangeStateData data) {

        if(data == null || data.requesterUsername == null || data.targetUsername == null || data.state == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required data.").build();
        }

        Key requesterKey = userKeyFactory.newKey(data.requesterUsername);
        Entity requester = datastore.get(requesterKey);
        if (requester == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Requester not found.").build();
        }

        Key targetKey = userKeyFactory.newKey(data.targetUsername);
        Entity target = datastore.get(targetKey);

        if (target == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Target user not found.").build();
        }

        String requesterRole = requester.getString("user_role");
        AccountStatus newState = data.state;


        switch (requesterRole) {
            case "BACKOFFICE":
                if(newState.equals(AccountStatus.SUSPENSE)){
                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Suspended backoffice.")
                            .build();
                }

            case "ADMIN":
                break;

            default:
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid requester role")
                        .build();
        }

        Entity updatedTarget = Entity.newBuilder(target)
                .set("user_account_status", newState.getDescription())
                .build();

        datastore.update(updatedTarget);

        return Response.ok()
                .entity("User " + target.getString("user_name") + " has a new account state.")
                .build();
    }
}
