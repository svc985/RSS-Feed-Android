package org.prikic.yafr.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.prikic.yafr.BuildConfig;
import org.prikic.yafr.R;

public class AboutActivity extends AppCompatActivity {

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_screen);

        TextView applicationVersion = (TextView) findViewById(R.id.application_version);
        applicationVersion.setText(BuildConfig.VERSION_NAME);

    }
}
