package cleanRobot;

import jp.ac.kagawa_u.infoexpr.Sensor.ColorSensor;
import lejos.hardware.port.SensorPort;

public class JudgeLine{
	public static ColorSensor rightLight = new ColorSensor(SensorPort.S2);
	public static ColorSensor leftLight = new ColorSensor(SensorPort.S3);
	private static final float middleValue = 0.4F;

	public int Judge(){
		//黒&黒
		if(rightLight.getLight() < middleValue && leftLight.getLight() < middleValue){
			return 1;
		}
		//黒&白
		else if(leftLight.getLight() < middleValue && rightLight.getLight() >= middleValue){
			return 2;
		}
		//白&黒
		else if(leftLight.getLight() >= middleValue && rightLight.getLight() < middleValue){
			return 3;
		}
		//白&白
		else {
			return 0;
		}
	}

}
