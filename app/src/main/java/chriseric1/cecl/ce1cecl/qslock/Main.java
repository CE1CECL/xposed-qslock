package chriseric1.cecl.ce1cecl.qslock;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.util.Log;
import java.lang.reflect.Method;

public class Main implements IXposedHookLoadPackage {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.systemui")) {
            XposedBridge.log("qslock: " + "lpparam.packageName != com.android.systemui");
            Log.e("qslock", "lpparam.packageName != com.android.systemui");
            return;
        }

        XposedBridge.log("qslock: " + "started");
        Log.i("qslock", "started");

        Method updateDisplaysMethod = this.findMethodByName(XposedHelpers.findClass("com.android.keyguard.KeyguardDisplayManager", lpparam.classLoader), "updateDisplays");
        XposedBridge.hookMethod(updateDisplaysMethod, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("qslock: " + "updateDisplays");
                Log.i("qslock", "updateDisplays");
                Context context = (Context) AndroidAppHelper.currentApplication();
                if (context == null) {
                    XposedBridge.log("qslock: " + "updateDisplays context == null");
                    Log.e("qslock", "context == null");
                    return;
                }
                Object statusBarManager = context.getSystemService("statusbar");
                if (statusBarManager == null) {
                    XposedBridge.log("qslock: " + "updateDisplays statusBarManager == null");
                    Log.e("qslock", "statusBarManager == null");
                    return;
                }
                boolean isLockScreenActive = param.args[0].toString() == "true";
                if (isLockScreenActive) {
                    XposedBridge.log("qslock: " + "isLockScreenActive true");
                    Log.i("qslock", "isLockScreenActive true");
                    XposedHelpers.callMethod(statusBarManager, "disable", -1);
                    XposedHelpers.callMethod(statusBarManager, "disable2", -1);
                } else {
                    XposedBridge.log("qslock: " + "isLockScreenActive false");
                    Log.i("qslock", "isLockScreenActive false");
                    XposedHelpers.callMethod(statusBarManager, "disable", 0);
                    XposedHelpers.callMethod(statusBarManager, "disable2", 0);
                }
            }
        });

    private Method findMethodByName(Class cl, String name) {
        Method[] methods = cl.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName() == name) {
                return methods[i];
            }
        }
        return null;
    }
}
