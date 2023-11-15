package HeavensCraft;

public class Time {
	private String day;
	private String month;
	private String year;
	private String hour;
	private String minute;
	
	Time(String y, String m, String d, String h, String minute){
		this.year=y;
		this.month=m;
		this.day=d;
		this.hour=h;
		this.minute=minute;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return (year+"/"+month+"/"+day+" "+hour+" : "+minute);
	}
	
}
