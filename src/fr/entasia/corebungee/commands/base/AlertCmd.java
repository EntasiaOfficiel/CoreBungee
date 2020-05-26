package fr.entasia.corebungee.commands.base;

import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class AlertCmd extends Command {

	public AlertCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("entasia.alert")) {
			if(args.length==0)
				sender.sendMessage("§cSyntaxe §8» §c/alert <message>");
			else{
				String msg = String.join(" ", args).replace("&", "§");
				Main.main.getProxy().broadcast("§d§b §d§l﴾§bEnta§7sia§d§l﴿ §c/§4!§c\\ §6 "+ msg +" §c/§4!§c\\");
			}
		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");
	}
}
