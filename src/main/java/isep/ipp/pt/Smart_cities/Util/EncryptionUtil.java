package isep.ipp.pt.Smart_cities.Util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Optional;

@Component
public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    //TODO AS for now this going to be like this
    private static final String SECRET_KEY = "b270d27936999e591589f72d98e959b1";

    public Optional<String> decrypt(String encryptedData){
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return Optional.of(new String(decryptedBytes));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<String> encrypt(String data)  {
        try {


        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Optional.of(Base64.getEncoder().encodeToString(encryptedBytes));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }
}
