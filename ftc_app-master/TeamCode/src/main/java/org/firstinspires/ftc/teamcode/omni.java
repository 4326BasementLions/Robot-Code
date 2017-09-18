
package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/*
	Holonomic concepts from:
	http://www.vexforum.com/index.php/12370-holonomic-drives-2-0-a-video-tutorial-by-cody/0
    Robot wheel mapping:
          X FRONT X
        X           X
      X  FL   X   FR  X
             XXX
             XXX
              X
      X  BL       BR  X
        X           X
          X       X
*/

@TeleOp(name = "Concept: HolonomicDrivetrain", group = "Concept")
//@Disabled
public class omni extends OpMode {

    /*
     * SHREY DO NOT FORGET TO FIND THESE VALUES
     * DO NOT FORGET
     */

    final double RIGHT_ARM_CLOSED = 0;//? idk lol
    final double LEFT_ARM_CLOSED = 0;//? no clue lmao
    final double RIGHT_ARM_OPEN = 1;//? not remotely sure
    final double LEFT_ARM_OPEN = -1;//?  ¯\_(ツ)_/¯

    // declare motors

    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;

    // declare lift
    DcMotor liftMotor;

    // declare servo arms for lift

    Servo rightArm;
    Servo leftArm;

    @Override
    public void init() {

        // use hardwaremap to map the motors to their respective variables

        motorFrontRight = hardwareMap.dcMotor.get("motor front right");
        motorFrontLeft = hardwareMap.dcMotor.get("motor front left");
        motorBackLeft = hardwareMap.dcMotor.get("motor back left");
        motorBackRight = hardwareMap.dcMotor.get("motor back right");
        //If these need to be reversed, uncomment this code:
        //motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        //motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        //motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        //motorBackRight.setDirection(DcMotor.Direction.REVERSE);

        liftMotor = hardwareMap.dcMotor.get("lift");

        rightArm = hardwareMap.servo.get("right arm");
        leftArm = hardwareMap.servo.get("left arm");

    } // end of init()

    @Override
    public void loop() {


        // game pad 1 left stick controls direction
        // game pad 1 right stick X controls rotation

        float gamepad1LeftY = -gamepad1.left_stick_y;
        float gamepad1LeftX = gamepad1.left_stick_x;
        float gamepad1RightX = gamepad1.right_stick_x;

        // game pad 2 left stick Y controls lift motor

        float gamepad2LeftY = gamepad1.left_stick_y;

        // gamepad 2 right stick Y opens/closes the servo arms

        float gamepad2RightY = gamepad2.right_stick_y;

        // holonomic formulas

        float FrontLeft = -gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        float FrontRight = gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        float BackRight = gamepad1LeftY + gamepad1LeftX - gamepad1RightX;
        float BackLeft = -gamepad1LeftY + gamepad1LeftX - gamepad1RightX;

        // clip the right/left values so that the values never exceed +/- 1

        FrontRight = Range.clip(FrontRight, -1, 1);
        FrontLeft = Range.clip(FrontLeft, -1, 1);
        BackLeft = Range.clip(BackLeft, -1, 1);
        BackRight = Range.clip(BackRight, -1, 1);

        // write the values to the motors

        motorFrontRight.setPower(FrontRight);
        motorFrontLeft.setPower(FrontLeft);
        motorBackLeft.setPower(BackLeft);
        motorBackRight.setPower(BackRight);

        liftMotor.setPower(gamepad2LeftY);

        /*
         * VALUES MUST BE OBTAINED EXPERIMENTALLY.
         * THESE ARE ONLY PLACEMENT VALUES
         * SHREY YOU DUMBASS DO NOT FORGET THIS
         * AND IF YOU DO USE REHA AS A SCAPEGOAT
         * GODDAMN IT REHA
         */
        if (gamepad2RightY < 0)
        {
            rightArm.setPosition(RIGHT_ARM_CLOSED);
            leftArm.setPosition(LEFT_ARM_CLOSED);
        }
        else if (gamepad2RightY > 0)
        {
            rightArm.setPosition(RIGHT_ARM_OPEN);
            leftArm.setPosition(LEFT_ARM_OPEN);
        }


		/*
		 * Telemetry for debugging
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Joy XL YL XR",  String.format("%.2f", gamepad1LeftX) + " " +
                String.format("%.2f", gamepad1LeftY) + " " +  String.format("%.2f", gamepad1RightX));
        telemetry.addData("f left pwr",  "front left  pwr: " + String.format("%.2f", FrontLeft));
        telemetry.addData("f right pwr", "front right pwr: " + String.format("%.2f", FrontRight));
        telemetry.addData("b right pwr", "back right pwr: " + String.format("%.2f", BackRight));
        telemetry.addData("b left pwr", "back left pwr: " + String.format("%.2f", BackLeft));

    } // end of loop()

    @Override
    public void stop() {

    } // end of stop()

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    } // end of scaleInput()

} // end of class