package com.library.wrapper;

import android.app.Activity;

public class AppStateHelper extends Observable<AppStateHelper, AppStateHelper.Message, Activity> {
    private int activityCount;
    private static AppStateHelper instance;

    public enum Message {BACKGROUNDED, FOREGROUNDED}

    public static AppStateHelper getInstance() {
        if (instance == null) {
            instance = new AppStateHelper();
        }
        return instance;
    }

    private AppStateHelper() {}

    /** Call from every Activity's onStart method */
    public boolean activityStarting(final Activity activity) {
        boolean ret = false;
        if (activityCount == 0) {
            onForeground(activity);
            ret = true;
        }
        activityCount++;
        return ret;
    }

    private void onForeground(Activity activity) {
        notifyObservers(Message.FOREGROUNDED, activity);
    }

    /** Call from every Activity's onStop() method */
    public synchronized boolean activityStopping(final Activity activity)
    {
        activityCount--;
        if (activityCount == 0) {
            onBackground(activity);
            return true;
        }
        return false;
    }

    private void onBackground(Activity activity) {
        notifyObservers(Message.BACKGROUNDED, activity);
    }

    public boolean isForeground() {
        return activityCount != 0;
    }

    /**
     * 返回当前的App状态
     * @return
     */
    public Message getState() {
        if (isForeground()) {
            return Message.FOREGROUNDED;
        } else {
            return Message.BACKGROUNDED;
        }
    }


}
