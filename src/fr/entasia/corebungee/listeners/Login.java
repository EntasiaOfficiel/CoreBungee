package fr.entasia.corebungee.listeners;

import fr.entasia.apis.ChatComponent;
import fr.entasia.corebungee.Main;
import fr.entasia.libraries.Common;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class Login implements Listener {

	static final String[] logincmds = new String[]{"login", "log", "lo", "l", "register", "reg"};

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(ChatEvent e) {
		if(!Common.enableDev) {
			if (Main.logins.contains(e.getSender().toString())) return;
			String a = e.getMessage().split(" ")[0];
			if(a.substring(0, 1).equals("/")){
				a = a.substring(1).toLowerCase();
				for(String i : logincmds)if(a.equals(i))return;
			}
			e.setCancelled(true);
			((ProxiedPlayer) e.getSender()).sendMessage("§cTu dois d'abord te t'enregistrer ou t'authentifier !");
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerChange(ServerSwitchEvent e) {
		if(!Common.enableDev) {
			if (e.getPlayer().getServer().getInfo().getName().equals("login")) return;
			if (Main.logins.contains(e.getPlayer().getName())) return;
			e.getPlayer().disconnect(ChatComponent.create("§cTu dois d'abord te t'enregistrer ou t'authentifier !"));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDisconnect(PlayerDisconnectEvent e) {
		if(!Common.enableDev){
			while(Main.logins.contains(e.getPlayer().getName())){
				Main.logins.remove(e.getPlayer().getName());
			}
		}
	}
}
