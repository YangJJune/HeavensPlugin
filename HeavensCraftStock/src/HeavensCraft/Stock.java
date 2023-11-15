package HeavensCraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.md_5.bungee.api.ChatColor;

public class Stock {
	private String name;
	private int now;
	private int past;
	private int status; //-2 -1 0 1 2
	private int max;
	private int total;
	private int leftTotal;
	private boolean end;
	private String realname;
	public boolean isEnd() {
		return end;
	}
	public void setEnd(boolean end) {
		this.end = end;
	}
	public int getLeftTotal() {
		return leftTotal;
	}
	public void setLeftTotal(int leftTotal) {
		if(leftTotal>this.total) {
			this.leftTotal=leftTotal;
		}
		else
			this.leftTotal = leftTotal;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNow() {
		return now;
	}
	public void setNow(int now) {
		this.now = now;
	}
	public int getPast() {
		return past;
	}
	public void setPast(int past) {
		this.past = past;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getRealName() {
		return this.realname;
	}
	
	Stock(String name, int now, int past, int status, int max, int total, int l, boolean e){
		this.name=name;
		this.now=now;
		this.past=past;
		this.status=status;
		this.max=max;
		this.total=total;
		this.leftTotal=l;
		this.end=e;
		switch(name) {
		case "A":
			realname = "삼송전자";
			break;
		case "B":
			realname = "천국여행사";
			break;
		case "C":
			realname = "강원시티";
			break;
		case "D":
			realname = "리플컴퍼니";
			break;
		case "E":
			realname = "이더리움컴퍼니";
			break;
		case "F":
			realname = "어드민컴퍼니";
			break;
		case "G":
			realname = "헤븐즈주식회사";
			break;
		case "H":
			realname = "사과일렉트로닉스";
			break;
		case "I":
			realname = "고글";
			break;
		case "J":
			realname = "양쭌전자";
			break;
		}
		
	}
	@Override
	public String toString() {
		String newname="";
		switch(name) {
		case "A":
			newname = "삼송전자";
			break;
		case "B":
			newname = "천국여행사";
			break;
		case "C":
			newname = "강원시티";
			break;
		case "D":
			newname = "리플컴퍼니";
			break;
		case "E":
			newname = "이더리움컴퍼니";
			break;
		case "F":
			newname = "어드민컴퍼니";
			break;
		case "G":
			newname = "헤븐즈주식회사";
			break;
		case "H":
			newname = "사과일렉트로닉스";
			break;
		case "I":
			newname = "고글";
			break;
		case "J":
			newname = "양쭌전자";
			break;
		}
		String str = ChatColor.GOLD+" :: HCStock :: " +ChatColor.GREEN+""+newname + ""+ChatColor.WHITE +" 주가 : ";
		DecimalFormat d = new DecimalFormat("###,###");
		
		if(now>past) {
			str+=ChatColor.RED+"▲ " + d.format(this.now)+" "+ "(+" + String.valueOf(d.format(now-past)) + ")원";
		}
		else if(now<past){
			str+=ChatColor.DARK_AQUA+"▼ "+ d.format(this.now)+" " + "(-" + String.valueOf(d.format(past-now)) + ")원";
		}
		else {
			str+=ChatColor.GRAY+"■ "+ d.format(this.now)+" " + "(=" + String.valueOf(d.format(now-past)) + ")원";
		}
		return str;
		
	}
	
	
}
