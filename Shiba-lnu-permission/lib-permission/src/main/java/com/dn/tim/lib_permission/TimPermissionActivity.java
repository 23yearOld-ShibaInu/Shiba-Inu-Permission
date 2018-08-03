package com.dn.tim.lib_permission;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.dn.tim.lib_permission.core.IPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具activity
 */
public class TimPermissionActivity extends Activity{

    private static final String PARAM_PERMISSION = "param_permission";
    private static final String PARAM_REQUEST_CODE = "param_request_code";

    private String[] mPermissions;
    private int mRequestCode;
    private static IPermission permissionListener;

    /**
     * 请求权限
     */
    public static void requestPermission(Context context, String[] permissions, int requestCode, IPermission iPermission) {

        permissionListener = iPermission;
        Intent intent = new Intent(context, TimPermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArray(PARAM_PERMISSION, permissions);
        bundle.putInt(PARAM_REQUEST_CODE, requestCode);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 权限申请
        setContentView(R.layout.tim_permission_layout);
        this.mPermissions = getIntent().getStringArrayExtra(PARAM_PERMISSION);
        this.mRequestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE, -1);

        if (mPermissions == null || mRequestCode < 0 || permissionListener == null) {
            this.finish();
            return;
        }

        //检查是否已授权
        if (PermissionUtils.hasPermission(this, mPermissions)) {
            permissionListener.ganted();
            finish();
            return;
        }
        ActivityCompat.requestPermissions(this, this.mPermissions, this.mRequestCode);

    }

    /**
     * grantResults对应于申请的结果，这里的数组对应于申请时的第二个权限字符串数组。
     * 如果你同时申请两个权限，那么grantResults的length就为2，分别记录你两个权限的申请结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //请求权限成功
        if (PermissionUtils.verifyPermission(this, grantResults)) {
            permissionListener.ganted();
            finish();
            return;
        }

        //用户点击了不再显示
        if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {
//            if (permissions.length != grantResults.length) {
//                return;
//            }
//            List<String> deinedList = new ArrayList<>();
//            for (int i = 0; i < grantResults.length; i++) {
//                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                    deinedList.add(permissions[i]);
//                }
//            }
            permissionListener.denied();
            finish();
            return;
        }

        //用户取消
        permissionListener.cancled();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
