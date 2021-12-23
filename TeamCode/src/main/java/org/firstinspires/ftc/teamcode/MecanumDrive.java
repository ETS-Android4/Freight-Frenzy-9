package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class MecanumDrive {

    public double speed = 1;

    private static final double FRICTION_COEF = 1.75;
    private double flP = 0;
    private double blP = 0;
    private double frP = 0;
    private double brP = 0;

    private final DcMotor leftFront;
    private final DcMotor rightFront;
    private final DcMotor leftBack;
    private final DcMotor rightBack;

    private ElapsedTime runtime;

    public MecanumDrive(HardwareMap hardwareMap, ElapsedTime runtime) {
        this.leftFront = hardwareMap.get(DcMotor.class, "fL");
        this.rightFront = hardwareMap.get(DcMotor.class, "fR");
        this.leftBack = hardwareMap.get(DcMotor.class, "bL");
        this.rightBack = hardwareMap.get(DcMotor.class, "bR");

        this.leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        this.leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        this.rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        this.rightBack.setDirection(DcMotorSimple.Direction.FORWARD);
        this.runtime = runtime;

    }

    public double magnitude(double x, double y) {
        return -Math.hypot(x, y);
    }

    public double angle(double x, double y) {
        return Math.atan2(y, x);
    }

    public void sleep(double sleepTime) {
        double time = runtime.time();
        double initTime = time;

        while (time <= initTime + sleepTime) {
            time = runtime.time();
        }
    }
    public MecanumDrive calculateDirectionsRobotCentric(double x, double y, double turn) {

        double phi = (angle(x,y));

        this.blP =
                magnitude(x,y)
                        * Math.sin( phi + (Math.PI / 4))
                        + turn; // flP
        this.flP =
                magnitude(x,y)
                        * Math.sin(phi - (Math.PI / 4))
                        + turn; // blP

        this.brP =
                magnitude(x,y)
                        * Math.sin(phi + (Math.PI / 4))
                        - turn; // frP
        this.frP =
                magnitude(x,y)
                        * Math.sin(phi - (Math.PI / 4))
                        - turn; // brP

        return this;
    }
    public MecanumDrive calculateDirectionsFieldCentric(double x, double y, double turn, double heading) {

        double phi = (angle(x,y)-heading);

        this.blP =
                magnitude(x,y)
                        * Math.sin( phi + (Math.PI / 4))
                        + turn; // flP
        this.flP =
                magnitude(x,y)
                        * Math.sin(phi - (Math.PI / 4))
                        + turn; // blP

        this.brP =
                magnitude(x,y)
                        * Math.sin(phi + (Math.PI / 4))
                        - turn; // frP
        this.frP =
                magnitude(x,y)
                        * Math.sin(phi - (Math.PI / 4))
                        - turn; // brP

        return this;
    }

    public MecanumDrive calculateDirections(double x, double y, double turn) {
        calculateDirectionsRobotCentric(x,y,turn);
        return this;
    }

    public MecanumDrive off() {
        flP = 0;
        blP = 0;
        frP = 0;
        brP = 0;
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        return this;
    }

    public MecanumDrive applyPower() {
        leftFront.setPower(-flP * speed);
        rightFront.setPower(-frP * speed);
        leftBack.setPower(-blP * speed);
        rightBack.setPower(-brP * speed);
        return this;
    }

    public MecanumDrive goFor(double seconds) {
        applyPower();
        sleep(seconds);
        off();
        return this;
    }

    public MecanumDrive goDist(double runningDistance) {
        applyPower();
        sleep(runningDistance * FRICTION_COEF);
        off();
        return this;
    }

    // support the old API style
    public MecanumDrive runFor(double seconds) {return goFor(seconds);}
    public MecanumDrive runDist(double d) {return goDist(d);}

    public MecanumDrive forward() {
        calculateDirections(0, -1, 0);
        applyPower();
        return this;
    }

    public MecanumDrive backward() {
        calculateDirections(0, 1, 0);
        applyPower();
        return this;
    }

    public MecanumDrive left() {
        calculateDirections(-1, 0, 0);
        applyPower();
        return this;
    }

    public MecanumDrive right() {
        calculateDirections(1, 0, 0);
        applyPower();
        return this;
    }

    public MecanumDrive backwardsLeft() {
        calculateDirections(-1, -1, 0);
        applyPower();
        return this;
    }

    public MecanumDrive backwardsRight() {
        calculateDirections(1, -1, 0);
        applyPower();
        return this;
    }

    public MecanumDrive forwardsLeft() {
        calculateDirections(-1, 1, 0);
        applyPower();
        return this;
    }

    public MecanumDrive forwardsRight() {
        calculateDirections(1, 1, 0);
        applyPower();
        return this;
    }

    public MecanumDrive rotateLeft() {
        calculateDirections(0, 0, -1);
        applyPower();
        return this;
    }

    public MecanumDrive rotateRight() {
        calculateDirections(0, 0, 1);
        applyPower();
        return this;
    }

}
