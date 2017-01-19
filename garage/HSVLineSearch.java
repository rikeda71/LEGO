package garage;

import jp.ac.kagawa_u.infoexpr.Sensor.ColorSensor;
import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;

public class HSVLineSearch implements Runnable{

	// 光センサー
	public static ColorSensor rightLight = new ColorSensor(SensorPort.S2);
	public static ColorSensor leftLight = new ColorSensor(SensorPort.S3);

	@Override
	public void run() {
		while(! Button.ESCAPE.isDown() ) {
			Robot.leftColor = colorDecision(leftLight.getHSV(), Robot.leftCalibration);
			Robot.rightColor = colorDecision(rightLight.getHSV(), Robot.rightCalibration);
		}
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
		int redDecision = 30;
		int blueDecision = 150; //青判定
		float calibrationData[];

		if(sensorValue[0] < redDecision) { return 7; }
		else if(sensorValue[0] > blueDecision) { return 6;} // 色相が高いなら青!
		else if(sensorValue[2] > whiteDecision) { return 0;} // 明度が高いなら白!

		// 青と白を除いて判定
		for(int i = 1;i < Robot.mesureValue;i++){
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
}
