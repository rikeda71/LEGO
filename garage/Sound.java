package garage;

import lejos.hardware.Button;

public class Sound implements Runnable{

	private static boolean p1 = false, p2 = false, p3 = false, p4 = false;   // 発音の制御

	@Override
	public void run() {
		while(! Button.ESCAPE.isDown() ) {	ToneSound(Robot.leftColor, Robot.rightColor); }
	}

	/**
	 * 階調に合わせて音を鳴らす
	 * @param right
	 * @param left
	 */
	void ToneSound(int right, int left){
		int hz = 0;
		if(right != left) { return; }
		switch(right){
		case 2:
			if ( !p1 ) {
				hz = 261;
				p1 = true;
			}
			break;
		case 3:
			if ( !p2 ) {
				hz = 329;
				p2 = true;
			}

			break;
		case 4:
			if ( !p3 ) {
				hz = 391;
				p3 = true;
			}
			break;
		case 5:
			if ( !p4 ) {
				hz = 493;
				p1 = false;
				p2 = false;
				p3 = false;
				p4 = true;
			}
			break;
		default:
			return;
		}
		if ( hz != 0 ) {
			lejos.hardware.Sound.playTone(hz, 100);
		}
	}
}
