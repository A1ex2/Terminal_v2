package ua.org.algoritm.terminal.Activity;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

class ShowCamera extends SurfaceView implements SurfaceHolder.Callback{
    Camera mCamera;
    SurfaceHolder mHolder;

    public ShowCamera(Context context, Camera camera) {
        super(context);
        this.mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Camera.Parameters parameters = mCamera.getParameters();

        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        parameters.setPictureSize(sizes.get(0).width, sizes.get(0).height); // mac dinh solution 0

        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            parameters.set("orientation", "portrait");
            mCamera.setDisplayOrientation(90);
            parameters.setRotation(90);
        } else {
            parameters.set("orientation", "landscape");
            mCamera.setDisplayOrientation(0);
            parameters.setRotation(0);
        }

        List<Camera.Size> size = parameters.getSupportedPreviewSizes();
        parameters.setPreviewSize(size.get(0).width, size.get(0).height);

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }
}
