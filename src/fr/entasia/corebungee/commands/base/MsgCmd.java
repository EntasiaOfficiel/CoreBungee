package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.apis.utils.TextUtils;
import fr.entasia.corebungee.Main;
import fr.entasia.sanctions.utils.MuteEntry;
import fr.entasia.sanctions.utils.SanctionsAPI;
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
				sender.sendMessage(ChatComponent.create("§7" + args[0] + " n'est pas connecté !"));
			} else if(target == sender){
				sender.sendMessage(ChatComponent.create("§cVous ne pouvez pas vous msg vous même..."));
			} else{
				String msg = TextUtils.formatMessage(String.join(" ", Arrays.copyOfRange(args, 1, args.length)), ChatColor.WHITE);
				msg = TextUtils.setColors(msg);
				ProxiedPlayer p;
				if (sender instanceof ProxiedPlayer){
					p = (ProxiedPlayer) sender;
					MuteEntry se = SanctionsAPI.getMuteEntry(p.getName());
					if(se==null){
						Main.msgs.put(p.getUniqueId(), target.getDisplayName());
						Main.msgs.put(target.getUniqueId(), p.getDisplayName());
						sender.sendMessage(ChatComponent.create("§6MSG §8» " + Main.getSuffix(p) + "§b moi §3-> " + Main.formatPlayer(target) + " §7| §f" + msg));
						target.sendMessage(ChatComponent.create("§6MSG §8» " + Main.formatPlayer(p) + "§3 -> " + Main.getSuffix(target) + "§b moi§7 | §f" + msg));
					}else{
						sender.sendMessage(ChatComponent.create("§cTu es encore muté pour §8"+TextUtils.secondsToTime(se.remaning())+"§c !"));
					}
				}else {
					Main.msgs.put(null, target.getDisplayName());
					Main.msgs.put(target.getUniqueId(), null);
					sender.sendMessage(ChatComponent.create("§6MSG §8» §bmoi §3-> " + Main.formatPlayer(target) + " §7| §f" + msg));
					target.sendMessage(ChatComponent.create("§6MSG §8» §cConsole §3 -> " + Main.getSuffix(target) + "§b moi§7 | §f" + msg));
				}
			}
		} else sender.sendMessage(ChatComponent.create("§cSyntaxe : §c/msg <joueur> <message>"));

	}

}
