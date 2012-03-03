package nima.nikzad.annoydroid.logcat;

public class LogcatWatcher {
	
	private boolean isRunning;
	
	public LogcatWatcher() {
		isRunning = false;
	}
	
	public synchronized void start() {
		if(!isRunning) {
			isRunning = true;
		}
	}
	
	public synchronized void stop() {
		if(isRunning) {
			isRunning = false;
		}
	}

}
