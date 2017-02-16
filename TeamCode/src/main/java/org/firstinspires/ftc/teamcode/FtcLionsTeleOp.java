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

    DcMotor scooper;
    DcMotor shooter1;
    DcMotor shooter2;

    DcMotor lifter;
    boolean limiter = false; //as a random boolean until further notice
    Servo holder; //ballCap refrence
    boolean holderTrue = true; //true = is holding the arm
    
    
    Servo sensorExtend;
    boolean retracted = true;
    
    double lifterBot = 0;
    double lifterTop = 100; //pre-set from testing

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

        scooper = hardwareMap.dcMotor.get("scooper");
        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2");
        shooter2.setDirection(DcMotor.Direction.REVERSE);
//        buttonPush = hardwareMap.servo.get("button");


        scooper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

//        buttonPush.scaleRange(0, 1);


        lifter = hardwareMap.dcMotor.get("lifter");
        lifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //for intital start


//        set lifter to start at a sightly higher position that it's current rest.
//        set holder to continously hold the arm from the initialization
//        lifter.setTargetPosition(1);
        lifterBot = (lifter.getCurrentPosition() + .1); //getting base of lifter

        colorSensor = hardwareMap.colorSensor.get("colorSensor");
        
        sensorExtend = hardwareMap.servo.get("sensorExtend");
        sensorExtend.scaleRange(0,1);
    }

    @Override
    public void init() {
        holder = hardwareMap.servo.get("holder");
        holder.scaleRange(0, 1);
        holder.setPosition(1.0);

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
            telemetry.addData("Text:", "Lifter Data: " + gamepad2.right_stick_y); //*** --> for encoders
            telemetry.addData("Text:", "Holder Data: " + gamepad2.x + ", " + holderTrue);
            telemetry.addData("Text:", "Shooting Data: scoop." + gamepad2.left_stick_y + " ; shoot." + gamepad2.right_trigger);

            telemetry.addData("Text:", "isColor Data: " + isColor(colorSensor, 5));
        }

        ////////////////////////////////
        //     GAMEPAD 1 CONTROLS     //
        ////////////////////////////////

//        float div = 0;
//        if (jLow(gamepad1.right_stick_y) != 0) {
//            div += 1;
//        }
//        if (jLow(gamepad1.right_stick_x) != 0) {
//            div += 1;
//        }
//        if (jLow(gamepad1.left_stick_x) != 0) {
//            div += 1;
//        }

        //right front motor and left back motors are backwards
        float rf = (jLow(gamepad1.right_stick_y) - jLow(gamepad1.right_stick_x) - jLow(gamepad1.left_stick_x));  //right-stick-y = forward/backward
        float lf = (jLow(-gamepad1.right_stick_y) - jLow(gamepad1.right_stick_x) - jLow(gamepad1.left_stick_x));  //right-stick-x = left/right
        float rb = (jLow(-gamepad1.right_stick_y) - jLow(gamepad1.right_stick_x) + jLow(gamepad1.left_stick_x));  //left-stick-x = turning
        float lb = (jLow(gamepad1.right_stick_y) - jLow(gamepad1.right_stick_x) + jLow(gamepad1.left_stick_x));

//        To fix diagonals, we need to change the power of certain motors, but only during the time when we are going diagonal in a specific direction



        rightFront.setPower(rf);
        leftFront.setPower(lf);
        rightBack.setPower(rb);
        leftBack.setPower(lb);

        telemetry.addData("Text:", "Variable Motors: " + rf + ", " + lf + ", " + rb + ", " + lb);
        
        
        //Brings out sensor
        if(gamepad1.x && retracted == true){
            sensorExtend.setPosition(1.0);
            retracted = false;
        }
        
        //Retracts sensor
        if(gamepad1.x && retracted == false){
            sensorExtend.setPosition(0.0);
            retracted = true;
        }


        ////////////////////////////////
        //     GAMEPAD 2 CONTROLS     //
        ////////////////////////////////


        if(gamepad2.right_trigger != 0) { //shooter
            shooter1.setPower(jHigh(gamepad2.right_trigger) / 1.4); //to prevent penalty and incerase accuracy
            shooter2.setPower(jHigh(gamepad2.right_trigger) / 1.4);
        }
        if(Math.abs(gamepad2.left_stick_y) > .09) { //scooper
            scooper.setPower(jHigh(-gamepad2.left_stick_y) / 1.3);

        }

        if(lifter.getCurrentPosition() > lifterBot || lifter.getCurrentPosition() > lifterTop) {
            limiter = true;
        }
        else {
            limiter = false;
        }

        if(gamepad2.right_stick_y != 0 && limiter == false && holderTrue == false) { //lifter for the ball/climb
            lifter.setPower(jHigh(-gamepad2.right_stick_y));
        }
        if(gamepad2.x) { //holder for the claw
            holderTrue = false;
            holder.setPosition(0.0);
        }
//        else if(gamepad2.y) {
//            holderTrue = true;
//            holder.setPosition(1.0);
//        }
        if(limiter == true && holderTrue == false && gamepad2.right_stick_y <= 0) {
            lifter.setPower(-gamepad2.right_stick_y);
        }


        /// E-STOP \\\
        if (gamepad1.left_bumper && gamepad1.right_bumper && gamepad1.right_stick_button && gamepad1.left_stick_button ||
                gamepad2.left_bumper && gamepad2.right_bumper && gamepad2.right_stick_button && gamepad2.left_stick_button) { //mash those bumpers & stick buttons
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);

            shooter1.setPower(0);
            shooter2.setPower(0);
            scooper.setPower(0);
            lifter.setPower(0);
//            buttonPush.setPosition(0);
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

    @Override
    public void stop() {

    }
}
