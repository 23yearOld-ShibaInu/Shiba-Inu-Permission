package com.trust.testpermisson;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dn.tim.lib_permission.annotation.Permission;
import com.dn.tim.lib_permission.annotation.PermissionCanceled;
import com.dn.tim.lib_permission.annotation.PermissionDenied;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.test).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       test();
    }

    @Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void test() {
        Toast.makeText(this, "请求一个权限成功（写权限）", Toast.LENGTH_SHORT).show();
    }

    @PermissionCanceled
    private void canceled(){
        Toast.makeText(this, "你拒绝了权限", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied
    private void denied(){
        Toast.makeText(this, "拒绝这个权限软件无法运行，请开启！", Toast.LENGTH_SHORT).show();
    }
}
