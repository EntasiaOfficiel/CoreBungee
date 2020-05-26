package fr.entasia.corebungee.commands.base;

import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;


public class GLockCmd extends Command {

	public GLockCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("entasia.glockdown.use")) {
			if (args.length != 0 && (args[0].equals("status") || args[0].equals("statut"))) {
				if (Main.lockdown == null)
					sender.sendMessage("§6§lGlobal§6 Lockdown §8» §6Actuellement §cDésactivé§6 !");
				else
					sender.sendMessage("§6§lGlobal§6 Lockdown §8» §6Actuellement §aActivé§6 !");
			} else {
				if (Main.lockdown == null) {
					if(args.length==0) {
						Main.lockdown = "aucune raison définie";
						sender.sendMessage("§6§lGlobal§6 Lockdown §8» §aActivé !");
					}else {

						Main.lockdown = String.join(" ", args);
						sender.sendMessage("§6§lGlobal§6 Lockdown §8» §aActivé ! Raison : §6"+Main.lockdown);
					}
				} else {
					Main.lockdown = null;
					sender.sendMessage("§6§lGlobal§6 Lockdown §8» §cDésactivé !");
				}
				Main.configuration.set("lockdown", Main.lockdown);
				try{
					Main.provider.save(Main.configuration, Main.configFile);
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");
	}
}
