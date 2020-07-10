package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ServerCmd extends Command {
    public ServerCmd(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            if(p.hasPermission("bungee.server")) {
                if(args.length == 1){
                    ServerInfo server = Main.main.getProxy().getServers().get(args[0]);
                    if(server == null){
                        p.sendMessage(new ChatComponent("§cCe serveur est invalide !").create());
                    } else{
                        p.connect(server);
                        p.sendMessage(new ChatComponent("§aTu as rejoint le serveur "+server.getName()).create());
                    }
                }else  p.sendMessage(new ChatComponent("§cSyntaxe : /server <server>").create());
            }else p.sendMessage(ChatComponent.create("§cTu n'as pas accès à cette commande !"));
        }
    }
}
