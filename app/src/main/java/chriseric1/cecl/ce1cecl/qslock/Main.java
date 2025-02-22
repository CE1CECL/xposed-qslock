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
            XposedBridge.log("qslock: " + "lpparam.packageName != com.android.systemui");
            Log.e("qslock", "lpparam.packageName != com.android.systemui");
            return;
        }

        XposedBridge.log("qslock: " + "started");
        Log.i("qslock", "started");

        XposedHelpers.findAndHookMethod("com.android.keyguard.KeyguardDisplayManager", lpparam.classLoader, "show", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("qslock: " + "show");
                Log.i("qslock", "show");
                Context context = (Context) AndroidAppHelper.currentApplication();
                if (context == null) {
                    XposedBridge.log("qslock: " + "show context == null");
                    Log.e("qslock", "context == null");
                    return;
                }
                Object statusBarManager = context.getSystemService("statusbar");
                if (statusBarManager == null) {
                    XposedBridge.log("qslock: " + "show statusBarManager == null");
                    Log.e("qslock", "statusBarManager == null");
                    return;
                }
                XposedHelpers.callMethod(statusBarManager, "disable", -1);
                XposedHelpers.callMethod(statusBarManager, "disable2", -1);
            }
        });

        XposedHelpers.findAndHookMethod("com.android.keyguard.KeyguardDisplayManager", lpparam.classLoader, "hide", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("qslock: " + "hide");
                Log.i("qslock", "hide");
                Context context = (Context) AndroidAppHelper.currentApplication();
                if (context == null) {
                    XposedBridge.log("qslock: " + "hide context == null");
                    Log.e("qslock", "context == null");
                    return;
                }
                Object statusBarManager = context.getSystemService("statusbar");
                if (statusBarManager == null) {
                    XposedBridge.log("qslock: " + "hide statusBarManager == null");
                    Log.e("qslock", "statusBarManager == null");
                    return;
                }
                XposedHelpers.callMethod(statusBarManager, "disable", 0);
                XposedHelpers.callMethod(statusBarManager, "disable2", 0);
            }
        });
    }
}
