package mrtequila.bookworm;

import android.content.ComponentName;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static android.support.test.espresso.intent.Intents.intended;



/**
 * Created by Michal on 2017-08-27.
 */


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static String AUTHOR = "Martin";
    private static String TITLE = "Game of Thrones";
    private static String START_DATE_OK = "2017-04-04";
    private static String FINISH_DATE_OK = "2017-04-05";
    private static String START_DATE_NOK = "2017-04-06";
    private static String FINISH_DATE_NOK = "2017-04-03";
    private static String PAGES = "542";

    private static String ERROR_BOOK_TITLE = "Add Book Title !";
    private static String ERROR_DATE = "Start date should be before finish date.";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void messageShouldNotBeSentIfFieldsAreEmpty(){
        MainActivity activity = mActivityRule.getActivity();

        onView(withId(R.id.bookAuthor)).perform(typeText(AUTHOR), closeSoftKeyboard());

        onView(withId(R.id.buttonAdd)).perform(click());

        onView(withText(ERROR_BOOK_TITLE)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void startDateShouldBeBeforeFinishDate(){
        MainActivity activity = mActivityRule.getActivity();

        onView(withId(R.id.bookAuthor)).perform(typeText(AUTHOR), closeSoftKeyboard());
        onView(withId(R.id.bookTitle)).perform(typeText(TITLE), closeSoftKeyboard());
        onView(withId(R.id.pagesNo)).perform(typeText(PAGES), closeSoftKeyboard());

        onView(withId(R.id.startDate)).perform(typeText(START_DATE_NOK), closeSoftKeyboard());
        onView(withId(R.id.finishDate)).perform(typeText(FINISH_DATE_NOK), closeSoftKeyboard());

        onView(withId(R.id.buttonAdd)).perform(click());

        onView(withText(ERROR_DATE)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    @Test
    public void messageShouldBeSent(){
        Intents.init();


        onView(withId(R.id.bookAuthor)).perform(typeText(AUTHOR), closeSoftKeyboard());
        onView(withId(R.id.bookTitle)).perform(typeText(TITLE), closeSoftKeyboard());
        onView(withId(R.id.pagesNo)).perform(typeText(PAGES), closeSoftKeyboard());

        onView(withId(R.id.startDate)).perform(typeText(START_DATE_OK), closeSoftKeyboard());
        onView(withId(R.id.finishDate)).perform(typeText(FINISH_DATE_OK), closeSoftKeyboard());

        onView(withId(R.id.buttonAdd)).perform(click());

        intended(hasComponent(new ComponentName(getTargetContext(),DisplayMessageActivity.class)));

        intended(hasExtra("bookAuthor", AUTHOR));
        intended(hasExtra("bookTitle", TITLE));
        intended(hasExtra("startDate", START_DATE_OK));
        intended(hasExtra("finishDate", FINISH_DATE_OK));
        intended(hasExtra("pagesNumber", PAGES));


        Intents.release();
    }

    @After
    public void tearDown(){

        try {
            Thread.sleep(2000, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
