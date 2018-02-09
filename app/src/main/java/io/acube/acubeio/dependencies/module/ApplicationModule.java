/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 *
 * Module Class that provides the application level dependency.
 *
 */

package io.acube.acubeio.dependencies.module;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.polidea.rxandroidble.RxBleClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.acube.acubeio.dependencies.ApplicationContext;
import io.acube.acubeio.dependencies.DatabaseInfo;


@Module
public class ApplicationModule {

    private final Application application;


    public ApplicationModule(Application app) {
        application = app;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return "user";
    }

    @Provides
    @DatabaseInfo
    Integer provideDatabaseVersion() {
        return 1;
    }

    @Provides
    @Singleton
    RxBleClient provideRxBleClient(){
        return RxBleClient.create(application);
    }

    @Provides
    @Singleton
    RequestQueue provideRequestQueue(){
        return Volley.newRequestQueue(application);
    }
}
