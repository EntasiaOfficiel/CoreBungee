package fr.entasia.corebungee.antibot;

import fr.entasia.corebungee.Main;
import fr.entasia.corebungee.utils.BungeePlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Utils {

	public static AntibotLevel level;
	public static AntibotMode mode;

	public static ArrayList<String> safeList = new ArrayList<>();
	public static HashMap<String, String> safeListSQL = new HashMap<>();
	public static ScheduledTask update;


	public static void changeLevel(AntibotLevel level){
		if(level==AntibotLevel.CAPTCHA){
			safeList.clear();

			update = ProxyServer.getInstance().getScheduler().schedule(Main.main, new SQLUpdate(), 15, TimeUnit.SECONDS);

			for(Map.Entry<String, BungeePlayer> e : Main.playerCache.entrySet()){
				if(e.getValue().getConnectedTime()>15*60||(new Date().getTime()-e.getValue().lastjointime)/1000>5*60){
					safeList.add(e.getKey());
				}
			}
		}else{
			update.cancel();
			safeList.clear();
			safeListSQL.clear();
		}


	}

}
