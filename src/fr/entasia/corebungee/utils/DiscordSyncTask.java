package fr.entasia.corebungee.utils;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.TimeUnit;

public class DiscordSyncTask implements Runnable {

	public BungeePlayer bp;

	public DiscordSyncTask(BungeePlayer bp) {
		this.bp = bp;
	}

	public static void runNew(BungeePlayer bp){
		bp.discordTask = ProxyServer.getInstance().getScheduler().schedule(Main.main, new DiscordSyncTask(bp), 30, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		if(bp.isOnline()) bp.p.sendMessage(ChatComponent.create("§cTon code §4"+bp.discordID+"§c à expiré !"));
		bp.discordID = "none";

	}
}
