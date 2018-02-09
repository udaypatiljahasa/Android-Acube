/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 *
 * Module Class that provides the activity level dependency.
 *
 */

package io.acube.acubeio.dependencies.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.acube.acubeio.LoginActivity;
import io.acube.acubeio.dependencies.ActivityContext;
import io.acube.acubeio.oauth.FetchTokenAsync;

@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity _activity) {
        activity = _activity;
    }

    /*
     * Provides activity context
     */
    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    /*
     * Provides activity
     */
    @Provides
    Activity provideActivity() {
        return activity;
    }

    /*
     * Provides Async response interface object
     */
    @Provides
    FetchTokenAsync.AsyncResponse provideAsyncResponse(){
        return (LoginActivity)activity;
    }
}