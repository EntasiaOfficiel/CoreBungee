package fr.entasia.corebungee.listeners;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import fr.entasia.corebungee.commands.staff.StaffChatCmd;
import fr.entasia.corebungee.utils.BungeePlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;

public class TabChat implements Listener {


	private final String[] cmdcompletes = {"ctime", "forcekick", "ipcheck", "join", "msg", "ping", "whois", "find"};
	@EventHandler
	public void onChat(ChatEvent e){
		if(e.getSender() instanceof ProxiedPlayer &&!e.isCommand()) {
			ProxiedPlayer p = (ProxiedPlayer)e.getSender();
			BungeePlayer bp = Main.getPlayer(p.getName());

			if(e.getMessage().startsWith(".sync")) {
				p.sendMessage(ChatComponent.create("§cCette commande doit s'éxecuter sur discord ! Fait §4/discord§c pour avoir le lien"));
				return;
			}

			if(bp.sentSince()>450){
				if (Main.staffchat.contains(p.getUniqueId())) {
					if(p.hasPermission("staff.staffchat")){
						e.setCancelled(true);
						StaffChatCmd.staffchatTalk(p, e.getMessage());
					}else{
						Main.staffchat.remove(p.getUniqueId());
					}
				}else{
					if(bp.lastmsg.equals(e.getMessage())&&bp.sentSince()<5000){
						p.sendMessage(ChatComponent.create("§cNe spam pas !"));
						e.setCancelled(true);
					}else{
						if(checkMajs(e.getMessage())){
							p.sendMessage(ChatComponent.create("§cAttention à l'abus de majuscules dans ton message !"));
							e.setMessage(e.getMessage().toLowerCase());
						}

						// on est good
						bp.lastsentmsg = new Date().getTime();
						bp.lastmsg = e.getMessage();
					}
				}
			}else{
				p.sendMessage(ChatComponent.create("§cHép ! Attend un peu avant de d'envoyer de nouveau un message !"));
				e.setCancelled(true);
			}
		}
	}
	public static boolean checkMajs(String msg){
		return false;
//		if(msg.length()<4)return false;
//		int pe = 0;
//		int max = 0;
//		for(char c : msg.toCharArray()){
//			if(c>=65&&c<=90){
//				pe++;
//				max++;
//			}else if(c>=97&&c<=122){
//				max++;
//			}
//		}
//		if(pe<3)return false;
//		return max/(float)pe > 0.6;
	}

	private boolean isTabCmd(String value) {
		for (String i : cmdcompletes) {
			if (value.equals(i)) return true;
		}
		return false;
	}

	@EventHandler
	public void onTabComplete(TabCompleteEvent e) {
		String[] a = e.getCursor().split(" ");
		if(a[0].length()>1&&isTabCmd(a[0].substring(1).toLowerCase())){
			if(a.length>1){
				a[1] = a[1].toLowerCase();
				for(ProxiedPlayer p : Main.main.getProxy().getPlayers()) {
					if(p.getDisplayName().toLowerCase().startsWith(a[1]))e.getSuggestions().add(p.getDisplayName());
				}
			}
		}
	}

}
