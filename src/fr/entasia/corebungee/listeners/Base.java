package fr.entasia.corebungee.listeners;

import fr.entasia.antibot.AntibotAPI;
import fr.entasia.apis.other.ChatComponent;
import fr.entasia.apis.socket.SocketClient;
import fr.entasia.apis.utils.ServerUtils;
import fr.entasia.corebungee.Main;
import fr.entasia.corebungee.utils.BungeePlayer;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class Base implements Listener {

	@EventHandler
	public void serverPing(ProxyPingEvent e){
		if(Main.antibotModule&&AntibotAPI.isActive())return;
		ServerPing serverPing = e.getResponse();
		String line1 = Main.configuration.getString("motd1").replace("&","§");
		String line2 = Main.configuration.getString("motd2").replace("&","§");
		serverPing.setDescriptionComponent(new TextComponent(line1 +"\n"+line2));

		ServerPing.PlayerInfo[] sample = new ServerPing.PlayerInfo[Main.main.getProxy().getOnlineCount()];
		int i=0;
		for (ProxiedPlayer p : Main.main.getProxy().getPlayers()) {
			sample[i] = new ServerPing.PlayerInfo(p.getDisplayName(), "");
			i++;
		}
		serverPing.getPlayers().setSample(sample);
		serverPing.getPlayers().setMax(Main.main.getProxy().getConfig().getPlayerLimit());
		e.setResponse(serverPing);
	}

	@EventHandler
	public void onLogin(LoginEvent e) {
		if(e.isCancelled())return;
		if(!Main.dev&&e.getConnection().getVersion()<110) { // 1.9.4
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
//			if(Main.main.getProxy().getOnlineCount() >= Main.main.getProxy().getConfig().getPlayerLimit()){
//				e.setCancelled(true);
//				e.setCancelReason(ChatComponent.create("§cLe nombre de joueur maximum est déjà atteint !"));
//				return;
//			}

			UUID uuid = e.getConnection().getUniqueId();
			User u = Main.lpAPI.getUserManager().getUser(uuid);
			if (u == null) {
				try {
					u = Main.lpAPI.getUserManager().loadUser(uuid).get();
					if (u == null) throw new Exception("Failed to load user data  : " + e.getConnection().getName());
				} catch (Exception e2) {
					e2.printStackTrace();
					e.setCancelled(true);
					e.setCancelReason(ChatComponent.create("§cUne erreur est survenue lors du chargement de tes données ! Contacte un membre du Staff"));
					return;
				}
			}
			if (Main.lockdown != null) {
				if (!hasPermission(u,"staff.lockdown.bypass")) {
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
				if(hasPermission(u, "mod.dnsvanish")){
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

			// SUITE DU CODE


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
		if(e.getPlayer().getGroups().contains("bot"))return;
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

			if(!Main.dev){
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

	@EventHandler
	public void onKick(ServerKickEvent e) {
		if(Main.isLogin(e.getPlayer().getName())){
			if(e.getCause()==ServerKickEvent.Cause.SERVER&&e.getState()==ServerKickEvent.State.CONNECTED){
				System.out.println(e.getCause());
				System.out.println(e.getState());
				System.out.println(e.getKickedFrom());
				System.out.println(e.getCancelServer());
				e.setCancelled(true);
				if(e.getKickedFrom()==Main.hubServer){
					e.getPlayer().disconnect(ChatComponent.create("§cLe Lobby vient de s'arrêter !"));
					System.out.println("kicked for "+e.getKickReason());
				}else{
					e.setCancelServer(Main.hubServer);
					e.getPlayer().sendMessage(ChatComponent.create("§cTu as été exclu du serveur ou tu étais ! Raison :"));
					e.getPlayer().sendMessage(e.getKickReasonComponent());
				}
			}
		}
	}


	private static boolean hasPermission(User u, String permission){
		return u.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
	}
}
