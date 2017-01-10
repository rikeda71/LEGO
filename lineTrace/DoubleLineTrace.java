package lineTrace;

import jp.ac.kagawa_u.infoexpr.Sensor.ColorSensor;
import jp.ac.kagawa_u.infoexpr.Sensor.TouchSensor;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;

public class DoubleLineTrace {

	// モーター・センサーの定義
	static TouchSensor touch = new TouchSensor(SensorPort.S1);
	public static ColorSensor rightLight = new ColorSensor(SensorPort.S2);
	public static ColorSensor leftLight = new ColorSensor(SensorPort.S3);
	static RegulatedMotor rightMotor  = Motor.B;
	static RegulatedMotor leftMotor  = Motor.C;

	// 早いスピード、遅いスピード
	private static int lowSpeed = 330;
	private static int highSpeed = 700;

	static int streightSpeed = 800; //直線時のスピード
	static int rotateSpeed = 700; //回転時のスピード
	static float middleValue = 0.40F; //色判定

	// 回転角度
	static int rotateRange = 580;

	// スレッド用のフラグ
	public static boolean redJudgeFlag = true;
	public static boolean greenJudgeFlag = true;

	public static void main(String[] args) {

		// 赤・緑パネル判定スレッドを定義
		Thread jR = new Thread(new JudgeRed());
		Thread jG = new Thread(new JudgeGreen());

		// ボタンを押してスタート
		while(!Button.ENTER.isDown());

		//motorSetSpeed(streightSpeed, streightSpeed);
		// 赤パネル判定スタート
		jR.start();


		/*while(! touch.isPressed()){
			LCD.drawString("R" + leftLight.getRed(), 0, 0);
			LCD.drawString("G" + leftLight.getGreen(), 0, 1);
			LCD.drawString("B " + leftLight.getBlue(), 0, 2);
			LCD.refresh();
		}*/


		//直線
		while(! JudgeRed.GetRedState()/* && ! touch.isPressed()*/){
			RunLine();
		}

		redJudgeFlag = false;

		try {
			jR.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 回転
		Rotate();

		jG.start();

		// ライントレース
		while( ! touch.isPressed() ){
			RunLine();
		}

		greenJudgeFlag = false;

		try {
			jG.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * スピードを設定
	 * @param leftMotorSpeed
	 * @param rightMotorSpeed
	 */
	private static void motorSetSpeed(int leftMotorSpeed, int rightMotorSpeed)
	{
		leftMotor.setSpeed(leftMotorSpeed);
		rightMotor.setSpeed(rightMotorSpeed);
	}

	/**
	 * 走行
	 */
	private static void motorForward()
	{
		leftMotor.forward();
		rightMotor.forward();
	}

	/**
	 * 回転
	 */
	private static void Rotate()
	{
		// 停止
		leftMotor.stop(true);
		rightMotor.stop();

		// 回転角リセット
		leftMotor.resetTachoCount();

		leftMotor.setSpeed(rotateSpeed);
		rightMotor.setSpeed(rotateSpeed);

		// 一回転
		while(leftMotor.getTachoCount() < rotateRange){
			leftMotor.forward();
			rightMotor.backward();
		}

	}

	/**
	 * ライントレース走行
	 */
	private static void RunLine()
	{
		// 黒＆黒
		if( leftLight.getLight() < middleValue && rightLight.getLight() < middleValue){	}
		// 黒＆白
		else if( leftLight.getLight() < middleValue && rightLight.getLight() >= middleValue){
			motorSetSpeed(lowSpeed, highSpeed);
			motorForward();
		}
		// 白＆黒
		else if( leftLight.getLight() >= middleValue && rightLight.getLight() < middleValue){
			motorSetSpeed(highSpeed, lowSpeed);
			motorForward();
		}
		// 白＆白
		else if( leftLight.getLight() >= middleValue && rightLight.getLight() >= middleValue){
			motorSetSpeed(highSpeed, highSpeed);
			motorForward();
		}
	}

}
