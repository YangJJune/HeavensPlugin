package HeavensCraft;

import java.text.DecimalFormat;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

public class Score {
	private int games=0;
	private int win=0;
	private int lose=0;
	private int streak=0;
	private int loseStreak=0;
	private int elo=1000;
	private UUID ID;
	public void win() {
		win++;
		streak++;
		games++;
	}
	public void lose() {
		lose++;
		games++;
		streak=0;
	}
	
	public int getGames() {
		return this.games;
	}
	
	public int getWin() {
		return this.win;
	}
	
	public int getLose() {
		return this.lose;
	}
	
	public UUID getID() {
		return ID;
	}
	public void setID(UUID iD) {
		ID = iD;
	}
	public int getStreak() {
		return this.streak;
	}
	public String showStatic() {
		
		DecimalFormat df = new DecimalFormat("##.##");
		String percentage = df.format((double)win/(double)games*100.00);
		String str=games+"Àü "+win+"½Â "+lose+" ÆÐ ½Â·ü "+percentage+"%"+" "+this.streak+"¿¬½Â "+this.elo+"Á¡";
		return str;
	}
	
	public int getElo() {
		return elo;
	}
	public void setElo(int elo) {
		this.elo = elo;
	}
	Score(){
	}
	Score(int g, int w, int l, int s, int e){
		this.games=g;
		this.win=w;
		this.lose=l;
		this.streak=s;
		this.elo=e;
	}
}
