package com.hussainmukadam.watchit.notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;

/**
 * Created by hussain on 03/03/18.
 */

public class NotificationImageTask extends AsyncTask<String, Void, Bitmap> {

    private Context context;
    NotificationImageTask(Context context) {
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap itemBitmap = null;
        try {
            itemBitmap = Picasso.with(context).load(urls[0]).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemBitmap;
    }
}
