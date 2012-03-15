package nima.nikzad.annoydroid.tracktarget;

import android.os.Handler;

public abstract class TrackTarget {
	private String m_tag;
	private String m_priority;
	private String m_contains;
	protected Handler m_handler;
	
	public TrackTarget(String name, String priority, String contains) {
		m_tag = name;
		m_priority = priority;
		m_contains = contains;
		m_handler = new Handler();
	}
	
	public String getTag() {
		return m_tag;
	}
	
	public String getPriority() {
		return m_priority;
	}
	
	public String getContains() {
		return m_contains;
	}
	
	public Handler getHandler() {
		return m_handler;
	}
	
}
