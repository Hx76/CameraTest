package com.example.cameratest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RegisterFaceActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 1;
    private Uri imageuri;
    private Button takephoto;
    private ImageView picture;
    private String mFilePaths;
    private final int MAX_FACES = 5;    //最大可识别的人脸数
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_face);

        mFilePaths = Environment.getExternalStorageDirectory().getPath();
        mFilePaths = mFilePaths + "/" + "face.jpg";
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        takephoto = findViewById(R.id.take_photo);
        picture = findViewById(R.id.picture);

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri = Uri.fromFile(new File(mFilePaths));
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(intent, 1);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            FileInputStream fis = null;
            if (requestCode == 1) {
                try {
                    fis = new FileInputStream(mFilePaths);
                    final Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    picture.setImageBitmap(bitmap);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
                            //格式必须为RGB_565才可以识别
                            Bitmap bmp = bitmap.copy(Bitmap.Config.RGB_565, true);
                            //返回识别的人脸数
                            int faceCount = new FaceDetector(bmp.getWidth(), bmp.getHeight(), MAX_FACES).findFaces(bmp, faces);
                            bmp.recycle();
                            bmp = null;
                            num = faceCount;
                            Log.e("tag", "识别的人脸数:" + faceCount);


                            /*switch (faceCount) {
                                case 1:
                                    Toast.makeText(RegisterFaceActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    break;
                                case 0:
                                    Toast.makeText(RegisterFaceActivity.this,"请将面部对准摄像头",Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(RegisterFaceActivity.this,"请勿多人同时注册",Toast.LENGTH_SHORT).show();
                                    break;
                            }*/

                        }
                    }).start();
                    Toast.makeText(RegisterFaceActivity.this,"人脸数"+num,Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
