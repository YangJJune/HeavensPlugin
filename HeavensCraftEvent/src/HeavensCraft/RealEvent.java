package HeavensCraft;

public class RealEvent {
	private String str;
	private int index;
	private Time time;
	
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public void seTime(Time time) {
		this.time=time;
	}
	public Time getTime() {
		return this.time;
	}
	RealEvent(String s, int i, Time t){
		this.str=s;
		index = i;
		time=t;
	}
	
}
