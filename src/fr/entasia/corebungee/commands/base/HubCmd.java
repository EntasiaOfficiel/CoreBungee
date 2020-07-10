package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCmd extends Command {

	public HubCmd(String... names) {
		super(names[0], null, names);
	}

	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer))return;
		ProxiedPlayer p = (ProxiedPlayer)sender;
		String a = p.getServer().getInfo().getName().toLowerCase();
		if(a.equals("login")) p.chat("/hub");
		else if(a.equals("hub")) p.sendMessage(ChatComponent.create("§cTu es déja au lobby !"));
		else{
			p.sendMessage(ChatComponent.create("§6Téléportation au lobby en cours..."));
			p.connect(Main.hubServer);
		}
	}
}
