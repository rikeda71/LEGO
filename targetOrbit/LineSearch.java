package targetOrbit;

import lejos.hardware.Button;


public class LineSearch implements Runnable{
	private static float middleValue = 0.3F;
	private static int state = 0; //ロボットの下の状態

	private void LineState()
	{
		// 黒 && 黒
		if( Robot.leftLight.getLight() < middleValue && Robot.rightLight.getLight() < middleValue){
			state = 0;
		}
		// 黒＆白
		else if( Robot.leftLight.getLight() < middleValue && Robot.rightLight.getLight() >= middleValue){
			state = 1;
		}
		// 白＆黒
		else if( Robot.leftLight.getLight() >= middleValue && Robot.rightLight.getLight() < middleValue){
			state = 2;
		}
		// 白＆白
		else if( Robot.leftLight.getLight() >= middleValue && Robot.rightLight.getLight() >= middleValue){
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
}
