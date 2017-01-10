package lineTrace;

import lejos.hardware.Sound;
import lejos.utility.Delay;



public class JudgeGreen implements Runnable{

	// 基準値
	private static float criteriaG = 0.15F;
	private static float criteriaRB = 0.13F;
	// 音を鳴らした回数
	//static int beepCount = 0;

	@Override
	public void run() {
		while(DoubleLineTrace.greenJudgeFlag){
			if(DoubleLineTrace.leftLight.getRed() < criteriaRB &&
					DoubleLineTrace.leftLight.getGreen() >= criteriaG &&
					DoubleLineTrace.leftLight.getBlue() < criteriaRB &&
					DoubleLineTrace.leftLight.getGreen() > DoubleLineTrace.leftLight.getRed() &&
					DoubleLineTrace.leftLight.getGreen() > DoubleLineTrace.leftLight.getBlue()){
				Sound.beep();
			}
			Delay.msDelay(50);
		}
	}
}
