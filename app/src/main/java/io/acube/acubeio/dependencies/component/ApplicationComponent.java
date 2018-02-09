/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 03.02.2018
 *
 * Application Component class, contians the functions which to fetch dependencies which are required at application level.
 *
 *
 */

package io.acube.acubeio.dependencies.component;

import android.app.Application;
import android.content.Context;


import com.android.volley.RequestQueue;
import com.polidea.rxandroidble.RxBleClient;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import io.acube.acubeio.AcubeAppApplication;
import io.acube.acubeio.helper.DBHelper;
import io.acube.acubeio.dependencies.ApplicationContext;
import io.acube.acubeio.dependencies.module.ApplicationModule;

@Singleton
@Component(modules = {ApplicationModule.class,AndroidSupportInjectionModule.class})
public interface ApplicationComponent {

    void inject(AcubeAppApplication acubeAppApplication);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DBHelper getDBHelper();

    RxBleClient getRxBleClient();

    RequestQueue getRequestQueue();
}
