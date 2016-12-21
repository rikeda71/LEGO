package cleanRobot;

import lejos.hardware.Button;
import lejos.utility.Delay;

public class Time implements Runnable{
	private static int timer = 0;

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		while(! Button.ENTER.isDown() && timer < 20000){
			Delay.msDelay(50);
			timer += 50;
		}
	}

	public static int getTime() {
		return timer;
	}

	public static void setTime(int t){
		timer = t;
	}

}
