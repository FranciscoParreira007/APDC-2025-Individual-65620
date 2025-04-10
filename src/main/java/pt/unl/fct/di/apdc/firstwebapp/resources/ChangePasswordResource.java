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
    private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());

    private final Gson g = new Gson();

    @POST
    @Path("/changePwd")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(ChangePasswordData data) {

        Transaction txn = datastore.newTransaction();

        if(data == null || data.token == null || data.newPassword == null
            || data.confirmPassword == null || data.currentPassword == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        String newPwd = data.newPassword;
        String confirmation = data.confirmPassword;

        if(!newPwd.equals(confirmation)) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Password and its confirmation must be equal.").build();
        }

        Key targetTokenKey = datastore.newKeyFactory()
                .setKind("Token")
                .newKey(data.token);
        Entity targetTokenEntity = txn.get(targetTokenKey);

        if(targetTokenEntity == null) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Invalid token.").build();
        }

        String currentPassword = targetTokenEntity.getString("user_pwd");
        String currentPwdHash = DigestUtils.sha256Hex(data.currentPassword);

        if(!currentPassword.equals(currentPwdHash)) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Current Password is not correct.")
                    .build();
        }

        String hashPwd = DigestUtils.sha512Hex(newPwd);

        Entity updatedUser = Entity.newBuilder(targetTokenEntity)
                .set("user_pwd", hashPwd)
                .build();

        datastore.put(updatedUser);
        txn.put(updatedUser);
        txn.commit();

        return Response.ok()
                .entity("Password changed successfully.")
                .build();
    }
}
