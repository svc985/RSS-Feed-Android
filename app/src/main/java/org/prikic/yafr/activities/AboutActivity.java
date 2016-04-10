package org.prikic.yafr.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.prikic.yafr.BuildConfig;
import org.prikic.yafr.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_screen);

        TextView application_version = (TextView) findViewById(R.id.application_version);

        application_version.setText(BuildConfig.VERSION_NAME);

    }
}
