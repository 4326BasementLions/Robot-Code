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

   @Override
  &nbsp;public void loop(){
    // TIME CHECK
       if ( time < 1) return;

       //DRIVE
       wait(1);  // 1 second (not milliseconds) check method below
       driveForward(45); // 45 is not a set number -- just a stand in for now
       leftShuffle(2); // 2 for leftShuffle() and rightShuffle() are stand ins
       rightShuffle(2);
       wait(1);


       leftShuffle(2); // 2 for leftShuffle() and rightShuffle() are stand ins
       rightShuffle(2);
       wait(1);


       stopDaBot();
   }

   @Override
   public void init(){
       leftFront = hardwareMap.dcMotor.get("leftFront");
       leftBack = hardwareMap.dcMotor.get("leftBack");
       rightFront = hardwareMap.dcMotor.get("rightFront");
       rightBack = hardwareMap.dcMotor.get("rightBack");
   }

   // Drive Methods //


   public void tankDrive (double power){
       leftFront.setPower(-power);
       rightFront.setPower(power);
       leftBack.setPower(-power);
       rightBack.setPower(power);
   }

   public void rightShuffle(double power){
   &nbsp;   leftFront.setPower(power);
       leftBack.setPower(-power);
       rightFront.setPower(-power);
       rightBack.setPower(power);
   }
   public void leftShuffle(double power){
       leftFront.setPower(-power);
&nbsp;      leftBack.setPower(power);
       rightFront.setPower(power);
       rightBack.setPower(-power);
   }

   public void driveForward(int position){
       leftFront.setTargetPosition(position);
       leftBack.setTargetPosition(position);
       rightFront.setTargetPosition(position);
       rightBack.setTargetPosition(position);
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

      &nbsp;// bLedOn represents the state of the LED.
       boolean bLedOn = true;

    ;   // turn the LED on in the beginning, just so user will know that the sensor is active.
       colorSensor.enableLed(bLedOn);

       colorSensor = hardwareMap.colorSensor.get("sensor_color");

       // convert the RGB values to HSV values.
       Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);

       // send the info back to driver station using telemetry function.
       telemetry.addData("LED", bLedOn ? "On" : "Off");
       telemetry.addData("Clear", colorSensor.alpha());
       telemetry.addData("Red  ", colorSensor.red());
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
 &nbsp;     leftBack.setPower(0);
       leftFront.setPower(0);
       rightBack.setPower(0);
       rightFront.setPower(0);
       wait(1);
 &nbsp; }

}


