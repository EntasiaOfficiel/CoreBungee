package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.TextUtils;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCmd extends Command {

	private String cmd;

	public ReplyCmd(String name) {
		super(name);
		cmd = name;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 0)
			sender.sendMessage("§cSyntaxe §8» §c/"+cmd+" <message>");
		else {
			if(sender instanceof ProxiedPlayer){
				ProxiedPlayer player = (ProxiedPlayer)sender;
				if (Main.msgs.containsKey(player.getUniqueId())){
					String msg = String.join(" ", args).replace("<3", "❤").replace("&", "§");
					if(player.hasPermission("chat.color"))msg = msg.replace("&", "§");
					String reply = Main.msgs.get(player.getUniqueId());
					if(reply==null){
						sender.sendMessage("§6MSG §8» " + Main.getSuffix(player) + "§b moi §3-> §cConsole §7| §f" + msg);
						Main.main.getProxy().getConsole().sendMessage("§6MSG §8» " + Main.formatPlayer(player) + "§3 -> §b moi§7 | §f" + msg);
						Main.msgs.put(null, player.getDisplayName());
					}else{
						ProxiedPlayer replier = Main.main.getProxy().getPlayer(reply);
						if (replier==null) {
							sender.sendMessage("§6MSG §8» §c"+reply+" n'est plus connecté !");
							Main.msgs.remove(player.getUniqueId());
						}else{
							sender.sendMessage("§6MSG §8» " + Main.getSuffix(player) + "§b moi §3-> " + Main.formatPlayer(replier) + " §7| §f" + msg);
							replier.sendMessage("§6MSG §8» " + Main.formatPlayer(player) + "§3 -> " + Main.getSuffix(replier) + "§b moi§7 | §f" + msg);
							Main.msgs.put(replier.getUniqueId(), player.getDisplayName());
						}
					}

				}else sender.sendMessage("§7Vous n'avez personne à qui répondre");

			}else {
				if (Main.msgs.containsKey(null)) {
					String msg = TextUtils.formatMessage(String.join(" ", args), ChatColor.WHITE).replace("&", "§").replace("&", "§");
					ProxiedPlayer replier = Main.main.getProxy().getPlayer(Main.msgs.get(null));
					if (replier == null) {
						sender.sendMessage("§6MSG §8» §c"+Main.msgs.get(null)+" n'est plus connecté !");
						Main.msgs.remove(null);
					} else {
						Main.main.getProxy().getConsole().sendMessage("§6MSG §8» §bmoi §3-> " + Main.formatPlayer(replier) + " §7| §f" + msg);
						replier.sendMessage("§6MSG §8» §cConsole§3 -> §bmoi§7 | §f" + msg);
						Main.msgs.put(replier.getUniqueId(), null);
					}
				} else sender.sendMessage("§7Vous n'avez personne à qui répondre");
			}
		}
	}
}
