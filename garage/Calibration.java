package garage;

import jp.ac.kagawa_u.infoexpr.Sensor.ColorSensor;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Calibration {
	private int calibNumber;		// キャリブレーションを行う回数
	private float calibData[][];	// キャリブレーションで記録した値
    static ColorSensor colorSensor;

    /**
     * コンストラクタ
     * キャリブレーションの回数を設定
     * @param n
     * @param left
     * @param right
     */
    public Calibration(int n, ColorSensor color) {
		calibNumber = n;
		calibData = new float[calibNumber][3];
		colorSensor = color;
	}

    /**
     * キャリブレーションの実行
     **/
    public void executeCalibration() {
        int i;
        LCD.clear();
        LCD.drawString("Please Push Enter", 0, 0);
        for(i = 0;i < calibNumber;i++) {
            enterPressWait();
            calibData[i] = getAction(colorSensor);
            LCD.drawString(calibData[i][0] + " " + calibData[i][1] + " " + calibData[i][2], 0, i + 1);
        }
        LCD.drawString("Complete!!!!", 0, i + 1);
        Delay.msDelay(1000);
    }

    /**
     * 色彩センサで値を取得し、小数部を加工
     * @param ColorSensor 値の取得に使用するセンサ
     * @return float[] 簡略化されたセンサの値
     **/
    private float[] getAction(ColorSensor s) {
        float f[];
        f = s.getHSV();
        f = cleanDecimal(f);
        return f;
    }

    /**
     * float型の小数第4位以下を除去
     * @param float[] float型の配列
     * @return float[] float型の配列
     **/
    private float[] cleanDecimal(float f[]){
        for(int i = 0; i < f.length; i++) {
            f[i] = (float)Math.floor((double)f[i] * 1000) / 1000;
        }
        return f;
    }

    /**
     * キャリブレーションで記録した値をLCDに表示
     **/
    public void showCalibData() {
        int i;
        LCD.clear();
        LCD.drawString("==CalibData==", 0, 0);
        for(i = 0;i < calibNumber;i++) {
            LCD.drawString(calibData[i][0] + " " + calibData[i][1] + " " + calibData[i][2], 0, i + 1);
        }
    }

    /**
     * キャリブレーションで記録した値の取得
     * @param int 記録した値の番号
     * @return float[] RGBの値
     **/
    public float[] getCalibData(int n) {
        if (n < calibNumber) {
            return calibData[n];
        } else {
            return null;
        }
    }

    /**
     * 真ん中ボタンが押されるまで停止
     **/
    private void enterPressWait(){
        while(Button.ENTER.isUp()){}
        while(Button.ENTER.isDown()){}
    }

    /**
     * 自作関数 1つずつキャリブレーション
     * @param val
     */
    public void GetByOne(int val){
    	calibData[val] = getAction(colorSensor);
    }

}


