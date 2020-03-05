package com.example.cloudstoragedemo.repo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class Repo {

    static FirebaseStorage storage = FirebaseStorage.getInstance();

    public static void uploadImage(ImageView imageView)
    {
        StorageReference ref = storage.getReference("image.jpg");

        //makes an imageview into a bytearray
        Drawable drawable = imageView.getDrawable();
        Bitmap bm = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        ref.putBytes(bitmapdata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                System.out.println("Succes med upload af billede!");
            }
        });
    }

    public static void uploadFile(Context context)
    {
        try {
            InputStream is = context.getAssets().open("alice.txt");
            StorageReference ref = storage.getReference("alice.txt");
            ref.putStream(is).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    System.out.println("upload completed");
                }
            });

        } catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void downloadImage(String name, final ImageView iv) {
        StorageReference ref = storage.getReference(name);
        int max = 1024 * 1024 * 2; // max 2 megabytes
        ref.getBytes(max).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iv.setImageBitmap(bm); //set imagedata to imageview
            }
        });

    }


}
