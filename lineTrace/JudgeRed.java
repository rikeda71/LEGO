package lineTrace;


public class JudgeRed implements Runnable{

	// 基準値
	private static float criteriaR = 0.15F;
	private static float criteriaGB = 0.12F;
	//private static ColorSensor leftLight = new ColorSensor(SensorPort.S3);

	private static boolean redState = false;

	@Override
	public void run() {
		while(DoubleLineTrace.redJudgeFlag){
			if(DoubleLineTrace.leftLight.getRed() >= criteriaR && DoubleLineTrace.leftLight.getGreen() < criteriaGB &&	DoubleLineTrace.leftLight.getBlue() < criteriaGB){
				redState = true;
			}
			else {
				redState = false;
			}
		}
	}

	public static boolean GetRedState(){
		return redState;
	}
}
