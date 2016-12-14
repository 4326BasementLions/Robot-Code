import android.hardware.SensorManager;
        import android.os.Bundle;
        import android.view.View;

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

@TeleOp(name="TeleOp", group="TeleOp")  //TELEOP!

public class FtcLionsTeleOp extends OpMode {


    final boolean DEBUG = true;

    final int threshold = 5;

    DcMotor leftBack;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor rightBack;


    public class FtcLionsTeleOp extends OpMode {


        // TANK DRIVE


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
    }
}
////////////////////////////////
//     GAMEPAD 2 CONTROLS     //