package cleanRobot;

import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

public class Robot implements Runnable{
	static RegulatedMotor rightMotor  = Motor.B;
	static RegulatedMotor leftMotor  = Motor.C;

	Thread timer = new Thread(new Time());
	private static final int setTime = 0;
	private int count = 0;

	// 早いスピード、遅いスピード
	private static int lowSpeed = 600;      // 300 →　600　→　５００（失敗？）
	private static int highSpeed = 1000;    // 800
	// RightRotate, LeftRotate の遅らせる時間
	private static final int timeStop = 400;

	// 回転角度
	private static final int rotate45 = 45;
	private static final int rotate90 = 90;

	private static final int width = 30; //振れ幅
	private static final int round = 600; //一回転のためのタイヤの角度

	private JudgeLine judge;

	public Robot() {
		judge = new JudgeLine();
		SetSpeed(lowSpeed, highSpeed);   // highSpeed highSpeed
	}

	/**
	 * 与えられた角度だけ回転
	 * @param rad
	 */
	private void RightRotate(int rad){
		// 停止
		leftMotor.stop(true);
		rightMotor.stop();

		Time.setTime(setTime);
		while(Time.getTime() < timeStop && ! Button.ENTER.isDown()){
			Backward();
		}

		RoundRotate();

		// 回転角リセット
		leftMotor.resetTachoCount();
		SetSpeed(lowSpeed, lowSpeed); // lowSpeed lowSpeed
		// 指定角度だけ回転
		while(leftMotor.getTachoCount() < rad){
			leftMotor.forward();
			rightMotor.backward();
		}
		SetSpeed(lowSpeed, highSpeed);   // highSpeed low

	}

	/**
	 * 与えられた角度だけ回転
	 * @param rad
	 */
	private void LeftRotate(int rad){
		// 停止
		leftMotor.stop(true);
		rightMotor.stop();

		Time.setTime(setTime);
		while(Time.getTime() < timeStop && ! Button.ENTER.isDown()){
			Backward();
		}

		RoundRotate();

		// 回転角リセット
		rightMotor.resetTachoCount();
		SetSpeed(lowSpeed, lowSpeed);
		// 指定角度だけ回転
		while(rightMotor.getTachoCount() < rad){
			leftMotor.backward();
			rightMotor.forward();
		}
		SetSpeed(highSpeed, lowSpeed);   // lowSpeed highSpeed

	}

	/**
	 * 360度回転
	 */
	private void RoundRotate(){
		// 回転角リセット
		rightMotor.resetTachoCount();
		SetSpeed(highSpeed, highSpeed);
		// 一回転
		while(rightMotor.getTachoCount() < round){
			leftMotor.backward();
			rightMotor.forward();
		}
	}

	/**
	 * 走行
	 */
	private void Forward(){
		rightMotor.forward();
		leftMotor.forward();
	}

	/**
	 * バック
	 */
	private void Backward(){
		rightMotor.backward();
		leftMotor.backward();
	}


	private int RandRad(int a, int b){
		Random r = new Random();
		return r.nextInt(a)+b;   // b～a+bの乱数
	}

	/**
	 * スピードの設定
	 * @param right
	 * @param left
	 */
	private void SetSpeed(int left, int right){
		rightMotor.setSpeed(right);
		leftMotor.setSpeed(left);
	}


	@Override
	public void run() {
		timer.start();

		// ボタンが押されるまで掃出
		while (! Button.ENTER.isDown()){
			switch (judge.Judge()){
			case 0:
				Forward();
				break;
			case 1:
				// RoundRotate();
				if ( count%2 == 0) {
					LeftRotate(RandRad(width, rotate90));
				} else {
					RightRotate(RandRad(width, rotate90));
				}
				count++;
				break;
			case 2:
				RightRotate(RandRad(width, rotate45));
				break;
			case 3:
				LeftRotate(RandRad(width, rotate45));
				break;
			}
		}
		try {
			timer.join();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
