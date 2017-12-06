package com.draxvel.kommersant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class SettingActivity extends AppCompatActivity {

    ToggleButton tButtonsoundOn, tButtonsoundEffectOn, tButtonvibrationOn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //no title and full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setContentView(R.layout.activity_setting);

        initView();

    }

    private void initView() {
        tButtonsoundOn = (ToggleButton) findViewById(R.id.setting_music_on_btn);
        tButtonsoundEffectOn = (ToggleButton) findViewById(R.id.setting_sound_effects_on_btn);
        tButtonvibrationOn = (ToggleButton) findViewById(R.id.setting_vibro_on_btn);

        tButtonsoundOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AppData.isSoundOn=b;
            }
        });

        tButtonsoundEffectOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AppData.isSoundEffectOn=b;
            }
        });

        tButtonvibrationOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AppData.isVibrationOn=b;
            }
        });


        tButtonsoundOn.setChecked(AppData.isSoundOn);
        tButtonsoundEffectOn.setChecked(AppData.isSoundEffectOn);
        tButtonvibrationOn.setChecked(AppData.isVibrationOn);
    }

}

