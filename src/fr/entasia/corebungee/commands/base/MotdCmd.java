package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MotdCmd extends Command {
    public MotdCmd(String name) {
        super(name);

    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            if(p.hasPermission("entasia.motd")) {
                if(args.length >= 1){
                    StringBuilder motdBuilder = new StringBuilder();
                    for(String s : args){
                        motdBuilder.append(s).append(" ");
                    }
                    String motd = motdBuilder.toString();
                    Main.updateMotd(motd.replace("&","§"));

                }else{
                    p.sendMessage(new ChatComponent("§cSyntaxe : /setmotd <motd>").create());
                }
            }
        }
    }
}
