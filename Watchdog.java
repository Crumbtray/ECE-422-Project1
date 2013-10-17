import java.util.TimerTask;

public class Watchdog extends TimerTask {

	Thread watched;
	
	public Watchdog(Thread target)
	{
		watched = target;
	}
	
	@Override
	public void run() {
		System.out.println("Watchdog: Timeout occurred, stopping thread.");
		watched.stop();
	}
}