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
			realname = "�������";
			break;
		case "B":
			realname = "õ�������";
			break;
		case "C":
			realname = "������Ƽ";
			break;
		case "D":
			realname = "�������۴�";
			break;
		case "E":
			realname = "�̴��������۴�";
			break;
		case "F":
			realname = "�������۴�";
			break;
		case "G":
			realname = "������ֽ�ȸ��";
			break;
		case "H":
			realname = "����Ϸ�Ʈ�δн�";
			break;
		case "I":
			realname = "���";
			break;
		case "J":
			realname = "��������";
			break;
		}
		
	}
	@Override
	public String toString() {
		String newname="";
		switch(name) {
		case "A":
			newname = "�������";
			break;
		case "B":
			newname = "õ�������";
			break;
		case "C":
			newname = "������Ƽ";
			break;
		case "D":
			newname = "�������۴�";
			break;
		case "E":
			newname = "�̴��������۴�";
			break;
		case "F":
			newname = "�������۴�";
			break;
		case "G":
			newname = "������ֽ�ȸ��";
			break;
		case "H":
			newname = "����Ϸ�Ʈ�δн�";
			break;
		case "I":
			newname = "���";
			break;
		case "J":
			newname = "��������";
			break;
		}
		String str = ChatColor.GOLD+" :: HCStock :: " +ChatColor.GREEN+""+newname + ""+ChatColor.WHITE +" �ְ� : ";
		DecimalFormat d = new DecimalFormat("###,###");
		
		if(now>past) {
			str+=ChatColor.RED+"�� " + d.format(this.now)+" "+ "(+" + String.valueOf(d.format(now-past)) + ")��";
		}
		else if(now<past){
			str+=ChatColor.DARK_AQUA+"�� "+ d.format(this.now)+" " + "(-" + String.valueOf(d.format(past-now)) + ")��";
		}
		else {
			str+=ChatColor.GRAY+"�� "+ d.format(this.now)+" " + "(=" + String.valueOf(d.format(now-past)) + ")��";
		}
		return str;
		
	}
	
	
}
