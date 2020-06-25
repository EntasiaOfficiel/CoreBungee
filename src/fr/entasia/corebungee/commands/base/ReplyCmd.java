package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.apis.utils.TextUtils;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCmd extends Command {

	private final String cmd;

	public ReplyCmd(String name) {
		super(name);
		cmd = name;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 0)
			sender.sendMessage(ChatComponent.create("§cSyntaxe §8» §c/"+cmd+" <message>"));
		else {
			if(sender instanceof ProxiedPlayer){
				ProxiedPlayer player = (ProxiedPlayer)sender;
				if (Main.msgs.containsKey(player.getUniqueId())){
					String msg = TextUtils.formatMessage(String.join(" ", args), ChatColor.WHITE);
					msg = TextUtils.setColors(msg);
					String reply = Main.msgs.get(player.getUniqueId());
					if(reply==null){
						sender.sendMessage(ChatComponent.create("§6MSG §8» " + Main.getSuffix(player) + "§b moi §3-> §cConsole §7| §f" + msg));
						Main.main.getProxy().getConsole().sendMessage(ChatComponent.create("§6MSG §8» " + Main.formatPlayer(player) + "§3 -> §b moi§7 | §f" + msg));
						Main.msgs.put(null, player.getDisplayName());
					}else{
						ProxiedPlayer replier = Main.main.getProxy().getPlayer(reply);
						if (replier==null) {
							sender.sendMessage(ChatComponent.create("§6MSG §8» §c"+reply+" n'est plus connecté !"));
							Main.msgs.remove(player.getUniqueId());
						}else{
							sender.sendMessage(ChatComponent.create("§6MSG §8» " + Main.getSuffix(player) + "§b moi §3-> " + Main.formatPlayer(replier) + " §7| §f" + msg));
							replier.sendMessage(ChatComponent.create("§6MSG §8» " + Main.formatPlayer(player) + "§3 -> " + Main.getSuffix(replier) + "§b moi§7 | §f" + msg));
							Main.msgs.put(replier.getUniqueId(), player.getDisplayName());
						}
					}

				}else sender.sendMessage(ChatComponent.create("§7Vous n'avez personne à qui répondre"));

			}else {
				if (Main.msgs.containsKey(null)) {
					String msg = TextUtils.formatMessage(String.join(" ", args), ChatColor.WHITE);
					msg = TextUtils.setColors(msg);
					ProxiedPlayer replier = Main.main.getProxy().getPlayer(Main.msgs.get(null));
					if (replier == null) {
						sender.sendMessage(ChatComponent.create("§6MSG §8» §c"+Main.msgs.get(null)+" n'est plus connecté !"));
						Main.msgs.remove(null);
					} else {
						Main.main.getProxy().getConsole().sendMessage(ChatComponent.create("§6MSG §8» §bmoi §3-> " + Main.formatPlayer(replier) + " §7| §f" + msg));
						replier.sendMessage(ChatComponent.create("§6MSG §8» §cConsole§3 -> §bmoi§7 | §f" + msg));
						Main.msgs.put(replier.getUniqueId(), null);
					}
				} else sender.sendMessage(ChatComponent.create("§7Vous n'avez personne à qui répondre"));
			}
		}
	}
}
