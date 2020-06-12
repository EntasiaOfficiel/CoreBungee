package fr.entasia.corebungee.commands.troll;

import fr.entasia.apis.ChatComponent;
import fr.entasia.apis.ServerUtils;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.TimeUnit;

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
