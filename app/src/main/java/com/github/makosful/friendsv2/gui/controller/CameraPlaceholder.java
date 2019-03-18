package com.github.makosful.friendsv2.gui.controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.gui.controller.FriendDetail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static com.github.makosful.friendsv2.Common.CAMERA_REQUEST_CODE;

public class CameraPlaceholder extends AppCompatActivity
{
    private static final String TAG = "";

    private CameraDevice cameraDevice;
    private CameraManager cameraManager;
    private int cameraFacing;
    private TextureView.SurfaceTextureListener surfaceTextureListener;
    private String cameraId;
    private Size previewSize;
    private TextureView textureView;
    private CameraCaptureSession cameraCaptureSession;
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;

    public CameraPlaceholder()
    {
        //textureView = findViewById(R.id.textureView);
        setup();
    }

    private void setup() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        cameraFacing = CameraCharacteristics.LENS_FACING_BACK;

        surfaceTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                setUpCamera();
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        };
    }

    private void setUpCamera() {
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics =
                        cameraManager.getCameraCharacteristics(cameraId);
                Log.d(TAG, "Looking for back camera, current cameraId=" + cameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) ==
                    cameraFacing) {
                    StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(
                            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    previewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
                    this.cameraId = cameraId;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

                cameraManager.openCamera(cameraId, stateCallback, backgroundHandler);
                Log.d(TAG, "Camera with id= " + cameraId + " open.");

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void takePicture(View view)
    {
        FileOutputStream outputPhoto = null;
        try {
            File f = getOutputMediaFile();
            outputPhoto = new FileOutputStream(f);
            textureView.getBitmap()
                       .compress(Bitmap.CompressFormat.PNG, 100, outputPhoto);
            Log.d(TAG, "Photo taken - size: " + f.length() );
            Log.d(TAG, "Location: " + f.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputPhoto != null) {
                    outputPhoto.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice cameraDevice) {
            CameraPlaceholder.this.cameraDevice = cameraDevice;
            createPreviewSession();
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            cameraDevice.close();
            CameraPlaceholder.this.cameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            cameraDevice.close();
            CameraPlaceholder.this.cameraDevice = null;
        }
    };

    private void openBackgroundThread() {
        backgroundThread = new HandlerThread("camera_background_thread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    @Override
    protected void onResume() {
        super.onResume();
        openBackgroundThread();
        if (textureView.isAvailable()) {
            setUpCamera();
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeCamera();
        closeBackgroundThread();
    }

    private void closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
            backgroundHandler = null;
        }
    }

    private void createPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
            final CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(
                    CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);

            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                                              new CameraCaptureSession.StateCallback() {

                                                  @Override
                                                  public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                                                      if (cameraDevice == null) {
                                                          return;
                                                      }

                                                      try {
                                                          CaptureRequest captureRequest = captureRequestBuilder.build();
                                                          CameraPlaceholder.this.cameraCaptureSession = cameraCaptureSession;
                                                          CameraPlaceholder.this.cameraCaptureSession.setRepeatingRequest(captureRequest,
                                                                                                                     null, backgroundHandler);
                                                      } catch (CameraAccessException e) {
                                                          e.printStackTrace();
                                                      }
                                                  }

                                                  @Override
                                                  public void onConfigureFailed(
                                                          CameraCaptureSession cameraCaptureSession) {

                                                  }
                                              }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private File getOutputMediaFile(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_name));

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String postfix = "jpg";
            String prefix = "IMG";

            File mediaFile = new File(mediaStorageDir.getPath() +
                                      File.separator + prefix +
                                      "_" + timeStamp + "." + postfix);

            return mediaFile;
        }
        Log.d(TAG, "Permission for writing NOT granted");
        return null;
    }
}
