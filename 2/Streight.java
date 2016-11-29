import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

public class Streight {

	private RegulatedMotor R = Motor.B;
	private RegulatedMotor L = Motor.C;

	/**
	 * まっすぐ進む
	 */
	public void Move(){
		R.forward();
		L.forward();
	}
	/**
	 * 与えられた角度だけ右に回転する
	 * @param rad
	 */
	public void Rotation_R(int rad){
		Stop();
		R.setSpeed(500);
		R.resetTachoCount();
		//0.2はずれ調整
		while(R.getTachoCount() < rad * (4 - 0.3)){
			R.forward();
		}
		Stop();
	}

	/**
	 * 与えられた角度だけ左に回転する
	 * @param rad
	 */
	public void Rotation_L(int rad){
		Stop();
		L.setSpeed(600);
		L.resetTachoCount();
		while(L.getTachoCount() <= rad * 4){
			L.forward();
		}
	}

	/**
	 * 移動の停止
	 */
	private void Stop(){
		R.stop(true);
		L.stop();
	}

	/**
	 * 引数で与えられた速度を両モーターに与える
	 * @param speed
	 */
	private void Set_Speed(int speed){
		R.setSpeed(speed);
		L.setSpeed(speed);
	}

	/**
	 * 時間を取得する
	 * @param dis
	 * @param speed
	 * @return
	 */
	public int Get_Time(int dis, int speed){
		Set_Speed(speed);
		return (int) ((dis/10.0) * (500.0/speed) * 375.0);
	}


	/**
	 * ビープ音
	 */
	public void Beep(){
		Sound.beep();
	}

}


