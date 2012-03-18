package nima.nikzad.annoydroid.logcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nima.nikzad.annoydroid.tracktarget.TrackTarget;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LogcatWatcher {
	private static final String TAG = "LogcatWatcher";
	private boolean isRunning;
	private Process m_logcatProcess;
	private BufferedReader m_logcatReader;
	private LogcatThread m_logcatReadThread;
	
	private List<TrackTarget> m_targetList;
	private Map<String, List<TrackTarget>> m_targetMap;
	
	public LogcatWatcher() {
		isRunning = false;
		m_targetList = new ArrayList<TrackTarget>();
		m_targetMap = new HashMap<String, List<TrackTarget>>();
	}
	
	public void addTarget(TrackTarget target) {
		if(!m_targetList.contains(target)) {
			String targetKey = target.getPriority() + '/' + target.getTag();
			m_targetList.add(target);
			List<TrackTarget> values = m_targetMap.get(targetKey);
			if(values == null) {
				List<TrackTarget> newValues = new ArrayList<TrackTarget>();
				newValues.add(target);
				m_targetMap.put(targetKey, newValues);
			} else {
				values.add(target);
			}
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
			m_logcatProcess.destroy();
			//Log.d(TAG, "Process exited: " + m_logcatProcess.exitValue());
			
			// Destroy the log
			try {
				Runtime.getRuntime().exec("logcat -c");
			} catch (IOException e) {
				Log.e(TAG, "Exception clearing the log: " + e);
			}
		}
	}
	
	private class LogcatThread extends Thread {
		
		@Override
		public void run() {
			Log.d(TAG, "Running helper thread...");
			try {
				// Throw away anything already in the queue
				// Give some time to fill buffer
				sleep(5000);
				while(m_logcatReader.ready()) {
					m_logcatReader.readLine();
				}
				while(isRunning) {
//					if(m_logcatReader.ready()) {
						deliverLog(m_logcatReader.readLine());
//					} else {
//						sleep(1000);
//					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Log.d(TAG, "Finished helper thread!");
		}
		
		private void deliverLog(String logMessage) {
			if(logMessage == null)
				return;
			Log.d(TAG, "*** " + logMessage);
			int endOfSource = logMessage.indexOf('(', 0);
			int startOfMessage = logMessage.indexOf(':', 0);
			if(endOfSource != -1 && startOfMessage != -1) {
				String messageSource = logMessage.substring(0, endOfSource).trim();
				String messageContent = logMessage.substring(startOfMessage + 1, logMessage.length() - 1).trim();
				//Log.d(TAG, "*** " + messageSource + ": " + messageContent);
				
				List<TrackTarget> potentialTargets = m_targetMap.get(messageSource);
				if(potentialTargets != null) {
					for(TrackTarget target : potentialTargets) {
						if(messageContent.contains(target.getContains())) {
							Handler targetHandler = target.getHandler();
							Message msg = targetHandler.obtainMessage();
							msg.obj = messageContent;
							targetHandler.sendMessage(msg);
						}
					}
				}
			}
		}
	}

}
