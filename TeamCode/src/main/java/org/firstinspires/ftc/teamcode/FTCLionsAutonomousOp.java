//package com.qualcomm.ftcrobotcontroller.opmodes;
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;


import com.qualcomm.robotcore.util.ElapsedTime;

import android.graphics.Color;
import com.qualcomm.robotcore.hardware.ColorSensor;
import android.app.Activity;
import android.view.View;

@Autonomous(name="Autonomous", group="Autonomous")  //AUTONOMOUS!

public class FTCLionsAutonomousOp extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();

    final boolean DEBUG = true;
    DcMotor leftBack;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor rightBack;

    DcMotor scooper;
    DcMotor shooter1;
    DcMotor shooter2;
    ColorSensor colorSensor;

    Servo holder;
    Servo sensorExtend;
    int pos = 1;

    @Override
    public void loop() {
        sense();
        if (DEBUG) {
            telemetry.addData("Text:", "Time: " + runtime.seconds());
            telemetry.addData("Text:", "isColor Data: " + isColor(colorSensor, 5));
        }
        wait(10); //10th of a sec


//        shoot();
//        moveToBeacon("left");
        checkColor("left");

//        stopRobot();

    }

    public void moveToBeacon(String allianceSide) {
        if (allianceSide == "left") {
            driveForward(0, 0, 8, 8, 15);
            driveForward(10, 10, 10, 10, 45);
            driveForward(0, 0, 8, 8, 15);
        }
        else if (allianceSide == "right") {
            driveForward(8, 8, 0, 0, 15);
            driveForward(10, 10, 10, 10, 45);
            driveForward(8, 8, 0, 0, 15);
        }
    }
    public void moveToBeacon2(String allianceSide) {
        if (allianceSide == "left") {
            driveForward(0, -8, 0, -8, 15);
            driveForward(-1, 1, -1, 1); //fix!!!!!
            driveForward(10, 10, 10, 10, 45);
            driveForward(0, 0, 8, 8, 15);
        }
        else if (allianceSide == "right") {
            driveForward(0, -8, 0, -8, 15);
            driveForward(10, 10, 10, 10, 45);
            driveForward(8, 8, 0, 0, 15);
        }
    }

    @Override
    public void init() {
       // sensorExtend.setPosition(0.0);
    }

    public void driveForward(int pos1, int pos2, int pos3, int pos4, double newTime) {
        if(newTime == 0) { //in loops like do whiles
            leftFront.setPower(pos1 / 10);
            leftBack.setPower(pos2 / 10);
            rightFront.setPower(pos3 / 10);
            rightBack.setPower(pos4 / 10);
        }

        while (newTime > 0 && runtime.milliseconds() <= (runtime.milliseconds() + (newTime * 100))) { //1s = 1000ms
            leftFront.setPower(pos1 / 10);
            leftBack.setPower(pos2 / 10);
            rightFront.setPower(pos3 / 10);
            rightBack.setPower(pos4 / 10);
            wait(5);

            runtime.reset();
        }
    }

    public void shoot() {
        while (runtime.seconds() <= 6) {
            shooter1.setPower(pos / 1.4);
            shooter2.setPower(pos / 1.4);
            scooper.setPower(pos);
        }
        runtime.reset();
    }

    public void stopRobot() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        scooper.setPower(0);
        shooter1.setPower(0);
        shooter2.setPower(0);
        wait(1);
    }


    public void wait(int time) {
        try {
            Thread.sleep(time * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //SENSOR CODE
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
    public String isColor(ColorSensor colorSensor, final int colorDiff) { //outputs which color {red, blue, none} is shown by sensor
        String whichColor = "";
        if((colorSensor.red() - colorDiff) > colorSensor.blue() && (colorSensor.red() - colorDiff) > colorSensor.green()) {
            whichColor = "red";
        }
        else if((colorSensor.blue() - colorDiff) > colorSensor.red() && (colorSensor.blue() - colorDiff) > colorSensor.green()) {
            whichColor = "blue";
        }
        else {
            whichColor = "none";
        }
        return whichColor;
    }

    double senseDuration = 15;
    public void checkColor(String allianceSide) {
        sense();
        sensorExtend.setPosition(1);
        if (allianceSide == "right"|| allianceSide == "Right") { //assuming we're on the blue alliance
            do {
                driveForward(-10, 10, 10, -10, 0);
                if(isColor(colorSensor,5) != "none") //saftey to prevent infinite loops
                    break;
            }while (isColor(colorSensor,5) == "none"); //while no blue/red color

            if(isColor(colorSensor,5) == "blue") { //if blue
                driveForward(-10, 10, 10, -10, senseDuration);
            }

            else if(isColor(colorSensor,5) == "red") { //if red
                driveForward(-10, 10, 10, -10, (senseDuration * 1.8)); //drive slightly longer
            }
        }

        if (allianceSide == "left" || allianceSide == "Left") { //assuming we're on the red alliance
            do {
                driveForward(10, -10, -10, 10, 0);
                if(isColor(colorSensor,5) != "none") //saftey to prevent infinite loops
                    break;
            }while (isColor(colorSensor,5) == "none"); //while no blue/red color

            if(isColor(colorSensor,5) == "blue") { //if blue
                driveForward(10, -10, -10, 10, senseDuration * 1.9); //drive slightly longer
            }

            else if(isColor(colorSensor,5) == "red") { //if red
                driveForward(-10, 10, 10, -10, (senseDuration * 1.4));
            }
        }
        sensorExtend.setPosition(0); //retract
        driveForward(8, 8, 8, 9, 20); //medium power drive forward for 2 seconds
    }


    @Override
    public void start() {
        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

        scooper = hardwareMap.dcMotor.get("scooper");
        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2");
        shooter2.setDirection(DcMotor.Direction.REVERSE);

        scooper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        shooter1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        shooter2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);

        holder = hardwareMap.servo.get("holder");
        holder.scaleRange(0, 1);

        sensorExtend = hardwareMap.servo.get("sensorExtend");
        sensorExtend.scaleRange(0, 1);

        colorSensor = hardwareMap.colorSensor.get("colorSensor");

        //button = hardwareMap.servo.get("button");
        //button.scaleRange(0, 1);

        //button.setPosition(1);


    }
}