package net.kdt.pojavlaunch.prefs.screens;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.kdt.pickafile.FileListView;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.prefs.LauncherPreferences;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Preference for the main screen, any sub-screen should inherit this class for consistent behavior,
 * overriding only onCreatePreferences
 */
public class LauncherPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.setBackgroundColor(Color.parseColor("#232323"));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle b, String str) {
        addPreferencesFromResource(R.xml.pref_main);
        findPreference("control_mouse_setting").setOnPreferenceClickListener((preference) -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
            return true;
        });

        findPreference("control_mouse_remove").setOnPreferenceClickListener((preference) -> {
            File file = new File(Tools.DIR_GAME_HOME, "mouse");
            if (file.exists()) {
                file.delete();
            }
            Toast.makeText(getContext(), R.string.notif_mouse1, Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public void onActivityResult(
            int requestCode, int resultCode, final Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            // Handle error
            return;
        }

        if (requestCode == 1) {// Get photo picker response for single select.
            Uri currentUri = data.getData();
            try {
                File file = new File(Tools.DIR_GAME_HOME, "mouse");
                if (file.exists()) {
                    file.delete();
                }

                InputStream stream1 = getContext().getContentResolver().openInputStream(currentUri);
                FileOutputStream stream = new FileOutputStream(file);

                IOUtils.copy(stream1, stream);
                stream.close();
                stream1.close();
                Toast.makeText(getContext(), R.string.notif_mouse, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        if(sharedPreferences != null) sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        if(sharedPreferences != null) sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences p, String s) {
        LauncherPreferences.loadPreferences(getContext());
    }

    protected Preference requirePreference(CharSequence key) {
        Preference preference = findPreference(key);
        if(preference != null) return preference;
        throw new IllegalStateException("Preference "+key+" is null");
    }
    @SuppressWarnings("unchecked")
    protected <T extends Preference> T requirePreference(CharSequence key, Class<T> preferenceClass) {
        Preference preference = requirePreference(key);
        if(preferenceClass.isInstance(preference)) return (T)preference;
        throw new IllegalStateException("Preference "+key+" is not an instance of "+preferenceClass.getSimpleName());
    }
}
