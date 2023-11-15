package HeavensCraft;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class hcAntiXRay extends JavaPlugin implements Listener {
	
	
	List<UUID> players = new ArrayList<>();
	
	
	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		this.readFile();
		this.saveFile();
		super.onEnable();
	}
	private void readFile() {
		try {
			File file = new File("D:\\새 폴더 (8)\\새 폴더 (4)\\서버\\plugins\\uuid.txt");
			Scanner scan = new Scanner(file);
			int N = scan.nextInt();
			for(int i=0; i<N; i++) {
				UUID uid = UUID.fromString(scan.next());
				if(!(players.contains(uid))) {
					players.add(uid);
				}				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void saveFile() {
		// TODO Auto-generated method stub
        try{
            //파일 객체 생성
        	File file = new File("D:\\새 폴더 (8)\\새 폴더 (4)\\서버\\plugins\\uuid.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            int size = players.size();
            if(file.isFile() && file.canWrite()){
            	bufferedWriter.write(String.valueOf(size));
    				for (UUID u : players) {
                		//개행문자쓰기
                		bufferedWriter.newLine();
                		bufferedWriter.write(u.toString());
    				}
                bufferedWriter.close();
            }
        }catch (IOException e) {
            System.out.println(e);
        }
	}
}
