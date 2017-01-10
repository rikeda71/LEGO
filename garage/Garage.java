package garage;

import jp.ac.kagawa_u.infoexpr.Sensor.TouchSensor;
import lejos.hardware.port.SensorPort;

public class Garage {

	static TouchSensor touch = new TouchSensor(SensorPort.S1);

	public static void main(String[] args) {
		Thread robot = new Thread(new Robot());
		// ボタンが押されるまで待機
		while (! touch.isPressed()){}
		robot.start();
		try {
			robot.join();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}

