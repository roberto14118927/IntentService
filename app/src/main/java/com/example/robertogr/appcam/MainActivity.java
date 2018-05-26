package com.example.robertogr.appcam;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaDataSource;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonChoose, buttonUpload;
    private ImageView imageView;
    private EditText editText;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int STORAGE_PERMISSION_CODE = 2342;
    private static final int PICK_IMAGE_REQUEST = 22;

    private Uri filePath;

    private Bitmap bitmap;

    private static final String UPLOAD_URL = "http://jovenesemp.ddns.net/api/who-is/photo";

    //Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //intent = new Intent(this, ServicesUpload.class);
        //startService(intent);

        requestStoragePermission();

        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);

        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editTextName);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);




    }

    private void requestStoragePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permiso ok", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Error permisos", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void callIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e){

            }
        }*/
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            filePath = data.getData();
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
            filePath = getImageUri(getApplicationContext(), bitmap);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getPath(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null,null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);

        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,MediaStore.Images.Media._ID + "=?", new String[]{document_id}, null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    private void uploadImage(){
        String path = getPath(filePath);
       // Toast.makeText(this,path,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ServicesUpload.class);
        intent.putExtra("file", path.toString());
        startService(intent);
        /*String name = editText.getText().toString().trim();
        String path = getPath(filePath);
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjA4ZjdmZjdiNGM4MTUwZmU2YWU1MDFhMjYyMDAxZDZiZDQ1ZGU3NjgzZjNiZTlhN2Q1Nzg1OWNlY2NmNjUwMGZmYTdkODEwYzJmNDI2ZGM1In0.eyJhdWQiOiIxIiwianRpIjoiMDhmN2ZmN2I0YzgxNTBmZTZhZTUwMWEyNjIwMDFkNmJkNDVkZTc2ODNmM2JlOWE3ZDU3ODU5Y2VjY2Y2NTAwZmZhN2Q4MTBjMmY0MjZkYzUiLCJpYXQiOjE1MjczMDg0MDIsIm5iZiI6MTUyNzMwODQwMiwiZXhwIjoxNTU4ODQ0NDAyLCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.C_-adYwLZhjzYyiA4r2bcGRO5hOOXEQwPAGtR4hH9eByuJXkYql3d0WjX4U5hX3fvt9VMicIxXu4GkO9u3VZD941aUv3M75DIdITdOLW49ZD4GSz0OW33-AKsM4rHspULaoVazlokQQX2S-I59ZmYUho_gCAElZwtE24-0Fwr-GrQ0GyEkNaWtlL6XoCSueTfW6KbVfO4lyPzAUsALYTf5P7CV2cdRIcWG5BuCq6WEwXWQCYCOVLzyTaAijJNn3mAlvAwv3xNGj6yVHj0Y3B8Dom8iEaBRvmWc17ZK38XsqJXNEdy-PgzH0UYIA94tNUT8K7AnxFMUep5dYJKtLG181rLWU83CQaJVIwU1rVEBSUu5idFJGY2gIQSg0zu4t2aXSKY89VNXKgEIE64eag3LOgtOildLgTetSjb9hTTAeI1Z9XLV-r_CqJ9ts6vo4C62jDrTxMj6HDQ1MD7omrxYScZHj5dAdZXsYrUFIilbpUPFhExkEmJSZ9YuorsPS8dD1wQWIvY7GPDHvDCKxUWC4sj5AY8E0KPxNRG7K034rBZhcbC2PC96uStPUmcLTd1gfz1tw7M-55JGq95L3_wQLcCo1eT2ojYkN94BtZCyNkzzPRkvUI0MvjXB8AB8VWe6Wla0qnjF8uZtQhEuhXDtnTBIY22NRvDyCXGtREAVQ";

        try{
            String uploadid = UUID.randomUUID().toString();

            new MultipartUploadRequest(this, uploadid, UPLOAD_URL)
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Bearer " + token)
                    .addParameter("id", "2")
                    .addFileToUpload(path,"image")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
        }catch (Exception e){
            Log.e("", e.getMessage(), e);
        }*/
    }

    @Override
    public void onClick(View view){
        if(view == buttonUpload){
            uploadImage();
        }

        if(view == buttonChoose){
            callIntent();
        }
    }
}
