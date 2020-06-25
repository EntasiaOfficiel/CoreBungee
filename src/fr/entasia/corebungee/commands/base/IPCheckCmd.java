package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IPCheckCmd extends Command {

	public IPCheckCmd(String name) {
		super(name);
		
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission("staff.checkip")) {
			if (args.length == 1) {
				ProxiedPlayer a = Main.main.getProxy().getPlayer(args[0]);
				byte[] ip;
				if(a==null){
					try{
						ip = InetAddress.getByName(args[0]).getAddress();
					}catch(UnknownHostException|NullPointerException e){
						sender.sendMessage(ChatComponent.create("§cArgument invalide !"));
						return;
					}
				}else ip = a.getAddress().getAddress().getAddress();

				List<ProxiedPlayer> paccept = new ArrayList<>();
				for (ProxiedPlayer p : Main.main.getProxy().getPlayers())
					if (Arrays.equals(p.getAddress().getAddress().getAddress(), ip)) paccept.add(p);

				if (paccept.size() == 0)
					sender.sendMessage(ChatComponent.create("§cAucun joueur ne possède cette adresse ip"));
				else {
					sender.sendMessage(ChatComponent.create("§aLes joueurs qui possèdent cette ip sont :"));
					paccept.forEach(b -> sender.sendMessage(ChatComponent.create("§8- §7" + b.getDisplayName())));
				}
			} else sender.sendMessage(ChatComponent.create("§cSyntaxe §8» §c/whoisip <ip/pseudo>"));
		}else sender.sendMessage(ChatComponent.create("§cTu n'as pas accès à cette commande !"));
	}
}
