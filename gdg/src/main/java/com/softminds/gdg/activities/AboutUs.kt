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

package com.softminds.gdg.activities

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.softminds.gdg.BuildConfig
import com.softminds.gdg.R
import com.softminds.gdg.utils.Constants
import kotlinx.android.synthetic.main.activity_about_us.*

class AboutUs : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        val product = Typeface.createFromAsset(assets, Constants.PathConstants.PRODUCT_SANS_FONT)

        gdg_title.typeface = product
        textView3.typeface = product
        textView4.typeface = product

        val versionText = getString(R.string.version) + " " + BuildConfig.VERSION_NAME
        textView3.text = versionText
    }


    fun showSource(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.github.com/coder3101/gdgApp")
        startActivity(intent)
    }
}
