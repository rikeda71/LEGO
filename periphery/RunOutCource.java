/**
 * 結果
 * 1: 47.53
 * 2: ボタンスイッチで止まってしまう
 * 3: ↑と同じ
 * 4: パーキングに入れない パーキングが終了した後、パーキングメソッドで一定時間まっすぐ走るようにした
 * 5: 目標周回後ミス
 * 6: 47.58 目標周回でコースに復帰したときすぐにライントレースで秒数を調整した
 * 7: 43.02 青線認識を片方だけ認識してもOKにした グレーの紙の貼り付け部分で青と認識してしまった。
 * 8: ↑を元に戻した。青の閾値を厳しくした 黒をライトグレーとみてしまった
 * 9: 両方とも黒を認識するか、10秒を超えたらパーキングの準備をするように変更した 作戦は成功したが、紙に引っかかってしまったためもう1回
 * 10:ショートカット内のライントレースがいらないと判断したが、黒黒の条件に入り、パーキングしてしまった。
 * 11:44.73 ↑のショートカット内のライントレースを復活させたらうまくいく
 * 12:目標周回後の直線で脱線
 * 13:ショートカットが終わった後に黒黒検知してしまう。黒黒検知の判定をかなり先延ばしにし、次に望む
 * 14:やはり失敗 ソースのミスがあったので修正
 * 15:ショートカットのオブジェクトがずれていたためやり直し
 * 16:46.41　ラップタイムはt1：18.47　t2：25.94
 * 17:オブジェクトがずれてショートカット失敗　しかし、ほかのところは動いたため制度は向上している
 * 18:
 */


package periphery;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Stopwatch;

public class RunOutCource implements Runnable{

	public static Calibration leftCalibration;
	public static Calibration rightCalibration;

	private static final int rotate90 = 85;
	private static final int highspeed1 = 800;    // 直線走行用のスピード  600
	private static final int lowspeed1 = 600;     // 直線走行用のスピード  450
	private static final int highspeed2 = 600;    // 曲線走行用のスピード 600→9回だったら成功した
	private static final int lowspeed2 = 400;     // 曲線走行用のスピード 400→9階だったら成功した
	private static final double distance = 0.35;  // 0.25 0.30
	private static boolean rightBlack = false;
	private static boolean leftBlack = false;


	public static float leftColor, rightColor;
	MotorControllor motor = new MotorControllor();
	Stopwatch stopwatch = new Stopwatch();

	Thread sensor;

	public RunOutCource(){
		Calibration();
		sensor = new Thread(new SensorControllor());
	}

	@Override
	public void run() {
		while(! SensorControllor.getTouch());

		//センサー開始
		sensor.start();

		LCD.clear();
		LCD.drawString("LineTrace", 0, 0);
		stopwatch.reset();

		while(! Button.ESCAPE.isDown()) {
			LineTrace1();
			motor.Foward();
			if ( stopwatch.elapsed() > 3000 ) break; //3000 → 2800 → 3000 たまーに反応できないことが。曲がり開始で反応できるように
		}

		// 障害物を発見するまで
		while(! Button.ESCAPE.isDown() ) {
			LineTrace2();
			motor.Foward();
			if(SensorControllor.ObjectDetction(distance)) break;
		}

		// 障害物を一周
		TargetOrbit();

		//  直線走行
		stopwatch.reset();
		while(! Button.ESCAPE.isDown()) {
			LineTrace1();
			motor.Foward();
			if ( stopwatch.elapsed() > 2500 ) break;
		}

		// 障害物を発見するまで
		while(! Button.ESCAPE.isDown()){
			LineTrace2();
			motor.Foward();
			if(SensorControllor.ObjectDetction(0.25)) break;
		}

		LCD.clear();
		LCD.drawString("ShortCutStart!", 0, 0);

		// ショートカット
		ShortCut();

		// ライトグレー誤検知対策
		LCD.clear();
		LCD.drawString("Run 7 seconds!", 0, 0);

		stopwatch.reset();
		while (! Button.ESCAPE.isDown()) {
			LineTrace2();
			motor.Foward();
			if ( stopwatch.elapsed() > 7000 ) break;
		}

		LCD.clear();
		LCD.drawString("Run 7 seconds End!", 0, 0);

		// 黒＆黒になるまで走行
		while (!(rightBlack && leftBlack) && ! Button.ESCAPE.isDown() ) {
			if ( rightColor == 0 ) rightBlack = true;
			if ( leftColor == 0 ) leftBlack = true;
			LineTrace2();
			motor.Foward();
		}

		LCD.clear();
		LCD.drawString("Gradation Course!", 0, 0);

		Parking();

		LCD.clear();
		LCD.drawString("Last", 0, 0);
		SensorControllor.SetEndFlag();
		while(! SensorControllor.getTouch()){   // 1月29日現在タッチセンサーが反応しない。おそらくSensorColor.java のrun メソッドが邪魔しているかもor タッチセンサの反応が良すぎるだけ？
			LineTrace2();
			motor.Foward();
		}

	}

	private void LineTrace1(){
		// 黒
		if(rightColor == 0 && leftColor == 0){
			motor.SetSpeed(highspeed1, highspeed1);
		}
		// 右：白　左：黒
		else if(rightColor != 0 && leftColor == 0){
			motor.SetSpeed(lowspeed1, highspeed1);
		}
		// 右：黒　左：白
		else if(rightColor == 0 && leftColor != 0){
			motor.SetSpeed(highspeed1, lowspeed1);
		}
		// 白
		else if(rightColor != 0 && leftColor != 0){
			motor.SetSpeed(highspeed1, highspeed1);
		}
	}

	private void LineTrace2(){
		// 黒
		if(rightColor == 0 && leftColor == 0){
			motor.SetSpeed(highspeed2, highspeed2);
		}
		// 右：白　左：黒
		else if(rightColor != 0 && leftColor == 0){
			motor.SetSpeed(lowspeed2, highspeed2);
		}
		// 右：黒　左：白
		else if(rightColor == 0 && leftColor != 0){
			motor.SetSpeed(highspeed2, lowspeed2);
		}
		// 白
		else if(rightColor != 0 && leftColor != 0){
			motor.SetSpeed(highspeed2, highspeed2);
		}
	}

	private void TargetOrbit(){   // 30cmの場合
		int leftspeed = 600;      // 500
		int rightspeed = 800;     // 750
		int time = 6700;          // 5500

		LCD.clear();
		LCD.drawString("TargetOrbit", 0, 0);

		motor.SetSpeed(leftspeed, rightspeed);
		stopwatch.reset();
		while(! Button.ESCAPE.isDown()){
			motor.Foward();
			if(stopwatch.elapsed() > time) break;  //
		}

		motor.SetSpeed(highspeed2, lowspeed2);  // 300, 200
		while(! Button.ESCAPE.isDown()){
			if(leftColor == 0 || rightColor == 0) {
				LineTrace2(); // スピードを先にセットしておく
				break;
			}
			motor.Foward();
		}

		// 目標周回後、コース復帰
		stopwatch.reset();
		while(! Button.ESCAPE.isDown()){
			LineTrace2();
			motor.Foward();
			if(stopwatch.elapsed() > 2000) break;
		}
	}

	private void ShortCut(){
		int leftspeed = 450; // 360
		int rightspeed = 600; // 480

		LCD.clear();
		LCD.drawString("ShortCut", 0, 0);

		// ショートカット
		motor.SetSpeed(leftspeed, rightspeed);
		while (! Button.ESCAPE.isDown() && leftColor == 1) {
			motor.Foward();
		}

		// 方向転換
		motor.SetSpeed(0,300);
		while (! Button.ESCAPE.isDown()) {
			if ( leftColor == 1 ) break;
			motor.Foward();
		}

		/* 少し走る
		stopwatch.reset();
		while (! Button.ESCAPE.isDown()) {
			LineTrace2();
			motor.Foward();
			if ( stopwatch.elapsed() > 7000) break;
		}
		*/

	}

	private void Parking(){
		int tacho; //タコメータ確保

		LCD.clear();
		LCD.drawString("Parking", 0, 0);

		motor.SetSpeed(300, 300);

		//右か左がライトグレーと認識した場合、抜ける。
		while (rightColor != 2 && leftColor != 2 && ! Button.ESCAPE.isDown()) {
			LineTrace2();
			motor.Foward();
		}

		LCD.clear();
		LCD.drawString("LightGray!", 0, 0);

		stopwatch.reset();
		while (stopwatch.elapsed() < 200) {
			motor.Foward();
		}

		motor.SetSpeed(300, 300);

		// 回転
		motor.LeftRotate(rotate90);
		// バックのためにタコメータリセット
		MotorControllor.leftMotor.resetTachoCount();
		// 青線まで
		//while( !(leftColor == 3 && rightColor == 3) && ! Button.ESCAPE.isDown()){ // 両方が認識すれば
		while( !(leftColor == 3 || rightColor == 3) && ! Button.ESCAPE.isDown()){ // 片方が認識すれば
			motor.Foward();
		}
		// バックした分だけ戻る
		tacho = -1 * MotorControllor.leftMotor.getTachoCount();
		MotorControllor.leftMotor.resetTachoCount();
		while( MotorControllor.leftMotor.getTachoCount() > tacho && ! Button.ESCAPE.isDown()){
			motor.Back();
		}
		// 回転
		motor.RightRotate(rotate90);
		MotorControllor.leftMotor.resetTachoCount();

		// タッチセンサーが反応するのが嫌なのでここでも走らせる
		motor.SetSpeed(highspeed2, highspeed2);
		stopwatch.reset();
		while(! Button.ESCAPE.isDown()){
			motor.Foward();
			if(stopwatch.elapsed() > 800) break;
		}
	}

	private void Calibration(){
		leftCalibration = new Calibration(1,SensorControllor.leftLight);
		rightCalibration = new Calibration(1, SensorControllor.rightLight);
		LCD.clear();
		LCD.drawString("Please Push Button", 0, 0);
		while(! Button.ENTER.isDown());
		LCD.drawString("LeftCalibration", 0, 1);
		leftCalibration.GetByOne(0);
		LCD.drawString("rightColorCalibration", 0, 2);
		rightCalibration.GetByOne(0);
	}
}
