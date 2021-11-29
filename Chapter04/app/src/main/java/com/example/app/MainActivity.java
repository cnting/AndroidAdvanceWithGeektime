package com.example.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //在layout中引用了图片，在代码中再创建一个bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
        ImageView imageView = findViewById(R.id.imageView1);
        imageView.setImageBitmap(bitmap);

        try {
            File file = getExternalCacheDir();
            Debug.dumpHprofData(file.getAbsolutePath() + File.separator + "test_" + System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}