package fr.entasia.corebungee.listeners;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.apis.socket.SocketClient;
import fr.entasia.apis.socket.SocketEvent;
import fr.entasia.corebungee.Main;
import fr.entasia.corebungee.utils.BungeePlayer;

public class SocketSpecials {

	public static void init() throws Throwable {


		SocketClient.addListener(new SocketEvent("vanish") {
			@Override
			public void onEvent(String[] data) {
				if (data[0].equals("0")) {
					Main.vanishs.remove(data[1]);
				}else if (data[0].equals("1")) {
					if (!Main.vanishs.containsKey(data[1])) {
						Main.vanishs.put(data[1], Main.main.getProxy().getPlayer(data[1]));
					}
				}
			}
		});

		SocketClient.addListener(new SocketEvent("syncok") {
			@Override
			public void onEvent(String[] data) {
				BungeePlayer bp = Main.getPlayer(data[0]);
				bp.discordID = null;
				bp.discordTask.cancel();
				bp.discordTask = null;
				if(bp.isOnline())bp.p.sendMessage(ChatComponent.create("§aTon compte à été lié avec succès ! :D"));
			}
		});

	}
}
