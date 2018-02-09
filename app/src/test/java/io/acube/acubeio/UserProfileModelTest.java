/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 * <p>
 * User Profile model unit test case class
 */

package io.acube.acubeio;

import org.junit.Before;
import org.junit.Test;

import io.acube.acubeio.model.UserProfileModel;

import static junit.framework.Assert.assertEquals;

public class UserProfileModelTest {

    UserProfileModel userProfileModel;

    @Before
    public void setUp(){
        userProfileModel = new UserProfileModel();
        userProfileModel.setId("manjudg");
        userProfileModel.setFirstName("manjudg");
        userProfileModel.setLastName("manjudg");
        userProfileModel.setUserName("manjudg");
        userProfileModel.setPassword("manjudg");
        userProfileModel.setEmail("manjudg");
        userProfileModel.setCountryCode("91");
        userProfileModel.setMobile("manjudg");
        userProfileModel.setGender(2);
        userProfileModel.setDob("manjudg");
        userProfileModel.setProfilePic("manjudg");
    }

    @Test
    public void checkGetters(){
        assertEquals("manjudg",userProfileModel.getId());
        assertEquals("manjudg",userProfileModel.getFirstName());
        assertEquals("manjudg",userProfileModel.getLastName());
        assertEquals("manjudg",userProfileModel.getUserName());
        assertEquals("manjudg",userProfileModel.getPassword());
        assertEquals("manjudg",userProfileModel.getEmail());
        assertEquals("91",userProfileModel.getCountryCode());
        assertEquals("manjudg",userProfileModel.getMobile());
        assertEquals(2,userProfileModel.getGender());
    }
}
