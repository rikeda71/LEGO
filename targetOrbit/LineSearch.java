package targetOrbit;

import jp.ac.kagawa_u.infoexpr.Sensor.ColorSensor;
import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;

public class LineSearch implements Runnable{

	// 光センサー
	static public ColorSensor rightLight = new ColorSensor(SensorPort.S2);
	static public ColorSensor leftLight = new ColorSensor(SensorPort.S3);

	private static float middleValue = 0.3F;
	private static int state = 0; //ロボットの下の状態

	private void LineState()
	{
		// 黒 && 黒
		if( leftLight.getLight() < middleValue && rightLight.getLight() < middleValue){
			state = 0;
		}
		// 黒＆白
		else if( leftLight.getLight() < middleValue && rightLight.getLight() >= middleValue){
			state = 1;
		}
		// 白＆黒
		else if( leftLight.getLight() >= middleValue && rightLight.getLight() < middleValue){
			state = 2;
		}
		// 白＆白
		else if( leftLight.getLight() >= middleValue && rightLight.getLight() >= middleValue){
			state = 3;
		}
	}

	public static int GetState(){
		return state;
	}

	@Override
	public void run() {
		while( ! Button.ESCAPE.isDown() ) {
			LineState();
		}
	}

	public static void sleep(int ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
