package targetOrbit;

import lejos.hardware.Button;

public class SonicSearch implements Runnable {

	private static float distance;

	public static float GetDistance(){
		return distance;
	}

	@Override
	public void run() {
		while( ! Button.ESCAPE.isDown() ) {
			distance = Robot.sonic.getDistance();
		}
	}

}
