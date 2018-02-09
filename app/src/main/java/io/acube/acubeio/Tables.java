/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 03.02.2018
 *
 * Database Table and respective columns constants.
 *
 *
 */


package io.acube.acubeio;

import android.provider.BaseColumns;

public final class Tables {

    private Tables(){

    }

    /*
     * User Table Constants
     */
    public static class USER_TABLE implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String ACCESS_TOKEN = "access_token";
        public static final String REFRESH_TOKEN = "refresh_token";
    }

    /*
     * User Profile Table Constants
     */
    public static class USER_PROFILE_TABLE implements BaseColumns {
        public static final String TABLE_NAME = "user_profile";
        public static final String USER_NAME = "userName";
        public static final String PASSWORD = "password";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String COUNTRY_CODE = "country_code";
        public static final String MOBILE = "mobile";
        public static final String PROFILE_PIC = "profile_pic";
        public static final String GENDER = "gender";
        public static final String DOB = "dob";
        public static final String EMAIL = "email";
    }

    /*
     * Symmetric Key Table Constants
     */
    public static class SYMMETRIC_KEY_TABLE implements BaseColumns{
        public static final String TABLE_NAME = "symmetric_key";
        public static final String SYMMETRIC_KEY_VALUE = "sym_key";
    }
}
