package targetOrbit;

import jp.ac.kagawa_u.infoexpr.Sensor.TouchSensor;
import lejos.hardware.port.SensorPort;

public class TargetOrbit {
	static TouchSensor touch = new TouchSensor(SensorPort.S1);
	public static void main(String[] args) {
		Thread robot = new Thread(new Robot());
		Thread linesearch = new Thread(new LineSearch());
		Thread sonicsearch = new Thread(new SonicSearch());
		// ボタンが押されるまで待機
		while (! touch.isPressed()){}

		robot.start();
		linesearch.start();
		sonicsearch.start();
		try {
			robot.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
