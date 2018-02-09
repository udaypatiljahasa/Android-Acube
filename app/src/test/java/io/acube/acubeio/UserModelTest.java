/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 * <p>
 * User model unit test case class.
 */

package io.acube.acubeio;

import org.junit.Before;
import org.junit.Test;

import io.acube.acubeio.model.UserModel;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class UserModelTest {

    UserModel userModel;

    @Before
    public void setUp(){
        userModel = new UserModel();
        userModel.setId("vbv");
        userModel.setAccessToken("vbcbv");
        userModel.setRefreshToken("xbvbv");
    }

    @Test
    public void checkGetters(){
        assertEquals("vbv",userModel.getId());
        assertEquals("vbcbv",userModel.getAccessToken());
        assertEquals("xbvbv",userModel.getRefreshToken());
    }

}
