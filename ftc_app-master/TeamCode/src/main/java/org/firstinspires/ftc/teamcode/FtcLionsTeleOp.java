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
    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */

    final boolean DEBUG = true;

    DcMotor leftBack;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor rightBack;
    DcMotor Scooper;
    DcMotor Shooter1;
    DcMotor Shooter2;
    
    Servo Button;

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

    }

    @Override
    public void loop() {
        if (DEBUG) {
            // TELEMETRY FOR JOYSTICK DEBUGGING
            telemetry.addData("Text:", "Gamepad1 Movement y: " + gamepad1.left_stick_y + ", " + gamepad1.right_stick_y);
            telemetry.addData("Text:", "Gamepad1 Movement x: " + gamepad1.left_trigger + ", " + gamepad1.right_trigger);
        }

        ////////////////////////////////
        //     GAMEPAD 1 CONTROLS     //
        ////////////////////////////////


        // TANK DRIVE
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
