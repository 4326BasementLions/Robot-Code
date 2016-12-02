//package com.qualcomm.ftcrobotcontroller.opmodes;

import android.bluetooth.BluetoothClass;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

@TeleOp(name="TeleOp", group="TeleOp")  //TELEOP!

public class FTCLionsTeleOp extends OpMode {

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

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera)
        {
            Camera.Parameters parameters = camera.getParameters();
            width = parameters.getPreviewSize().width;
            height = parameters.getPreviewSize().height;
            yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
            looped += 1;
        }
    };

    private void convertImage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 0, out);
        byte[] imageBytes = out.toByteArray();
        image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */

    final boolean DEBUG = true;

    DcMotor leftBack;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor rightBack;

    public FTCLionsTeleOp() {

    }

    @Override
    public void start() {
        boolean  started = true;

        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void init() {
        camera = ((FtcRobotControllerActivity)hardwareMap.appContext).camera;
        camera.setPreviewCallback(previewCallback);

        Camera.Parameters parameters = camera.getParameters();
        data = parameters.flatten();

        ((FtcRobotControllerActivity) hardwareMap.appContext).initPreview(camera, this, previewCallback);
    }

    public int highestColor(int red, int green, int blue) {
        int[] color = {red,green,blue};
        int value = 0;
        for (int i = 1; i < 3; i++) {
            if (color[value] < color[i]) {
                value = i;
            }
        }
        return value;
    }

    @Override
    public void loop() {
        if (DEBUG) {
            // TELEMETRY FOR JOYSTICK DEBUGGING
              telemetry.addData("Text:", "Gamepad1 Movement y: " + gamepad1.left_stick_y + ", " + gamepad1.right_stick_y);
            telemetry.addData("Text:", "Gamepad1 Movement x: " + gamepad1.left_trigger + ", " + gamepad1.right_trigger);
        }

        if (yuvImage != null) {  //color sensor output and value swaps
            int redValue = 0;
            int blueValue = 0;
            int greenValue = 0;
            convertImage();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = image.getPixel(x, y);
                    redValue += red(pixel);
                    blueValue += blue(pixel);
                    greenValue += green(pixel);
                }
            }
            int color = highestColor(redValue, greenValue, blueValue);
            String colorString = "";
            switch (color) {
                case 0:
                    colorString = "RED";
                    break;
                case 1:
                    colorString = "GREEN";
                    break;
                case 2:
                    colorString = "BLUE";
            }
            telemetry.addData("Color:", "Color detected is: " + colorString);
        }
        telemetry.addData("Looped","Looped " + Integer.toString(looped) + " times");
        Log.d("DEBUG:",data);

        ////////////////////////////////
        //     GAMEPAD 1 CONTROLS     //
        ////////////////////////////////


        // TANK DRIVE \\
        leftFront.setPower(-gamepad1.left_stick_y / 1.4); // /1.4 for general power issues considering the robot is somewhat tipsy
        rightFront.setPower(gamepad1.right_stick_y / 1.4);
        leftBack.setPower(-gamepad1.left_stick_y / 1.4);
        rightBack.setPower(gamepad1.right_stick_y / 1.4);

        while (gamepad1.right_trigger != 0) { //right side of robot
            rightFront.setPower(-gamepad1.right_trigger / (2 + .7)); //assuming that the right side is backwards
            rightBack.setPower(gamepad1.right_trigger / (2 + .7));
            leftFront.setPower(gamepad1.right_trigger / (2 + 0));
            leftBack.setPower(-gamepad1.right_trigger / (2 + 0));

        }
        while (gamepad1.left_trigger != 0) { //left side of the robot
            rightFront.setPower(gamepad1.left_trigger / (2 + 0)); //assuming that the right side is backwards
            rightBack.setPower(-gamepad1.left_trigger / (2 + 0));
            leftFront.setPower(-gamepad1.left_trigger / (2 + .7));
            leftBack.setPower(gamepad1.left_trigger / (2 + .7)); //.7 for the general power per side and .8 for the wheels moving forward
        }


        ////////////////////////////////
        //     GAMEPAD 2 CONTROLS     //
        ////////////////////////////////




        // E-STOP \\
        if (gamepad1.left_bumper && gamepad1.right_bumper && gamepad2.left_bumper && gamepad2.right_bumper) { //mash those bumpers
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
        }
    }


    @Override
    public void stop() {

    }
}
