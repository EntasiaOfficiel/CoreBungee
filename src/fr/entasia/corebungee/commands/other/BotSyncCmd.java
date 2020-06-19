package fr.entasia.corebungee.commands.other;

import fr.entasia.apis.socket.SocketClient;
import fr.entasia.corebungee.Main;
import fr.entasia.corebungee.utils.BungeePlayer;
import fr.entasia.corebungee.utils.DiscordSyncTask;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BotSyncCmd extends Command {

	public BotSyncCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer))return;
		if(sender.hasPermission("entasia.botsync")) {
			BungeePlayer bp = Main.getPlayer(sender.getName());
			if(bp.discordID==null){
				try{
					ResultSet rs = Main.sql.fastSelectUnsafe("SELECT discord_id FROM global WHERE name=?", bp.p.getName());
					if(rs.next()){
						bp.discordID = rs.getString("discord_id");
						if(bp.discordID.equals(""))bp.discordID = null;
					}else bp.discordID = null;
				}catch(SQLException e){
					e.printStackTrace();
					Main.sql.broadcastError();
					bp.p.sendMessage("§cUne erreur s'est produite ! Contacte un Membre du Staff");
					return;
				}
			}

			if(bp.discordID==null) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < 6; i++) {
					if (Main.r.nextBoolean()) {
						sb.append(Main.r.nextInt(10));
					} else {
						sb.append((char) (Main.r.nextInt(26) + (Main.r.nextBoolean() ? 97 : 65)));
					}
				}

				bp.discordID = sb.toString();

				SocketClient.sendData("EBH sync " + bp.discordID+" "+bp.p.getName());

				bp.p.sendMessage("§6Voici ton code de synchronisation : §c" + bp.discordID);
				bp.p.sendMessage("§6Tu as 30 secondes pour le l'entrer sur Discord à l'aide de la commande §c.sync " + bp.discordID);
				bp.p.sendMessage("§cNe partage ce code à personne ! (Il est recommandé de d'envoyer la commande en MP au bot §6Entasia Bot Helper#9194§c)");
				DiscordSyncTask.runNew(bp);
			}else if(bp.discordID.length()==7){
				bp.p.sendMessage("§6Ton code de synchronisation est encore valide ! §c" + bp.discordID);
				bp.p.sendMessage("§6Entre le sur Discord à l'aide de la commande §c.sync " + bp.discordID);
			}else{
				if(args.length==0){
					sender.sendMessage("§cArguments disponibles :");
					sender.sendMessage("§c-unlink");
				}else if(args[0].equals("unlink")){
					if(Main.sql.fastUpdate("UPDATE playerdata.global SET discord_id=null WHERE name=?", bp.p.getName())==-1){
						sender.sendMessage("§cUne erreur s'est produite ! Contacte un membre du Staff !");
					}else{
						sender.sendMessage("§aTu as été délié de ton compte Discord !");
						bp.discordID = null;
					}
				}else sender.sendMessage("§cArgument invalide !");
			}
		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");

	}
}
