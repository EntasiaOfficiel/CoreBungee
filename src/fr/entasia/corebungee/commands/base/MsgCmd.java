package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.TextUtils;
import fr.entasia.corebungee.Main;
import me.leoko.advancedban.manager.PunishmentManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;

public class MsgCmd extends Command {

	public MsgCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length >= 2){
			ProxiedPlayer target = Main.main.getProxy().getPlayer(args[0]);
			if(target == null||(Main.vanishs.containsKey(target.getName())&&!target.hasPermission("mod.vanish.bypass"))) {
				sender.sendMessage("§7" + args[0] + " n'est pas connecté !");
			} else if(target == sender){
				sender.sendMessage("§cVous ne pouvez pas vous msg vous même...");
			} else{
				String msg = TextUtils.formatMessage(String.join(" ", Arrays.copyOfRange(args, 1, args.length)), ChatColor.WHITE).replace("&", "§");
				ProxiedPlayer p;
				if (sender instanceof ProxiedPlayer){
					p = (ProxiedPlayer) sender;
					if(PunishmentManager.get().isMuted(p.getUniqueId().toString())) sender.sendMessage("§7Tu es mute !");
					else{
						Main.msgs.put(p.getUniqueId(), target.getDisplayName());
						Main.msgs.put(target.getUniqueId(), p.getDisplayName());
						sender.sendMessage("§6MSG §8» " + Main.getSuffix(p) + "§b moi §3-> " + Main.formatPlayer(target) + " §7| §f" + msg);
						target.sendMessage("§6MSG §8» " + Main.formatPlayer(p) + "§3 -> " + Main.getSuffix(target) + "§b moi§7 | §f" + msg);
					}
				}else {
					Main.msgs.put(null, target.getDisplayName());
					Main.msgs.put(target.getUniqueId(), null);
					sender.sendMessage("§6MSG §8» §bmoi §3-> " + Main.formatPlayer(target) + " §7| §f" + msg);
					target.sendMessage("§6MSG §8» §cConsole §3 -> " + Main.getSuffix(target) + "§b moi§7 | §f" + msg);
				}
			}
		} else sender.sendMessage("§cSyntaxe §8» §c/msg <joueur> <message>");

	}

}
