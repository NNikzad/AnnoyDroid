package nima.nikzad.annoydroid.logcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nima.nikzad.annoydroid.tracktarget.TrackTarget;
import android.util.Log;

public class LogcatWatcher {
	private static final String TAG = "LogcatWatcher";
	private boolean isRunning;
	private Process m_logcatProcess;
	private BufferedReader m_logcatReader;
	private LogcatThread m_logcatReadThread;
	
	private List<TrackTarget> m_targetList;
	
	public LogcatWatcher() {
		isRunning = false;
		m_targetList = new ArrayList<TrackTarget>();
	}
	
	public void addTarget(TrackTarget target) {
		if(!m_targetList.contains(target)) {
			m_targetList.add(target);
		}
	}
	
	public void removeTarget(TrackTarget target) {
		m_targetList.remove(target);
	}
	
	public void clearTargetList() {
		m_targetList.clear();
	}
	
	public synchronized boolean start() {
		if(!isRunning) {
			if(m_targetList.size() == 0) {
				Log.d(TAG, "No targets to track. Not starting.");
				return false;
			}
			isRunning = true;
			
			// The list of things to watch for
			String filterString = "";
			for(TrackTarget target : m_targetList) {
				filterString = filterString.concat( target.getTag() + ":" + target.getPriority() + " ");
			}
			
			try {
				String processString = "logcat " + filterString + "*:S";
				Log.d(TAG, "Starting: " + processString);
				m_logcatProcess = Runtime.getRuntime().exec(processString);
				m_logcatReader = new BufferedReader(new InputStreamReader(m_logcatProcess.getInputStream()));
				m_logcatReadThread = new LogcatThread();
				m_logcatReadThread.start();
			} catch (IOException e) {
				Log.e(TAG, "Exception starting LogcatWatcher: " + e);
				return false;
			}
			return true;
		} else {
			Log.d(TAG, "Already running.");
			return false;
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
