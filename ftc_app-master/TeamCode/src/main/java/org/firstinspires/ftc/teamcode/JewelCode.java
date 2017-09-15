package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;


import com.qualcomm.robotcore.util.ElapsedTime;

import android.graphics.Color;
import com.qualcomm.robotcore.hardware.ColorSensor;
import android.app.Activity;
import android.view.View;


@Autonomous(name="ColorSensorTest", group="Autonomous")

public class ColorSensorTest extends OpMode {
    final boolean DEBUG = true;
    DcMotor leftBack;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor rightBack;
    ColorSensor testSense;
    Servo continuousServo;
    String whichColor;
    @Override
    public void loop{
        if(DEBUG){
            telemetry.addData("Text:", "isColor Data(4): " + isColor(colorSensor));
            //telemetry.addData("Text:", "RGB Colors: " + colorSensor.red() + ", " + colorSensor.green() + ", " + colorSensor.blue());
            telemetry.addData("Colors (R, B, G)", hsvValues[0]);
        }
    }

    @Override
    public void runOpMode{
        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F,0F,0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);

        colorSensor.enableLed(false);

        waitForStart();

        Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

        telemetry.update();

        continuousServo.setPosition(0.0);

        //assuming red team





        @Override
        public void start() {
            checkColor();
            leftFront = hardwareMap.dcMotor.get("leftFront");
            leftBack = hardwareMap.dcMotor.get("leftBack");
            rightFront = hardwareMap.dcMotor.get("rightFront");
            rightBack = hardwareMap.dcMotor.get("rightBack");

            testSense = hardwareMap.colorSensor.get("colorSensor");

            continuousServo = hardwareMap.Servo.get("servo");

            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            leftFront.setDirection(DcMotor.Direction.REVERSE);
            leftBack.setDirection(DcMotor.Direction.REVERSE);

            continuousServo.setPosition(.5);
            driveForward(8,8,8,8,2);

            while(whichColor!="red"){
                //side to side until it finds red
                driveForward(8,-8,-8,8,2);
                driveForward(-8,8,8,-8,2);
            }
            if (whichColor=="red"){
                continuousServo.setPosition(1.0);
            }


        }

    public void checkColor(){
        if(colorSensor.red>colorSensor.blue){
            whichColor="red";

        }
        else{
            whichColor="blue";
        }

    }

    public void driveForward(int pos1, int pos2, int pos3, int pos4, double newTime) {
        if(newTime == 0) { //in loops like do whiles
            leftFront.setPower(pos1 / 10);
            leftBack.setPower(pos2 / 10);
            rightFront.setPower(pos3 / 10);
            rightBack.setPower(pos4 / 10);
        }

        else { //extra precaution
            while (newTime > 0 && runtime.milliseconds() <= (runtime.milliseconds() + (newTime * 100))) { //1s = 1000ms
                leftFront.setPower(pos1 / 10);
                leftBack.setPower(pos2 / 10);
                rightFront.setPower(pos3 / 10);
                rightBack.setPower(pos4 / 10);
                wait(5);

                runtime.reset();
            }
        }
    }

}
}

