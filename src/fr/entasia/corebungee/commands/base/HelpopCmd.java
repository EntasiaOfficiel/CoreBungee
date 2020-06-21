package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.ChatComponent;
import fr.entasia.apis.ServerUtils;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HelpopCmd extends Command {

	public HelpopCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer))return;
		if(args.length==0)
			sender.sendMessage(ChatComponent.create("§cSyntaxe §8» §c/helpop <message>"));
		else{
			ProxiedPlayer p = (ProxiedPlayer) sender;
			String msg = String.join(" ", args);
			ServerUtils.permMsg("staff.helpop.recieve","§eMessage Staff §8» §7"+p.getServer().getInfo().getName()+" §8» "+Main.formatPlayer(p)+" §d» §f"+msg);
		}
	}

}
