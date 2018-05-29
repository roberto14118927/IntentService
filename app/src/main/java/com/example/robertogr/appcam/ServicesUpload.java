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
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjE3Yjg2YmZlYTA4NGE5ZDk3Zjk2NDk0OTVmMGRlYjdhNjhiZmIxYjI2YjQ0OTZhZGRkY2Q0MmJkZWFiODdiNDdlMWYwM2M1NTJhNTcyZjE2In0.eyJhdWQiOiIxIiwianRpIjoiMTdiODZiZmVhMDg0YTlkOTdmOTY0OTQ5NWYwZGViN2E2OGJmYjFiMjZiNDQ5NmFkZGRjZDQyYmRlYWI4N2I0N2UxZjAzYzU1MmE1NzJmMTYiLCJpYXQiOjE1Mjc2MDkwMDIsIm5iZiI6MTUyNzYwOTAwMiwiZXhwIjoxNTU5MTQ1MDAyLCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.pBH9KEUxoPzsxTrORs8fEeSCvi5VKA7p-5xCUarqLT12P_OzlsOvoKxWKJfjVe87PwjzJnwXHLN1ofcT4_shcrJ7pEyPMXC6lIm625vsI1qchwWW41gkWBf0Gkoe-hvTGnWlcOf8mn6wTqDoSk5AtSJlvaavtWgEZ4ounbFL0qdaXWt6AJYy2ZkE585eZBFyjfvwZKc-_Ze61DdD_KUy-luukt8Y7sJ13CLsQIVtRkLNnWw1W8Kr3u4OBO679jCRY0jh3qZMV_szjlBLcQ8lqlF6m9t4i4bnnSS58kfSgdPojL69JJhNd6ROq9TOukT5AUUTTxhqVSY8ZakLYfQPLVg45gapCCNjqD00d3fRsF_K9fhUeL262ftoSIwO3D8qMU_ZsfyMPC0lUv0yEBGMGOdDKLIRBMKdsrCkTgxwI0u7FOCMB5ft11eLPsPzVzUldSw2aQAorfApY1599StjlSQURAxLRvccF2e6enTTcvR5kYFD8C2g1LihcZf3M-2ZHPf-iShr7_p1bJ_mT0LKnmWwNHex7z0rJxIhdqfC6cHY0sYNu2gpJbKgukVVjAyfnzsm-NtE7HeqTXa9l4dlQPCNdZTKFJcgRfH0hLE5cRjdtOvEAY-9kwCLHXF4pzj_d61kimhMiUJoqgBTIUW4sYjD78WahxCIjVl4tN6sd8Q";

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
