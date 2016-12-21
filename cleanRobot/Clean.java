package cleanRobot;

import jp.ac.kagawa_u.infoexpr.Sensor.TouchSensor;
import lejos.hardware.port.SensorPort;

public class Clean {

	static TouchSensor touch = new TouchSensor(SensorPort.S1);

	public static void main(String[] args) {
		Thread robot = new Thread(new Robot());
		// ボタンが押されるまで待機
		while (! touch.isPressed()){}
		robot.start();
		try {
			robot.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
