package periphery;

import jp.ac.kagawa_u.infoexpr.Sensor.ColorSensor;
import jp.ac.kagawa_u.infoexpr.Sensor.TouchSensor;
import jp.ac.kagawa_u.infoexpr.Sensor.UltrasonicSensor;
import lejos.hardware.port.SensorPort;

public class SensorControllor implements Runnable{

	public static TouchSensor touchSensor = new TouchSensor(SensorPort.S1);
	public static ColorSensor leftLight = new ColorSensor(SensorPort.S3);
	public static ColorSensor rightLight = new ColorSensor(SensorPort.S2);
	public static UltrasonicSensor sonicSensor = new UltrasonicSensor(SensorPort.S4);

	private static boolean endFlag = false;
	/**
	 * 物体と距離を返す
	 * @return
	 */
	public static float getDistance(){
		return sonicSensor.getDistance();
	}

	/**
	 * 物体と任意の距離であるか判定
	 * @param distance
	 * @return
	 */
	public static boolean ObjectDetction(double distance){
		float nowDistance = getDistance();
		if(nowDistance > 0.01 && nowDistance < distance) { return true; }  // 0.05 ←11階は反応せず
		else { return false; }
	}

	/**
	 * キャリブレーションの値を元に色の判断
	 * @param float[] 読み取ったHSV
	 * @param Calibration キャリブレーション済みのクラス
	 * @return String 色
	 **/
	public static float getBright(float[] sensorValue, Calibration calibration){

		float whiteDecision = 0.1F; //白判定
		float rightGrayDecision = 0;
		int blueDecision = 180; //青判定
		float lightgray = 0.002F;   // 0.005
		float calibrationData[] = calibrationData = calibration.getCalibData(0); // キャリブレーションした値を格納;

		if(sensorValue[0] > blueDecision) { return 3;} // 色相が高いなら青!
		//↓↓後で調整
		else if(Math.abs(sensorValue[2] - calibrationData[2]) < lightgray) { return 2; } // ライトグレー
		else if(sensorValue[2] > whiteDecision) { return 1;} // 明度が高いなら白
		return 0; // それ以外は黒

	}

	/**
	 * タッチセンサーで終了するためのメソッド
	 */
	public static void SetEndFlag(){
		endFlag = true;
	}


	public static boolean getTouch(){
		return touchSensor.isPressed();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(50); //Thread.sleep(100); ライントレースをできるだけ早く開始できるように
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while(! getTouch() || ! endFlag) {         // 最後にタッチセンサーが反応しないのここが原因？
			RunOutCource.leftColor = getBright(leftLight.getHSV(), RunOutCource.leftCalibration);
			RunOutCource.rightColor = getBright(rightLight.getHSV(), RunOutCource.rightCalibration);

			/*
			LCD.clear();
			LCD.drawString("left:" + RunOutCource.leftColor, 0, 2);
			LCD.drawString("right:" + RunOutCource.rightColor, 0, 3);
			*/
		}
	}
}
