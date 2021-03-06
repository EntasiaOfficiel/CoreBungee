package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;

public class DiscordCmd extends Command {

	public DiscordCmd(String... names) {
		super(names[0], null, names);
	}

	public void execute(CommandSender sender, String[] args) {
		ChatComponent cc = new ChatComponent("§6Notre Discord :§d https://discord.gg/sYRPk3dtxg");
		cc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/sYRPk3dtxg"));
		sender.sendMessage(cc.create());
	}
}
