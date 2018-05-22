package viettel.cyberspace.assitant.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import viettel.cyberspace.assitant.model.QuestionExperts;
import viettel.cyberspace.assitant.model.ResponseAnswer;
import viettel.cyberspace.assitant.model.User;
import viettel.cyberspace.assitant.utils.Const;
import viettel.cyberspace.assitant.utils.TextUtil;


public class StorageManager {
    private static SharedPreferences getSharedPreferences(Context context) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static boolean setValue(Context context, String key, String value) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }


    public static void saveUser(Context context, User user) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("user", json);
        editor.commit();
    }

    public static User getUser(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        Gson gson = new Gson();
        String json = preferences.getString("user", "");
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public static void saveQuestionExperts(Context context, List<QuestionExperts> questionExperts) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(questionExperts);
        editor.putString(Const.PREF_EXPERT_QUESTION, json);
        editor.commit();
    }

    public static List<QuestionExperts> getQuestionExperts(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        Gson gson = new Gson();
        String json = preferences.getString(Const.PREF_EXPERT_QUESTION, null);
        Type type = new TypeToken<List<QuestionExperts>>() {
        }.getType();
        if (json != null)
            return gson.fromJson(json, type);
        else return new ArrayList<>();
    }

    public static void saveResponseAnswer(Context context, List<ResponseAnswer> responseAnswers) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(responseAnswers);
        editor.putString(Const.PREF_EXPERT_ANSWER, json);
        editor.commit();
    }

    public static List<ResponseAnswer> getResponseAnswers(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        Gson gson = new Gson();
        String json = preferences.getString(Const.PREF_EXPERT_ANSWER, null);
        Type type = new TypeToken<List<ResponseAnswer>>() {
        }.getType();
        if (json != null)
            return gson.fromJson(json, type);
        else return new ArrayList<>();
    }

    public static String getStringValue(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return TextUtil.EMPTY_STRING;
        }
        return preferences.getString(key, TextUtil.EMPTY_STRING);
    }

    public static String getStringValue(Context context, String key, String defaultValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return defaultValue;
        }
        return preferences.getString(key, TextUtil.EMPTY_STRING);
    }

    public static boolean setStringValue(Context context, String key, String value) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean setBooleanValue(Context context, String key, boolean value) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return defaultValue;
        }
        return preferences.getBoolean(key, defaultValue);
    }

    public static int getIntValue(Context context, String key, int defaultValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return defaultValue;
        }
        String savedValue = preferences.getString(key, String.valueOf(defaultValue));
        if (TextUtils.isDigitsOnly(savedValue)) {
            return Integer.valueOf(savedValue);
        }
        return defaultValue;
    }

    public static boolean setIntValue(Context context, String key, int value) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, String.valueOf(value));
        return editor.commit();
    }


    public static void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }

    public static boolean putListString(Context context, String key, ArrayList<String> stringList) {
        SharedPreferences preferences = getSharedPreferences(context);
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, TextUtils.join("‚‗‚", myStringList));
        return editor.commit();
    }


    public static ArrayList<String> getListString(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    public static long getLongValue(Context context, String key, long defaultValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return defaultValue;
        }
        String savedValue = preferences.getString(key, String.valueOf(defaultValue));
        if (TextUtils.isDigitsOnly(savedValue)) {
            return Long.valueOf(savedValue);
        }
        return defaultValue;
    }

    public static boolean setLongValue(Context context, String key, long value) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, String.valueOf(value));
        return editor.commit();
    }


}
