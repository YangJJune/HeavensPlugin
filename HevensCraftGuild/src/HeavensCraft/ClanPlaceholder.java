package HeavensCraft;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class ClanPlaceholder extends PlaceholderExpansion {
	  // We get an instance of the plugin later.
    private hcGuild plugin;

    /**
     * Since this expansion requires api access to the plugin "SomePlugin" 
     * we must check if said plugin is on the server or not.
     *
     * @return true or false depending on if the required plugin is installed.
     */
    @Override
    public boolean canRegister(){
        return Bukkit.getPluginManager().getPlugin("HCguild") != null;
    }

    /**
     * We can optionally override this method if we need to initialize variables 
     * within this class if we need to or even if we have to do other checks to 
     * ensure the hook is properly set up.
     *
     * @return true or false depending on if it can register.
     */
    
    ClanPlaceholder(hcGuild pl){
    	plugin = pl;
    }
    @Override
    public boolean register(){
  
        // Make sure "SomePlugin" is on the server
        if(!canRegister()){
            return false;
        }
 
        /*
         * "SomePlugin" does not have static methods to access its api so we must 
         * create a variable to obtain access to it.
         */
  
        // if for some reason we can not get our variable, we should return false.
        if(plugin == null){
            return false;
        }

        /*
         * Since we override the register method, we need to call the super method to actually
         * register this hook
         */
        return super.register();
    }

    /**
     * The name of the person who created this expansion should go here.
     * 
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return "someauthor";
    }
 
    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest 
     * method to obtain a value if a placeholder starts with our 
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "HCguild";
    }
  
    /**
     * if the expansion requires another plugin as a dependency, the 
     * proper name of the dependency should go here.
     * <br>Set this to {@code null} if your placeholders do not require 
     * another plugin to be installed on the server for them to work.
     * <br>
     * <br>This is extremely important to set your plugin here, since if 
     * you don't do it, your expansion will throw errors.
     *
     * @return The name of our dependency.
     */
    @Override
    public String getRequiredPlugin(){
        return "HCguild";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return "1.0.0";
    }
  
    /**
     * This is the method called when a placeholder with our identifier 
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link org.bukkit.Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player player, String identifier){
    	hcCardMain instance = null;
        if(player == null){
            return "";
        }
        // %someplugin_placeholder1%
        if(identifier.equals("name")){
        	String str = plugin.getClanName(player);
        	if(str == null) return "미가입";
        	else return str;
        }

        // %someplugin_placeholder2%
        if(identifier.equals("card")){
    		if(Bukkit.getPluginManager().isPluginEnabled("HCCardFight")) {
    			instance = (hcCardMain) Bukkit.getPluginManager().getPlugin("HCCardFight");
    		}
        	if(instance.cards.containsKey(player)) {
        		switch(instance.cards.get(player)) {
        		case 0:
        			return "&6용광로&f의 &a장인";
        		case 1:
        			return "&e수리부엉이&f의 &3눈";
        		case 2:
        			return "&b스카이 &e스크래퍼";
        		case 4:
        			return "&e월드 &a네비게이터";
        		case 3:
        			return "&3피스 &c부스터";
        		case 5:
        			return "&e랜덤 &7스톤";
        		case 6:
        			return "&e낙법의 &6고수";
        		case 7:
        			return "&b광물 &e탐지기";
        		case 8:
        			return "&e자라나라 &a작물작물";
        		case 9:
        			return "&3마법 &e제거자";
        		case 10:
        			return "&b광물 탐지&f의 &6장인";
        		case 11:
        			return "&c일격&b필살";
        		case 13:
        			return "&b확성기";
        		case 14:
        			return "&c애프터&6버너";
        		case 15:
        			return "&b멀티&e셋홈";
        		case 16:
        			return "&e오늘은 &f내가 &a요리사";
                }
        	
        	}
        	return "없음";
        }
 
        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%) 
        // was provided
        return "없음";
    }
}

