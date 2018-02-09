/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 * <p>
 * The class handles the login screen activity,
 * The class checks for valid credentials to login and redirects to the Home screen.
 */

package io.acube.acubeio;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.acube.acubeio.encryption.RSACipher;
import io.acube.acubeio.helper.DBHelper;
import io.acube.acubeio.dependencies.component.ActivityComponent;
import io.acube.acubeio.dependencies.component.DaggerActivityComponent;
import io.acube.acubeio.dependencies.module.ActivityModule;
import io.acube.acubeio.model.SymmetricKeyModel;
import io.acube.acubeio.model.UserModel;
import io.acube.acubeio.model.UserProfileModel;
import io.acube.acubeio.oauth.FetchTokenAsync;
import io.acube.acubeio.oauth.OAuthConstants;


public class LoginActivity extends AppCompatActivity implements FetchTokenAsync.AsyncResponse {

    private Button loginBtn;
    private EditText userNameET;
    private EditText passwordET;
    private View.OnClickListener loginBtnClickListener;
    private SharedPreferences sharedPreferences;
    ProgressBar progressBar;


    private ActivityComponent activityComponent;
    private String wrappedSymmetricKey;
    private static final String NOT_CONNECTED_TO_INTERNET = "Please connect to internet";

    @Inject
    FetchTokenAsync fetchTokenAsync;

    @Inject
    RSACipher rsaCipher;

    @Inject
    DBHelper dbHelper;

    @Inject
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initEventListeners();
        initView();
    }

    /**
     * Initializes the view, by referencing all the required widgets and
     * actions to be taken after the view is rendered.
     **/
    private void initView() {
        getActivityComponent();
        activityComponent.inject(this);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(loginBtnClickListener);

        userNameET = (EditText) findViewById(R.id.userNameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        try {
            rsaCipher.initialize();

            wrappedSymmetricKey = dbHelper.getSymmetricKey();
            if (wrappedSymmetricKey.length() == 0){
                rsaCipher.initializeSymmetricKeys();
                wrappedSymmetricKey = rsaCipher.getWrappedSymmetricKey();
                SymmetricKeyModel symmetricKeyModel = new SymmetricKeyModel();
                symmetricKeyModel.setSymmetricKey(wrappedSymmetricKey);
                dbHelper.insertSymmetricKey(symmetricKeyModel);
            }

            rsaCipher.unWrapKey(wrappedSymmetricKey);
        }catch (Exception e){
            Log.d("Home Activity", e.getMessage());
        }

        progressBar = findViewById(R.id.progressBar);
    }

    /**
     * Initializes the all the event handlers of the class.
     **/
    private void initEventListeners() {
        loginBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                try {
                    if (isNetworkConnected()) {
                        if (dbHelper.checkUserExists(userNameET.getText().toString().trim(), passwordET.getText().toString().trim())) {
                            UserModel userModel = dbHelper.getUserById(userNameET.getText().toString().trim());
                            String accessToken = rsaCipher.decrypt(userModel.getAccessToken());
                            getUserProfile(accessToken);
                        } else {
                            fetchTokenAsync = activityComponent.getFetchTokenAsync();
                            progressBar.setVisibility(View.VISIBLE);
                            fetchTokenAsync.execute(OAuthConstants.TOKEN_URL, userNameET.getText().toString().trim(), passwordET.getText().toString().trim());
                        }
                    }else{
                        showAlertDialog(NOT_CONNECTED_TO_INTERNET);
                    }
                } catch (Exception e) {

                }
            }
        };
    }

    public void getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(AcubeAppApplication.get(this).getComponent())
                    .build();
        }
    }

    /**
     * Process the response from the Async Task.
     *
     * @Param tokenList - String Array List of the tokens , of the Sync task response.
     **/
    @Override
    public void processResponse(ArrayList<String> tokenList) {
        progressBar.setVisibility(View.INVISIBLE);
        if (tokenList.size()== 2) {
            try {
                String encryptedAccessToken = rsaCipher.encrypt(tokenList.get(0));
                String encryptedRefreshToken = rsaCipher.encrypt(tokenList.get(1));
                insertIntoUser(encryptedAccessToken, encryptedRefreshToken);
                getUserProfile(tokenList.get(0));
            } catch (Exception e) {
                Log.d("Home Activity", e.getMessage());
            }
        } else {
            showAlertDialog(tokenList.get(0));
        }
    }

    private void showAlertDialog(String errorMsg){
        final Dialog alertDialog = new Dialog(LoginActivity.this);
        alertDialog.setContentView(R.layout.custom_alert_dialog);
        TextView errorTxt = (TextView) alertDialog.findViewById(R.id.errorMsg);
        errorTxt.setText(errorMsg);
        Button closeBtn = (Button) alertDialog.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }
    /**
     * Updates or inserts the data to the user table.
     *
     * @Param
     *      encryptedAccessToken - String of encrypted access token
     *      encryptedRefreshToken- String of encrypted refresh token
     **/
    private void insertIntoUser(String encryptedAccessToken, String encryptedRefreshToken) {
        UserModel userModel = new UserModel();
        userModel.setId(userNameET.getText().toString().trim());
        userModel.setAccessToken(encryptedAccessToken);
        userModel.setRefreshToken(encryptedRefreshToken);
        if (dbHelper.checkUserExistsById(userNameET.getText().toString().trim())) {
            dbHelper.updateUser(userModel);
        } else {
            dbHelper.insertUser(userModel);
        }
    }

    /**
     * Updates or inserts the data to the user profile table.
     *
     * @Param
     *      userProfile - JSON Object containing the user profile details.
     **/
    private void insertIntoUserProfile(JSONObject userProfile) {
        try {
            UserProfileModel userProfileModel = new UserProfileModel();
            userProfileModel.setId(userProfile.get("id").toString());
            userProfileModel.setFirstName(userProfile.get("first_name").toString());
            userProfileModel.setLastName(userProfile.get("last_name").toString());
            userProfileModel.setUserName(userProfile.get("userName").toString());
            userProfileModel.setPassword(userProfile.get("password").toString());
            userProfileModel.setEmail(userProfile.get("email").toString());
            userProfileModel.setCountryCode(userProfile.get("country_code").toString());
            userProfileModel.setMobile(userProfile.get("mobile").toString());
            userProfileModel.setGender(Integer.parseInt(userProfile.get("gender").toString()));
            userProfileModel.setDob(userProfile.get("dob").toString());
            userProfileModel.setProfilePic(userProfile.get("profile_pic").toString());
            long rowId;
            if (dbHelper.checkUserProfileExistsById(userProfile.get("id").toString())) {
                rowId = dbHelper.updateUserProfile(userProfileModel);
            } else {
                rowId = dbHelper.insertUserProfile(userProfileModel);
            }
            if (rowId != -1) {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Calls the api to get user profile details.
     *
     * @Param
     *      userProfile - JSON Object containing the user profile details.
     **/
    private void getUserProfile(final String accessToken) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, OAuthConstants.USER_PROFILE_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        insertIntoUserProfile(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                VolleyLog.d("Login Activity", "Error: " + error.getMessage());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            ;
        };
        progressBar.setVisibility(View.VISIBLE);
        requestQueue.add(jsonObjReq);
    }
}
