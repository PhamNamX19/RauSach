package nam.com.rausach.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    public static String SHARED_PREFERENCES_NAME = "my_shared_preferences";

    public static void saveSharedPreferences(Context context, String id){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                MySharedPreferences.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.ID_LOCAL, String.valueOf(id));
        editor.commit();
    }

    public static String getSharedPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                MySharedPreferences.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        String idcustomer = sharedPreferences.getString(Constant.ID_LOCAL, null);
        return idcustomer;
    }

    public static void removeSharedPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MySharedPreferences.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constant.ID_LOCAL).commit();
    }

}
