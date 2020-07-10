package fr.entasia.corebungee.commands.base;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.lang.reflect.Field;

public class MaxPlayersCmd extends Command {

    public static Field playerLimit;

    public MaxPlayersCmd(String name) {
        super(name);
        try{
            playerLimit = Main.main.getProxy().getConfig().getClass().getSuperclass().getDeclaredField("playerLimit");
            playerLimit.setAccessible(true);
        }catch(ReflectiveOperationException e){
            e.printStackTrace();
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("entasia.maxplayers")) {
            if(args.length == 1){
                try {
                    int maxPlayers = Integer.parseInt(args[0]);
                    playerLimit.set(Main.main.getProxy().getConfig(), maxPlayers);
                    sender.sendMessage(new ChatComponent("§aLe nombre maximum de joueurs à bien été défini à "+maxPlayers +" !").create());
                    sender.sendMessage(new ChatComponent("§3(Cette action est temporaire)").create());
                } catch (NumberFormatException ignore) {
                    sender.sendMessage(new ChatComponent("§cNombre invalide !").create());
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                    sender.sendMessage(new ChatComponent("§cUne erreur s'est produite ! (Reflection)").create());
                }

            }else{
                sender.sendMessage(new ChatComponent("§cSyntaxe : /setmaxplayer <maxPlayer>").create());
            }
        }
    }
}
