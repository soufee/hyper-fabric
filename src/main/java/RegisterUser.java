import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.RegistrationException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shomakhov on 11.08.2017.
 */
public class RegisterUser {
    public static FCUser registerUser(String userName, FCUser admin, HFCAClient client, String MspId) throws Exception {
        FCUser user = new FCUser(userName);
        RegistrationRequest registrationRequest = new RegistrationRequest(user.getName(), "org1.department1");
        String enrollmentSecret = client.register(registrationRequest, admin);
        user.setEnrollment(client.enroll(user.getName(), enrollmentSecret));
        user.setMspId(MspId);
        return user;
    }

    public static void register(String userName) {
        try {
            Main.org1_ca = HFCAClient.createNewInstance("http://" + Main.IP + ":7054", Main.properties);
            Main.org1_ca.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            Main.org1_admin = new FCUser("admin");
            Main.org1_admin.setEnrollment(Main.org1_ca.enroll(Main.org1_admin.getName(), Main.ADMINSECRET));
            Main.org1_user = RegisterUser.registerUser(userName, Main.org1_admin, Main.org1_ca, Main.MSPID);
            System.out.println("User " + Main.org1_user.getName() + " registered");
            Set<String> roles = new HashSet<>();
            // roles.add("member");
            roles.add("admin");
            Main.org1_user.setRoles(roles);
        } catch (RegistrationException e) {
            System.out.println(e.getMessage());
            System.out.println("Перезапусти докер");
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }


}


