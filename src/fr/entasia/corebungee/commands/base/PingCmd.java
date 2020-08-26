package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PingCmd extends Command {

	public PingCmd(String name) {
		super(name);
	}

	public String ping(int a){
		if(a<100) return "§a"+a;
		else if(a>325) return "§c"+a;
		else return "§3"+a;

	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length==0){
			if(sender instanceof ProxiedPlayer) {
				ProxiedPlayer p = (ProxiedPlayer) sender;
				p.sendMessage(ChatComponent.create("§6Ton ping actuel : " + ping(p.getPing())));
			}
		}else{
			ProxiedPlayer target = Main.main.getProxy().getPlayer(args[0]);
			if(target==null)
				sender.sendMessage(ChatComponent.create("§c"+args[0]+" n'existe pas ou n'est pas connecté !"));
			else
				sender.sendMessage(ChatComponent.create("§6Ping actuel de §3"+args[0]+" §6 : "+ping(target.getPing())));
		}
	}
}
