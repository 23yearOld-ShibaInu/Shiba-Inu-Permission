package com.dn.tim.lib_permission;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;

import com.dn.tim.lib_permission.menu.Default;
import com.dn.tim.lib_permission.menu.OPPO;
import com.dn.tim.lib_permission.menu.VIVO;
import com.dn.tim.lib_permission.menu.base.IMenu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class PermissionUtils {

    public static final int DEFAULT_REQUEST_CODE = 0xABC1994;

    private static SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23);
    }

    private static HashMap<String, Class<? extends IMenu>> permissionMenu = new HashMap<>();

    private static final String MANUFACTURER_DEFAULT = "Default";//默认

    public static final String MANUFACTURER_HUAWEI = "huawei";//华为
    public static final String MANUFACTURER_MEIZU = "meizu";//魅族
    public static final String MANUFACTURER_XIAOMI = "xiaomi";//小米
    public static final String MANUFACTURER_SONY = "sony";//索尼
    public static final String MANUFACTURER_OPPO = "oppo";
    public static final String MANUFACTURER_LG = "lg";
    public static final String MANUFACTURER_VIVO = "vivo";
    public static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    public static final String MANUFACTURER_LETV = "letv";//乐视
    public static final String MANUFACTURER_ZTE = "zte";//中兴
    public static final String MANUFACTURER_YULONG = "yulong";//酷派
    public static final String MANUFACTURER_LENOVO = "lenovo";//联想

    static {
        permissionMenu.put(MANUFACTURER_DEFAULT, Default.class);
        permissionMenu.put(MANUFACTURER_OPPO, OPPO.class);
        permissionMenu.put(MANUFACTURER_VIVO, VIVO.class);
    }

    /**
     * 检查是否需要请求权限
     * @param context
     * @param permissions
     * @return
     *      false --- 需要  true ---不需要
     */
    public static boolean hasPermission(Context context, String ...permissions) {

        for (String permission : permissions) {
            if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Description 检测某个权限是否已经授权；如果已授权则返回true，如果未授权则返回false
     * @version
     */
    private static boolean hasSelfPermission(Context context, String permission) {
        try {
            // ContextCompat.checkSelfPermission，主要用于检测某个权限是否已经被授予。
            // 方法返回值为PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED
            // 当返回DENIED就需要进行申请授权了。
            return ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return false;
        }
    }

    /**
     * @Description 如果在这个SDK版本存在的权限，则返回true
     * @version
     */
    private static boolean permissionExists(String permission) {
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion;
    }

    public static boolean verifyPermission(Context context, int ... gantedResults) {

        if (gantedResults == null || gantedResults.length == 0 ) {
            return false;
        }

        for (int ganted : gantedResults) {
            if (ganted != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Description 检查需要给予的权限是否需要显示理由
     * @version
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            // 这个API主要用于给用户一个申请权限的解释，该方法只有在用户在上一次已经拒绝过你的这个权限申请。
            // 也就是说，用户已经拒绝一次了，你又弹个授权框，你需要给用户一个解释，为什么要授权，则使用该方法。
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }


    public static void invokAnnotation(Object object, Class annotationClass) {

        //获取切面上下文的类型
        Class<?> clz = object.getClass();
        //获取类型中的方法
        Method[] methods = clz.getDeclaredMethods();
        if (methods == null) {
            return;
        }
        for (Method method : methods) {
            //获取该方法是否有PermissionCanceled注解
            boolean isHasAnnotation = method.isAnnotationPresent(annotationClass);
            if (isHasAnnotation) {
                method.setAccessible(true);
                try {
                    method.invoke(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @date 创建时间 2018/4/18
     * @author Jiang zinc
     * @Description 前往权限设置菜单的工具类
     * @version
     */
    public static void goToMenu(Context context) {

        Class clazz = permissionMenu.get(Build.MANUFACTURER.toLowerCase());
        if (clazz == null) {
            clazz = permissionMenu.get(MANUFACTURER_DEFAULT);
        }

        try {
            IMenu iMenu = (IMenu) clazz.newInstance();

            Intent menuIntent = iMenu.getMenuIntent(context);
            if (menuIntent == null) {
                return;
            }
            context.startActivity(menuIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
