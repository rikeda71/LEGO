package targetOrbit;

import jp.ac.kagawa_u.infoexpr.Sensor.UltrasonicSensor;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;

public class SonicSearch implements Runnable {

	// 反響センサー
	static public UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S4);

	private static float distance;

	public static float GetDistance(){
		return distance;
	}

	// 物体検知
	public static boolean ObjectDetction(){
		if(distance > 0.05 && distance < 0.21) { return true; }
		else { return false; }
	}

	@Override
	public void run() {
		while( ! Button.ESCAPE.isDown() ) {
			distance = sonic.getDistance();
			LCD.clear();
			LCD.drawString(Float.toString(distance), 0, 0);
		}
	}

}
