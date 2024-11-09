package isep.ipp.pt.Smart_cities.Util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EncryptionUtilTest {
    @InjectMocks
    EncryptionUtil encryptionUtil;



    @ParameterizedTest
    @CsvSource({
            "test, Kt8L2Y4aP+KeRJqeSFHdWg==",
            "test1, FGo0I6QjtSlH30bgBx0Sqw=="
    })
    void decrypt(String name, String encryptedData) {
        Optional<String> decryptedData = encryptionUtil.decrypt(encryptedData);
        assertEquals(name, decryptedData.get());
    }

    @ParameterizedTest
    @CsvSource({
            "test, Kt8L2Y4aP+KeRJqeSFHdWg==",
            "test1, FGo0I6QjtSlH30bgBx0Sqw=="
    })
    void encrypt(String name, String encryptedData) {
        Optional<String> encrypted = encryptionUtil.encrypt(name);
        assertEquals(encryptedData, encrypted.get());
    }

    @ParameterizedTest
    @CsvSource({
            "testa, Kt8L2Y4aP+KeRJqeSFHdWg==",
            "test1a, FGo0I6QjtSlH30bgBx0Sqw==",
            "testdddd, FGo0I6QjtSlH30bgBx0Sqw==",
            "test1fdgsg, Kt8L2Y4aP+KeRJqeSFHdWg=="
    })
    void shouldTestIfADiferentStringIsEncrypted(String name, String encryptedData) {
        Optional<String> encrypted = encryptionUtil.encrypt(name);
        assertNotEquals(encryptedData, encrypted.get());
    }

    @ParameterizedTest
    @CsvSource({
            "testff, Kt8L2Y4aP+KeRJqeSFHdWg==",
            "test1gg, FGo0I6QjtSlH30bgBx0Sqw==",
            "testdddd, FGo0I6QjtSlH30bgBx0Sqw==",
            "test1fdgsg, Kt8L2Y4aP+KeRJqeSFHdWg=="
    })
    void shouldTestIfADiferentStringIsDecrypted(String name, String encryptedData) {
        Optional<String> decryptedData = encryptionUtil.decrypt(encryptedData);
        assertNotEquals(name, decryptedData.get());
    }
}