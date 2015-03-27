package com.example.inakhatri.downloadimage;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends ActionBarActivity
{
    ImageView image;
    Bitmap bmp;
    ProgressDialog pDialog;
    String imageurl="http://images6.fanpop.com/image/photos/34800000/Nature-Wallpaper-daydreaming-34811098-1024-768.jpg";
    private final int SET_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this,"This is toast",Toast.LENGTH_LONG).show();
        image = (ImageView) findViewById(R.id.image);
        Thread thread = new Thread(new Task());
        thread.start();
        image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //This class contains all the information necessary to request a new download
                DownloadManager.Request request=new DownloadManager.Request(Uri.parse(imageurl));
                request.setTitle("Download Complete");
                request.setDescription("File is being Downloaded");
                //If the file to be downloaded is to be scanned by MediaScanner, this method should be called before enqueue(Request) is called.
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //Guesses canonical filename that a download would have, using the URL and contentDisposition. File extension, if not defined, is added based on the mimetype
                String nameoffile= URLUtil.guessFileName(imageurl,null, MimeTypeMap.getFileExtensionFromUrl(imageurl));
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,nameoffile);
                DownloadManager download=(DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                download.enqueue(request);
            }
        });


    }
    class Task implements Runnable
    {
        private final String TAG = Task.class.getSimpleName();

        @Override
        public void run()
        {

            Log.d(TAG, "run");
            bmp = getBitmapFromURL(imageurl);
            UiHandler.sendEmptyMessage(SET_IMAGE);
        }
    }
   Handler UiHandler =  new Handler()
    {
    @Override
    public void handleMessage(Message inputMessage)
    {
        switch (inputMessage.what)
        {
            case SET_IMAGE :
                if (null != bmp)
                    image.setImageBitmap(bmp);
                break;
        }
    }
};
    public Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
            urlConnection.connect();
            InputStream input = urlConnection.getInputStream();
            bmp = BitmapFactory.decodeStream(input);
            return bmp;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            int id = item.getItemId();
            if (id == R.id.action_settings)
            {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


        }





