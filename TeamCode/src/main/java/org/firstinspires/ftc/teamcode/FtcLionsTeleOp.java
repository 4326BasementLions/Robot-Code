//package com.qualcomm.ftcrobotcontroller.opmodes;
package org.firstinspires.ftc.teamcode;

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

public class FtcLionsTeleOp extends OpMode {
    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */

    final boolean DEBUG = true;

    DcMotor leftBack;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor rightBack;
    
    DcMotor scooper;
    DcMotor shooter1;
    DcMotor shooter2;
//    Servo buttonPush;

    DcMotor lifter;
    boolean limiter = false; //as a random boolean until further notice
    Servo holder; //ballCap refrence
    boolean holderTrue = true; //true = is holding the arm

//    double lifterBot = 0;
//    double lifterTop = 0;
    public FtcLionsTeleOp() {

    }

    @Override
    public void start() {
        boolean  started = true;

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
//        buttonPush = hardwareMap.servo.get("button");
        
        scooper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        shooter1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        shooter2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);

//        buttonPush.scaleRange(0, 1);
        

        lifter = hardwareMap.dcMotor.get("lifter");
        lifter.setMode(DcMotor.RunMode.RUN_USING_ENCODERS); //for intital start
        holder = hardwareMap.servo.get("holder");
        holder.scaleRange(0, 1);

    }

    @Override
    public void init() {
////        set lifter to start at a sightly higher position that it's current rest.
//        set holder to continously hold the arm from the initialization
//        lifter.setTargetPosition(1);
//        holder.setPosition(1);
//        holderTrue = true;
//        holder.scaleRange(1, 0);
//        lifterBot = lifter.getCurrentPosition(); //getting base of lifter
    }

    public void wait(int  time){
        try{
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loop() {
        if (DEBUG) {
            // TELEMETRY FOR JOYSTICK DEBUGGING
            telemetry.addData("Text:", "Gamepad1 Movement y: " + gamepad1.left_stick_y + ", " + gamepad1.right_stick_y);
            telemetry.addData("Text:", "Gamepad1 Movement x: " + gamepad1.left_trigger + ", " + gamepad1.right_trigger);
            telemetry.addData("Text:", "Lifter Data: " + gamepad2.right_stick_y); //*** --> for encoders
            telemetry.addData("Text:", "Holder Data: " + gamepad2.x + ", " + holderTrue);
            telemetry.addData("Text:", "Shooting Data: scoop." + gamepad2.left_bumper + " ; shoot." + gamepad2.right_bumper);
        }

        ////////////////////////////////
        //     GAMEPAD 1 CONTROLS     //
        ////////////////////////////////


        // TANK DRIVE
        leftFront.setPower(-gamepad1.left_stick_y / 1.2); // /1.4 for general power issues considering the robot is somewhat tipsy
        rightFront.setPower(gamepad1.right_stick_y / 1.2);
        leftBack.setPower(-gamepad1.left_stick_y / 1.2);
        rightBack.setPower(gamepad1.right_stick_y / 1.2);

        if(gamepad1.right_trigger != 0) { //right side of robot
            rightFront.setPower(-gamepad1.right_trigger); //assuming that the right side is backwards
            rightBack.setPower(gamepad1.right_trigger);
            leftFront.setPower(gamepad1.right_trigger);
            leftBack.setPower(-gamepad1.right_trigger);

        }
        if(gamepad1.left_trigger != 0) { //left side of the robot
            rightFront.setPower(gamepad1.left_trigger); //assuming that the right side is backwards
            rightBack.setPower(-gamepad1.left_trigger);
            leftFront.setPower(-gamepad1.left_trigger);
            leftBack.setPower(gamepad1.left_trigger); //.7 for the general power per side and .8 for the wheels moving forward
        }


        ////////////////////////////////
        //     GAMEPAD 2 CONTROLS     //
        ////////////////////////////////

//        while(gamepad2.a) {
//            buttonPush.setPosition(1);
//        }
        if(gamepad2.right_bumper) { //shooter
            shooter1.setPower(1/2);
            shooter2.setPower(-1/2);
        }
        if(gamepad2.left_stick_y != 0) { //scooper
          scooper.setPower(gamepad2.left_stick_y);
 
        }

//        if(lifter.getCurrentPosition() > lifterBot || lifter.getCurrentPosition() > lifterTop) {
//            limiter = true;
//        }
//        else {
//            limiter = false;
//        }

        if(gamepad2.right_stick_y != 0 && limiter == false && holderTrue == false) { //lifter for the ball/climb
            lifter.setPower(-gamepad2.right_stick_y);
        }
        if(limiter == true && holderTrue == false && gamepad2.right_stick_y <= 0) {
            lifter.setPower(-gamepad2.right_stick_y);
        }

        if(gamepad2.x && holderTrue == true) { //holder for the claw
            holderTrue = false;
            holder.setPosition(1);
            wait(1);
            holder.scaleRange(1, 0); //sets current pos to default till further notice
        }



            /// E-STOP \\\
        if (gamepad1.left_bumper && gamepad1.right_bumper && gamepad2.left_bumper && gamepad2.right_bumper) { //mash those bumpers
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);

            shooter1.setPower(0);
            shooter2.setPower(0);
            scooper.setPower(0);
//            buttonPush.setPosition(0);
        }
    }


    @Override
    public void stop() {

    }
}
