package viettel.cyberspace.assitant.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.cloud.android.speech.R;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import viettel.cyberspace.assitant.activity.ChatBotActivity;

import static com.activeandroid.Cache.getContext;

public class TextUtil {
    public static final String EMPTY_STRING = "";
    public static final String CHARACTER_QUESTION_MARK = "?";
    public static final String CHARACTER_EQUATION = "=";
    public static final String CHARACTER_AND = "&";
    public static final String CHARACTER_SLASH = "/";
    public static final String CHARACTER_COMMA = ",";
    public static final String CHARACTER_NEWLINE = "\n";
    public static final String CHARACTER_UNDER_SCORE = "_";
    public static final String NULL_STRING = "null";

    /**
     * Parse the list of strings by splitting with comma character
     *
     * @param input
     * @return ArrayList<String>
     * @throws JSONException
     */
    public static ArrayList<String> parseListFromJsonByComma(String input) throws JSONException {
        if (TextUtils.isEmpty(input)) {
            return null;
        }
        return (ArrayList<String>) Arrays.asList(input.split(","));
    }

    /**
     * Get Currency Symbol
     *
     * @param context
     * @return currency symbol character
     */
    public static String getCurrenySymbol(Context context) {
        Locale currentLocal = context.getResources().getConfiguration().locale;
        String symbol;
        try {
            symbol = Currency.getInstance(currentLocal).getSymbol();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            symbol = Currency.getInstance(Locale.US).getSymbol();
        }
        return symbol;
    }

    /**
     * Check if the input string is null or empty or "null" character sequence
     *
     * @param text String
     * @return boolean
     */
    public static boolean isEmpty(String text) {
        return TextUtils.isEmpty(text) || NULL_STRING.equalsIgnoreCase(text);
    }

    /**
     * haitt22
     * check array string empty
     *
     * @param text
     * @return
     */
    public static boolean isEmptyArray(String text) {
        text = text.replaceAll("\\[", "").replaceAll("\\]", "");
        return TextUtils.isEmpty(text) || NULL_STRING.equalsIgnoreCase(text);
    }

    /**
     * Build the saved file name format as a_b_c
     *
     * @param params
     * @return string
     */
    public static String buildFileName(Object[] params) {
        String result = EMPTY_STRING;
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                result += params[i].toString();
                if (i < params.length - 1) result += CHARACTER_UNDER_SCORE;
            }
        }
        return result;
    }

    public static String formatDateTime(Long time) {

        if (time == null)
            return "";

        if (time < 1000000000000L)
            time *= 1000;

        SimpleDateFormat sdf = new SimpleDateFormat("HH'h'mm dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return sdf.format(time);
    }

    public static long[] vibrate = new long[]{0, 2000, 200, 2000, 0};

    public static void pushNotification(String content, String title) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(getContext(), ChatBotActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putInt("notification", 1);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setVibrate(vibrate)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        manager.notify(m, builder.build());
    }


}
