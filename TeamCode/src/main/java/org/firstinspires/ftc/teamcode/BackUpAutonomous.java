package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

public class BackUpAutonomous extends OpMode {

  DcMotor leftBack;
  DcMotor rightBack;
  DcMotor leftFront;
  DcMotor rightFront;
  
  DcMotor scooper;
  DcMotor shooter1;
  DcMotor shooter2;
  
  Servo button;

}

    @Override
    public void loop() {
      wait(1);
      drivePower(2);
      driveForward(20);
      wait(1);
      driveBackwards(10);
      turnLeft(45);
      driveForward(10);
      turnRight(125);
      wait(1);
      scoop(10);
      shoot(10);
      
     stopRobot();
    }

 double startPos=0;
    @Override
    public void init() {
      leftBack=hardwareMap.dcMotor.get("leftBack");
      leftFront=hardwareMap.dcMotor.get("leftFront");
      rightFront=hardwareMap.dcMotor.get("rightFront");
      rightBack=hardwareMap.dcMotor.get("rightBack");
      
      
      scooper=hardwareMap.dcMotor.get("scooper");
      shooter1=hardwareMap.dcMotor.get("shooter1");
      shooter2=hardwareMap.dcMotor.get("shooter2");
      shooter2.setDirection(DcMotor.Direction.REVERSE);
      
      
      button=hardwareMap.servo.get("button");
      button.scaleRange(0, 1);
      
      rightBack.setDirection(DcMotor.Direction.REVERSE);
      rightFront.setDirection(DcMotor.Direction.REVERSE);
      
      leftBack.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      leftFront.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      rightBack.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      rightFront.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      
      scooper.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
      shooter1.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
      shooter2.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }
    
@Override
public void start {

  public void driveForward (int position) {
    leftFront.setTargetPosition(position);
    rightFront.setTargetPosition(position);
    leftBack.setTargetPosition(position);
    rightBack.setTargetPosition(position);
  }
    public void driveBackwards (int position) {
    leftFront.setTargetPosition(-position);
    rightFront.setTargetPosition(-position);
    leftBack.setTargetPosition(-position);
    rightBack.setTargetPosition(-position);
  }
      public void turnLeft (int position) {
         rightFront.setTargetPosition(position);
        rightBack.setTargetPosition(position);
      }
    public void turnRight (int position) {
        leftFront.setTargetPosition(position);
      leftBack.setTargetPosition(position);
    }
  
    public void scoop (int position) {
     scooper.setTargetPosition(position); 
    }
  
    public void shoot (int position) {
      shooter1.setTargetPosition(position); 
      shooter2.setTargetPosition(position); 
     
    }
      public void buttonMove (double position) {
        button.setPosition(position);
        button.setPosition(1 - position);
    }
      public void drivePower (double power) {
        leftFront.setPower(power / 2);
        rightFront.setPower(power / 2);
        leftBack.setPower(power / 2);
        rightBack.setPower(power / 2);        
    }
      public void stopRobot() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        scooper.setPower(0);
        shooter1.setPower(0);
        shooter2.setPower(0);
        buttonMove(1);
        
        wait(1);
    }
        public void wait(int time) {
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    @Override 
    public void stop() {
    }

}
