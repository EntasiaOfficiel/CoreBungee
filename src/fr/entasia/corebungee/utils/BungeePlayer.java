package fr.entasia.corebungee.utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.Date;

public class BungeePlayer {

	public ProxiedPlayer p;

	public String lastmsg = "";
	public long lastsentmsg = 10000;

	public long sentSince(){
		return new Date().getTime()-lastsentmsg;
	}

	public String discordID=null;
	public ScheduledTask discordTask=null;

	public long lastjointime; // ms
	public long connectedtime; // seconds

	public long getConnectedTime(){
		return connectedtime+(new Date().getTime() - lastjointime)/1000;
	}

	public boolean isOnline(){
		return p!=null&&p.isConnected();
	}
}
