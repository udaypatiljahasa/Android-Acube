/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 *
 * Async task to get tokens using OAuth2Client
 *
 */

package io.acube.acubeio.oauth;


import android.os.AsyncTask;
import android.util.Log;


import java.util.ArrayList;


import javax.inject.Inject;

import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthError;
import ca.mimic.oauth2library.OAuthResponse;

public class FetchTokenAsync extends AsyncTask<String, Void, ArrayList<String>> {

    public interface AsyncResponse {
        void processResponse(ArrayList<String> output);
    }

    AsyncResponse asyncResponse;

    @Inject
    public FetchTokenAsync(AsyncResponse asyncResponse){
        this.asyncResponse = asyncResponse;
    }
    @Override
    protected ArrayList<String> doInBackground(String... parms ){
        ArrayList<String> tokenList = new ArrayList<String>();
        try {
            OAuth2Client client = new OAuth2Client.Builder(parms[1], parms[2], "ClientId", "secrete", parms[0]).build();
            OAuthResponse response = client.requestAccessToken();

            if (response.isSuccessful()) {
                 tokenList.add(response.getAccessToken());
                 tokenList.add(response.getRefreshToken());
            } else {
                OAuthError error = response.getOAuthError();
                String errorMsg = error.getError();
                tokenList.add(errorMsg);
            }

            response.getCode();
        }catch (Exception e){
            tokenList.add(e.getMessage());
        }
        return tokenList;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        asyncResponse.processResponse(result);
    }
}
