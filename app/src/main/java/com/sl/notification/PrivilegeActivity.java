package com.sl.notification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sl.notification.toast.SLToast;

/**
 * Created by wuhongqi on 17/3/18.
 */
public class PrivilegeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_privilege1;
    private Button btn_privilege2;
    private Button btn_privilege3;
    private TextView tv_privilege;

    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privilege);

        btn_privilege1 = (Button) findViewById(R.id.btn_privilege1);
        btn_privilege2 = (Button) findViewById(R.id.btn_privilege2);
        btn_privilege3 = (Button) findViewById(R.id.btn_privilege3);
        tv_privilege = (TextView) findViewById(R.id.tv_privilege);

        btn_privilege1.setOnClickListener(this);
        btn_privilege2.setOnClickListener(this);
        btn_privilege3.setOnClickListener(this);

        checkNotification();
    }

    private void checkNotification() {
        boolean enable = NotificationUtil.isNotificationEnabled(this);
        boolean apiEnable = NotificationManagerCompat.from(this).areNotificationsEnabled();
        if (enable || apiEnable) {
            tv_privilege.setText("通知允许状态");
        } else {
            tv_privilege.setText("通知禁止状态");
            NotificationUtil.openNotification(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNotification();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_privilege1:
                Toast.makeText(PrivilegeActivity.this, "我是一个系统toast", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_privilege2:
                SLToast.makeText(this, "我是一个自定义Toast", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_privilege3:
                requestCall();
                break;
        }

    }

    private void requestCall() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            call10086();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //shouldShowRequestPermissionRationale主要用于给用户一个申请权限的解释，该方法只有在用户在上一次已经拒绝过你的这个权限申请。也就是说，用户已经拒绝一次了，你又弹个授权框，你需要给用户一个解释，为什么要授权，则使用该方法。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                Toast.makeText(PrivilegeActivity.this, "没有授权，此处可以设计一个引导用户开启权限的弹窗！！", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        } else {
            call10086();
        }
    }

    private void call10086() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:10086"));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call10086();
            } else {
                // Permission Denied
                Toast.makeText(PrivilegeActivity.this, "onRequestPermissionsResult 您没有授权该权限", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
