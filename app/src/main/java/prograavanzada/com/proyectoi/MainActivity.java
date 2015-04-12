package prograavanzada.com.proyectoi;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    //Twitter data
    private static final String TWITTER_KEY = "x7fJ7RoUDrteArYOSbVfmV6dU";
    private static final String TWITTER_SECRET = "RSGb3moTbOCahmnohUN3oImLScfNIeAL31XFmgXQZJsFiZyzx8";
    private TwitterLoginButton tlb;

    // Facebook data
    private LoginButton loginBtn;
    private UiLifecycleHelper uiHelper;
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        // Twitter init
        tlb = (TwitterLoginButton)
                findViewById(R.id.twitter_login_button);
        tlb.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    // Misc. methods
    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                Log.d("FacebookSampleActivity", "Sesión abierta");
            } else if (state.isClosed()) {
                Log.d("FacebookSampleActivity", "Sesión cerrada");
            }
        }
    };

    public boolean checkPermissions() {
        Session s = Session.getActiveSession();
        if (s != null) {
            return s.getPermissions().contains("publish_actions");
        } else
            return false;
    }

    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null)
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    this, PERMISSIONS));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        tlb.onActivityResult(requestCode, resultCode,
                data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }
}
