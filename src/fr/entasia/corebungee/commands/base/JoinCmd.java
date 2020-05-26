package fr.entasia.corebungee.commands.base;

import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class JoinCmd extends Command {

	public JoinCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer))return;
		if (sender.hasPermission("entasia.join")) {
			if (args.length == 1) {
				ProxiedPlayer to = Main.main.getProxy().getPlayer(args[0]);
				if (to==null) sender.sendMessage("§cErreur : " + args[0] + " n'est pas connecté ou n'existe pas!");
				else {
					ServerInfo target = to.getServer().getInfo();
					if (((ProxiedPlayer) sender).getServer().getInfo().getName().equalsIgnoreCase(target.getName()))
						sender.sendMessage("§dJoin §8» §7Vous êtes déjà sur le serveur de "+args[0]+" !");
					else {
						((ProxiedPlayer) sender).connect(target);
						sender.sendMessage("§dJoin §8» §7Vous avez rejoint le serveur de "+args[0]+" !");
					}
				}
			} else sender.sendMessage("§cSyntaxe §8» §c/join <joueur>");
		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");
	}
}
