package com.qualcomm.ftcrobotcontroller.opmodes.c2botz.opmodes;

/**
 * Created by FTC-Robotics on 11/6/15.
 */
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class TeleopMain extends OpMode {

    DcMotor motorRearRight;
    DcMotor motorRearLeft;
    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor armRotationMotor;
    DcMotor armExtensionMotor;

    /**
     * Constructor
     */
    public TeleopMain() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot and reversed.
		 *
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the arm joint of the manipulator.
		 *    "servo_6" controls the claw joint of the manipulator.
		 */
        motorFrontLeft = hardwareMap.dcMotor.get("motor_fl");
        motorRearLeft = hardwareMap.dcMotor.get("motor_rl");
        motorFrontRight = hardwareMap.dcMotor.get("motor_fr");
        motorRearRight = hardwareMap.dcMotor.get("motor_rr");
        armRotationMotor = hardwareMap.dcMotor.get("rMotor");
        armExtensionMotor = hardwareMap.dcMotor.get("eMotor");

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRearLeft.setDirection(DcMotor.Direction.REVERSE);


    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float leftStrafe = gamepad1.left_trigger;
        float rightStrafe = gamepad1.right_trigger;
        float extension = -gamepad2.left_stick_y;
        float minusRotation = gamepad2.left_trigger;
        float plusRotation = gamepad2.right_trigger;

        float right = throttle - direction;
        float left = throttle + direction;
        float extensionValue;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -2, 1);
        left = Range.clip(left, -2, 1);
        extensionValue = Range.clip(extension, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);
        extensionValue = (float)(scaleInput(extensionValue));

        float speedMultiplier = .5f;
        float extensionSpeed = .5f;
        float strafeSpeed = .5f;

        if(gamepad1.dpad_up){
            if(speedMultiplier < 1) {
                speedMultiplier += .05;
            }
        }else if(gamepad1.dpad_down){
            if(speedMultiplier > -1) {
                speedMultiplier -= .05;
            }
        }

        // write the values to the motors
        if(leftStrafe == 0 && rightStrafe == 0) {
            motorFrontRight.setPower(right * speedMultiplier);
            motorRearRight.setPower(right * speedMultiplier);
            motorFrontLeft.setPower(left * speedMultiplier);
            motorRearLeft.setPower(left * speedMultiplier);
        }
        else{
            if(leftStrafe > 0 && rightStrafe == 0) {
                motorFrontRight.setPower(-leftStrafe * strafeSpeed);
                motorFrontLeft.setPower(-leftStrafe * strafeSpeed);
                motorRearRight.setPower(leftStrafe * strafeSpeed);
                motorRearLeft.setPower(leftStrafe * strafeSpeed);
            }else if (rightStrafe > 0 && leftStrafe == 0){
                motorFrontRight.setPower(rightStrafe * strafeSpeed);
                motorFrontLeft.setPower(rightStrafe * strafeSpeed);
                motorRearRight.setPower(-rightStrafe * strafeSpeed);
                motorRearLeft.setPower(-rightStrafe * strafeSpeed);
            }
        }

        armRotationMotor.setPower(plusRotation-minusRotation);

       armExtensionMotor.setPower(extensionValue*extensionSpeed);





        // update the position of the arm.




		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
        telemetry.addData("right strafe power", "right stafe: " + String.format("%.2f", rightStrafe));
        telemetry.addData("left strafe power", "left strafe: " + String.format("%.2f", leftStrafe));



    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }


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
    }

}

