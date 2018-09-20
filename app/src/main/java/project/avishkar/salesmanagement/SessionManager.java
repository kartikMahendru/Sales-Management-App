package project.avishkar.salesmanagement;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Details";
    private static final String UNIQ_ID = "id";
    public static final String ROLE = "role";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String role){

        editor.putString(UNIQ_ID, id);
        editor.putString(ROLE, role);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(UNIQ_ID, pref.getString(UNIQ_ID, "Not Found"));
        user.put(ROLE, pref.getString(ROLE, "Not Found"));
        return user;
    }
    public void logoutUser(){

        editor.clear();
        editor.commit();
        //Intent i = new Intent(_context, MainActivity.class);
       // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

}