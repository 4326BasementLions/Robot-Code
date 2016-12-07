 package com.qualcomm.ftcrobotcontroller.opmodes;


import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
* Created by Alba_101 on 12/2/16.
*/
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColorSensorCode_Autonomous extends OpMode {


   DcMotor leftBack;
   DcMotor leftFront;
   DcMotor rightFront;
   DcMotor rightBack;
   ColorSensor colorSensor;


   public void init(){
       leftFront = hardwareMap.dcMotor.get("leftFront");
       leftBack = hardwareMap.dcMotor.get("leftBack");
       rightFront = hardwareMap.dcMotor.get("rightFront");
       rightBack = hardwareMap.dcMotor.get("rightBack");
       colorSensor = hardwareMap.colorSensor.get("colorSensor");
   }

   // Drive Methods //


   public void tankDrive (double power){
       leftFront.setPower(-power);
       rightFront.setPower(power);
       leftBack.setPower(-power);
       rightBack.setPower(power);
   }

   public void rightShuffle(double power){
       leftFront.setPower(power);
       leftBack.setPower(-power);
       rightFront.setPower(-power);
       rightBack.setPower(power);
   }
   public void leftShuffle(double power){
       leftFront.setPower(-power);
       leftBack.setPower(power);
       rightFront.setPower(power);
       rightBack.setPower(-power);
   }

   public void driveForward(int pos1, int pos2, int pos3, int pos4){
       leftFront.setTargetPosition(pos1);
       leftBack.setTargetPosition(pos2);
       rightFront.setTargetPosition(pos3);
       rightBack.setTargetPosition(pos4);
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

    ;   // turn the LED on in the beginning, just so user will know that the sensor is active.
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
       telemetry.addData("Is Red: ", (colorSensor.red()>200) && (colorSensor.blue()<100));
       telemetry.addData("Is Blue: ", (colorSensor.blue()>200) && (colorSensor.red()<100));

       // change the background color to match the color detected by the RGB sensor.
       // pass a reference to the hue, saturation, and value array as an argument
       // to the HSVToColor method.


       //pass HSV values to Driver Station phones, thereby changing phone backgrounds to RGB color values

   }


   public void wait(int time){
       try{
           Thread.sleep(time * 1000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }

   public void stopDaBot(){
       leftBack.setPower(0);
       leftFront.setPower(0);
       rightBack.setPower(0);
       rightFront.setPower(0);
       wait(1);
   }


    public void checkColor(String direction, double power) {
        if (direction = "left" || direction = "Left") { //assiming we're on the red alliance
            do {
                leftShuffle(power);
            }while (colorSensor.red() < 100 && colorSensor.blue() < 100)

            if(colorSensor.blue() >= 200 && colorSensor.red() < 100) {
                do {
                    leftShuffle(power);
                }while (colorSensor.blue() < 100 && colorSensor.red() >= 200)

                if(colorSensor.red() >= 200 && colorSensor.blue() < 100) {
                    //push button
                }
            }
            if(colorSensor.red() >= 200 && colorSensor.blue() < 100) {
                //push button
            }
        }

        if (direction = "right" || direction = "Right") { //assuming we're on the blue alliance
            do {
                rightShuffle(power);
            }while (colorSensor.red() < 100 && colorSensor.blue() < 100)

            if(colorSensor.red() >= 200 && colorSensor.blue() < 100) {
                do {
                    rightShuffle(power);
                }while (colorSensor.red() < 100 && colorSensor.blue() >= 200)

                if(colorSensor.blue() >= 200 && colorSensor.red() < 100) {
                    //push button
                }
            }
            if(colorSensor.blue() >= 200 && colorSensor.red() < 100) {
                //push button
            }
        }
    }

    //actual running code
    public void loop(){
        // TIME CHECK
        if ( time < 1) return;

        //DRIVE
//        wait(1);  // 1 second (not milliseconds) check method below
//        driveForward(45); // 45 is not a set number -- just a stand in for now
//        leftShuffle(2); // 2 for leftShuffle() and rightShuffle() are stand ins
//        rightShuffle(2);
//        wait(1);
//
//        leftShuffle(2); // 2 for leftShuffle() and rightShuffle() are stand ins
//        rightShuffle(2);
//        wait(1);

        wait(1);
        sense();
        driveForward(45,45,45,46);
        checkColor(left,30); //direction of starting movement + power of the driving



        stopDaBot();
    }

}

