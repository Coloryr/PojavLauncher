package coloryr.colormc.load;

import android.app.Application;
import android.util.Log;
import net.kdt.pojavlaunch.PojavApplication;
import net.kdt.pojavlaunch.utils.JREUtils;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("MainApplication", "Application create");

        PojavApplication.init(this);
    }
}
