package analytics_package;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import app.easycreate.R;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Soham Bhattacharya on 24-03-2016.
 */
public final class AnalyticsTrackers {

    public enum Target {
        APP,
        // Add more trackers here if you need, and update the code in #get(Target) below
    }

    private static AnalyticsTrackers sInstance;

    public static synchronized void initialize(Context context) {
        if (sInstance != null) {
            try {
                throw new IllegalStateException("Extra call to initialize analytics trackers");
            }
            catch (Exception e)
            {
                Log.e("exception","check");
            }
        }

        sInstance = new AnalyticsTrackers(context);
    }

    public static synchronized AnalyticsTrackers getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Call initialize() before getInstance()");
        }

        return sInstance;
    }

    private final Map<Target, Tracker> mTrackers = new HashMap<Target, Tracker>();
    private final Context mContext;

    /**
     * Don't instantiate directly - use {@link #getInstance()} instead.
     */
    private AnalyticsTrackers(Context context) {
        mContext = context.getApplicationContext();
    }

    public synchronized Tracker get(Target target) {
        if (!mTrackers.containsKey(target)) {
            Tracker tracker;
            switch (target) {
                case APP:
                    tracker = GoogleAnalytics.getInstance(mContext).newTracker(R.xml.app_tracker);
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled analytics target " + target);
            }
            mTrackers.put(target, tracker);
        }

        return mTrackers.get(target);
    }
}