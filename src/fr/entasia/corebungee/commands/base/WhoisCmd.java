package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WhoisCmd extends Command {

	public WhoisCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission("staff.whois")) {
			if (args.length == 1) {
				ProxiedPlayer target = Main.main.getProxy().getPlayer(args[0]);
				if (target == null)
					sender.sendMessage(ChatComponent.create("§cErreur : " + args[0] + " n'est pas connecté ou n'existe pas!"));
				else {
					sender.sendMessage(ChatComponent.create("§e» §6Informations Bungee : §a" + target.getName()));
					ChatComponent comp = new ChatComponent("§bUUID : §a" + target.getUniqueId().toString());
					comp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, target.getUniqueId().toString()));
					comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponent.create("§7Clique pour copier !")));
					sender.sendMessage(comp.create());
					if (sender.hasPermission("restricted.whoisip")) {
						String ip = target.getAddress().getAddress().getHostAddress();
						comp = new ChatComponent("§bIP : §a" + ip);
						comp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + ip));
						comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponent.create("§7Clique pour copier !")));
						sender.sendMessage(comp.create());
					} else sender.sendMessage(ChatComponent.create("§bIP : §cPermissions manquantes"));

					comp = new ChatComponent("§bServeur actuel : §a" + target.getServer().getInfo().getName());
					comp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/server " + target.getServer().getInfo().getName()));
					comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponent.create("§7Clique pour aller sur ce serveur !")));
					target.sendMessage(comp.create());
					sender.sendMessage(ChatComponent.create("§bGrade : §a" + Main.getSuffix(target)));
				}
			}else sender.sendMessage(ChatComponent.create("§cSyntaxe §8» §c/forcekick <joueur>"));
		}else sender.sendMessage(ChatComponent.create("§cTu n'as pas accès à cette commande !"));
	}
}
