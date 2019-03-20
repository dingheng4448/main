package planmysem.storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.SecretKey;

/**
 * Manages key storage and load.
 */
public class KeyStorage {

    private char[] pwdArray = "password".toCharArray();

    KeyStorage(String password) {
        this.pwdArray = password.toCharArray();
    }

    /**
     * Saves the secret key.
     */
    private void save(SecretKey key) throws KeyStoreException, IOException, NoSuchAlgorithmException,
            CertificateException {
        //Initialize keystore.
        KeyStore ks = KeyStore.getInstance("JKS");

        //Save key.
        KeyStore.SecretKeyEntry secret = new KeyStore.SecretKeyEntry(key);
        KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(pwdArray);
        ks.setEntry("db-encryption-secret", secret, password);

        try (FileOutputStream fos = new FileOutputStream("newKeyStoreFileName.jks")) {
            ks.store(fos, pwdArray);
        }
    }
}
