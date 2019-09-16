package com.example.cameratest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {
    Button register;
    Button contrast;
    Button photo;
    ImageView imageView;

    String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photo = findViewById(R.id.photo);
        imageView = findViewById(R.id.image);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dView = getWindow().getDecorView();
                dView.setDrawingCacheEnabled(true);
                dView.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
                if (bitmap != null) {
                    try {
                        // 获取内置SD卡路径
                        String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                        // 图片文件路径
                        String filePath = sdCardPath + File.separator + "screenshot.png";
                        File file = new File(filePath);
                        FileOutputStream os = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                        imageView.setImageBitmap(bitmap);
                        os.flush();
                        os.close();
                    } catch (Exception e) {
                    }
                }
            }
        });


        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);

        register = findViewById(R.id.register_face);
        contrast = findViewById(R.id.contrast);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(intent, 1);*/
                Intent intent = new Intent(MainActivity.this,RegisterFaceActivity.class);
                startActivity(intent);
            }
        });

        contrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ContrastActivity.class);
                startActivity(intent);
            }
        });

    }
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            Toast.makeText(MainActivity.this, "权限通过，可以做其他事情!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void forbitPermissons() {
//            finish();
            Toast.makeText(MainActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }





}
