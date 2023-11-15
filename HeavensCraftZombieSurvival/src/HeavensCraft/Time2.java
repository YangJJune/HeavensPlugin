package HeavensCraft;

public class Time2 {
	private int minute;
	private int second;
	private int score;
	
	public int getScore() {
		return score;
	}
	public void setScore(int s) {
		this.score = s;
	}
	
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	
	Time2(int m, int s, int Score){
		this.minute=m;
		this.second=s;
		score = Score;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str;
		
		str=String.valueOf(minute)+" "+String.valueOf(second)+" "+String.valueOf(score);
		return str;
	}
}
