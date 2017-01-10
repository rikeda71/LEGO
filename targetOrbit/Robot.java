package targetOrbit;

import jp.ac.kagawa_u.infoexpr.Sensor.ColorSensor;
import jp.ac.kagawa_u.infoexpr.Sensor.UltrasonicSensor;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Stopwatch;

public class Robot implements Runnable{

	// タイヤ
	static RegulatedMotor rightMotor = Motor.B;
	static RegulatedMotor leftMotor = Motor.C;

	// 光センサー
	static public ColorSensor rightLight = new ColorSensor(SensorPort.S2);
	static public ColorSensor leftLight = new ColorSensor(SensorPort.S3);

	// 反響センサー
	static public UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S4);

	static int lowspeed = 250;
	static int highspeed = 400;

	static int count = 0; //

	private static Stopwatch stopwatch = new Stopwatch();
	private static int time;

	private void SetSpeed(int left, int right){
		leftMotor.setSpeed(left);
		rightMotor.setSpeed(right);
	}

	private void drive(){
		leftMotor.forward();
		rightMotor.forward();
	}

	private void Stop(){
		leftMotor.stop(true);
		rightMotor.stop();
	}

	public Robot() {
		// TODO 自動生成されたコンストラクター・スタブ
		Thread linesearch = new Thread(new LineSearch());
		Thread sonicsearch = new Thread(new SonicSearch());
		linesearch.start();
		sonicsearch.start();
	}

	@Override
	public void run() {
		// 障害物を発見
		while( ! Button.ESCAPE.isDown() && SonicSearch.GetDistance() > 0.2) {
			switch(LineSearch.GetState()){
			case 0:
				SetSpeed(highspeed,highspeed);
				break;
			case 1:
				SetSpeed(lowspeed,highspeed);
				break;
			case 2:
				SetSpeed(highspeed,lowspeed);
				break;
			case 3:
				SetSpeed(highspeed,highspeed);
				break;
			}
			drive();
		}

		// 回転に誤差をつける
		SetSpeed(lowspeed,highspeed);
		drive();

		// 障害物を一回りする
		Sound.beep();
		SetSpeed(lowspeed,highspeed);
		while(! Button.ESCAPE.isDown() && LineSearch.GetState() >= 2){
			drive();
		}

		// コースに戻る
		SetSpeed(lowspeed,highspeed);
		while(! Button.ESCAPE.isDown() && LineSearch.GetState() != 3 ){
			drive();
		}

		// ライントレース
		while( ! Button.ESCAPE.isDown()) {
			switch(LineSearch.GetState()){
			case 0:
				SetSpeed(highspeed,highspeed);
				break;
			case 1:
				SetSpeed(lowspeed,highspeed);
				break;
			case 2:
				SetSpeed(highspeed,lowspeed);
				break;
			case 3:
				SetSpeed(highspeed,highspeed);
				break;
			}
			drive();
		}
	}

}


