package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.ChatComponent;
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
			sender.sendMessage("§cSyntaxe §8» §c/ctime <player>");
		else{
			Main.sqlConnection.checkConnect();
			ResultSet rs;
			try {
				rs = Main.sqlConnection.connection.prepareStatement("SELECT time_week, time_month, time_total FROM playerdata.global WHERE name = '"+args[0]+"'").executeQuery();
				String weekTime, monthTime, totalTime;

				if(rs.next()){
					weekTime = rs.getString(0);
					monthTime = rs.getString(1);
					totalTime = rs.getString(2);
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
