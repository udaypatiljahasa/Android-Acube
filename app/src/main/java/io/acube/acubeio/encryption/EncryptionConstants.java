/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 *
 * Data Encryption Constants
 *
 */

package io.acube.acubeio.encryption;

public interface EncryptionConstants {
    String KEY_STORE_ALIAS = "Jahasa";
    String KEY_SYMMETRIC_ALIAS = "Jahasa_Sym";
    String SYM_CRYPTO_METHOD = "AES";
    String KEY_STORE_PROVIDER = "AndroidKeyStore";
    String CRYPTO_METHOD = "RSA";
    int CRYPTO_BITS = 1024;
    String XPRINCIPAL_DITINGUISHED_NAMES = "CN=Sample Name, O=Android Authority";
    String TRANSFORMATIONS = "RSA/ECB/PKCS1Padding";
    String TRANSFORMATION_SYMMETRIC = "AES/ECB/PKCS5Padding";
}
