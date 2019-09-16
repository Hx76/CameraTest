package com.example.cameratest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.FaceDetector;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ContrastActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceViewCamera2 mCameraView;
    private Camera2Proxy mCameraProxy;
    private final int MAX_FACES = 5;    //最大可识别的人脸数
    private String mFilePaths;
    private String mImageName;
    private String mImagePath;
    private ImageReader mImageReader;
    private Button skip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrast);
        mCameraView = findViewById(R.id.surface);
        skip = findViewById(R.id.skip);
        mCameraProxy = mCameraView.getCameraProxy();

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraProxy.setImageAvailableListener(mOnImageAvailableListener);
                mCameraProxy.captureStillPicture(); // 拍照
            }
        });

        /*new Thread(new Runnable() {
            @Override
           public void run() {
                FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(mFilePaths);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //格式必须为RGB_565才可以识别
                Bitmap bmp = bitmap.copy(Bitmap.Config.RGB_565, true);
                //返回识别的人脸数
                int faceCount = new FaceDetector(bmp.getWidth(), bmp.getHeight(), MAX_FACES).findFaces(bmp, faces);
                bmp.recycle();
                bmp = null;
                Log.e("tag", "识别的人脸数:" + faceCount);

                switch (faceCount) {
                    case 1:

                        Toast.makeText(ContrastActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(ContrastActivity.this,"请将面部对准摄像头",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(ContrastActivity.this,"请勿多人同时注册",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        }).start();*/
    }

    private ImageReader.OnImageAvailableListener mOnImageAvailableListener =
            new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    new ImageSaveTask().execute(reader.acquireNextImage()); // 保存图片
                }
            };

    private class ImageSaveTask extends AsyncTask<Image, Void, Void> {

        @Override
        protected Void doInBackground(Image ... images) {
            ByteBuffer buffer = images[0].getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);

            long time = System.currentTimeMillis();
            if (mCameraProxy.isFrontCamera()) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
              //  Log.d(TAG, "BitmapFactory.decodeByteArray time: " + (System.currentTimeMillis() - time));
                time = System.currentTimeMillis();
                // 前置摄像头需要左右镜像
                Bitmap rotateBitmap = ImageUtils.rotateBitmap(bitmap, 0, true, true);
               // Log.d(TAG, "rotateBitmap time: " + (System.currentTimeMillis() - time));
                time = System.currentTimeMillis();
                ImageUtils.saveBitmap(rotateBitmap);
             //   Log.d(TAG, "saveBitmap time: " + (System.currentTimeMillis() - time));
                rotateBitmap.recycle();
            } else {
                ImageUtils.saveImage(bytes);
             //   Log.d(TAG, "saveBitmap time: " + (System.currentTimeMillis() - time));
            }
            images[0].close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }







    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
