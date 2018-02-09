/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 * <p>
 * RSA Cipher unit test case class.
 */

package io.acube.acubeio;

import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Test;

import io.acube.acubeio.encryption.RSACipher;

import static junit.framework.Assert.assertEquals;


public class RSACipherUnitTest {

    RSACipher rsaCipher;

    @Before
    public void setUp() {
        rsaCipher = new RSACipher(new MockContext());
    }

    @Test
    public void checkWrapedKey(){
        try {
            String symKey = rsaCipher.getWrappedSymmetricKey();
            assertEquals("fffsdds", symKey);
        }catch (Exception e){

        }
    }

    @Test
    public void keyGeneration() {
        try {
            rsaCipher.initialize();
            rsaCipher.initializeSymmetricKeys();
            String symKey = rsaCipher.getWrappedSymmetricKey();
            System.out.println(symKey);
        }catch (Exception e){

        }
    }
    @Test
    public void encryption() {
        try {
            String encrypted = rsaCipher.encrypt("dfdf");
            assertEquals("fffsdds", encrypted);
        }catch (Exception e){

        }
    }
    @Test
    public void decryption(){
        try {
            String decrypted = rsaCipher.decrypt("dfdf");
            assertEquals("fffsdds", decrypted);
        }catch (Exception e){

        }
    }
}
