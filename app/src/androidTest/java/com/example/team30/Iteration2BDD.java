package com.example.team30;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Iteration2BDD {

  @Rule
  public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
          new ActivityScenarioRule<>(MainActivity.class);

  @Rule
  public GrantPermissionRule mGrantPermissionRule =
          GrantPermissionRule.grant(
                  "android.permission.ACCESS_FINE_LOCATION",
                  "android.permission.ACCESS_COARSE_LOCATION");

  @Test
  public void iteration2BDDTest() {
    //Check UI
    ViewInteraction editText = onView(
            allOf(withId(R.id.UsernameEntry),
                    withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                    isDisplayed()));
    editText.check(matches(isDisplayed()));

    ViewInteraction button = onView(
            allOf(withId(R.id.UID_generator), withText("GENERATE UID"),
                    withParent(withParent(withId(android.R.id.content))),
                    isDisplayed()));
    button.check(matches(isDisplayed()));

    //Input action
    ViewInteraction appCompatEditText = onView(
            allOf(withId(R.id.UsernameEntry),
                    childAtPosition(
                            childAtPosition(
                                    withClassName(is("android.widget.LinearLayout")),
                                    0),
                            2),
                    isDisplayed()));
    appCompatEditText.perform(replaceText("test"), closeSoftKeyboard());

    //Click action
    ViewInteraction materialButton = onView(
            allOf(withId(R.id.UID_generator), withText("Generate UID"),
                    childAtPosition(
                            childAtPosition(
                                    withId(android.R.id.content),
                                    0),
                            1),
                    isDisplayed()));
    materialButton.perform(click());

    //Check UI
    ViewInteraction textView = onView(
            allOf(withId(R.id.NewUIDDisplay),
                    withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                    isDisplayed()));
    textView.check(matches(isDisplayed()));

    ViewInteraction button2 = onView(
            allOf(withId(R.id.GoToCompass), withText("GO TO COMPASS"),
                    withParent(withParent(withId(android.R.id.content))),
                    isDisplayed()));
    button2.check(matches(isDisplayed()));

    //Click the Compass button
    ViewInteraction materialButton2 = onView(
            allOf(withId(R.id.GoToCompass), withText("Go to Compass"),
                    childAtPosition(
                            childAtPosition(
                                    withId(android.R.id.content),
                                    0),
                            2),
                    isDisplayed()));
    materialButton2.perform(click());

    //Check UI
    ViewInteraction linearLayout3 = onView(
            allOf(withParent(allOf(withId(android.R.id.content),
                            withParent(withId(com.google.android.material.R.id.decor_content_parent)))),
                    isDisplayed()));
    linearLayout3.check(matches(isDisplayed()));

    //Add friend
    ViewInteraction materialButton3 = onView(
            allOf(withId(R.id.addLoc), withText("Add Friend"),
                    childAtPosition(
                            childAtPosition(
                                    withId(android.R.id.content),
                                    0),
                            2),
                    isDisplayed()));
    materialButton3.perform(click());

    //NewLayout
    ViewInteraction appCompatEditText2 = onView(
            allOf(withId(R.id.FriendsUIDEntry),
                    childAtPosition(
                            childAtPosition(
                                    withClassName(is("android.widget.LinearLayout")),
                                    1),
                            3),
                    isDisplayed()));
    appCompatEditText2.perform(replaceText("yiy054"), closeSoftKeyboard());


    ViewInteraction materialButton4 = onView(
            allOf(withId(R.id.submitBtn), withText("Submit"),
                    childAtPosition(
                            childAtPosition(
                                    withId(android.R.id.content),
                                    0),
                            2),
                    isDisplayed()));
    materialButton4.perform(click());
  }

  private static Matcher<View> childAtPosition(
          final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
                && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
