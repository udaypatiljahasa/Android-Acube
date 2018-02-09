/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 * <p>
 * Login Activity unit test case class.
 */

package io.acube.acubeio;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void validLogin() throws Exception{
        LoginActivity loginActivity = loginActivityActivityTestRule.getActivity();
        onView(withId(R.id.userNameET)).perform(clearText(),typeText("manjudg"));
        onView(withId(R.id.passwordET)).perform(clearText(),typeText("manjudg"));
        onView(withId(R.id.passwordET)).perform(closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
    }

    @Test
    public void inValidLogin() throws Exception{
        LoginActivity loginActivity = loginActivityActivityTestRule.getActivity();
        onView(withId(R.id.userNameET)).perform(clearText(),typeText("uday"));
        onView(withId(R.id.passwordET)).perform(clearText(),typeText("uay"));
        onView(withId(R.id.passwordET)).perform(closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
    }
}
