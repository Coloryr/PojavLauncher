package coloryr.colormc.load;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.core.content.ContextCompat;
import net.kdt.pojavlaunch.PojavApplication;
import net.kdt.pojavlaunch.utils.JREUtils;
import net.kdt.pojavlaunch.utils.LocaleUtils;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("MainApplication", "Application create");

        PojavApplication.init(this);
    }

    public static void permission(Activity context)
    {
        if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 29 && !IsStorageAllowed(context))
            requestStoragePermission(context);

        if (Build.VERSION.SDK_INT >= 33 &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_DENIED)
            context.requestPermissions(new String[] { Manifest.permission.POST_NOTIFICATIONS }, 1);
    }

    public static boolean IsStorageAllowed(Activity context)
    {
        //Getting the permission status
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result1 ==  PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private static void requestStoragePermission(Activity context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleUtils.setLocale(base));
    }
}
