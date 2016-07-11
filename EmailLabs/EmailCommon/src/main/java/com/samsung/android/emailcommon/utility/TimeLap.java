package com.samsung.android.emailcommon.utility;

import java.util.ArrayList;

import android.util.SparseArray;

public class TimeLap {
	
	static public int LAPTIME_ID_DECRYPTION = 1;	static public int ID_LISTVIEW = 2;
	static public int ID_OUTER_CONTAINER = 3;
	static public int ID_INNER_CONTAINER = 4;
	static public int ID_LAUNCH = 5;		static public int LAPTIME_ID_SEND_REVOCATION = 6;
	private static SparseArray<TimeLapData> _insts = new SparseArray<TimeLapData> ();
	
	private static TimeLapData getInstance(int id) {
		if (_insts.get(id) == null) {
			TimeLapData data = new TimeLapData(id);
			_insts.put(id, data);
		}
		return _insts.get(id);
		
	}
	
	static public TimeLapData getLap(int id) {
		return getInstance(id);
	}
	
	static public class TimeLapData {
		private long startTime;
		private long lapTime = 0;   
		ArrayList <Score> lapTimes = new ArrayList<Score> ();
		final private int mId;
		public TimeLapData(int id) {
			mId = id;
		}
		
		public void reset() {
			startTime = System.currentTimeMillis();
			lapTimes.clear();
		}
		
		public void event(String tag) {
			long c = System.currentTimeMillis();
			lapTime = c - startTime;
			startTime = c;
			lapTimes.add(new Score(tag, lapTime));
		}
		
		public void eventp(String tag) {
			long c = System.currentTimeMillis();
			lapTime = c - startTime;
			startTime = c;
			
			lapTimes.add(new Score(tag, lapTime));
			EmailLog.i("LapTime (" + String.valueOf(mId) + ")", "Point " + tag + ": " + getLapTimeString());
		}
		
		public long getLapTimeLong() {
			return lapTime;
		}
		
		public String getLapTimeString() {
			
			final long MIN = 60 * 1000;
			final long SEC = 1000;
			StringBuilder lap = new StringBuilder();
			if (lapTime > 60 * 1000) {
				long m = lapTime / MIN;
				long s = (lapTime - m * MIN) /  SEC;
				long ms = lapTime - m * MIN - s * SEC;
				lap.append(String.valueOf(m)).append("m ").append(String.valueOf(s)).append("s ").append(String.valueOf(ms));
			} else if (lapTime > 1000) {
				long s = lapTime / SEC;
				long ms = lapTime - s * SEC;
				lap.append(String.valueOf(s)).append("s ").append(String.valueOf(ms));
			} else {
				lap.append(String.valueOf(lapTime));
			}
			return lap.toString();
		}
		
		private String getStringTime(long time) {
			StringBuilder lap = new StringBuilder();
				final long MIN = 60 * 1000;
				final long SEC = 1000;
				if (time > 60 * 1000) {
					long m = time / MIN;
					long s = (time - m*MIN) /  SEC;
				long ms = time - m * MIN - s * SEC;
					lap.append(String.valueOf(m)).append("m ").append(String.valueOf(s)).append("s ").append(String.valueOf(ms));
				} else if (time > 1000) {
					long s = time / SEC;
				long ms = time - s * SEC;
					lap.append(String.valueOf(s)).append("s ").append(String.valueOf(ms));
				} else {
					lap.append(String.valueOf(time));
				}
			return lap.toString();
		}
		public void result() {
			long total = 0;
			for (Score score : lapTimes) {
				
				String tag = score.tag;
				long time = score.time;
				
				EmailLog.i("TimeLap (" + String.valueOf(mId) + ")", "Point " + tag + ": " + getStringTime(time));
				total += time;
			}
			
			EmailLog.i("TimeLap (" + String.valueOf(mId) + ")", "Total time :" + getStringTime(total));
			}
		
		public void resultTotalOnly() {
			long total = 0;
			for (Score score : lapTimes) {
				long time = score.time;
				total += time;
			}
			
			EmailLog.i("TimeLap (" + String.valueOf(mId) + ")", "Total time :" + getStringTime(total));
		}
		
		static private class Score {
			String tag;
			long time;
			public Score (String t, long m) {
				tag = t;
				time = m;
				
			}
		}
	}
}
