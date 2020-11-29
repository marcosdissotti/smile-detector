package com.dissotti.smiledetector;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity  {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Boolean joy, sorrow;
    String detectionConfidence;

    Button btTakePhoto, btReturn;
    ImageView imgView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btTakePhoto = (Button)findViewById(R.id.btnTakePhoto);
        imgView = (ImageView) findViewById(R.id.imageView);

        btTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imgView = (ImageView)findViewById(R.id.imageView);

            Log.d("file ", "onActivityResult   == " + requestCode);

            try {
                saveBitmapToFile(imageBitmap, requestCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void saveBitmapToFile(final Bitmap bitmap, final int position) throws Exception {
        Log.d("file ", "saveBitmapToFile   == " + bitmap);

        final String fileName = position + "_" + System.currentTimeMillis() + ".jpeg";

        File file = new File(this.getCacheDir(), fileName);
        file.createNewFile();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapData);
        fos.flush();
        fos.close();
        imgView.setImageBitmap(bitmap);
        Log.d("file ", "file   == " + file);

        RequestBody requestBody = RequestBody.create(MediaType.parse("**/*//*"), file);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        IRetrofitWebservice getResponse = AppConfig.getRetrofit().create(IRetrofitWebservice.class);


        Call<ServerResponse> call = getResponse.uploadFile(fileToUpload, filename);

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                System.out.println("ENTROUUUU");
                ServerResponse serverResponse = response.body();

//                System.out.println(serverResponse.getDetectionConfidence());
//                detectionConfidence = serverResponse.getDetectionConfidence();
//                System.out.println("JOY");
//                joy = serverResponse.getJoy();
//                System.out.println(serverResponse.getJoy());
//                System.out.println("SORROW");
//                sorrow = serverResponse.getSorrow();
//                System.out.println(serverResponse.getSorrow());

                if((joy != null) || (sorrow != null) ){
                   if(joy == true) {
                        setContentView(R.layout.smile_screen);
                    }

                    if(sorrow == true){
                        setContentView(R.layout.sad_screen);
                    }

                    if(joy == false && sorrow == false){
                        setContentView(R.layout.not_detected_screen);

                        btReturn = (Button)findViewById(R.id.btnVoltar);

                        btReturn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setContentView(R.layout.activity_main);
                            }
                        });
                    }
                }



        }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }




}