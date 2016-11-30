package main.java.org.firstinspires.ftc.robotcontroller.internal;
//package com.qualcomm.ftcrobotcontroller;

import android.context.Context
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.camera;

/**
 * Created by Mather2 on 11/27/2016.
 */

public class CameraPreview //extends SurfaceView implements SurfaceHolder.Callback
{

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private static String TAG = "DEBUG";
    private Camera.PreviewCallback previewCallback;

    public CameraPreview(Context context, Camera camera, Camera.PreviewCallback previewCallback) {
        super(context);
        mCamera = camera;

        this.previewCallback = previewCallback;

        // The comment said to 'Install a SurfaceHolder.Callback so we get notified when the underlying surface is created and destroyed.'
        mHolder = getHolder();
        mHolder.addCallback(this);
        // line below perhaps uneeded for our version
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The comment said 'The Surface has been created, now tell the camera where to draw the preview'
        try {
          mCamera.setPreviewCallback(previewCallback);
          mCamera.setPreviewDisplay(holder);
          mCamera.StartPreview();
        } catch (IOException e) {
          Log.d(TAG, "Error setting camera preview : " + e.getMessage());
        }
      }
     public void surfaceDestroyed(SurfaceHolder holder) {
         // The comment said 'empty. Take care of releasing the Camera preview in your activity.'
     }

     public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
         // The comment said 'If your preview can change or rotate, take care of those events here.'
         // and 'Make sure to stop the preview before resizing or reformatting it.'

         if (mHolder.getSurface() == null){
         // This is for if the preview surface does not exist
         return;
         }

         //Stops preview before making changes
         try{
            mCamera.stopPreview();
          } catch (Exception e){
                // Comment said 'ignore: tried to stop a non-existent preview'
          }

            //This space is for our own preview size and format changes


         // For starting the preview with new settings
         try {
             mCamera.setPreviewCallback(previewCallback);
             mCamera.setPreviewDisplay(mHolder);
             mCamera.startPreview();

         }  catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
         }


}
