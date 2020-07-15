package xyz.ramil.zomcalyp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Base64Coder;

import java.util.ArrayList;

public class DataBase {
    static Preferences preferences = Gdx.app.getPreferences(Base64Coder.encodeString("xyz.ramil.zomcalyp"));
    String setting_key = Base64Coder.encodeString("sound_key");
    ArrayList<String> list = new ArrayList<>();


    public DataBase() {
        String setting_text= "1 0 1";
        if (!preferences.contains(setting_key)) {
            preferences.putString(setting_key, Base64Coder.encodeString(setting_text));
            preferences.flush();
        } else {

          String str =  Base64Coder.decodeString(preferences.getString(setting_key));

          Gdx.app.log("-----------", str);
            for(String item : str.split(" ")) {
                list.add(item);
            }
        }
    }

    public boolean isSound () {
        if(list.get(0).equals("1")) {
            return true;
        } else return false;
    }

    public void setIsSound(boolean isSound) {
        if(isSound)
            flush(0, "1");
            else
                flush(0, "0");

        }


    private void flush(int numb, String str) {
        list.set(numb, str);
        StringBuilder s = new StringBuilder();
        for(String item : list) {
            if(s.length() == 0)
                s.append(item); else
            s.append(" ").append(item);
        }
        Gdx.app.log("-----------", String.valueOf(s));
        preferences.putString(setting_key, Base64Coder.encodeString(String.valueOf(s)));
        preferences.flush();
    }

}
