//package com.qualcomm.ftcrobotcontroller.opmodes;
package org.firstinspires.ftc.teamcode;


import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

 @Autonomous(name="ColorAutonomous", group="ColorAutonomous")  //AUTONOMOUS!

 public class ColorSensorCode_Autonomous extends OpMode {


    DcMotor leftBack;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor rightBack;

    DcMotor scooper;
    DcMotor shooter1;
    DcMotor shooter2;
    //Servo button;

    DcMotor lifter;
    Servo holder;
    ColorSensor colorSensor;


   public void start() {
       leftFront = hardwareMap.dcMotor.get("leftFront");
       leftBack = hardwareMap.dcMotor.get("leftBack");
       rightFront = hardwareMap.dcMotor.get("rightFront");
       rightBack = hardwareMap.dcMotor.get("rightBack");

       leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

       scooper = hardwareMap.dcMotor.get("scooper");
       shooter1 = hardwareMap.dcMotor.get("shooter1");
       shooter2 = hardwareMap.dcMotor.get("shooter2");
       shooter2.setDirection(DcMotor.Direction.REVERSE);
       button = hardwareMap.servo.get("button");

       scooper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
       shooter1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
       shooter2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);

       button.scaleRange(0, 1);


       lifter = hardwareMap.dcMotor.get("lifter");
       lifter.setMode(DcMotor.RunMode.RUN_USING_ENCODERS); //for intital start
       holder = hardwareMap.servo.get("holder");
       holder.scaleRange(0, 1);
   }
   public void init(){

   }

   // Drive Methods //


  public void driveForward(int pos1, int pos2, int pos3, int pos4, int newTime) {
        while (runtime.seconds() <= (runtime.seconds() + newTime)) {
            leftFront.setPower(pos1 / 10);
            leftBack.setPower(pos2 / 10);
            rightFront.setPower(pos3 / 10);
            rightBack.setPower(pos4 / 10);
        }
        wait(5);
        runtime.reset();
    }

    public void shoot() {
        while (runtime.seconds() <= 6) {
            shooter1.setPower(pos / 1.4);
            shooter2.setPower(pos / 1.4);
            scooper.setPower(pos);
        }
        runtime.reset();
    }

    public void drivePower(double power) {
        leftFront.setPower(power / 100);
        rightFront.setPower(power / 100);
        leftBack.setPower(power / 100);
        rightBack.setPower(power / 100);
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
  public void turnRight(pos1, pos2){
          while (runtime.seconds() <= (runtime.seconds() + newTime)) {
            leftFront.setPower(pos1 / 10);
            leftBack.setPower(pos2 / 10);
        }
        wait(5);
        runtime.reset();
    }
    public void turnLeft(pos3, pos4){
          while (runtime.seconds() <= (runtime.seconds() + newTime)) {
            rightFront.setPower(pos3 / 10);
            rightBack.setPower(pos4 / 10);
        }
        wait(5);
        runtime.reset();
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



    public void checkColor(String direction, double power) {
        sense();
        if (direction == "left"|| direction == "Left") { //assiming we're on the red alliance
            do {
               // leftShuffle(power);
             turnLeft(10, 10);
             driveForward(10, 10, 10, 10);
             turnRight(10,10);
           
            }while (colorSensor.red() < 100 && colorSensor.blue() < 100);

            if(colorSensor.blue() >= 200 && colorSensor.red() < 100) {
                do {
                   // leftShuffle(power);
                 turnLeft(10, 10);
                 driveForward(10, 10, 10, 10);
                 turnRight(10,10);
                 
                }while (colorSensor.blue() < 100 && colorSensor.red() >= 200);

                if(colorSensor.red() >= 200 && colorSensor.blue() < 100) {
                   // button.setPosition(1); //push button
                 driveForward(5,5,5,5);
                }
            }
            if(colorSensor.red() >= 200 && colorSensor.blue() < 100) {
                //button.setPosition(1); //push button
             driveForward(5,5,5,5);
            }
        }

        if (direction == "right" || direction == "Right") { //assuming we're on the blue alliance
            do {
              //  rightShuffle(power);
              turnRight(10, 10);
              driveForward(10, 10, 10, 10);
              turnLeft(10,10);
            }while (colorSensor.red() < 100 && colorSensor.blue() < 100);

            if(colorSensor.red() >= 200 && colorSensor.blue() < 100) {
                do {
                   // rightShuffle(power);
                   turnRight(10, 10);
                   driveForward(10, 10, 10, 10);
                   turnLeft(10,10);
                }while (colorSensor.red() < 100 && colorSensor.blue() >= 200);

                if(colorSensor.blue() >= 200 && colorSensor.red() < 100) {
                   // button.setPosition(1); //push button
                 driveForward(5, 5, 5, 5);
                }
            }
            if(colorSensor.blue() >= 200 && colorSensor.red() < 100) {
                button.setPosition(1); //push button
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
        checkColor("left",30); //direction of starting movement + power of the driving

        stopRobot();
    }

}


