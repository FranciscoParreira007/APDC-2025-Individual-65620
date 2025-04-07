package pt.unl.fct.di.apdc.firstwebapp.listener;

import com.google.cloud.datastore.*;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.commons.codec.digest.DigestUtils;
import pt.unl.fct.di.apdc.firstwebapp.resources.RegisterResource;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class DeployRoot implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());
    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Transaction txn = datastore.newTransaction();
        try{
            Key userKey = datastore.newKeyFactory().setKind("User").newKey("fa");
            Entity user = txn.get(userKey);

            if(user != null) {
                txn.rollback();
            }

            Entity newUser = Entity.newBuilder(userKey)
                    .set("user_name", "fa")
                    .set("user_complete_name", "Francisco Parreira")
                    .set("user_pwd", DigestUtils.sha512Hex("Password*123!"))
                    .set("user_email", "faabparreira@gmail.com")
                    .set("user_phone", "234365866")
                    .set("user_account_profile", "privado")
                    .set("user_cc_number", "513675485")
                    .set("user_role", "ADMIN")
                    .set("user_nif", "2161562412")
                    .set("user_work_place", "FCT NOVA")
                    .set("user_work_function", "Estudante")
                    .set("user_address", "Rua dos Caçadores, Canaviais")
                    .set("user_nif_work_place", "1321512235326")
                    .set("user_account_status", "DESATIVADA")
                    .build();

            txn.put(newUser);
            txn.commit();
            LOG.info("User registered ");

        } catch(DatastoreException e) {
            LOG.log(Level.ALL, e.toString());

            if (txn.isActive()) {
                txn.rollback();
            }
            LOG.info(e.toString());

        } finally {
            if (txn.isActive())
                txn.rollback();
        }
    }
}
