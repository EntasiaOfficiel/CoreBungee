package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ForceKickCmd extends Command {

	public ForceKickCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("admin.forcekick")) {
			if(args.length == 1) {
				ProxiedPlayer kicked = Main.main.getProxy().getPlayer(args[0]);
				if(kicked==null)
					sender.sendMessage(ChatComponent.create("§7Kick §8» §7"+args[0]+" n'existe pas ou n'est pas connecté !"));
				else{
					sender.sendMessage(ChatComponent.create("§7Kick §8» §7Vous avez kick " + args[0]));
					kicked.disconnect(new TextComponent("§7Vous avez été kick du serveur !"));
				}
			}else sender.sendMessage(ChatComponent.create("§cSyntaxe : §c/forcekick <joueur>"));
		}else sender.sendMessage(ChatComponent.create("§cTu n'as pas accès à cette commande !"));
	}
}
