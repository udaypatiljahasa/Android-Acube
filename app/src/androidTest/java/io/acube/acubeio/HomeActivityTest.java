/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 * <p>
 * Home Activity unit test case class.
 */

package io.acube.acubeio;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {
    @Rule
    public ActivityTestRule<HomeActivity> homeActivityTestRule = new ActivityTestRule<HomeActivity>(HomeActivity.class);

    @Test
    public void scanBtn() throws Exception{
        HomeActivity homeActivity = homeActivityTestRule.getActivity();
        onView(withId(R.id.scanBtn)).perform(click());
    }
}
