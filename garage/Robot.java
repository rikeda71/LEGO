package garage;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Robot implements Runnable{

	// キャリブレーション用
	public static Calibration rightCalibration;
	public static Calibration leftCalibration;

	// 局所宣言
	private static boolean parkingFlag = false;
	protected static int leftColor, rightColor;
	int rotate90 = 90;
	public static final int speed = 200;
	public static final int lowspeed = 100;
	protected static final int mesureValue = 6; // キャリブレーションの回数

	MotorControllor motor = new MotorControllor();

	public Robot() {
		rightCalibration = new Calibration(mesureValue, HSVLineSearch.rightLight);
		leftCalibration = new Calibration(mesureValue, HSVLineSearch.leftLight);

		/**
		 * キャリブレーションで色を記憶
		 * 0	: 白
		 * 1	: 黒
		 * 2～	: グレー4色(黒→白になるように)
		 * ---キャリブレーションしない---
		 * 青
		 * 赤
		 */
		DoubleExecuteCalibration(rightCalibration, leftCalibration);
		motor.SetSpeed(speed, speed);
	}

	@Override
	public void run() {
		// 赤を判別するまでライントレース
		while( ! Button.ESCAPE.isDown() ) {
			HSVLineTrace(rightColor, leftColor);
			if(leftColor == 7) break;
		}

		// グラデーションの認識
		motor.SetSpeed(speed, speed);
		while( ! Button.ENTER.isDown() ){
			motor.Drive();
			// ライトグレーに入った
			if(leftColor == 5 && rightColor == 5){ parkingFlag = true; }
			// 駐車開始
			else if(parkingFlag && leftColor != 5 && rightColor != 5) break;
		}

		// 駐車して元の位置に戻る
		ParkingAndBack();
		while( ! Button.ENTER.isDown()){
			motor.Drive();
			// 黒&黒(グラデーションを抜けようとしたら)
			if(leftColor == 1 && rightColor == 1) break;
		}

		// ライントレース
		while( ! Button.ESCAPE.isDown() ) {
			HSVLineTrace(rightColor, leftColor);
		}

	}


	/**
	 * 2つ一緒にキャリブレーション
	 * @param a
	 * @param b
	 */
	private int DoubleExecuteCalibration(Calibration a, Calibration b){
		int i;
		LCD.clear();
		LCD.drawString("Please Push Enter", 0, 0);

		for(i = 0;i < mesureValue; i++) {
			while(! Button.ENTER.isDown()){}
			LCD.clear();
			LCD.drawString(""+ i + " ", 0, 0);
			a.GetByOne(i);
			b.GetByOne(i);
			Delay.msDelay(1000);
		}
		LCD.clear();
		LCD.drawString("Complete!!!!", 0, 0);
		Delay.msDelay(1000);
		return 0;
	}

	/**
	 * 駐車とバック
	 */
	private void ParkingAndBack(){
		int tacho; //タコメータ確保
		motor.LeftRotate(rotate90);
		// バックのためにタコメータリセット
		MotorControllor.leftMotor.resetTachoCount();
		// 青線まで
		while( !(leftColor == 6 && rightColor == 6) ){
			motor.Drive();
		}
		tacho = -1 * MotorControllor.leftMotor.getTachoCount();
		MotorControllor.leftMotor.resetTachoCount();
		while( MotorControllor.leftMotor.getTachoCount() > tacho && ! Button.ENTER.isDown()){
			motor.Back();
		}
		motor.RightRotate(rotate90);
		MotorControllor.leftMotor.resetTachoCount();
		while( MotorControllor.leftMotor.getTachoCount() > -1 * rotate90){
			motor.Back();
		}
	}

	/**
	 * HSVを用いたライントレース
	 * @param right
	 * @param left
	 */
	void HSVLineTrace(int right, int left){
		// 黒
		if(right != 0 && left != 0){
			motor.SetSpeed(speed, speed);
		}
		// 右：白　左：黒
		else if(right == 0 && left != 0){
			motor.SetSpeed(lowspeed, speed);
		}
		// 右：黒　左：白
		else if(right != 0 && left == 0){
			motor.SetSpeed(speed, lowspeed);
		}
		// 白
		else if(right == 0 && left == 0){
			motor.SetSpeed(speed, speed);
		}
		motor.Drive();
	}
}
