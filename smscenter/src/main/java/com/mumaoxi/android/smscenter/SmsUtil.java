package com.NicolasYiu.android.smscenter;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

public class SmsUtil {
    private Activity activity;
    private static SmsUtil sms;

    private SmsUtil(Activity activity) {
        this.activity = activity;
    }

    public static SmsUtil getInstance(Activity activity) {
        if (sms == null) {
            sms = new SmsUtil(activity);
        }
        return sms;
    }

    /**
     * @return
     */
    public String getSmsCenter() {
        String[] projection = new String[]{"service_center"};
        try {
            Cursor myCursor = activity.managedQuery(Uri.parse("content://sms/inbox"),
                    projection,
                    null, null, "date desc");
            return doCursor(myCursor);
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String doCursor(Cursor cur) {
        String smscenter = null;
        if (cur.moveToFirst()) {
            String smsc;
            int smscColumn = cur.getColumnIndex("service_center");
            Frequency fre = new Frequency();
            int index = 0;
            do {
                smsc = cur.getString(smscColumn);
                fre.addStatistics(smsc);
                index++;
            } while (cur.moveToNext() && index < 50);
            smscenter = fre.getMaxValueItem().getKey();
        }
        return smscenter;
    }

}
