package fr.entasia.corebungee.commands.troll;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.apis.utils.ServerUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class HeroCmd extends Command {

	public HeroCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		ServerUtils.permMsg("log.troll", "§6Troll : §e"+sender.getName()+" à activé la commande /herobrine !");
		sender.sendMessage(ChatComponent.create("§fI'm here.."));
	}
}
