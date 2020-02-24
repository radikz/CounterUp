package id.radikz.counterup1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button mulai_hitung, selesai_hitung;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String date_time;
    TextView stopwatch;

    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stopwatch =  findViewById(R.id.textView_stopwatch);
        mulai_hitung =  findViewById(R.id.button_mulai_hitung);
        selesai_hitung = findViewById(R.id.button_selesai_hitung);
        prefManager = new PrefManager(this);

        mpref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mpref.edit();

        mulai_hitung.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selesai_hitung.setEnabled(true);
                mulai_hitung.setEnabled(false);
                mulai_hitung.setBackgroundColor(Color.GRAY);
                selesai_hitung.setBackgroundColor(Color.RED);


                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                date_time = simpleDateFormat.format(calendar.getTime());

                mEditor.putString("timeAwal", date_time).commit();
                prefManager.saveString("pengisian_filling1_timeAwal", date_time);


                Intent intent_service = new Intent(MainActivity.this, BackgroundService.class);
                startService(intent_service);
            }
        });

        selesai_hitung.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selesai_hitung.setBackgroundColor(Color.GRAY);
                mulai_hitung.setBackgroundColor(Color.RED);
                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                date_time = simpleDateFormat.format(calendar.getTime());
                prefManager.saveString("pengisian_filling1_timeAkhir", date_time);
                stopwatch.setText(mpref.getString("timeAkhir", ""));

                Intent intent = new Intent(MainActivity.this,BackgroundService.class);
                stopService(intent);

                mEditor.clear().commit();

                mulai_hitung.setEnabled(true);
                selesai_hitung.setEnabled(false);

            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str_time = intent.getStringExtra("time");
            stopwatch.setText(str_time);

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.this.registerReceiver(broadcastReceiver,new IntentFilter(BackgroundService.str_receiver));

    }

    @Override
    public void onPause() {

        //String str_time = inte.getStringExtra("time");
        //tv_timer.setText(str_time);
        super.onPause();
        MainActivity.this.unregisterReceiver(broadcastReceiver);
    }


}
