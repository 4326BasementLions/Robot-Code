//package com.qualcomm.ftcrobotcontroller.opmodes;
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="BackUpAutonomous", group="BackUpAutonomous")  //AUTONOMOUS!

public class BackUpAutonomous extends OpMode {

  DcMotor leftBack;
  DcMotor leftFront;
  DcMotor rightFront;
  DcMotor rightBack;

  DcMotor scooper;
  DcMotor shooter1;
  DcMotor shooter2;

  Servo holder;


  @Override
  public void loop() {
    wait(1);
    drivePower(100);
    driveForward(20);
//     wait(1);
//     driveBackwards(10);
//     turnLeft(45);
//     driveForward(10);
//     turnRight(125);
//     wait(1);
//     scoop(10);
//     shoot(10);
//     stopRobot();
  }

  double startPos = 0;

  @Override
  public void init() {
  }

  public void driveForward(int position) {
    leftFront.setTargetPosition(position);
    rightFront.setTargetPosition(position);
    leftBack.setTargetPosition(position);
    rightBack.setTargetPosition(position);
  }

  public void driveBackwards(int position) {
    leftFront.setTargetPosition(-position);
    rightFront.setTargetPosition(-position);
    leftBack.setTargetPosition(-position);
    rightBack.setTargetPosition(-position);
  }

  public void turnLeft(int position) {
    rightFront.setTargetPosition(position);
    rightBack.setTargetPosition(position);
    leftFront.setTargetPosition(-position);
    leftBack.setTargetPosition(-position);
  }

  public void turnRight(int position) {
    leftFront.setTargetPosition(position);
    leftBack.setTargetPosition(position);
    rightFront.setTargetPosition(-position);
    rightBack.setTargetPosition(-positoion);
  }

  public void scoop(int position) {
    scooper.setTargetPosition(position);
  }

  public void shoot(int position) {
    shooter1.setTargetPosition(position);
    shooter2.setTargetPosition(position); //was originally set backwards
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
      Thread.sleep(time * 1000);
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

    leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    scooper = hardwareMap.dcMotor.get("scooper");
    shooter1 = hardwareMap.dcMotor.get("shooter1");
    shooter2 = hardwareMap.dcMotor.get("shooter2");
    shooter2.setDirection(DcMotor.Direction.REVERSE);

    scooper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
    shooter1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
    shooter2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);

    holder = hardwareMap.servo.get("holder");
    holder.scaleRange(0, 1);
  }
}

