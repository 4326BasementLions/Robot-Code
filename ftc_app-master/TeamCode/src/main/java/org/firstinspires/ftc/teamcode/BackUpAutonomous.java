package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

public class ZakiAutonomousOp extends OpMode {

  DcMotor leftBack;
  DcMotor rightBack;
  DcMotor leftFront;
  DcMotor rightFront;
  
  DcMotor Scooper;
  DcMotor Shooter1;
  DcMotor Shooter2;
  
  Servo Button;

}

 double startPos=0;
    @Override
    public void init() {
      leftBack=hardwareMap.dcMotor.get("leftBack");
      leftFront=hardwareMap.dcMotor.get("leftFront");
      rightFront=hardwareMap.dcMotor.get("rightFront");
      rightBack=hardwareMap.dcMotor.get("rightBack");
      
      rightBack.setDirection(DcMotor.Direction.REVERSE);
      rightFront.setDirection(DcMotor.Direction.REVERSE);
      
      leftBack.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      leftFront.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      rightBack.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      rightFront.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);      
    }
    
    
