package targetOrbit;

import lejos.hardware.Button;
import lejos.hardware.Sound;

public class Robot implements Runnable{

	// モーター(タイヤ)
	MotorControllor motor = new MotorControllor();

	// 速度
	private static int lowspeed = 270;
	private static int highspeed = 500;

	private static final int rotate90 = 90;

	@Override
	public void run() {
		// 障害物を発見するまで
		while( ! Button.ESCAPE.isDown() && ! SonicSearch.ObjectDetction()) {
			LineTrace();
		}

		motor.SetSpeed(lowspeed, highspeed);
		motor.Drive();

		// 障害物を一回りする
		Sound.beep();
		motor.SetSpeed(lowspeed,highspeed);
		while(! Button.ESCAPE.isDown() && LineSearch.GetState() >= 2){
			motor.Drive();
		}

		// コースに戻る
		motor.SetSpeed(lowspeed,highspeed);
		while(! Button.ESCAPE.isDown() && LineSearch.GetState() != 3 ){
			motor.Drive();
		}

		// ライントレース
		while( ! Button.ESCAPE.isDown()) {
			LineTrace();
		}
	}

	private void LineTrace(){
		switch(LineSearch.GetState()){
		// 黒
		case 0:
			motor.SetSpeed(highspeed,highspeed);
			break;
		// 左：黒 右；白
		case 1:
			motor.SetSpeed(lowspeed,highspeed);
			break;
		// 左：白 右：黒
		case 2:
			motor.SetSpeed(highspeed,lowspeed);
			break;
		// 白
		case 3:
			motor.SetSpeed(highspeed,highspeed);
			break;
		}
		motor.Drive();
	}
}


