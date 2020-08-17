package fr.entasia.corebungee.commands.staff;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.apis.utils.TextUtils;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.ChatColor;
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
				sender.sendMessage(ChatComponent.create("§cSyntaxe : §c/alert <message>"));
			else{
				String msg = TextUtils.setColors(TextUtils.formatMessage(String.join(" ", args), ChatColor.GOLD));
				Main.main.getProxy().broadcast(
						ChatComponent.create("§d§b §d§l﴾§bEnta§7sia§d§l﴿ §c/§4!§c\\ §6 "+ msg +" §c/§4!§c\\"));
			}
		}else sender.sendMessage(ChatComponent.create("§cTu n'as pas accès à cette commande !"));
	}
}
