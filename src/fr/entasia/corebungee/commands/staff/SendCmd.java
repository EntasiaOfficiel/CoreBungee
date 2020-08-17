package fr.entasia.corebungee.commands.staff;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public class SendCmd extends Command implements TabExecutor {
    public SendCmd(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            if(p.hasPermission("bungee.send")) {
                if(args.length == 2){
                    ProxiedPlayer player = Main.main.getProxy().getPlayer(args[0]);
                    if(player == null){
                        p.sendMessage(ChatComponent.create("§cCe joueur est invalide !"));
                    } else{
                        ServerInfo server = Main.main.getProxy().getServerInfo(args[1]);
                        if(server == null){
                            p.sendMessage(ChatComponent.create("§cCe serveur est invalide !"));
                        } else{
                            player.connect(server);
                            //player.sendMessage(new ChatComponent("§aTu as été envoyé au serveur §e"+server.getName() +"§a par §e"+p.getDisplayName()).create()+"§a !");
                        }
                    }
                }else{
                    p.sendMessage(ChatComponent.create("§cSyntaxe : /send <player> <server>"));
                }
            }else p.sendMessage(ChatComponent.create("§cTu n'as pas accès à cette commande !"));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        ArrayList<String> comp = new ArrayList<>();
        if(args.length==1) {
            args[0] = args[0].toLowerCase();
            for(ProxiedPlayer p : Main.main.getProxy().getPlayers()) {
                if(p.getDisplayName().toLowerCase().startsWith(args[0]))comp.add(p.getDisplayName());
            }
        }else if(args.length==2){
            args[1] = args[1].toLowerCase();
            for(String server : Main.main.getProxy().getServersCopy().keySet()){
                if(server.toLowerCase().startsWith(args[1])){
                    comp.add(server);
                }
            }
        }
        return comp;
    }
}
