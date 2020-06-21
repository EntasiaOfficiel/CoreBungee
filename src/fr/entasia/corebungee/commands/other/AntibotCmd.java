package fr.entasia.corebungee.commands.other;

import fr.entasia.apis.ChatComponent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class AntibotCmd extends Command {

	public AntibotCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("admin.antibot")) {
			if(args.length==0){
				sender.sendMessage(ChatComponent.create("§cArguments disponibles :"));
			}else {
				args[0] = args[0].toLowerCase();
			}
		}else sender.sendMessage(ChatComponent.create("§cTu n'as pas accès à cette commande !"));

	}
}
