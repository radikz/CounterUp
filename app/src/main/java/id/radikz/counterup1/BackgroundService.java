package id.radikz.counterup1;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class BackgroundService extends Service {

    public static String str_receiver = "id.radikz.counterup1";

    private Handler mHandler = new Handler();
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String strDate;
    Date date_current, date_diff, date_diff2;
    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;

    //String str_testing;

    private Timer mTimer = null;
    public static final long NOTIFY_INTERVAL = 1000;
    Intent intent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mpref.edit();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        Toast.makeText(this, mpref.getString("timeAwal",""), Toast.LENGTH_SHORT).show();

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 5, NOTIFY_INTERVAL);


        intent = new Intent(str_receiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job


        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    calendar = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                    strDate = simpleDateFormat.format(calendar.getTime());
                    Log.e("strDate", strDate);
                    twoDatesBetweenTime();

                }

            });
        }

    }

    public String twoDatesBetweenTime() {


        try {
            date_current = simpleDateFormat.parse(strDate);
        } catch (Exception e) {

        }

        try {
            date_diff = simpleDateFormat.parse(mpref.getString("timeAwal",""));
        } catch (Exception e) {}

        try {

            long long_hours = Math.abs(date_current.getTime() - date_diff.getTime());
            //long long_hours = TimeUnit.DAYS.convert(long_diff, TimeUnit.MILLISECONDS);

            long int_timer = TimeUnit.HOURS.toMillis(1);
            //long long_hours = diff;
            long diffSeconds2 = long_hours / 1000 % 60;
            long diffMinutes2 = long_hours / (60 * 1000) % 60;
            long diffHours2 = long_hours / (60 * 60 * 1000);
            String str_testing = String.format("%02d",diffHours2) + ":" +
                    String.format("%02d",diffMinutes2) + ":" + String.format("%02d",diffSeconds2);
            mEditor.putString("timeAkhir", str_testing).commit();

            intent.putExtra("time",str_testing);
            sendBroadcast(intent);

        }catch (Exception e){
            mTimer.cancel();
            mTimer.purge();

        }

        return "";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mEditor.putBoolean("finish", true).commit();
        mTimer.cancel();
        Log.e("Service finish","Finish");
    }
}
