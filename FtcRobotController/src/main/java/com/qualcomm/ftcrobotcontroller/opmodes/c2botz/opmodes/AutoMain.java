package com.qualcomm.ftcrobotcontroller.opmodes.c2botz.opmodes;

//------------------------------------------------------------------------------
//
// PushBotAuto
//

import com.qualcomm.ftcrobotcontroller.opmodes.PushBotTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders implemented using a state machine for
 * the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-01-06-01
 */





public class AutoMain extends OpMode {

    DcMotor motorRearRight;
    DcMotor motorRearLeft;
    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor armRotationMotor;
    DcMotor armExtensionMotor;
    List<DcMotor> allDriveMotors = new ArrayList<DcMotor>();
    float speedMultiplier = 0;
    AutoMode mode = AutoMode.NULL;

    @Override
    public void init() {
        motorFrontLeft = hardwareMap.dcMotor.get("motor_fl");
        motorRearLeft = hardwareMap.dcMotor.get("motor_rl");
        motorFrontRight = hardwareMap.dcMotor.get("motor_fr");
        motorRearRight = hardwareMap.dcMotor.get("motor_rr");
        armRotationMotor = hardwareMap.dcMotor.get("rMotor");
        armExtensionMotor = hardwareMap.dcMotor.get("eMotor");
        speedMultiplier = .5f;
        allDriveMotors.add(motorFrontLeft);
        allDriveMotors.add(motorFrontRight);
        allDriveMotors.add(motorRearLeft);
        allDriveMotors.add(motorRearRight);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRearLeft.setDirection(DcMotor.Direction.REVERSE);
        //telemetry.
    }
    @Override public void loop ()

    {
        //----------------------------------------------------------------------
        //
        // State: Initialize (i.e. state_0).
        //
        switch (v_state)
        {
        //
        // Synchronize the state machine and hardware.
        //
        case 0:

            float seconds1 = 2.5f;


            long time1= System.currentTimeMillis();
            long end1 = (long) (time1+(seconds1*1000));
            while(System.currentTimeMillis() < end1) {
                motorFrontLeft.setPower(.5);
                motorFrontRight.setPower(.5);
                motorRearLeft.setPower(.5);
                motorFrontRight.setPower(.5);

            }
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorRearLeft.setPower(0);
            motorFrontRight.setPower(0);
            v_state++;
            break;

        case 1:
            float seconds2 = 1f;

            long time2= System.currentTimeMillis();
            long end2 = (long) (time2+(seconds2*1000));
            while(System.currentTimeMillis() < end2) {
                //Go Straight
                if(mode == AutoMode.BLUE){
                    //Turn Left
                    motorFrontLeft.setPower(-.5);
                    motorRearLeft.setPower(-.5);
                    motorFrontRight.setPower(.5);
                    motorRearRight.setPower(.5);

                }else if(mode == AutoMode.RED){
                    //Turn Right
                    motorFrontLeft.setPower(.5);
                    motorRearLeft.setPower(.5);
                    motorFrontRight.setPower(-.5);
                    motorRearRight.setPower(-.5);

                }

            }
            v_state++;
            break;

        default:

            break;
        }


        telemetry.addData ("18", "State: " + v_state);

    }
    private int v_state = 0;

} // PushBotAuto
