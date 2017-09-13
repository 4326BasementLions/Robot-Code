
//Comments marked with 'L' here are for camera sensor code, but it may need to be in OpMode.java instead
/**
 * Created by horacemann on 11/9/16.
 */
package org.firstinspires.ftc.teamcode;

import com.google.blocks.ftcrobotcontroller.runtime.BlocksOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.eventloop.opmode.AnnotatedOpModeRegistrar;
import org.firstinspires.ftc.robotcontroller.external.samples.ConceptNullOp;

/*
'L'
package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.CameraPreview;
import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
 */
public class FtcOpModeRegister implements OpModeRegister {
    public void register(OpModeManager manager) {

        /**
         * Register OpModes implemented in the Blocks visual programming language.
         */
        BlocksOpMode.registerAll(manager);


        /**
         * Register OpModes that use the annotation-based registration mechanism.
         */
        AnnotatedOpModeRegistrar.register(manager);

        /**
         * Any manual OpMode class registrations should go here.
         */
        manager.register("TeleOp", FtcLionsTeleOp.class);
        manager.register("AutomonousTest", FTCLionsAutonomousOp.class);


    }


}
/*
'L'
public class CameraOp extends OpMode {
    private Camera camera;
    public CameraPreview preview;
    public Bitmap image;
    private int width;
    private int height;
    private YuvImage yuvImage = null;
    private int looped = 0;
    private String data;

    private int red(int pixel) {
        return (pixel >> 16) & 0xff;
    }
    private int green(int pixel) {
        return (pixel >> 8) & 0xff;
    }
    private int blue(int pixel) {
        return pixel & 0xff;
    }

    private Camera.PreviewCallback previewCallback = newCamera.PreviewCallBack()
        public void onPreviewwFrame(byte[] data, Camera camera)
        {
            Camera.Parameters parameters = camera.getParameters();
            width = parameters.getPreviewSize().width;
            height = parameters.getPreviewSize().height;
            yuvImage = newYuvImage(data, ImageFormat.NV21, width, height, null);
            looped += 1;
            }
        };

    private void convertImage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 0, out);
        byte[] imageBytes = out.toByteArray();
        image = BitmapFactory.decodeByteArray(imagebytes, 0, imageBytes.length);

    }

 */

/*
This stays a comment
    code to run when the op mode is first enabled goes here
    @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
 */
/*
'L'
    @Override
    public void init() {
        camera = ((FtcRobotControllerActivity)hardwareMap.appContext).camera;
        camera.setPreviewCallback(previewCallback);

        Camera.Parameters parameters = camera.getParameters();
        data = parameters.flatten();

        ((FtcRobotControllerActivity) hardwareMap.appContext).initPreview(camera, this, previewCallback);

    }
 */
/*
To be continued from here
 */