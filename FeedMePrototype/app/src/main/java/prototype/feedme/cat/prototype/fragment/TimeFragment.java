package prototype.feedme.cat.prototype.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import java.util.Calendar;

import prototype.feedme.cat.prototype.R;
import prototype.feedme.cat.prototype.notification.MyReceiver;

/**
 * Created by User on 2015/1/17.
 */
public class TimeFragment  extends DialogFragment {
    private Button time_first,time_second,time_third;
    private CheckBox checkBox_first,checkBox_second,checkBox_third;
    private int hour_first,hour_second,hour_third;
    private int minute_first,minute_second,minute_third;
    private int set_hour,set_minute;
    private int notificationId;
    private boolean set_btn_text_first,set_btn_text_second,set_btn_text_third;
    private boolean set_check_first,set_check_second,set_check_third;
    String format = "%02d";
    public static final String ALARM_TIME="ALARM_TIME";
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState)
    {
        View dialogLayout = getActivity().getLayoutInflater().inflate(R.layout.notificaton_setting, null);
        time_first=(Button)dialogLayout.findViewById(R.id.time_first);
        time_second=(Button)dialogLayout.findViewById(R.id.time_second);
        time_third=(Button)dialogLayout.findViewById(R.id.time_third);
        checkBox_first=(CheckBox)dialogLayout.findViewById(R.id.checkBox_first);
        checkBox_second=(CheckBox)dialogLayout.findViewById(R.id.checkBox_second);
        checkBox_third=(CheckBox)dialogLayout.findViewById(R.id.checkBox_third);
        readSharePreferences();
        if(set_btn_text_first==true)time_first.setText(String.format(format, hour_first)+" : "+String.format(format, minute_first));
        if(set_btn_text_second==true)time_second.setText(String.format(format, hour_second)+" : "+String.format(format, minute_second));
        if(set_btn_text_third==true)time_third.setText(String.format(format, hour_third)+" : "+String.format(format, minute_third));
        checkBox_first.setChecked(set_check_first);
        checkBox_second.setChecked(set_check_second);
        checkBox_third.setChecked(set_check_third);
        checkBox_first.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    notificationId=1;
                    set_check_first=isChecked;
                    set_hour=hour_first;
                    set_minute=minute_first;
                    setNotification();
                }
                else{
                    cancelNotification(1);
                    set_check_first=isChecked;
                }
                writeSharePreferences();
            }
        });
        checkBox_second.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    notificationId=2;
                    set_check_second=isChecked;
                    set_hour=hour_second;
                    set_minute=minute_second;
                    setNotification();
                }
                else{
                    cancelNotification(2);
                    set_check_second=isChecked;
                }
                writeSharePreferences();
            }
        });
        checkBox_third.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    notificationId=3;
                    set_check_third=isChecked;
                    set_hour=hour_third;
                    set_minute=minute_third;
                    setNotification();
                }
                else{
                    cancelNotification(3);
                    set_check_third=isChecked;
                }
                writeSharePreferences();
            }
        });


        time_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationId=1;

                checkBox_first.setChecked(true);
                TimePickerDialog tpd=new TimePickerDialog(getActivity(), mTimesetlistener, hour_first, minute_first, true);
                tpd.show();
            }
        });
        time_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationId=2;

                checkBox_second.setChecked(true);
                TimePickerDialog tpd=new TimePickerDialog(getActivity(), mTimesetlistener, hour_second, minute_second, true);
                tpd.show();

            }
        });
        time_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationId=3;

                checkBox_third.setChecked(true);
                TimePickerDialog tpd=new TimePickerDialog(getActivity(), mTimesetlistener, hour_third, minute_third, true);
                tpd.show();
            }
        });

        AlertDialog.Builder dialog= new AlertDialog.Builder(getActivity());
        dialog.setTitle("Meow～～");
        dialog.setMessage("When can I have a meal？");
        dialog.setView(dialogLayout);
        return dialog.create();
    }
    private TimePickerDialog.OnTimeSetListener mTimesetlistener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            set_hour=hourOfDay;
            set_minute=minute;
            if(notificationId==1){
                if(checkBox_first.isChecked())setNotification();
                time_first.setText(String.format(format, set_hour)+" : "+String.format(format, set_minute));
                set_btn_text_first=true;
            }
            else if(notificationId==2){
                if(checkBox_second.isChecked())setNotification();
                time_second.setText(String.format(format, set_hour)+" : "+String.format(format, set_minute));
                set_btn_text_second=true;
            }
            else if(notificationId==3){
                if(checkBox_third.isChecked())setNotification();
                time_third.setText(String.format(format, set_hour)+" : "+String.format(format, set_minute));
                set_btn_text_third=true;
            }
            writeSharePreferences();
        }
    };
    private void setNotification() {
        Intent myIntent = new Intent(getActivity(), MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), notificationId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        //set the notification time
        calendar.set(Calendar.HOUR_OF_DAY, set_hour);
        calendar.set(Calendar.MINUTE, set_minute);
        calendar.set(Calendar.SECOND,00);
        long startUpTime = calendar.getTimeInMillis();
        System.out.println(startUpTime + "time" + System.currentTimeMillis());
            if (System.currentTimeMillis() > startUpTime) {
                startUpTime = startUpTime + AlarmManager.INTERVAL_DAY;
            }
        //repeat every day
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_DAY , pendingIntent);
        alarmManager.setExact (AlarmManager.RTC_WAKEUP, startUpTime , pendingIntent);
    }

    private void cancelNotification(int id) {
        Intent myIntent = new Intent(getActivity(), MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void readSharePreferences() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(ALARM_TIME,getActivity().MODE_PRIVATE);
        hour_first=sharedPref.getInt("hour_first", 0);
        minute_first=sharedPref.getInt("minute_first", 0);
        hour_second=sharedPref.getInt("hour_second", 0);
        minute_second=sharedPref.getInt("minute_second", 0);
        hour_third=sharedPref.getInt("hour_third", 0);
        minute_third=sharedPref.getInt("minute_third", 0);
        set_btn_text_first=sharedPref.getBoolean("set_btn_text_first", false);
        set_btn_text_second=sharedPref.getBoolean("set_btn_text_second", false);
        set_btn_text_third=sharedPref.getBoolean("set_btn_text_third", false);
        set_check_first=sharedPref.getBoolean("set_check_first", false);
        set_check_second=sharedPref.getBoolean("set_check_second", false);
        set_check_third=sharedPref.getBoolean("set_check_third", false);

    }
    private void writeSharePreferences() {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(ALARM_TIME,getActivity().MODE_PRIVATE).edit();
        if(notificationId==1){
            editor.putInt("hour_first",  set_hour);
            editor.putInt("minute_first", set_minute);
            editor.putBoolean("set_btn_text_first", set_btn_text_first);
        }
        else if(notificationId==2){
            editor.putInt("hour_second",  set_hour);
            editor.putInt("minute_second", set_minute);
            editor.putBoolean("set_btn_text_second", set_btn_text_second);
        }
        else if(notificationId==3){
            editor.putInt("hour_third",  set_hour);
            editor.putInt("minute_third", set_minute);
            editor.putBoolean("set_btn_text_third", set_btn_text_third);
        }
        editor.putBoolean("set_check_first", set_check_first);
        editor.putBoolean("set_check_second", set_check_second);
        editor.putBoolean("set_check_third", set_check_third);
        editor.commit();
        System.out.println("Alarm saved");
    }

}
