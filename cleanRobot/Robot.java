package cleanRobot;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

public class Robot implements Runnable{
	static RegulatedMotor rightMotor  = Motor.B;
	static RegulatedMotor leftMotor  = Motor.C;

	// 早いスピード、遅いスピード
	//private static int lowSpeed = 330;
	private static int highSpeed = 800;

	// 回転角度
	private static final int rotate90 = 90;
	private static final int rotate45 = 45;
	private static final int rotate135 = 135;
	private static final int rotate225 = 225;

	private JudgeLine judge;

	public Robot() {
		judge = new JudgeLine();
		SetSpeed(highSpeed, highSpeed);
	}

	/**
	 * 与えられた角度だけ回転
	 * @param rad
	 */
	private void RightRotate(int rad){
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
	private void LeftRotate(int rad){
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

	/**
	 * 走行
	 * @param speed
	 */
	private void Forward(){
		rightMotor.forward();
		leftMotor.forward();
	}

	/**
	 * スピードの設定
	 * @param right
	 * @param left
	 */
	private void SetSpeed(int right, int left){
		rightMotor.setSpeed(right);
		leftMotor.setSpeed(left);
	}


	@Override
	public void run() {
		// ボタンが押されるまで掃出
		while (! Button.ENTER.isDown()){
			switch (judge.Judge()){
			case 0:
				Forward();
				break;
			case 1:
				RightRotate(rotate135);
				break;
			case 2:
				RightRotate(rotate45);
				break;
			case 3:
				LeftRotate(rotate45);
				break;
			}
		}
	}


	/*
	@Override
	public void run() {
		while(! Button.ENTER.isDown()){
			LCD.drawString(Double.toString(judge.leftLight.getLight()), 0, 1);
			LCD.drawString(Double.toString(judge.rightLight.getLight()), 0, 1);
			LCD.clear();
		}

	}
	*/
}
