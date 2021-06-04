package com.example.englishapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;
    ViewPager pager;
    BottomNavAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        bottomNav = findViewById(R.id.bottomNav);
        pager = findViewById(R.id.pager);
        adapter = new BottomNavAdapter(getSupportFragmentManager(), BottomNavAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pager.setAdapter(adapter);
        //khởi tạo bottom menu
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mVoca:
                        pager.setCurrentItem(0);
                        break;
                    case R.id.mPractice:
                        pager.setCurrentItem(1);
                        break;
                    case R.id.mSettings:
                        pager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
        //chuyển đổi trạng thái của viewPager
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNav.getMenu().findItem(R.id.mVoca).setChecked(true);
                        break;
                    case 1:
                        bottomNav.getMenu().findItem(R.id.mPractice).setChecked(true);
                        break;
                    default:
                        bottomNav.getMenu().findItem(R.id.mSettings).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}