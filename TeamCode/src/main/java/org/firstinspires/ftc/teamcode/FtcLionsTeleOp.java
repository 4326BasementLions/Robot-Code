//package com.qualcomm.ftcrobotcontroller.opmodes;
package org.firstinspires.ftc.teamcode;

import android.bluetooth.BluetoothClass;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

@TeleOp(name="TeleOp", group="TeleOp")  //TELEOP!

public class FtcLionsTeleOp extends OpMode {
    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */

    /*
    Controls

    Controller 1 → Drive

Joystick1_x	Mecanum drive(x)
Joystick1_y	Mecanum drive(y)
Joystick2_x	Mecanum drive(a)
Joystick2_y
D-pad up
D-pad down
D-pad left
D-pad right
A
B
X	Releases Claw
Y 	Engages Servo
Bumper L
Bumper R
Trigger L
Trigger R
    */


    final boolean DEBUG = true;

    DcMotor leftBack;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor rightBack;

    double lowThreshold = .02;
    double highThreshold = .75;
    double lowThreshold2 = .036;
    double highThreshold2 = .8;
    float div = 1;

    ColorSensor colorSensor;

    public FtcLionsTeleOp() {
    }

    @Override
    public void start() {
        boolean  started = true;

        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        colorSensor = hardwareMap.colorSensor.get("colorSensor");
    }

    @Override
    public void init() {
//        holder.setPosition(1.0);
    }

    public void wait(int time){
        try{
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public float jHigh(float in) //threshold checking function --> used for most motors
    {
        float absin = Math.abs(in);
        if( absin < lowThreshold )
            return (float) 0.;
        else if( absin < highThreshold )
            return (float) (in / 2.);
        return in;
    }
    public float jLow(float in) //threshold checking function --> used for most motors
    {
        float absin = Math.abs(in);
        if( absin < lowThreshold2 )
            return (float) 0.;
        else if( absin < highThreshold2 )
            return (float) (in / 2.);
        return in;
    }
    @Override
    public void loop() {
        sense();
        if (DEBUG) {
            // TELEMETRY FOR JOYSTICK DEBUGGING
            telemetry.addData("Text:", "Gamepad1 Movement 1: " + gamepad1.right_stick_y + ", " + gamepad1.right_stick_y);
            telemetry.addData("Text:", "Gamepad1 Movement 2: " + gamepad1.left_stick_x);

            telemetry.addData("Text:", "isColor Data: " + isColor(colorSensor));
            telemetry.addData("Text:", "RGB Colors: " + colorSensor.red() + ", " + colorSensor.green() + ", " + colorSensor.blue());
        }

        ////////////////////////////////
        //     GAMEPAD 1 CONTROLS     //
        ////////////////////////////////

        //right front motor and left back motors are backwards
        float rf = (jLow(gamepad1.right_stick_y) + jLow(gamepad1.right_stick_x) - jLow(gamepad1.left_stick_x));  //right-stick-y = forward/backward
        float lf = (jLow(-gamepad1.right_stick_y) + jLow(gamepad1.right_stick_x) - jLow(gamepad1.left_stick_x));  //right-stick-x = left/right
        float rb = (jLow(-gamepad1.right_stick_y) + jLow(gamepad1.right_stick_x) + jLow(gamepad1.left_stick_x));  //left-stick-x = turning
        float lb = (jLow(gamepad1.right_stick_y) + jLow(gamepad1.right_stick_x) + jLow(gamepad1.left_stick_x));

//        To fix diagonals, we need to change the power of certain motors, but only during the time when we are going diagonal in a specific direction
        rightFront.setPower(rf);
        leftFront.setPower(lf);
        rightBack.setPower(rb);
        leftBack.setPower(lb);
//        telemetry.addData("Text:", "Variable Motors: " + rf + ", " + lf + ", " + rb + ", " + lb);


        ////////////////////////////////
        //     GAMEPAD 2 CONTROLS     //
        ////////////////////////////////


        /// E-STOP \\\
        if (gamepad1.left_bumper && gamepad1.right_bumper || gamepad1.right_stick_button && gamepad1.left_stick_button ||
                gamepad2.left_bumper && gamepad2.right_bumper || gamepad2.right_stick_button && gamepad2.left_stick_button) { //mash those bumpers & stick buttons
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
        }
    }

    public void sense() {
        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F,0F,0F};
        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;
        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);
        // bPrevState and bCurrState represent the previous and current state of the button.
        boolean bPrevState = false;
        boolean bCurrState = false;
        // bLedOn represents the state of the LED.
        boolean bLedOn = true;
        // get a reference to our ColorSensor object.
        //colorSensor = hardwareMap.colorSensor.get("sensor_color");   //--> already declared
        // Set the LED in the beginning
        colorSensor.enableLed(bLedOn);
        // wait for the start button to be pressed.
//        waitForStart(); //--> unneeded in loop

        // while the op mode is active, loop and read the RGB data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
//        while (opModeIsActive()) {

        // check the status of the x button on either gamepad.
        bCurrState = gamepad1.x;
        // check for button state transitions.
        if ((bCurrState == true) && (bCurrState != bPrevState))  {
            // button is transitioning to a pressed state. So Toggle LED
            bLedOn = !bLedOn;
            colorSensor.enableLed(bLedOn);
        }
        // update previous state variable.
        bPrevState = bCurrState;
        // convert the RGB values to HSV values.
        Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);
        // send the info back to driver station using telemetry function.
//        telemetry.addData("LED", bLedOn ? "On" : "Off");
//        telemetry.addData("Clear", colorSensor.alpha());
//        telemetry.addData("Red  ", colorSensor.red());
//        telemetry.addData("Green", colorSensor.green());
//        telemetry.addData("Blue ", colorSensor.blue());
//        telemetry.addData("Hue", hsvValues[0]);

        // change the background color to match the color detected by the RGB sensor.
        // pass a reference to the hue, saturation, and value array as an argument
        // to the HSVToColor method.
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
            }
        });
        telemetry.update();
//        }
    }
    public String isColor(ColorSensor colorSensor) { //outputs which color {red, blue, none} is shown by sensor
        String whichColor = "";
        if(colorSensor.red() > 20 && (colorSensor.blue() - colorSensor.red()) >= 5) {
            whichColor = "red";
        }
        if((colorSensor.blue() - colorSensor.red()) >= 9) { //6 replaces color diff
            whichColor = "blue";
        }
        if((colorSensor.blue()  < 5 && colorSensor.red() < 4) || (whichColor != "red" && whichColor != "blue")) {
            whichColor = "none";
        }
        return whichColor;
    }

    @Override
    public void stop() {
    }
}