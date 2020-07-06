package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MaxPlayerCmd extends Command {
    public MaxPlayerCmd(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            if(p.hasPermission("entasia.maxPlayers")) {
                if(args.length == 1){
                    try {
                        int i = Integer.parseInt(args[0]);
                        Main.maxPlayer =i;


                    } catch (NumberFormatException nfe) {
                        p.sendMessage(new ChatComponent("§7Veuillez entrer un argument valide").create());
                    }

                }else{
                    p.sendMessage(new ChatComponent("§cSyntaxe : /setmaxplayer <maxPlayer>").create());
                }
            }
        }
    }
}
