import lejos.hardware.Button;
import lejos.utility.Delay;

public class Project2 {
	public static void main(String[] args) {

		Streight streight = new Streight();
		boolean flag = true;
		int time; //時間の取得用

		//ボタンを押してスタート
		while(!Button.ENTER.isDown());

		//動作する時間を取得
		time = streight.Get_Time(75, 600);
		//前進
		for(int i=0;i<time;i+=50){
			streight.Move();
			Delay.msDelay(50);
		}

		//90度回転
		streight.Rotation_R(90);

		//動作する時間を取得
		time = streight.Get_Time(90,600);
		//前進
		for(int i=0;i<time;i+=50){
			streight.Move();
			Delay.msDelay(50);
			if(i >= time / 2 && flag){
				streight.Beep();
				flag = false;
			}
		}

		//30度回転
		streight.Rotation_R(35);

		/*-----ここからは曲線移動-----*/
	}
}
