package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.cloud.datastore.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.codec.digest.DigestUtils;
import pt.unl.fct.di.apdc.firstwebapp.util.ChangePasswordData;

import java.util.logging.Logger;

@Path("/changePassword")
public class ChangePasswordResource {

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final Logger LOG = Logger.getLogger(ChangePasswordResource.class.getName());

    private final Gson g = new Gson();

    @POST
    @Path("/changePwd")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(ChangePasswordData data) {

        if (data == null || data.token == null || data.newPassword == null
                || data.confirmPassword == null || data.currentPassword == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        if (!data.newPassword.equals(data.confirmPassword)) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Password and its confirmation must be equal.").build();
        }

        Transaction txn = datastore.newTransaction();

        try {
            Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.token);
            Entity tokenEntity = txn.get(tokenKey);

            if (tokenEntity == null) {
                txn.rollback();
                return Response.status(Status.NOT_FOUND).entity("Invalid token.").build();
            }

            String username = tokenEntity.getString("username");
            Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
            Entity userEntity = txn.get(userKey);

            if (userEntity == null) {
                txn.rollback();
                return Response.status(Status.NOT_FOUND).entity("User not found.").build();
            }

            String currentPwdHash = DigestUtils.sha512Hex(data.currentPassword);
            String storedPwd = userEntity.getString("user_pwd");

            if (!storedPwd.equals(currentPwdHash)) {
                txn.rollback();
                return Response.status(Status.BAD_REQUEST)
                        .entity("Current password is not correct.").build();
            }

            String newPwdHash = DigestUtils.sha512Hex(data.newPassword);

            Entity updatedUser = Entity.newBuilder(userEntity)
                    .set("user_pwd", newPwdHash)
                    .build();

            txn.put(updatedUser);
            txn.commit();

            return Response.ok().entity("Password changed successfully.").build();

        } catch (Exception e) {
            LOG.severe("Failed to change password: " + e.getMessage());
            if (txn.isActive()) {
                txn.rollback();
            }
            return Response.serverError().entity("Internal Server Error").build();
        }
    }
}