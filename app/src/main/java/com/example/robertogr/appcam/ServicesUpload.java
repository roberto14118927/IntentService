package com.example.robertogr.appcam;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.UUID;


public class ServicesUpload extends IntentService {
    private static final String UPLOAD_URL = "http://jovenesemp.ddns.net/api/who-is/photo";
    private static final String TAG = "OKOKOKO";

    public ServicesUpload(){
        super("ServicesUpload");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        Log.i(TAG, "THE SERVICE HAS NOW STARTED");
        String path = intent.getStringExtra("file");

        if(path != null) {
            Log.i(TAG, path);
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjM2ODE5MTVjYzVkNGZlNzhhMzA4NGFjZDc5OGRlNzYyOTdkOWRiMjYwMWVlZDA0NWM2MDhmNjU2ZmM5MTY0NWI1YmM0ZDVmOGM2YjExYjY1In0.eyJhdWQiOiIxIiwianRpIjoiMzY4MTkxNWNjNWQ0ZmU3OGEzMDg0YWNkNzk4ZGU3NjI5N2Q5ZGIyNjAxZWVkMDQ1YzYwOGY2NTZmYzkxNjQ1YjViYzRkNWY4YzZiMTFiNjUiLCJpYXQiOjE1MjczNTU5NTIsIm5iZiI6MTUyNzM1NTk1MiwiZXhwIjoxNTU4ODkxOTUyLCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.UWeDso0PdFkyGELYCCE4Ca92OBOYjEmcivTEppZuAiAHbBhx8aYSTuyyhyUsmMvT23xqZGE15_Z6-U0wXLLzYO9CStzecw5A0dlux_9OtvcnCYa1SBV9SscmbvBNT0fJjmOYJI36Rv1aerbinJXFKTwREnHqqU0vBHbCDAg-sgpXtBxHCuP3r6zh5nm68eIGy9GtokK4QuU9vW607-Z5lJLD92mOZKA-kay63xHfjA9DDGYYA3DOI-Hmdn1jpWEiMnb028R4VLfSfWPvoWDTtefZhtHmBTeTkYXxc4sA_sXSUcTaooz8aoSG2qvKjMqMGIG8uF_2BpTVhik_L5FgPP12lGDgwI2IPx40kHBGLMytyVSnrpcPxh9mc9X9ADl5XAE0L-PDfCwYvnt1nDTiOkUXhZZPFAyehGa8hT2tg3ED35XVwQ8XAOoU-KZajQNG8KkaOxTgShuymwMH1XT4_ZzF-_ZnLypEghG3cGQEfPEmRfd0ma5LMU2PZKhe-W_dqz5fhbX_Aaji3_NQpV279JQLfJ7riDu5UI30t7r2JuhbKYeYQYGZjviCQJF64h_8-7uYeXdaMRLhPWRniwl7UxrJVFWrmhwA3cWFOKQQHZ7sLbIyt3A4xg3oeG1BZ3wCssxu2PkxWpX_QrurZH4HsGi_Dg0A6vATEzFTDh8xXRU";

            try {
                String uploadid = UUID.randomUUID().toString();

                new MultipartUploadRequest(this, uploadid, UPLOAD_URL)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer " + token)
                        .addParameter("id", "2")
                        .addFileToUpload(path, "image")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();
            } catch (Exception e) {
                //Log.e(TAG, e.getMessage(), e);
            }
        }
    }
}
