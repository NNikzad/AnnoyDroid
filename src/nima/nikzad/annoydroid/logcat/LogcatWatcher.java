package nima.nikzad.annoydroid.logcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

public class LogcatWatcher {
	private static final String TAG = "LogcatWatcher";
	private boolean isRunning;
	private Process m_logcatProcess;
	private BufferedReader m_logcatReader;
	private LogcatThread m_logcatReadThread;
	
	public LogcatWatcher() {
		isRunning = false;
	}
	
	public synchronized void start() {
		if(!isRunning) {
			isRunning = true;
			try {
				m_logcatProcess = Runtime.getRuntime().exec("logcat ActivityManager:I *:S");
				m_logcatReader = new BufferedReader(new InputStreamReader(m_logcatProcess.getInputStream()));
				m_logcatReadThread = new LogcatThread();
				m_logcatReadThread.start();
			} catch (IOException e) {
				Log.e(TAG, "Exception starting LogcatWatcher: " + e);
			}
		}
	}
	
	public synchronized void stop() {
		if(isRunning) {
			isRunning = false;
			if(m_logcatReadThread.isAlive()) {
				m_logcatReadThread.destroy();
			}
			m_logcatProcess.destroy();
		}
	}
	
	private class LogcatThread extends Thread {
		
		@Override
		public void run() {
			Log.d(TAG, "Running helper thread...");
			Log.d(TAG, "Finished helper thread!");
		}
	}

}
