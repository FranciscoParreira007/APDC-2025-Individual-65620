package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.cloud.datastore.*;
import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pt.unl.fct.di.apdc.firstwebapp.util.SessionLogoutData;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


@Path("/sessionLogout")
public class SessionLogoutResource {

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

    private final Gson g = new Gson();

    public SessionLogoutResource() {}

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sessionLogout(SessionLogoutData data) {

        if(data.token == null) {
            return Response.status(Status.BAD_REQUEST)
                    .build();
        }

        Transaction txn = datastore.newTransaction();

        Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.token);

        txn.delete(tokenKey);
        txn.commit();
        return Response.ok().entity("User Logged out.").build();
    }
}
