package fr.entasia.corebungee.listeners;

import fr.entasia.apis.ChatComponent;
import fr.entasia.corebungee.Main;
import fr.entasia.corebungee.antibot.AntibotLevel;
import fr.entasia.corebungee.antibot.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AntiBot implements Listener {

	public static HashMap<String, Short> ips;
	public static short i=0;

	public static short nextValue(){
		if(i==Short.MAX_VALUE)i=0;
		else i++;
		return i;
	}

	@EventHandler(priority = -128)
	public void a(PreLoginEvent e) {

		if(AntibotLevel.isActive()){
			if(AntibotLevel.activeMode(AntibotLevel.PING)){
				String ip = e.getConnection().getAddress().getAddress().getHostAddress();
				if(ips.get(ip)==null) {
					e.setCancelled(true);
					e.setCancelReason(new ChatComponent(
							"§cAntiBot :",
							"Une attaque est en cours !",
							"Les connexions directes au serveur (connexion rapide) ont été temporairement suspendues",
							"Veuillez ajouter le serveur à votre liste de serveur pour vous connecter")
					.create());
					return;
				}else ips.remove(ip);
			}
			if(AntibotLevel.activeMode(AntibotLevel.CAPTCHA)){
				String ip = e.getConnection().getAddress().getAddress().getHostAddress();
				UUID uuid = e.getConnection().getUniqueId();
				if(!Utils.safeList.contains(uuid)&&!Utils.safeListSQL.get(uuid).equals(ip)){
					e.setCancelled(true);
					e.setCancelReason(new ChatComponent(
							"§cAntiBot :",
							"Une attaque est en cours !",
							"Les nouvelles connexions au serveur ont été temporairement suspendues",
							"Si tu veux te connecter, va sur §bhttps://enta§7sia.fr/captcha-bot")
					.create());
					return; // réfléchis pour le warning
				}
			}
		}

	}
	@EventHandler(priority = -128)
	public void a(ProxyPingEvent e) {
		if(AntibotLevel.activeMode(AntibotLevel.PING)){
			String ip = e.getConnection().getAddress().getAddress().getHostAddress();
			short a = nextValue();
			ips.put(ip, a);


			ProxyServer.getInstance().getScheduler().schedule(Main.main, () -> {
				if(ips.get(ip)==a)ips.remove(ip);
			}, 7, TimeUnit.SECONDS);


		}
	}
}
