package xyz.ramil.zomcalyp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Base64Coder;

import java.util.ArrayList;
import java.util.List;

import xyz.ramil.zomcalyp.entities.car.Car;
import xyz.ramil.zomcalyp.entities.car.CarSetting;

public class DataBase {
    static Preferences preferences = Gdx.app.getPreferences("xyz.ramil.zomcalyp");
    String setting_key = Base64Coder.encodeString("settings");
    ArrayList<String> list = new ArrayList<>();
    ArrayList<CarSetting> carSettings = new ArrayList<>();

    public DataBase() {
        String setting_text= "1 0 " +
                 "1#0#0#35.0#1.0#1#2#3#0#0#car0" +
                "&1#0#0#35.0#1.0#1#2#3#0#0#car1" +
                "&1#0#0#35.0#1.0#1#2#3#0#0#car2" +
                "&1#0#0#35.0#1.0#1#2#3#0#0#car3";

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


    private void flush(int numb, String str) {
        if(!list.isEmpty()) {
            list.set(numb, str);
            StringBuilder s = new StringBuilder();
            for (String item : list) {
                if (s.length() == 0)
                    s.append(item);
                else
                    s.append(" ").append(item);
            }
            Gdx.app.log("-----------", String.valueOf(s));
            preferences.putString(setting_key, Base64Coder.encodeString(String.valueOf(s)));
            preferences.flush();
        }
    }

    /*
     0 - is sound - 0 or 1
     1 - camera type - 0, 1, 2
     2 - car setting list - List<CarSetting>
     */

    public boolean isSound () {
        if(!list.isEmpty()) {
            if (list.get(0).equals("1")) {
                return true;
            } else return false;
        } else { return true; }
    }

    public void setIsSound(boolean isSound) {
        if(isSound)
            flush(0, "1");
        else
            flush(0, "0");

    }

    public int getCameraType() {
        return Integer.parseInt(list.get(1));
    }

    public void setCameraType(int type) {
        switch (type) {
            case 0:
                flush(1, "0");
                break;
            case 1:
                flush(1, "1");
                break;
            case 2:
                flush(1, "2");
                break;
        }
    }


    public CarSetting getCarSetting(int id) {
        ArrayList<String> carSettingListItems = new ArrayList<>();
        for(String item : list.get(2).split("&")) {
            carSettingListItems.add(item);
        }
        ArrayList<String> settingsListItem = new ArrayList<>();
        for(String item : carSettingListItems.get(id).split("#")) {
            settingsListItem.add(item);
        }

        CarSetting carSetting = new CarSetting(
                Integer.parseInt(settingsListItem.get(0)),
                Integer.parseInt(settingsListItem.get(1)),
                Integer.parseInt(settingsListItem.get(2)),
                Float.parseFloat(settingsListItem.get(3)),
                Float.parseFloat(settingsListItem.get(4)),
                Integer.parseInt(settingsListItem.get(5)),
                Integer.parseInt(settingsListItem.get(6)),
                Integer.parseInt(settingsListItem.get(7)),
                Integer.parseInt(settingsListItem.get(8)),
                Integer.parseInt(settingsListItem.get(9)),
                settingsListItem.get(10)
        );
        
        return carSetting;
    }

//    val id: Int,
//    val type: Int,
//    val isLocked: Int,
//    val max_speed: Float,
//    val weight: Float,
//    val suspension: Int,
//    val engine: Int,
//    val tires: Int,
//    val transmission: Int,
//    val additionallyInt: Int,
//    val additionallyString: String

    public void setCarSetting(int id, CarSetting carSetting) {

    }



}
