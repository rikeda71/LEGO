package garage;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

public class MotorControllor {
	// モーター
	public static RegulatedMotor leftMotor  = Motor.C;
	public static RegulatedMotor rightMotor  = Motor.B;

	/**
	 * 前進
	 */
	protected void Drive(){
		leftMotor.forward();
		rightMotor.forward();
	}

	/**
	 * 後退
	 */
	protected void Back(){
		leftMotor.backward();
		rightMotor.backward();
	}

	protected void Stop(){
		leftMotor.stop(true);
		rightMotor.stop();
	}

	/**
	 * スピードの設定
	 * @param right
	 * @param left
	 */
	protected void SetSpeed(int left, int right){
		leftMotor.setSpeed(left);
		rightMotor.setSpeed(right);
	}

	/**
	 * 与えられた角度だけ回転
	 * @param rad
	 */
	protected void RightRotate(int rad){
		// 停止
		leftMotor.stop(true);
		rightMotor.stop();

		// 回転角リセット
		leftMotor.resetTachoCount();

		// 一回転
		while(leftMotor.getTachoCount() < rad * 2){
			leftMotor.forward();
			rightMotor.backward();
		}
		// 停止
		leftMotor.stop(true);
		rightMotor.stop();
	}

	/**
	 * 与えられた角度だけ回転
	 * @param rad
	 */
	protected void LeftRotate(int rad){
		// 停止
		leftMotor.stop(true);
		rightMotor.stop();

		// 回転角リセット
		rightMotor.resetTachoCount();

		// 一回転
		while(rightMotor.getTachoCount() < rad * 2){
			leftMotor.backward();
			rightMotor.forward();
		}
		// 停止
		leftMotor.stop(true);
		rightMotor.stop();
	}

}
