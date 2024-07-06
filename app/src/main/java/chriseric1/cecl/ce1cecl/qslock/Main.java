package chriseric1.cecl.ce1cecl.qslock;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.util.Log;

public class Main implements IXposedHookLoadPackage {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.systemui")) {
            return;
        }

        XposedBridge.log("qslock" + ": started");

        XposedHelpers.findAndHookMethod("com.android.keyguard.KeyguardDisplayManager", lpparam.classLoader, "show", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.i("qslock", "show");
                Context context = (Context) AndroidAppHelper.currentApplication();
                if (context == null) {
                    Log.e("qslock", "context is null");
                    return;
                }
                Object statusBarManager = context.getSystemService("statusbar");
                if (statusBarManager == null) {
                    Log.e("qslock", "statusBarManager is null");
                    return;
                }
                XposedHelpers.callMethod(statusBarManager, "disable2", new Class<?>[]{Integer.class}, -1);
            }
        });

        XposedHelpers.findAndHookMethod("com.android.keyguard.KeyguardDisplayManager", lpparam.classLoader, "hide", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.i("qslock", "hide");
                Context context = (Context) AndroidAppHelper.currentApplication();
                if (context == null) {
                    Log.e("qslock", "context is null");
                    return;
                }
                Object statusBarManager = context.getSystemService("statusbar");
                if (statusBarManager == null) {
                    Log.e("qslock", "statusBarManager is null");
                    return;
                }
                XposedHelpers.callMethod(statusBarManager, "disable2", new Class<?>[]{Integer.class}, 0);
            }
        });
    }
}
