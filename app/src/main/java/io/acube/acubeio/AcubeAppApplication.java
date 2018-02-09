/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 * <p>
 * The class handles the application component creation.
 */

package io.acube.acubeio;

import android.app.Application;
import android.content.Context;

import io.acube.acubeio.dependencies.component.ApplicationComponent;
import io.acube.acubeio.dependencies.component.DaggerApplicationComponent;
import io.acube.acubeio.dependencies.module.ApplicationModule;

public class AcubeAppApplication extends Application {
    protected ApplicationComponent applicationComponent;

    public static AcubeAppApplication get(Context context) {
        return (AcubeAppApplication) context.getApplicationContext();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getComponent(){
        return applicationComponent;
    }
}
