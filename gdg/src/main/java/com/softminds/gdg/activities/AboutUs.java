/*
*   Copyright (c) Ashar Khan 2017. <ashar786khan@gmail.com>
*    This file is part of Google Developer Group's Android Application.
*   Google Developer Group 's Android Application is free software : you can redistribute it and/or modify
*    it under the terms of GNU General Public License as published by the Free Software Foundation,
*   either version 3 of the License, or (at your option) any later version.
*
*   This Application is distributed in the hope that it will be useful
*    but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*   or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General  Public License for more details.
*
*   You should have received a copy of the GNU General Public License along with this Source File.
*   If not, see <http:www.gnu.org/licenses/>.
 */

package com.softminds.gdg.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.softminds.gdg.BuildConfig;
import com.softminds.gdg.R;
import com.softminds.gdg.utils.Constants;

public class AboutUs extends AppCompatActivity {

    TextView title,version,text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        title = findViewById(R.id.gdg_title);
        version = findViewById(R.id.textView3);
        text = findViewById(R.id.textView4);

        Typeface product = Typeface.createFromAsset(getAssets(), Constants.PathConstants.PRODUCT_SANS_FONT);

        text.setTypeface(product);
        version.setTypeface(product);
        title.setTypeface(product);

        String versionText = getString(R.string.version)+ " " + BuildConfig.VERSION_NAME;
        version.setText(versionText);
    }


    public void ShowSources(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.github.com/coder3101/gdgApp"));
        startActivity(intent);
    }
}
