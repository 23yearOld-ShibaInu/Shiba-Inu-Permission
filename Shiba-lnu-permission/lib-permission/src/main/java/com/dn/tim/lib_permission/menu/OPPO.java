package com.dn.tim.lib_permission.menu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.dn.tim.lib_permission.menu.base.IMenu;

/**
 * @date 创建时间：2018/4/18
 * @description
 */

public class OPPO implements IMenu {

    @Override
    public Intent getMenuIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }

}
