package com.example.cloudstoragedemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.example.cloudstoragedemo.repo.Repo;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        //Repo.downloadImage("billede.JPG", imageView);
       // Repo.uploadFile(this);

    }


    public void galleryPressed(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK); //make an implicit intent which will allow the user
        //to choose among different services to accomplish this task
        intent.setType("image/*"); //we need to set the type of the content to pick
        startActivityForResult(intent, 1); // start activity and in this case, expect an answer
    }

    public void cameraPressed(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //we might need the requestCode if we have more than one
        //implicit intent
        if(resultCode == -1) // -1 means that the code is OK
        {
            System.out.println("Success!");

            if(requestCode == 2) { //back from camera
                Bitmap bitmap = (Bitmap) data.getExtras().get("data"); //pick the image data and make it into a bitmap
                imageView.setImageBitmap(bitmap);
                Repo.uploadImage(imageView);
            }

            if(requestCode == 1) { // back from gallery intent
                Uri uri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imageView.setImageBitmap(bitmap);
                    Repo.uploadImage(imageView);
                } catch (Exception e)
                {

                }
            }
        }
    }
}
