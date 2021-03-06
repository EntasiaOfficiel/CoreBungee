package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CTimeCmd extends Command {


	public CTimeCmd(String name){
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length ==0)
			sender.sendMessage(ChatComponent.create("§cSyntaxe : §c/ctime <player>"));
		else{
			Main.sql.checkConnect();
			ResultSet rs;
			try {
				rs = Main.sql.connection.prepareStatement("SELECT time_week, time_month, time_total FROM playerdata.global WHERE name = '"+args[0]+"'").executeQuery();
				String weekTime, monthTime, totalTime;

				if(rs.next()){
					weekTime = rs.getString(1);
					monthTime = rs.getString(2);
					totalTime = rs.getString(3);
					sender.sendMessage(ChatComponent.create("§9Temps §8» : §7" + ((ProxiedPlayer)sender).getDisplayName()));
					sender.sendMessage(ChatComponent.create("§7Temps de connexion hebdomadaire : " + weekTime));
					sender.sendMessage(ChatComponent.create("§7Temps de connexion mensuel : " + monthTime));
					sender.sendMessage(ChatComponent.create("§7Temps de connexion total : " + totalTime));
				}else{
					sender.sendMessage(ChatComponent.create("§9Temps §8» §cCette personne n'existe pas !"));
				}
			}catch (SQLException e){
				e.printStackTrace();
				sender.sendMessage(ChatComponent.create("§9Temps §8» §cUne erreur interne s'est produite !"));
			}
		}
	}
}
