/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 * <p>
 * Database unit test case class.
 */


package io.acube.acubeio;


import android.database.sqlite.SQLiteDatabase;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.acube.acubeio.helper.DBHelper;
import io.acube.acubeio.model.SymmetricKeyModel;
import io.acube.acubeio.model.UserModel;
import io.acube.acubeio.model.UserProfileModel;


import static junit.framework.Assert.assertEquals;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ DBHelper.class })
public class DatabaseUnitTest{


    DBHelper dbHelper;

    @Mock
    SQLiteDatabase sqLiteDatabase;


    @Before
    public void setUp() {
        dbHelper = new DBHelper(new MockContext(),"user",1);
    }

    @Test
    public void dbCheckCreateTables() {
        dbHelper.onCreate(sqLiteDatabase);
    }

    @Test
    public void dbCheckUserExists() {
        try {
            boolean exists = dbHelper.checkUserExists("manjudg", "manjud");
            assertEquals(false, exists);
        }catch (Exception e){

        }
    }

    @Test
    public void dbCheckUserProfileExists(){
        try {
            boolean exists = dbHelper.checkUserProfileExistsById("manjudg");
            assertEquals(true, exists);
        }catch (Exception e){

        }
    }

    @Test
    public void dbgetUserByIdTest() {
        try {
            UserModel userModel = new UserModel();
            userModel.setId("manjudg");
            userModel.setAccessToken("vbcbv");
            userModel.setRefreshToken("xbvbv");
            UserModel userModelGot = dbHelper.getUserById("manjudg");
            assertEquals(userModel.getId(), userModelGot.getId());
        }catch (Exception e){

        }
    }

    @Test
    public void dbCheckUserTableInsertion() {
        try {
            UserModel userModel = new UserModel();
            userModel.setId("manjudg");
            userModel.setAccessToken("vbcbv");
            userModel.setRefreshToken("xbvbv");
            long rowId = dbHelper.insertUser(userModel);
            assertEquals(true, rowId != -1);
        }catch (Exception e){

        }
    }

    @Test
    public void dbCheckUserProfileTableInsertion() {
        try {
            UserProfileModel userProfileModel = new UserProfileModel();
            userProfileModel.setId("manjudg");
            userProfileModel.setFirstName("manjudg");
            userProfileModel.setLastName("manjudg");
            userProfileModel.setUserName("manjudg");
            userProfileModel.setPassword("manjudg");
            userProfileModel.setEmail("manjudg");
            userProfileModel.setCountryCode("manjudg");
            userProfileModel.setMobile("manjudg");
            userProfileModel.setGender(2);
            userProfileModel.setDob("manjudg");
            userProfileModel.setProfilePic("manjudg");
            long rowId = dbHelper.insertUserProfile(userProfileModel);
            assertEquals(true, rowId != -1);
        }catch (Exception e){

        }
    }

    @Test
    public void dbCheckIfUserExits(){
        try {
            boolean exists = dbHelper.checkUserExistsById("manjudb");
            assertEquals(true, exists);

            boolean notExists = dbHelper.checkUserExistsById("manjdb");
            assertEquals(false, notExists);
        }catch (Exception e){

        }
    }

    @Test
    public void dbCheckUpdateUser(){
        try {
            UserModel userModel = new UserModel();
            userModel.setId("vbv");
            userModel.setAccessToken("vbcbv");
            userModel.setRefreshToken("xbvbv");
            long rowId = dbHelper.updateUser(userModel);
            assertEquals(true, rowId == 1);
            rowId = dbHelper.updateUser(null);
            assertEquals(true, rowId == -1);
        }catch (Exception e){

        }
    }

    @Test
    public void dbCheckUserProfileTableUpdation() {
        try {
            UserProfileModel userProfileModel = new UserProfileModel();
            userProfileModel.setId("manjudg");
            userProfileModel.setFirstName("manjudg");
            userProfileModel.setLastName("manjudg");
            userProfileModel.setUserName("manjudg");
            userProfileModel.setPassword("manjudg");
            userProfileModel.setEmail("manjudg");
            userProfileModel.setCountryCode("manjudg");
            userProfileModel.setMobile("manjudg");
            userProfileModel.setGender(2);
            userProfileModel.setDob("manjudg");
            userProfileModel.setProfilePic("manjudg");
            long rowId = dbHelper.updateUserProfile(userProfileModel);
            assertEquals(true, rowId != -1);
        }catch (Exception e){

        }
    }

    @Test
    public void dbCheckInsertSymmetricKey(){
        try {
            SymmetricKeyModel symmetricKeyModel = new SymmetricKeyModel();
            symmetricKeyModel.setSymmetricKey("fdfdf");
            dbHelper.insertSymmetricKey(symmetricKeyModel);
        }catch (Exception e){

        }
    }

    @Test
    public void testGetSymmetricKey(){
        try {
            String symmetricKey = dbHelper.getSymmetricKey();
            assertEquals("fgfg", symmetricKey);
        }catch (Exception e){

        }
    }
}