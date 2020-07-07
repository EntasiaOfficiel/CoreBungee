package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class FindCmd extends Command {
    public FindCmd(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            if(p.hasPermission("entasia.find")) {
                if(args.length == 1){
                    ProxiedPlayer player = Main.main.getProxy().getPlayer(args[0]);
                    if(player == null){
                        p.sendMessage(new ChatComponent("§cErreur : Joueur invalide").create());
                    } else{
                        p.sendMessage(new ChatComponent("§7" + args[0] + " se trouve sur le serveur "+player.getServer().getInfo().getName()).create());
                    }
                }else{
                    p.sendMessage(new ChatComponent("§cSyntaxe : /find <player>").create());
                }
            }
        }

    }
}
