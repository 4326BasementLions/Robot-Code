/*
    Controls

    Controller 1 → Drive

Joystick1_x	Mecanum drive(x)
Joystick1_y	Mecanum drive(y)
Joystick2_x	Mecanum drive(a)
Joystick2_y
D-pad up 	lifter up
D-pad down	lifter down
D-pad left
D-pad right
A	shoot
B	fling
X	scooping
Y 	Reverse direction of Mecanum drive
Bumper L	High Speed
Bumper R	Low Speed
Trigger L
Trigger R
*/

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

public class FTCLionsTeleOp extends OpMode {
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

    DcMotor lifter;
    boolean limiter = false; //as a random boolean until further notice
    Servo holder; //ballCap refrence
    boolean holderTrue = true; //true = is holding the arm

    double lifterBot = 0;
    double lifterTop = 0;
    public FTCLionsTeleOp() {

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

        scooper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

//        buttonPush.scaleRange(0, 1);


        lifter = hardwareMap.dcMotor.get("lifter");
        lifter.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //for intital start
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
            telemetry.addData("Text:", "Shooting Data: scoop." + gamepad2.left_stick_y + " ; shoot." + gamepad2.right_trigger);
        }

        ////////////////////////////////
        //     GAMEPAD 1 CONTROLS     //
        ////////////////////////////////


        int rf = 0, lf = 0, rb = 0, lb = 0;
        boolean updateMotors = false;
        if(abs(gamepad1.right_stick_y)>threshold| |abs(gamepad1.right_stick_x)>threshold)

        {
            rf = (gamepad1.right_stick_y - gamepad1.right_stick_x) / 2;
            lf = (-gamepad1.right_stick_y - gamepad1.right_stick_x) / 2;
            rb = (-gamepad1.right_stick_y - gamepad1.right_stick_x) / 2;
            lb = (gamepad1.right_stick_y - gamepad1.right_stick_x) / 2;
            updateMotors = true;
        }

        if(abs(gamepad1.left_stick_x) >threshold)

        {
            //rotate
            rf = (-gamepad1.left_stick_x) / 2;
            lf = (-gamepad1.left_stick_x) / 2;
            rb = (gamepad1.left_stick_x) / 2;
            lb = (gamepad1.left_stick_x) / 2;
            updateMotors = true;
        }

        if(updateMotors)

        {
            rightFront.setPower(rf);
            leftFront.setPower(lf);
            rightBack.setPower(rb);
            leftBack.setPower(lb);
        }


        ////////////////////////////////
        //     GAMEPAD 2 CONTROLS     //
        ////////////////////////////////

        if(gamepad2.right_trigger != 0) { //shooter
            shooter1.setPower(gamepad2.right_trigger / 2);
            shooter2.setPower(-gamepad2.right_trigger / 2);
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
        if(gamepad2.x && holderTrue == true) { //holder for the claw
            holderTrue = false;
            holder.setPosition(1);
            wait(1);
//            holder.scaleRange(1, 0); //sets current pos to default till further notice
        }
        if(limiter == true && holderTrue == false && gamepad2.right_stick_y <= 0) {
            lifter.setPower(-gamepad2.right_stick_y);
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