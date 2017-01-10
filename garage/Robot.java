package garage;

import jp.ac.kagawa_u.infoexpr.Sensor.ColorSensor;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Robot implements Runnable{

	public static RegulatedMotor rightMotor  = Motor.B;
	public static RegulatedMotor leftMotor  = Motor.C;
	public static ColorSensor rightLight = new ColorSensor(SensorPort.S2);
	public static ColorSensor leftLight = new ColorSensor(SensorPort.S3);
	private static final int speed = 200;
	private static final int lowspeed = 100;
	private static final int mesureValue = 7; // キャリブレーションの回数
	Calibration rightCalibration;
	Calibration leftCalibration;
	private static boolean parkingFlag = false;
	float array[];
	int rightColor,leftColor;
	int rotate90 = 90;

	public Robot() {
		rightCalibration = new Calibration(mesureValue, rightLight);
		leftCalibration = new Calibration(mesureValue, leftLight);
		// キャリブレーションで色を記憶
		/**
		 * 白
		 * 黒
		 * グレー4色(黒→白になるように)
		 * 青
		 */
		/*
		LCD.drawString("==Right Calibration==", 0, 0);
		rightCalibration.executeCalibration();
		LCD.clear();
		LCD.drawString("==Leftt Calibration==", 0, 0);
		leftCalibration.executeCalibration();
		 */
		DoubleExecuteCalibration(rightCalibration, leftCalibration);
		SetSpeed(speed, speed);
	}

	@Override
	public void run() {
		while( ! Button.ENTER.isDown() ){
			leftColor = colorDecision(leftLight.getHSV(), leftCalibration);
			rightColor = colorDecision(rightLight.getHSV(), rightCalibration);
			LCD.drawString("left" + leftColor, 0, 0);
			LCD.drawString("right" + rightColor, 0, 1);
			//LCD.drawString("Pless Escape!", 0, 2);
			//while( ! Button.ESCAPE.isDown()){}

			LCD.clear();
			leftMotor.forward();
			rightMotor.forward();
			// ライトグレーに入った
			if(leftColor == 5 && rightColor == 5){ parkingFlag = true; }

			// 駐車開始
			if(parkingFlag && leftColor != 5 && rightColor != 5){
				break;
			}
		}

		LCD.drawString("parking start!", 0, 0);
		ParkingAndBack();


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

	/**
	 * キャリブレーションの値を元に色の判断
	 * @param float[] 読み取ったHSV
	 * @param Calibration キャリブレーション済みのクラス
	 * @return String 色
	 **/
	private static int colorDecision(float[] sensorValue, Calibration calibration) {
		// 一番誤差の小さいところを返すための変数
		float minerror = 300; // 最小の合計誤差
		float nowerror; // 今の色の合計誤差
		int minNum = -1; // 今の色の候補の番号
		float whiteDecision = 0.3F; //白判定
		int blueDecision = 150; //青判定
		float calibrationData[];

		if(sensorValue[0] > blueDecision) { return 6;} // 色相が高いなら青!
		if(sensorValue[2] > whiteDecision) { return 0;} // 明度が高いなら白!

		// 青と白を除いて判定
		for(int i = 1;i < mesureValue-1;i++){
			nowerror = 0; // 初期化
			calibrationData = calibration.getCalibData(i); // キャリブレーションした値を格納
			for(int j=1;j<3;j++){
				nowerror += Math.abs( sensorValue[j] - calibrationData[j] );
			}
			if(minerror > nowerror) {
				minerror = nowerror;
				minNum = i;
			}
		}
		return minNum;
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
		//LCD.drawString("Complete!!!!", 0, 0);
		Delay.msDelay(1000);
		return 0;
	}

	/**
	 * 走行のずれ修正
	 * @param left
	 * @param right
	 */
	private void CorrectGap(int left, int right){

	}

	/**
	 * 駐車とバック
	 */
	private void ParkingAndBack(){
		int tacho; //タコメータ確保
		LeftRotate(rotate90);
		leftColor = colorDecision(leftLight.getHSV(), leftCalibration);
		rightColor = colorDecision(rightLight.getHSV(), rightCalibration);
		// バックのためにタコメータリセット
		leftMotor.resetTachoCount();
		// 青線まで
		while( !(leftColor == 6 && rightColor == 6) ){
			leftColor = colorDecision(leftLight.getHSV(), leftCalibration);
			rightColor = colorDecision(rightLight.getHSV(), rightCalibration);
			leftMotor.forward();
			rightMotor.forward();
		}
		tacho = -leftMotor.getTachoCount();
		leftMotor.resetTachoCount();
		while( leftMotor.getTachoCount() > tacho && ! Button.ENTER.isDown()){
			leftMotor.backward();
			rightMotor.backward();
		}
		RightRotate(rotate90);
	}

}
