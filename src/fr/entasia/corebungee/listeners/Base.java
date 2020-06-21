package fr.entasia.corebungee.listeners;

import fr.entasia.apis.ChatComponent;
import fr.entasia.apis.ServerUtils;
import fr.entasia.apis.socket.SocketClient;
import fr.entasia.bungeelogin.LoginUtils;
import fr.entasia.corebungee.Main;
import fr.entasia.corebungee.commands.base.StaffChatCmd;
import fr.entasia.corebungee.utils.BungeePlayer;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.manager.UserManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Base implements Listener {

	private static final UserManager userManager = Main.lpAPI.getUserManager();
	private static final Node lockdownNode = fr.entasia.sanctions.Main.lpAPI.buildNode("staff.lockdown.bypass").build();
	private static final Node dnsVanishNode = fr.entasia.sanctions.Main.lpAPI.buildNode("mod.dnsvanish").build();

	private final String[] cmdcompletes = {"ctime", "forcekick", "ipcheck", "join", "msg", "ping", "whois"};
	@EventHandler
	public void onChat(ChatEvent e){
		if(e.getSender() instanceof ProxiedPlayer&&!e.isCommand()) {
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
		if(msg.length()<4)return false;
		int pe = 0;
		int max = 0;
		for(char c : msg.toCharArray()){
			if(c>=65&&c<=90){
				pe++;
				max++;
			}else if(c>=97&&c<=122){
				max++;
			}
		}
		if(pe<3)return false;
		return max/(float)pe > 0.6;
	}

	@EventHandler
	public void onKick(ServerKickEvent e) {
		System.out.println(1);
		if(LoginUtils.logins.contains(e.getPlayer().getName())){
			System.out.println(2);
			e.setCancelled(true);
			e.getPlayer().connect(Main.hubServer);
			e.getPlayer().sendMessage(e.getKickReasonComponent());
		}
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
			}else{
				for(ProxiedPlayer p : Main.main.getProxy().getPlayers()) {
					e.getSuggestions().add(p.getDisplayName());
				}
			}
		}
	}

	@EventHandler
	public void onLogin(LoginEvent e) {
		if(e.getConnection().getVersion()<110) { // 1.9.4
			e.setCancelled(true);
			e.setCancelReason(ChatComponent.create("§cDésolé, nous n'acceptons plus les versions inférieures à la 1.9.4 !"));
		}else {
			for(char c : e.getConnection().getName().toCharArray()){
				if(charCheck(c)) {
					e.setCancelled(true);
					e.setCancelReason(ChatComponent.create("Caractère invalide dans ton pseudo : " + e.getConnection().getName()));
					return;
				}
			}

			UUID uuid = e.getConnection().getUniqueId();
			User u = userManager.getUser(uuid);
			if (u == null) {
				try {
					u = userManager.loadUser(uuid).get();
					if (u == null) throw new Exception("Failed to load user data  : " + e.getConnection().getName());
				} catch (Exception e2) {
					e2.printStackTrace();
					e.setCancelled(true);
					e.setCancelReason(ChatComponent.create("§cUne erreur est survenue lors du chargement de tes données ! Contacte un membre du Staff"));
					return;
				}
			}
			if (Main.lockdown != null) {
				if (!u.hasPermission(lockdownNode).asBoolean()) {
					e.setCancelled(true);
					e.setCancelReason(ChatComponent.create(
							"§cLe serveur est en maintenance ! §7"+Main.lockdown)
					);
					ServerUtils.permMsg("log.lockdownjoin",
							"§c§lGlobal §cLockdown : "+e.getConnection().getName()+" à essayé de rejoindre le serveur en Lockdown !");
					return;
				}
			}

			if (e.getConnection().getVirtualHost().getHostString().equals("vanish.entasia.fr")) {
				if(u.hasPermission(dnsVanishNode).asBoolean()){
					if(Main.vanishs.containsKey(e.getConnection().getName())) {
						e.setCancelled(true);
						e.setCancelReason(ChatComponent.create("§cTu es déja en vanish ! Merci de te connecter via l'adresse play.entasia.fr"));
						return;
					}
				}else{
					e.setCancelled(true);
					e.setCancelReason(ChatComponent.create("§cMerci de te connecter via l'adresse play.entasia.fr !"));
					return;
				}
			}


		}
	}

	public boolean charCheck(char c){
		if(c>=97&&c<=122) return false;
		else if(c>=65&&c<=90) return false;
		else if(c>=48&&c<=57) return false;
		else return c != 95;

	}

	@EventHandler
	public void onPostLogin(PostLoginEvent e){
		BungeePlayer bp = Main.getPlayer(e.getPlayer().getName());
		bp.p = e.getPlayer();

		if(Main.lockdown!=null){
			bp.p.sendMessage(ChatComponent.create("§c§lGlobal §cLockdown bypass ! §7"+Main.lockdown));
		}

		if (bp.p.getPendingConnection().getVirtualHost().getHostString().equals("vanish.entasia.fr")) {
			bp.p.sendMessage(ChatComponent.create("§3Vanish » §aActivé par DNS ! §b(Se désactivera à ta déconnexion"));
			Main.vanishs.put(bp.p.getName(), bp.p);
			SocketClient.sendData("broadcast vanish 1 " + bp.p.getName());
			Main.sql.fastUpdate("INSERT INTO global.vanishs (name) VALUES (?)", bp.p.getName());
			vanishMsg(bp.p);
		}else if(Main.vanishs.containsKey(bp.p.getName())) {
			bp.p.sendMessage(ChatComponent.create("§3Vanish » §cRestauré !"));
			vanishMsg(bp.p);
		}else if(Main.joinquit)Main.main.getProxy().broadcast(ChatComponent.create("§aJoin §8»§7 " + Main.formatPlayer(bp.p) + "§7 a rejoint §bEnta§7sia !"));


		bp.lastjointime = new Date().getTime();
		StringBuilder sb = new StringBuilder();
		for(ProxiedPlayer lp : ProxyServer.getInstance().getPlayers())sb.append(" ").append(lp.getName());
		if(sb.length()!=0)SocketClient.sendData("players "+sb.toString().substring(1));
	}

	public static void vanishMsg(ProxiedPlayer p){
		ServerUtils.permMsg( "mod.vanishmsg", Main.formatPlayer(p)+"§e a rejoint §bEnta§7sia§e en vanish !");

	}

	@EventHandler
	public void onLeave(PlayerDisconnectEvent e) {
		BungeePlayer bp = Main.getPlayer(e.getPlayer().getName());

		StringBuilder sb = new StringBuilder();
		for(ProxiedPlayer lp : ProxyServer.getInstance().getPlayers()){
			if(lp!=bp.p)sb.append(" ").append(lp.getName());
		}
		if(sb.length()==0) SocketClient.sendData("players");
		else SocketClient.sendData("players "+sb.toString().substring(1));

		if (Main.vanishs.containsKey(bp.p.getName())) {
			ServerUtils.permMsg("mod.vanishmsg",Main.formatPlayer(bp.p) + "§e a quitté §bEnta§7sia§e en vanish !");
			if (bp.p.getPendingConnection().getVirtualHost().getHostString().equals("vanish.entasia.fr")) {
				SocketClient.sendData("broadcast vanish 0 " + bp.p.getName());
				Main.vanishs.remove(bp.p.getName());
				Main.sql.fastUpdate("DELETE FROM global.vanishs where name=?", bp.p.getName());
			}

		} else if(Main.joinquit)Main.main.getProxy().broadcast(ChatComponent.create("§cQuit §8»§7 " + Main.formatPlayer(bp.p) + "§7 a quitté §bEnta§7sia !"));

		if(bp.lastjointime!=0){
			int time = (int) (new Date().getTime() - bp.lastjointime)/1000;
			bp.lastjointime = 0;
			bp.connectedtime += time;
			Main.sql.checkConnect();
			try{
				PreparedStatement a = Main.sql.connection.prepareStatement("UPDATE playerdata.global SET time_week=time_week+?, time_month=time_month+?, time_total=time_total+? WHERE uuid=?");
				a.setInt(1, time);
				a.setInt(2, time);
				a.setInt(3, time);
				a.setString(4, e.getPlayer().getUniqueId().toString());
				a.execute();
			}catch(SQLException e2){
				e2.printStackTrace();
			}
		}
	}
}
