package com.sl.notification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private Button mBtn1;
    private Button mBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suo_activity);
        findViews();
        initData();
    }

    private void findViews() {
        mBtn1 = (Button) findViewById(R.id.suo_btn1);
        mBtn2 = (Button) findViewById(R.id.suo_btn2);

        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
    }

    private void initData() {

    }

    public void showRemoteNotification() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.suo_btn1:
                startActivity(new Intent(this, NormalNotificationActivity.class));
                break;
            case R.id.suo_btn2:
                startActivity(new Intent(this, CustomNotificationActivity.class));
                break;
        }
    }

}
