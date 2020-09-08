package ua.org.algoritm.terminal.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;

import ua.org.algoritm.terminal.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivityCamera extends AppCompatActivity {
    private Camera mCamera;
    private FrameLayout mFrameLayout;
    private ShowCamera mShowCamera;
    private String mCurrentPhotoPath;

    private ImageButton photo;
    private TextView description;

    private ImageButton switchFlashlightButton;
    private boolean isFlashLightOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_camera);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        mCurrentPhotoPath = intent.getStringExtra("mCurrentPhotoPath");

        mFrameLayout = findViewById(R.id.camera);
        description = findViewById(R.id.description);

        description.setText(intent.getStringExtra("description"));

//        mCamera = Camera.open();
//
//        mShowCamera = new ShowCamera(this, mCamera);
//        mFrameLayout.addView(mShowCamera);

        photo = findViewById(R.id.photo);
        photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null) {
                   mCamera.autoFocus(mAutoFocusCallback);
                }
            }
        });

        switchFlashlightButton = findViewById(R.id.switch_flashlight);
        switchFlashlightButton.setImageResource(R.drawable.ic_on_flash);

        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        } else {
            switchFlashlightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchFlashlight();
                }
            });
        }
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight() {
        Camera.Parameters parameters = mCamera.getParameters();

        if (isFlashLightOn) {
            isFlashLightOn = false;
            switchFlashlightButton.setImageResource(R.drawable.ic_on_flash);
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        } else {
            isFlashLightOn = true;
            switchFlashlightButton.setImageResource(R.drawable.ic_off_flash);
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

        }
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File picture = new File(mCurrentPhotoPath);
            if (picture == null){
                return;
            } else {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(picture);
                    fileOutputStream.write(data);
                    fileOutputStream.close();

                    setResult(RESULT_OK);
                    finish();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    };

    Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // если удалось сфокусироваться, делаем снимок
                camera.takePicture(null, null, mPictureCallback);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mCamera = Camera.open();

        mShowCamera = new ShowCamera(this, mCamera);
        mFrameLayout.addView(mShowCamera);
    }

    @Override
    protected void onPause() {
        super.onPause();

//        if(mCamera != null) {
//            mCamera.stopPreview();
//            mCamera.setPreviewCallback(null);
//            mCamera.release();
//            mCamera = null;
//        }
    }
}


//import android.widget.ImageView;
//        import android.widget.TextView;
//
//        import ua.org.algoritm.terminal.cameraLibrary.CustomCamera;
//        import ua.org.algoritm.terminal.cameraLibrary.OnPictureTaken;


//public class MainActivityCamera extends AppCompatActivity implements OnClickListener, OnPictureTaken {
//
//    private Button btnCustomCamera, btnDefaultCamera,btnFrontCamera;
//    private TextView txtPath;
//    private CustomCamera mCustomCamera;
//    private ImageView imgCapture;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_camera);
//        mCustomCamera = new CustomCamera(MainActivityCamera.this);
//        mCustomCamera.setPictureTakenListner(this);
//
//        btnCustomCamera = (Button) findViewById(R.id.btnCustomCamera);
//        btnCustomCamera.setOnClickListener(this);
//        btnDefaultCamera = (Button) findViewById(R.id.btnDefaultCamera);
//        btnDefaultCamera.setOnClickListener(this);
//        btnFrontCamera = (Button) findViewById(R.id.btnFrontCamera);
//        btnFrontCamera.setOnClickListener(this);
//
//        imgCapture = (ImageView) findViewById(R.id.imgPic);
//        txtPath = (TextView) findViewById(R.id.txtPath);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnCustomCamera:
//                mCustomCamera.startCamera();
//                break;
//
//            case R.id.btnDefaultCamera:
//                mCustomCamera.sendCameraIntent();
//                break;
//
//            case R.id.btnFrontCamera:
//                mCustomCamera.startCameraFront();
//                break;
//        }
//    }
//
//    @Override
//    public void pictureTaken(Bitmap bitmap, File file) {
//        imgCapture.setImageBitmap(bitmap);
//        txtPath.setText(file.getAbsolutePath());
//    }
//
//}