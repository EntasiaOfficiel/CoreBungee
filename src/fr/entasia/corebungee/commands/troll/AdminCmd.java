package fr.entasia.corebungee.commands.troll;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.apis.ServerUtils;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.TimeUnit;

public class AdminCmd extends Command {

	public AdminCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		ServerUtils.permMsg("log.troll", "§6Troll : §e"+sender.getName()+" à activé la commande /admin !");
		sender.sendMessage(ChatComponent.create("§cPassage admin en cours.."));
		Main.main.getProxy().getScheduler().schedule(Main.main, () -> sender.sendMessage(
				ChatComponent.create("§c§kMM§4§lUNE ERREUR INTERNE EST SURVENUE, SERVEUR CORROMPU§c§kMM")),
				6, TimeUnit.SECONDS);
	}
}
