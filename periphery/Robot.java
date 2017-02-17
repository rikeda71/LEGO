package periphery;

public class Robot {
	public static void main(String[] args) {
		Thread mission = new Thread(new RunOutCource());
		mission.start();
		try {
			mission.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
