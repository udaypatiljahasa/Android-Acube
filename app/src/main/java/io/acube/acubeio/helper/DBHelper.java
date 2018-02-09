/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 * <p>
 * Database connection and related operations.
 */


package io.acube.acubeio.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.acube.acubeio.Tables;
import io.acube.acubeio.dependencies.ApplicationContext;
import io.acube.acubeio.dependencies.DatabaseInfo;
import io.acube.acubeio.model.SymmetricKeyModel;
import io.acube.acubeio.model.UserModel;
import io.acube.acubeio.model.UserProfileModel;

@Singleton
public class DBHelper extends SQLiteOpenHelper {

    private static final String SYMMETRIC_TABLE_QUERY = "CREATE TABLE " + Tables.SYMMETRIC_KEY_TABLE.TABLE_NAME +
            " ( " + Tables.SYMMETRIC_KEY_TABLE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
             Tables.SYMMETRIC_KEY_TABLE.SYMMETRIC_KEY_VALUE + " TEXT )";

    private static final String USER_TABLE_QUERY = "CREATE TABLE " + Tables.USER_TABLE.TABLE_NAME +
            " ( " + Tables.USER_TABLE._ID + " VARCHAR(40) PRIMARY KEY," +
            Tables.USER_TABLE.ACCESS_TOKEN + " TEXT," +
            Tables.USER_TABLE.REFRESH_TOKEN + " TEXT )";

    private static final String USER_PROFILE_TABLE_QUERY = "CREATE TABLE " + Tables.USER_PROFILE_TABLE.TABLE_NAME +
            " ( " + Tables.USER_TABLE._ID + " VARCHAR(40) PRIMARY KEY," +
            Tables.USER_PROFILE_TABLE.FIRST_NAME + " VARCHAR(100)," +
            Tables.USER_PROFILE_TABLE.LAST_NAME + " VARCHAR(100)," +
            Tables.USER_PROFILE_TABLE.USER_NAME + " VARCHAR(50)," +
            Tables.USER_PROFILE_TABLE.PASSWORD + " VARCHAR(50)," +
            Tables.USER_PROFILE_TABLE.EMAIL + "  VARCHAR(100)," +
            Tables.USER_PROFILE_TABLE.GENDER + " INTEGER," +
            Tables.USER_PROFILE_TABLE.COUNTRY_CODE + " VARCHAR(5)," +
            Tables.USER_PROFILE_TABLE.MOBILE + " VARCHAR(15)," +
            Tables.USER_PROFILE_TABLE.DOB + " DATE," +
            Tables.USER_PROFILE_TABLE.PROFILE_PIC + " VARCHAR(255) )";

    private static final String USER_TABLE_FETCH_BY_ID_QUERY = "SELECT * FROM " + Tables.USER_PROFILE_TABLE.TABLE_NAME +
            " WHERE ";

    private static final String GET_COUNT_USER_PROFILE_TABLE = "SELECT Count(*) as UserProfileCount FROM " + Tables.USER_PROFILE_TABLE.TABLE_NAME
            + " WHERE " + Tables.USER_PROFILE_TABLE._ID + " = ?";

    private static final String GET_COUNT_USER_TABLE = "SELECT Count(*) as UserCount FROM " + Tables.USER_TABLE.TABLE_NAME
            + " WHERE " + Tables.USER_TABLE._ID + " = ?";

    private static final String GET_USER_EXISTS_QUERY_ = "SELECT Count(*) as UserCount FROM " + Tables.USER_PROFILE_TABLE.TABLE_NAME
            + " WHERE " + Tables.USER_PROFILE_TABLE.USER_NAME + " = ? and " + Tables.USER_PROFILE_TABLE.PASSWORD + " = ?";


    @Inject
    public DBHelper(@ApplicationContext Context context, @DatabaseInfo String dbName, @DatabaseInfo Integer version) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase dbObject) {
        createTables(dbObject);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbObject, int oldVersion, int newVersion) {

    }

    /*
    * Creates the database tables
    * @param
    *    dbObject - SQLiteDatabase object
    *
    */
    private void createTables(SQLiteDatabase dbObject) {
        try {
            dbObject.execSQL(USER_TABLE_QUERY);
            dbObject.execSQL(USER_PROFILE_TABLE_QUERY);
            dbObject.execSQL(SYMMETRIC_TABLE_QUERY);
        } catch (Exception e) {

        }
    }

    /*
    * Inserts the data to the user table
    * @param
    *    userModel - UserModel object of the user table
    *
    * @retruns long value of id of the row inserted, -1 if not successfully inserted
    */
    public long insertUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Tables.USER_TABLE._ID, userModel.getId());
        contentValues.put(Tables.USER_TABLE.ACCESS_TOKEN, userModel.getAccessToken());
        contentValues.put(Tables.USER_TABLE.REFRESH_TOKEN, userModel.getRefreshToken());
        long rowId = db.insert(Tables.USER_TABLE.TABLE_NAME, null, contentValues);
        db.close();
        return rowId;
    }

    /*
    * Inserts the data to the user Profile table
    * @param
    *    userProfileModel - UserProfileModel object of the user table
    *
    * @return
    *   long value of id of the row inserted, -1 if not successfully inserted
    */
    public long insertUserProfile(UserProfileModel userProfileModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Tables.USER_PROFILE_TABLE._ID, userProfileModel.getId());
        contentValues.put(Tables.USER_PROFILE_TABLE.FIRST_NAME, userProfileModel.getFirstName());
        contentValues.put(Tables.USER_PROFILE_TABLE.LAST_NAME, userProfileModel.getLastName());
        contentValues.put(Tables.USER_PROFILE_TABLE.USER_NAME, userProfileModel.getUserName());
        contentValues.put(Tables.USER_PROFILE_TABLE.PASSWORD, userProfileModel.getPassword());
        contentValues.put(Tables.USER_PROFILE_TABLE.EMAIL, userProfileModel.getEmail());
        contentValues.put(Tables.USER_PROFILE_TABLE.COUNTRY_CODE, userProfileModel.getCountryCode());
        contentValues.put(Tables.USER_PROFILE_TABLE.MOBILE, userProfileModel.getMobile());
        contentValues.put(Tables.USER_PROFILE_TABLE.GENDER, userProfileModel.getGender());
        contentValues.put(Tables.USER_PROFILE_TABLE.DOB, userProfileModel.getDob());
        contentValues.put(Tables.USER_PROFILE_TABLE.PROFILE_PIC, userProfileModel.getProfilePic());
        long rowId = db.insert(Tables.USER_PROFILE_TABLE.TABLE_NAME, null, contentValues);
        db.close();
        return rowId;
    }

    /*
    * update the data to the user table
    * @param
    *    userModel - UserModel object of the user table
    *
    * @return
     * long value of id of the row updated, -1 if not successfully inserted
    */
    public long updateUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Tables.USER_TABLE._ID, userModel.getId());
        contentValues.put(Tables.USER_TABLE.ACCESS_TOKEN, userModel.getAccessToken());
        contentValues.put(Tables.USER_TABLE.REFRESH_TOKEN, userModel.getRefreshToken());
        String[] params = new String[]{userModel.getId()};
        long rowId = db.update(Tables.USER_TABLE.TABLE_NAME, contentValues, Tables.USER_TABLE._ID + " =?", params);
        db.close();
        return rowId;
    }


    /*
    * update the data to the user profile table
    * @param
    *    userProfileModel - UserProfileModel object of the user table
    *
    * @return
    *   long value of id of the row updated, -1 if not successfully inserted
    */
    public long updateUserProfile(UserProfileModel userProfileModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Tables.USER_PROFILE_TABLE._ID, userProfileModel.getId());
        contentValues.put(Tables.USER_PROFILE_TABLE.FIRST_NAME, userProfileModel.getFirstName());
        contentValues.put(Tables.USER_PROFILE_TABLE.LAST_NAME, userProfileModel.getLastName());
        contentValues.put(Tables.USER_PROFILE_TABLE.USER_NAME, userProfileModel.getUserName());
        contentValues.put(Tables.USER_PROFILE_TABLE.PASSWORD, userProfileModel.getPassword());
        contentValues.put(Tables.USER_PROFILE_TABLE.EMAIL, userProfileModel.getEmail());
        contentValues.put(Tables.USER_PROFILE_TABLE.COUNTRY_CODE, userProfileModel.getCountryCode());
        contentValues.put(Tables.USER_PROFILE_TABLE.MOBILE, userProfileModel.getMobile());
        contentValues.put(Tables.USER_PROFILE_TABLE.GENDER, userProfileModel.getGender());
        contentValues.put(Tables.USER_PROFILE_TABLE.DOB, userProfileModel.getDob());
        contentValues.put(Tables.USER_PROFILE_TABLE.PROFILE_PIC, userProfileModel.getProfilePic());
        String[] params = new String[]{userProfileModel.getId()};
        long rowId = db.update(Tables.USER_PROFILE_TABLE.TABLE_NAME, contentValues, Tables.USER_TABLE._ID + " =?", params);
        db.close();
        return rowId;
    }

    /*
    * Check if user exists with user Id
    * @param
    *    userModel - UserModel object of the user table
    *
    * @return
    *    boolean value of true if user exists else false.
    */
    public boolean checkUserExistsById(String Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[]{Id};
        Cursor cursor = db.rawQuery(GET_COUNT_USER_TABLE, param);
        int dataCount = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                dataCount = cursor.getInt(cursor.getColumnIndex("UserCount"));
            }
            cursor.close();
        }
        db.close();
        return dataCount > 0 ? true : false;
    }

    /*
    * Check if user exists.
    * @param
    *    userModel - UserModel object of the user table
    *
    * @return
    *    boolean value of true if user exists else false.
    */
    public boolean checkUserExists(String userName,String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[]{userName,password};
        Cursor cursor = db.rawQuery(GET_USER_EXISTS_QUERY_, param);
        int dataCount = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                dataCount = cursor.getInt(cursor.getColumnIndex("UserCount"));
            }
            cursor.close();
        }
        db.close();
        return dataCount > 0 ? true : false;
    }
    /*
    * Check if user exists with user Id
    * @param
    *    userModel - UserModel object of the user table
    *
    * @return
    *    boolean value of true if user exists else false.
    */
    public boolean checkUserProfileExistsById(String Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] param = new String[]{Id};
        Cursor cursor = db.rawQuery(GET_COUNT_USER_PROFILE_TABLE, param);
        int dataCount = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                dataCount = cursor.getInt(cursor.getColumnIndex("UserProfileCount"));
            }
            cursor.close();
        }
        db.close();
        return dataCount > 0 ? true : false;
    }

    /*
    * Fetches the data from user table by ID and returns the UserModel Object
    * @param
    *    id - String containing user id
    *
    * @return
     *      UserModel - containing the user details
    */
    public UserModel getUserById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] param = new String[]{id};
        UserModel userModel = new UserModel();
        Cursor cursor = db.query(Tables.USER_TABLE.TABLE_NAME, null, Tables.USER_TABLE._ID + " = ? ", param, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userModel.setId(cursor.getString(cursor.getColumnIndex(Tables.USER_TABLE._ID)));
                userModel.setAccessToken(cursor.getString(cursor.getColumnIndex(Tables.USER_TABLE.ACCESS_TOKEN)));
                userModel.setRefreshToken(cursor.getString(cursor.getColumnIndex(Tables.USER_TABLE.REFRESH_TOKEN)));
            }
            cursor.close();
        }
        db.close();
        return userModel;
    }


    /*
   * Inserts the data to the Symmetric key table
   * @param
   *    symmetricKeyModel - SymmetricKeyModel object of the Symmetric key table
   *
   * @retruns long value of id of the row inserted, -1 if not successfully inserted
   */
    public long insertSymmetricKey(SymmetricKeyModel symmetricKeyModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Tables.SYMMETRIC_KEY_TABLE.SYMMETRIC_KEY_VALUE, symmetricKeyModel.getSymmetricKey());
        long rowId = db.insert(Tables.SYMMETRIC_KEY_TABLE.TABLE_NAME, null, contentValues);
        db.close();
        return rowId;
    }

    /*
    * Fetches the symmetric key from the Symmetric key table and returns the string of it.
    *
    *
    * @return
     *      String - containing the Symmetric key
    */
    public String getSymmetricKey(){
        String symmetricKey = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Tables.SYMMETRIC_KEY_TABLE.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                symmetricKey = cursor.getString(cursor.getColumnIndex(Tables.SYMMETRIC_KEY_TABLE.SYMMETRIC_KEY_VALUE));
            }
            cursor.close();
        }
        db.close();
        return symmetricKey;
    }
}
