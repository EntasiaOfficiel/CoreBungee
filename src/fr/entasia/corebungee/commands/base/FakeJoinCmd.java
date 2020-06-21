package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.ChatComponent;
import fr.entasia.apis.ServerUtils;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class FakeJoinCmd extends Command {

	public FakeJoinCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if(!(sender instanceof ProxiedPlayer))return;
		if(sender.hasPermission("entasia.fakemessages")){
			ProxiedPlayer p = (ProxiedPlayer) sender;
			ServerUtils.permMsg("staff.fakemessages", "§e" + p.getDisplayName() + " a utilisé le fakejoin");
			Main.main.getProxy().broadcast(ChatComponent.create("§aJoin §8»§7 " + Main.formatPlayer(p) + "§7 a rejoint §bEnta§7sia !"));
		}else sender.sendMessage(ChatComponent.create("§cTu n'as pas accès à cette commande !"));
	}
}