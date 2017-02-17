package com.duke.switchbutton_test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SwitchButtonView myswitchbutton;
    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = (TextView) findViewById(R.id.show);
        myswitchbutton = (SwitchButtonView) findViewById(R.id.myswitchbutton);
        myswitchbutton.setOnToggleChangeListener(new SwitchButtonView.OnToggleChangeListener() {
            @Override
            public void onChange(boolean isToggleOn) {
                show.setText(isToggleOn ? "开" : "关");
                show.setTextColor(isToggleOn ? Color.RED : Color.BLACK);
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myswitchbutton.setIsToggleOn(!myswitchbutton.getIsToggleOn());
            }
        });
    }
}