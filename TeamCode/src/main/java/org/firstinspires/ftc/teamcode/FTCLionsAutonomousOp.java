//package com.qualcomm.ftcrobotcontroller.opmodes;
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    Servo holder;
    int pos = 50;

    @Override
    public void loop() {
        telemetry.addData("Text:", "Time: " + runtime.seconds());

        wait(10); //10th of a sec

        shoot();

//        drivePower(50);
//        driveForward(-10, -10, 10, 10, 1);

//        drivePower(100);
//        driveForward(10, 10, 10, 10, 2);
//        driveForward(-10, -10, 10, 10, 1);
//        driveForward(10, 10, 10, 10, 2);


//        drivePower(50);
//        turnLeft(pos, 1);
//        wait(3);
//
//        driveForward(pos, 1); //push button
//        wait(5);
//
//        drivePower(95);
//        moveRight(pos, 2); //right for blue & left for red
//        wait(6);
//
//        driveForward(pos, 1); //push button
//        wait(5);


//        stopRobot();
    }



    @Override
    public void init() {
    }

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

        //button = hardwareMap.servo.get("button");
        //button.scaleRange(0, 1);

        //button.setPosition(1);


    }
}