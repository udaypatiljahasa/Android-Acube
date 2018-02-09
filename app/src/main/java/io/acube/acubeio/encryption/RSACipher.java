/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 03.02.2018
 *
 * Handles the data encryption with RSA.
 *
 *
 */


package io.acube.acubeio.encryption;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.security.auth.x500.X500Principal;

import io.acube.acubeio.dependencies.ActivityContext;

public class RSACipher {

    KeyPairGenerator kpg;
    KeyPair kp;
    PublicKey publicKey;
    PrivateKey privateKey;
    byte[] encryptedBytes, decryptedBytes;
    Cipher cipher, cipher1;
    String encrypted, decrypted;
    KeyStore keyStore;
    Context context;
    byte[] wrappedKey;
    Key unWrappedSecretKey;

    @Inject
    public RSACipher(@ActivityContext Context context) {
        this.context = context;
    }

    /**
     * Generates and Initializes private and public keys
     *
     * @throws Exception
     */
    public void initialize() throws Exception{
        generateKeysAlias();
        getKeysFromKeyStore();
    }

    /**
     * Initializes the Symmetric key
     *
     * @throws Exception
     */
    public void initializeSymmetricKeys() throws Exception{
        SecretKey secretKey = generateDefaultSymmetricKey();
        wrapSymmetricKey(secretKey);
    }

    /**
     * Wraps the Symmetric key
     *
     * @throws Exception
     */
    private void wrapSymmetricKey(SecretKey secretKey) throws Exception{
        Cipher symCipher = Cipher.getInstance(EncryptionConstants.TRANSFORMATIONS);
        symCipher.init(Cipher.WRAP_MODE,publicKey);
        wrappedKey = symCipher.wrap(secretKey);
    }

    /**
     * Returns the String of the symmetric key
     *
     * @throws Exception
     */
    public String getWrappedSymmetricKey(){
        return Base64.encodeToString(wrappedKey, Base64.DEFAULT);
    }

    /**
     * calls unwrappingkey function
     *
     * @throws Exception
     */
    public void unWrapKey(String wrapedKey) throws Exception{
        unWrappingKey( wrapedKey,EncryptionConstants.SYM_CRYPTO_METHOD,Cipher.SECRET_KEY,privateKey);
    }

    /**
     * Unwraps the symmetric key
     *
     * @throws Exception
     */
    private void unWrappingKey(String wrapedKey, String algorithm, int wrrapedKeyType, PrivateKey privateKey) throws Exception{
        byte[] encryptedKeyData = Base64.decode(wrapedKey, Base64.DEFAULT);
        Cipher symCipher1 = Cipher.getInstance(EncryptionConstants.TRANSFORMATIONS);
        symCipher1.init(Cipher.UNWRAP_MODE,privateKey);
        unWrappedSecretKey = symCipher1.unwrap(encryptedKeyData,algorithm,wrrapedKeyType);
    }

    private SecretKey generateDefaultSymmetricKey() throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptionConstants.SYM_CRYPTO_METHOD, "BC");
        return keyGenerator.generateKey();
    }
    /**
     * Generates the private and public keys and stores in android key store
     *
     * @throws Exception
     */

    private void generateKeysAlias() throws Exception{
            keyStore = KeyStore.getInstance(EncryptionConstants.KEY_STORE_PROVIDER);
            keyStore.load(null);
            if (!keyStore.containsAlias(EncryptionConstants.KEY_STORE_ALIAS)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 20);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(this.context)
                        .setAlias(EncryptionConstants.KEY_STORE_ALIAS)
                        .setSubject(new X500Principal(EncryptionConstants.XPRINCIPAL_DITINGUISHED_NAMES))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                kpg = KeyPairGenerator.getInstance(EncryptionConstants.CRYPTO_METHOD, EncryptionConstants.KEY_STORE_PROVIDER);
                kpg.initialize(spec);

                kp = kpg.generateKeyPair();
            }
    }

    /**
     * Fetches the private and public keys from the android keystore
     *
     * @throws Exception
     */
    private void getKeysFromKeyStore() throws Exception{
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(EncryptionConstants.KEY_STORE_ALIAS, null);
        publicKey = privateKeyEntry.getCertificate().getPublicKey();
        privateKey = privateKeyEntry.getPrivateKey();
    }

    /**
     * generates the private and public keys
     *
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private void generateKeyPair() throws NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,BadPaddingException {

        kpg = KeyPairGenerator.getInstance(EncryptionConstants.CRYPTO_METHOD);
        kpg.initialize(EncryptionConstants.CRYPTO_BITS);
        kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
    }

    /**
     * Encrypt plain text to RSA encrypted and Base64 encoded string
     *
     * @param args
     *          args[0] should be plain text that will be encrypted
     *          If args[1] is be, it should be RSA public key to be used as encrypt public key
     * @return a encrypted string that Base64 encoded
     * @throws Exception
     */
    public String encrypt(Object... args) throws Exception {

        String plain = (String) args[0];

        cipher = Cipher.getInstance(EncryptionConstants.TRANSFORMATION_SYMMETRIC);
        cipher.init(Cipher.ENCRYPT_MODE, unWrappedSecretKey);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            encryptedBytes = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
        }else{
            encryptedBytes = cipher.doFinal(plain.getBytes(Charset.forName("UTF-8")));
        }

        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    /**
     * Returns the text after decryption
     *
     * @param result - Encrypted Data to be decrypted
     *
     *
     * @return a encrypted string that Base64 encoded
     * @throws Exception
     */
    public String decrypt(String result) throws Exception {


        cipher1 = Cipher.getInstance(EncryptionConstants.TRANSFORMATION_SYMMETRIC);
        cipher1.init(Cipher.DECRYPT_MODE, unWrappedSecretKey);
        decryptedBytes = cipher1.doFinal(Base64.decode(result, Base64.DEFAULT));
        decrypted = new String(decryptedBytes);

        return decrypted;
    }
}