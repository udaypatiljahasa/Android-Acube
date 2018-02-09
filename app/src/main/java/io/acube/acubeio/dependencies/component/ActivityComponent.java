/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 03.02.2018
 *
 * Activity Component class, contians the activities where dependency need to be injected and functions to get the dependencies.
 *
 *
 */

package io.acube.acubeio.dependencies.component;



import dagger.Component;
import io.acube.acubeio.BLESendRecieve;
import io.acube.acubeio.encryption.RSACipher;
import io.acube.acubeio.HomeActivity;
import io.acube.acubeio.LoginActivity;
import io.acube.acubeio.dependencies.PerActivity;
import io.acube.acubeio.dependencies.module.ActivityModule;
import io.acube.acubeio.oauth.FetchTokenAsync;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(LoginActivity loginActivity);
    void inject(HomeActivity homeActivity);
    void inject(BLESendRecieve bleSendRecieve);

    RSACipher getRSACipher();

    FetchTokenAsync getFetchTokenAsync();
}
