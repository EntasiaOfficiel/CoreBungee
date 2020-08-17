package fr.entasia.corebungee.commands.staff;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.apis.utils.ServerUtils;
import fr.entasia.apis.utils.TextUtils;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffChatCmd extends Command {

	public StaffChatCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer))return;
		ProxiedPlayer p = (ProxiedPlayer) sender;
		if(p.hasPermission("staff.staffchat")) {
			if(args.length == 0){
				if(Main.staffchat.contains(p.getUniqueId())) {
					Main.staffchat.remove(p.getUniqueId());
					p.sendMessage(ChatComponent.create("§eVous parlez maintenant dans le chat normal !"));
				}else{
					Main.staffchat.add(p.getUniqueId());
					p.sendMessage(ChatComponent.create("§eVous parlez maintenant dans le §bStaff§3Chat §e!"));
				}
			}else{
				staffchatTalk(p, String.join(" ", args));
			}
		}else sender.sendMessage(ChatComponent.create("§cTu n'as pas accès à cette commande !"));
	}

	public static void staffchatTalk(ProxiedPlayer p, String msg){
		msg = TextUtils.setColors(TextUtils.formatMessage(msg, ChatColor.AQUA));
		ServerUtils.permMsg("staff.staffchat", "§bStaff§3Chat §8» §7"+p.getServer().getInfo().getName() +" §8» "+ Main.formatPlayer(p)+" §d» §b"+ msg);
	}
}
