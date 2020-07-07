package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SendCmd extends Command {
    public SendCmd(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            if(p.hasPermission("entasia.send")) {
                if(args.length == 2){
                    ProxiedPlayer player = Main.main.getProxy().getPlayer(args[0]);
                    if(player == null){
                        p.sendMessage(new ChatComponent("§cErreur : Joueur invalide").create());
                    } else{
                        ServerInfo server = Main.main.getProxy().getServers().get(args[1]);
                        if(server == null){
                            p.sendMessage(new ChatComponent("§cErreur : Serveur invalide").create());
                        } else{
                            player.connect(server);
                            //player.sendMessage(new ChatComponent("§7Vous avez été send au "+server.getName() +" par "+p.getDisplayName()).create());
                        }
                    }
                }else{
                    p.sendMessage(new ChatComponent("§cSyntaxe : /send <player> <server>").create());
                }
            }
        }
    }
}
