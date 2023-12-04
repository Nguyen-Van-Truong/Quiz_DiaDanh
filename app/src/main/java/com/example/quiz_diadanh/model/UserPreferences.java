package com.example.quiz_diadanh.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class UserPreferences {

    public static final String PREF_NAME = "UserPrefs";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FULL_NAME = "full_name";
    public static final String KEY_PHONE_NUMBER = "phone_number";

    private final SharedPreferences sharedPreferences;

    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, String.valueOf(user.getId()));
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_FULL_NAME, user.getFullName());
        editor.putString(KEY_PHONE_NUMBER, user.getPhoneNumber());

        editor.apply();
    }

    public User getUser() {
        String userIdString = sharedPreferences.getString(KEY_USER_ID, "-1");
        int userId = Integer.parseInt(userIdString); // Convert the string to an integer

        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String fullName = sharedPreferences.getString(KEY_FULL_NAME, "");
        String phoneNumber = sharedPreferences.getString(KEY_PHONE_NUMBER, "");

        User user = new User();
        user.setId(userId);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);

        return user;
    }


    public void clearUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public Map<String, ?> getAllUserData() {
        return sharedPreferences.getAll();
    }

    public String getValueForKey(String key) {
        return sharedPreferences.getString(key, ""); // Hoặc bạn có thể thay đổi giá trị mặc định nếu cần
    }
}
