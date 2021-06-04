package com.example.englishapp.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.englishapp.R;
import com.example.englishapp.notification.NotiTimeReceiver;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.getSystemService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private int NOTIFICATION_ID = 1;
    TextView tvTime;
    Button btSetTime;
    View v;
    SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_settings, container, false);
        initView();
        sharedPreferences = getContext().getSharedPreferences("NOTI_TIME", MODE_PRIVATE);
        tvTime.setText(sharedPreferences.getString("TIME", "08:00"));
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        btSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //set TextView
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);
                                String time = simpleDateFormat.format(calendar.getTime());

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("TIME", time);
                                editor.commit();
                                sharedPreferences = getContext().getSharedPreferences("NOTI_TIME", MODE_PRIVATE);
                                tvTime.setText(sharedPreferences.getString("TIME", "08:00"));

                                //set Broadcast Notification
                                Intent intent = new Intent(getContext(), NotiTimeReceiver.class);
                                intent.putExtra("notificationID", NOTIFICATION_ID);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                        getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                                //create time
                                long alarmStartTime = calendar.getTimeInMillis();
                                //set alarm
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_DAY, alarmStartTime, pendingIntent);
                                Toast.makeText(getContext(), "Đặt thời gian thành công", Toast.LENGTH_SHORT).show();
                            }
                        }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });
        return v;
    }

    private void initView() {
        tvTime = v.findViewById(R.id.tvTime);
        btSetTime = v.findViewById(R.id.btSetTime);
    }
}