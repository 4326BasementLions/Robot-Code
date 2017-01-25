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

    DcMotor leftBack;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor rightBack;

    DcMotor scooper;
    DcMotor shooter1;
    DcMotor shooter2;
    ColorSensor colorSensor;

    Servo holder;
    int pos = 1;

    @Override
    public void loop() {
        telemetry.addData("Text:", "Time: " + runtime.seconds());

        wait(10); //10th of a sec

//        shoot();
//        moveToBeacon();
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

    @Override
    public void init() {
    }

    public void driveForward(int pos1, int pos2, int pos3, int pos4, double newTime) {
        while(newTime == 0) { //in loops like do whiles
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
    public void sense(){
        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F,0F,0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);


        // bLedOn represents the state of the LED.
        boolean bLedOn = true;

        // turn the LED on in the beginning, just so user will know that the sensor is active.
        colorSensor.enableLed(bLedOn);

        colorSensor = hardwareMap.colorSensor.get("sensor_color");

        // convert the RGB values to HSV values.
        Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);

        // send the info back to driver station using telemetry function.
        telemetry.addData("LED", bLedOn ? "On" : "Off");
        telemetry.addData("Clear", colorSensor.alpha());
        telemetry.addData("Red ", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue ", colorSensor.blue());
        telemetry.addData("Hue", hsvValues[0]);
        // telemetry.addData("Is Red: ", (colorSensor.red()>200) && (colorSensor.blue()<100));
        // telemetry.addData("Is Blue: ", (colorSensor.blue()>200) && (colorSensor.red()<100));

        // change the background color to match the color detected by the RGB sensor.
        // pass a reference to the hue, saturation, and value array as an argument
        // to the HSVToColor method.
        //pass HSV values to Driver Station phones, thereby changing phone backgrounds to RGB color values
    }

    double senseDuration = 15;
    public void checkColor(String allianceSide) {
        sense();
        if (allianceSide == "right"|| allianceSide == "Right") { //assiming we're on the blue alliance
            do {
                driveForward(-10, 10, 10, -10, 0);
            }while (colorSensor.red() < 100 && colorSensor.blue() < 100); //while no blue/red color

            if(colorSensor.blue() >= 200 && colorSensor.red() < 100) { //if blue
                driveForward(-10, 10, 10, -10, senseDuration);
            }

            else if(colorSensor.red() >= 200 && colorSensor.blue() < 100) { //if red
                driveForward(-10, 10, 10, -10, (senseDuration * 1.8)); //drive slightly longer
            }
        }

        if (allianceSide == "left" || allianceSide == "Left") { //assuming we're on the red alliance
            do {
                driveForward(10, -10, -10, 10, 0);
            }while (colorSensor.red() < 100 && colorSensor.blue() < 100); //while no blue/red color

            if(colorSensor.blue() >= 200 && colorSensor.red() < 100) { //if blue
                driveForward(10, -10, -10, 10, senseDuration * 1.8); //drive slightly longer
            }

            else if(colorSensor.red() >= 200 && colorSensor.blue() < 100) { //if red
                driveForward(10, -10, -10, 10, (senseDuration));
            }
        }
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

        colorSensor = hardwareMap.colorSensor.get("colorSensor");

        //button = hardwareMap.servo.get("button");
        //button.scaleRange(0, 1);

        //button.setPosition(1);


    }
}