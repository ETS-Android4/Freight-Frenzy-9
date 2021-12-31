package org.firstinspires.ftc.teamcode;

/* 2019-2022 FTC Robotics Freight-Frenzy
 * (c) 2019-2022 La Salle Robotics
 * Developed for the Freight Frenzy competition
 */

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class RoboHelper {

    private final ElapsedTime runtime;

	private final DcMotor plateSpinner;

    public Arm arm;

    public MecanumDrive drive;

	public BNO055IMU imu = null;

    private final double FRICTION_COEF = 1.75; // the distance the robot goes in 1 second (in feet)

    public double speedScale = 1; // keep between 0 and 1

    // setup class initializer
    public RoboHelper(HardwareMap hardwareMap, ElapsedTime runtime) {
        this.runtime = runtime;

        // Setup Devices
        this.drive = new MecanumDrive(hardwareMap, runtime);
        this.arm = new Arm(hardwareMap);

        // Setup Motors
		this.plateSpinner = hardwareMap.get(DcMotor.class, "spinner");

        // Set Directions

		this.plateSpinner.setDirection(DcMotorSimple.Direction.FORWARD);


		// Setup Sensors
        this.imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        //parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        this.imu.initialize(parameters);
    }

    /*
     * This section is for making the driver programing experience simpler
     */


    // handleGamepads the second gamepad is currently ignored for this input code
    public RoboHelper handleGamepads(Gamepad gamepad1, Gamepad gamepad2) {
        drive.calculateDirections(gamepad1.left_stick_x, gamepad1.left_stick_y, Math.pow(gamepad1.right_stick_x, 3));
        drive.applyPower();
        return this;
    }

    /*
     * This section is for making autonomous programming simpler
     */

    public void sleep(double sleepTime) {
        double time = runtime.time();
        double initTime = time;

        while (time <= initTime + sleepTime) {
            time = runtime.time();
        }
    }

    public RoboHelper off() {
        // drive.off(); // Do we want to turn the drive train off here as well?
        stopSpinner();
        return this;
    }

    public RoboHelper goFor(double seconds) {
        sleep(seconds);
        off();
        return this;
    }

    // support the old API style
    public RoboHelper runFor(double seconds) {return goFor(seconds);}

    public double getHeading() {
        Orientation orientation = this.imu.getAngularOrientation();
        return orientation.firstAngle;
    }

	public RoboHelper startSpinner() {
		this.plateSpinner.setPower(1);
		return this;
	}

	public RoboHelper startSpinnerOther() {
        this.plateSpinner.setPower(-1);
        return this;
    }

	public RoboHelper stopSpinner() {
		this.plateSpinner.setPower(0);
		return this;
	}

}
