package com.cheat.module;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import com.cheat.R;
import com.cheat.utils.Config;

public class MainActivity extends AppCompatActivity {
    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        config = Config.getInstance(this);

        SwitchCompat aimbot = findViewById(R.id.sw_aimbot);
        aimbot.setChecked(config.isAimbotEnabled());
        aimbot.setOnCheckedChangeListener((b, checked) -> config.setAimbotEnabled(checked));

        SwitchCompat trigger = findViewById(R.id.sw_trigger);
        trigger.setChecked(config.isTriggerEnabled());
        trigger.setOnCheckedChangeListener((b, checked) -> config.setTriggerEnabled(checked));

        SwitchCompat esp = findViewById(R.id.sw_esp);
        esp.setChecked(config.isEspEnabled());
        esp.setOnCheckedChangeListener((b, checked) -> config.setEspEnabled(checked));

        SwitchCompat chams = findViewById(R.id.sw_chams);
        chams.setChecked(config.isChamsEnabled());
        chams.setOnCheckedChangeListener((b, checked) -> config.setChamsEnabled(checked));

        SwitchCompat fov = findViewById(R.id.sw_fov);
        fov.setChecked(config.isFovCircleEnabled());
        fov.setOnCheckedChangeListener((b, checked) -> config.setFovCircleEnabled(checked));

        SeekBar speed = findViewById(R.id.sb_speed);
        TextView speedLabel = findViewById(R.id.tv_speed);
        speed.setProgress((int) (config.getAimSpeed() * 100));
        speedLabel.setText("Aim speed: " + config.getAimSpeed());
        speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int p, boolean f) {
                float v = p / 100f;
                config.setAimSpeed(v);
                speedLabel.setText("Aim speed: " + v);
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });

        Button start = findViewById(R.id.btn_start);
        start.setOnClickListener(v -> startService(new Intent(this, CheatService.class)));
    }
}
