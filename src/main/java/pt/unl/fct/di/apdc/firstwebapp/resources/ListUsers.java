package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.cloud.datastore.*;
import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import pt.unl.fct.di.apdc.firstwebapp.enums.AccountProfile;
import pt.unl.fct.di.apdc.firstwebapp.enums.AccountStatus;
import pt.unl.fct.di.apdc.firstwebapp.enums.Roles;
import pt.unl.fct.di.apdc.firstwebapp.util.ListUsersData;

import java.util.*;


@Path("/list")
public class ListUsers {

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    private final Gson g = new Gson();

    @POST
    @Path("/listUsers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUsers(ListUsersData data) {

        Transaction txn = datastore.newTransaction();

        if (data == null || data.token.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .build();
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

        String requesterRole = tokenID.getString("user_role");
        List<Map<String, String>> users = new ArrayList<>();
        Map<String, String> usersInfo = new HashMap<>();

            switch (requesterRole) {
                case "ADMIN" -> {
                    Query<Entity> query = Query.newEntityQueryBuilder()
                            .setKind("User").build();

                    QueryResults<Entity> results = datastore.run(query);

                    while (results.hasNext()) {
                        Entity user = results.next();
                        usersInfo = new HashMap<>();

                        if (Objects.equals(user.getString("user_name"), "")) {
                            usersInfo.put("user_name", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_name", user.getString("user_name"));
                        }

                        if (Objects.equals(user.getString("user_email"), "")) {
                            usersInfo.put("user_email", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_email", user.getString("user_email"));
                        }

                        if (Objects.equals(user.getString("user_complete_name"), "")) {
                            usersInfo.put("user_complete_name", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_complete_name", user.getString("user_complete_name"));
                        }

                        if (Objects.equals(user.getString("user_account_profile"), "")) {
                            usersInfo.put("user_account_profile", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_account_profile", user.getString("user_account_profile"));
                        }

                        if (Objects.equals(user.getString("user_account_status"), "")) {
                            usersInfo.put("user_account_status", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_account_status", user.getString("user_account_status"));
                        }

                        if (Objects.equals(user.getString("user_address"), "")) {
                            usersInfo.put("user_address", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_address", user.getString("user_address"));
                        }

                        if (Objects.equals(user.getString("user_cc_number"), "")) {
                            usersInfo.put("user_cc_number", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_cc_number", user.getString("user_cc_number"));
                        }

                        if (Objects.equals(user.getString("user_nif"), "")) {
                            usersInfo.put("user_nif", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_nif", user.getString("user_nif"));
                        }

                        if (Objects.equals(user.getString("user_nif_work_place"), "")) {
                            usersInfo.put("user_nif_work_place", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_nif_work_place", user.getString("user_nif_work_place"));
                        }

                        if (Objects.equals(user.getString("user_phone"), "")) {
                            usersInfo.put("user_phone", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_phone", user.getString("user_phone"));
                        }

                        if (Objects.equals(user.getString("user_pwd"), "")) {
                            usersInfo.put("user_pwd", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_pwd", user.getString("user_pwd"));
                        }

                        if (Objects.equals(user.getString("user_role"), "")) {
                            usersInfo.put("user_role", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_role", user.getString("user_role"));
                        }

                        if (Objects.equals(user.getString("user_work_function"), "")) {
                            usersInfo.put("user_work_function", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_work_function", user.getString("user_work_function"));
                        }

                        if (Objects.equals(user.getString("user_work_place"), "")) {
                            usersInfo.put("user_work_place", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_work_place", user.getString("user_work_place"));
                        }
                        users.add(usersInfo);
                    }
                }

                case "ENDUSER" -> {
                    Query<Entity> query = Query.newEntityQueryBuilder()
                            .setKind("User")
                            .setFilter(StructuredQuery.PropertyFilter.eq("user_role", Roles.ENDUSER.getDescription()))
                            .setFilter(StructuredQuery.PropertyFilter.eq("user_account_status", AccountStatus.ACTIVE.getDescription()))
                            .setFilter(StructuredQuery.PropertyFilter.eq("user_account_profile", AccountProfile.PUBLIC.getDescription()))
                            .build();
                    QueryResults<Entity> results = datastore.run(query);

                    while (results.hasNext()) {
                        Entity user = results.next();
                        usersInfo = new HashMap<>();

                        if(Objects.equals(user.getString("user_name"), "")) {
                            usersInfo.put("user_name", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_name", user.getString("user_name"));
                        }
                        if (Objects.equals(user.getString("user_email"), "")) {
                            usersInfo.put("user_email", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_email", user.getString("user_email"));
                        }

                        if (Objects.equals(user.getString("user_complete_name"), "")) {
                            usersInfo.put("user_complete_name", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_complete_name", user.getString("user_complete_name"));
                        }
                        users.add(usersInfo);
                    }
                }
                case "BACKOFFICE" -> {

                    Query<Entity> query = Query.newEntityQueryBuilder()
                            .setKind("User")
                            .setFilter(StructuredQuery.PropertyFilter.eq("user_role", Roles.ENDUSER.getDescription()))
                            .build();
                    QueryResults<Entity> results = datastore.run(query);

                    while (results.hasNext()) {
                        Entity user = results.next();
                        usersInfo = new HashMap<>();

                        if (Objects.equals(user.getString("user_name"), "")) {
                            usersInfo.put("user_name", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_name", user.getString("user_name"));
                        }

                        if (Objects.equals(user.getString("user_email"), "")) {
                            usersInfo.put("user_email", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_email", user.getString("user_email"));
                        }

                        if (Objects.equals(user.getString("user_complete_name"), "")) {
                            usersInfo.put("user_complete_name", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_complete_name", user.getString("user_complete_name"));
                        }

                        if (Objects.equals(user.getString("user_account_profile"), "")) {
                            usersInfo.put("user_account_profile", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_account_profile", user.getString("user_account_profile"));
                        }

                        if (Objects.equals(user.getString("user_account_status"), "")) {
                            usersInfo.put("user_account_status", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_account_status", user.getString("user_account_status"));
                        }

                        if (Objects.equals(user.getString("user_address"), "")) {
                            usersInfo.put("user_address", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_address", user.getString("user_address"));
                        }

                        if (Objects.equals(user.getString("user_cc_number"), "")) {
                            usersInfo.put("user_cc_number", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_cc_number", user.getString("user_cc_number"));
                        }

                        if (Objects.equals(user.getString("user_nif"), "")) {
                            usersInfo.put("user_nif", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_nif", user.getString("user_nif"));
                        }

                        if (Objects.equals(user.getString("user_nif_work_place"), "")) {
                            usersInfo.put("user_nif_work_place", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_nif_work_place", user.getString("user_nif_work_place"));
                        }

                        if (Objects.equals(user.getString("user_phone"), "")) {
                            usersInfo.put("user_phone", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_phone", user.getString("user_phone"));
                        }

                        if (Objects.equals(user.getString("user_pwd"), "")) {
                            usersInfo.put("user_pwd", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_pwd", user.getString("user_pwd"));
                        }

                        if (Objects.equals(user.getString("user_role"), "")) {
                            usersInfo.put("user_role", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_role", user.getString("user_role"));
                        }

                        if (Objects.equals(user.getString("user_work_function"), "")) {
                            usersInfo.put("user_work_function", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_work_function", user.getString("user_work_function"));
                        }

                        if (Objects.equals(user.getString("user_work_place"), "")) {
                            usersInfo.put("user_work_place", "NOT DEFINED");
                        } else {
                            usersInfo.put("user_work_place", user.getString("user_work_place"));
                        }

                        users.add(usersInfo);
                    }
                }

                default -> {
                    txn.rollback();
                    return Response.status(Status.BAD_REQUEST)
                            .build();
                }
            }
            txn.commit();

        return Response.ok(g.toJson(users)).build();

    }
}
