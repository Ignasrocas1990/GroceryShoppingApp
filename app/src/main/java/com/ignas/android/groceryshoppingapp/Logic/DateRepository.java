package com.ignas.android.groceryshoppingapp.Logic;


import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.AlarmSwitch;
import com.ignas.android.groceryshoppingapp.Models.Item;

import java.util.Date;

import io.realm.Realm;

public class DateRepository {


    public DateRepository() {
        //clear();

    }
    //get Alarm Switch
    public boolean getDBSwitch() {
        Boolean[] result = new Boolean[1];
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm->{

                AlarmSwitch tempSwitch = inRealm.where(AlarmSwitch.class)
                        .equalTo("deleteFlag",false)
                        .findFirst();
                if(tempSwitch!=null){
                    result[0] = tempSwitch.isSwitched();
                }else{
                    result[0] = false;
                }
            });
        }catch(Exception e){
            Log.i("log", "getSwitch: get, not successfully");
        }
        return result[0];
    }
    //modify alarm switch
    public void updateSwitch(Boolean state){
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm ->{
                AlarmSwitch tempSwitch = inRealm.where(AlarmSwitch.class)
                        .equalTo("deleteFlag",false)
                        .findFirst();
                if(tempSwitch == null){
                    AlarmSwitch alarmSwitch = new AlarmSwitch();
                    alarmSwitch.setSwitch(state);
                    inRealm.insertOrUpdate(alarmSwitch);
                }else{
                    tempSwitch.setSwitch(state);
                }
            });

        }catch(Exception e){
            Log.i("log", "setSwitch: did not set switch"+e.getMessage());
        }
    }

    //get smallest date to schedule (is the switch is on)
    public Item getSmallestDateItem(int item_Id){
        Item[] result = {null};
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm->{

                AlarmSwitch tempSwitch = inRealm.where(AlarmSwitch.class).findFirst();
                if(tempSwitch!=null) {
                    if (tempSwitch.isSwitched()) {
                        if(item_Id != 0) {
                            Item running = inRealm.where(Item.class)
                                    .equalTo("item_Id", item_Id).findFirst();

                            if(running != null){ running.setNotified(true);}

                        }
                        Date dateResults = inRealm.where(Item.class)
                                .equalTo("notified", false)
                                .equalTo("deleteFlag",false)
                                .greaterThan("lastingDays", 0)
                                .notEqualTo("item_Id", item_Id)
                                .minimumDate("runOutDate");

                        if(dateResults !=null){
                            Item itemResult = inRealm.where(Item.class)
                                    .equalTo("runOutDate",dateResults).findFirst();

                            if(itemResult!=null)
                                Log.wtf("log", "db,item: "+item_Id+" "+itemResult.getItem_id());

                            result[0] = (inRealm.copyFromRealm(itemResult));
                        }
                    }
                }
            });

        }catch(Exception e){
            Log.i("log", "getSmallestDateItem: "+e);
        }
        if(result[0] != null) {
            return result[0];
        }
        return null;
    }
    private void clear() {
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm->{
                inRealm.deleteAll();
            });

        }catch(Exception e){

        }
    }



}
