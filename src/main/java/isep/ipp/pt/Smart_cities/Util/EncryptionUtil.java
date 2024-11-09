package isep.ipp.pt.Smart_cities.Util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
public class EncryptionUtil {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    //TODO AS for now this going to be like this
    private static final String SECRET_KEY = "b270d27936999e591589f72d98e959b1";
    private static final byte[] IV = new byte[16];

    private SecretKeySpec getKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
    }

    public Optional<String> decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(IV);
            cipher.init(Cipher.DECRYPT_MODE, getKey(), ivSpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return Optional.of(new String(decryptedBytes, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<String> encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(IV); // Use the same IV as the front-end
            cipher.init(Cipher.ENCRYPT_MODE, getKey(), ivSpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Optional.of(Base64.getEncoder().encodeToString(encryptedBytes));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }
}
