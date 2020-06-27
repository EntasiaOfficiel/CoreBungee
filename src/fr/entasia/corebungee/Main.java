package fr.entasia.corebungee;

import fr.entasia.apis.sql.SQLConnection;
import fr.entasia.corebungee.commands.base.*;
import fr.entasia.corebungee.commands.other.AntibotCmd;
import fr.entasia.corebungee.commands.other.BotSyncCmd;
import fr.entasia.corebungee.commands.troll.AdminCmd;
import fr.entasia.corebungee.commands.troll.HeroCmd;
import fr.entasia.corebungee.listeners.Base;
import fr.entasia.corebungee.listeners.SocketSpecials;
import fr.entasia.corebungee.utils.BungeePlayer;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Main extends Plugin{


	public static Random r = new Random();

	public static Main main;
	public static boolean joinquit=true;
	public static String lockdown;
	public static SQLConnection sql;
	public static ServerInfo hubServer;

	public static Configuration configuration;
	public static File configFile;

	public static LuckPermsApi lpAPI;
	public static ConfigurationProvider provider;

	public static Map<UUID, String> msgs = new HashMap<>();
	public static Map<String, ProxiedPlayer> vanishs = new HashMap<>();
	public static HashMap<String, BungeePlayer> playerCache = new HashMap<>();
	public static List<UUID> staffchat = new ArrayList<>();


	@Override
	public void onEnable() {
		try {
			main = this;
			lpAPI = LuckPerms.getApi();
			hubServer = getProxy().getServerInfo("hub");
			getLogger().info("Activation du plugin...");

			sql = new SQLConnection("corebungee", "playerdata");
//			SQLUpdate.ps = sql.connection.prepareStatement("SELECT * from global.safelist");

			getProxy().getPluginManager().registerCommand(this, new MsgCmd("msg"));
			getProxy().getPluginManager().registerCommand(this, new WhoisCmd("whois"));
			getProxy().getPluginManager().registerCommand(this, new GLockCmd("glockdown"));
			getProxy().getPluginManager().registerCommand(this, new PingCmd("ping"));
			getProxy().getPluginManager().registerCommand(this, new FakeJoinCmd("fakejoin"));
			getProxy().getPluginManager().registerCommand(this, new FakeLeaveCmd("fakeleave"));
			getProxy().getPluginManager().registerCommand(this, new CTimeCmd("ctime"));
			getProxy().getPluginManager().registerCommand(this, new ForceKickCmd("forcekick"));
			getProxy().getPluginManager().registerCommand(this, new HelpopCmd("helpop"));
			getProxy().getPluginManager().registerCommand(this, new IPCheckCmd("ipcheck"));
			getProxy().getPluginManager().registerCommand(this, new JoinCmd("join"));
			getProxy().getPluginManager().registerCommand(this, new StaffChatCmd("s"));
			getProxy().getPluginManager().registerCommand(this, new ReplyCmd("r"));
			getProxy().getPluginManager().registerCommand(this, new ReplyCmd("reply"));
			getProxy().getPluginManager().registerCommand(this, new AlertCmd("alert"));
			getProxy().getPluginManager().registerCommand(this, new HubCmd("hub"));
			getProxy().getPluginManager().registerCommand(this, new HubCmd("lobby"));
			getProxy().getPluginManager().registerCommand(this, new HubCmd("lob"));
			getProxy().getPluginManager().registerCommand(this, new HubCmd("lobby"));
			getProxy().getPluginManager().registerCommand(this, new HubCmd("lob"));

			getProxy().getPluginManager().registerCommand(this, new AntibotCmd("antibot"));
			getProxy().getPluginManager().registerCommand(this, new BotSyncCmd("botsync"));
			getProxy().getPluginManager().registerCommand(this, new BotSyncCmd("botlink"));

			getProxy().getPluginManager().registerCommand(this, new AdminCmd("admin"));
			getProxy().getPluginManager().registerCommand(this, new HeroCmd("herobrine"));


			getProxy().getPluginManager().registerListener(this, new Base());

			// PARTIE FICHIER CONFIG
			if (!getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
			configFile = new File(getDataFolder(), "config.yml");

			if (!configFile.exists()) {
				try (InputStream in = getResourceAsStream("config.yml")) {
					Files.copy(in, configFile.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
			try {
				configuration = provider.load(configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

//			String token = configuration.getString("token");
//			if(token!=null&&!token.equals(""))JDABot.init(token);

			// FIN PARTIE FICHIER CONFIG

			lockdown = configuration.getString("lockdown");
			if (lockdown.equals("")) lockdown = null;


			ResultSet rs = sql.connection.prepareStatement("SELECT * from global.vanishs").executeQuery();
			while(rs.next()){
				vanishs.put(rs.getString("name"), null);
			}

			SocketSpecials.init();

			getLogger().info("Plugin activé !");
		}catch(Throwable e){
			e.printStackTrace();
			getLogger().info("LE SERVEUR VA S'ETEINDRE !");
			ProxyServer.getInstance().stop();
		}
	}

	public static String getSuffix(ProxiedPlayer player) {
		User user = lpAPI.getUser(player.getUniqueId());
		if(user==null)return "§7Inconnu";

		Contexts contexts = lpAPI.getContextManager().getApplicableContexts(player);
		MetaData metaData = user.getCachedData().getMetaData(contexts);

		if(metaData.getSuffix()==null)return "";
		else return metaData.getSuffix().replace("&", "§");
	}

	public static String formatPlayer(ProxiedPlayer player) {
		return getSuffix(player)+" "+player.getDisplayName();
	}

	public static String isOnline(ServerInfo server) {
		try{
	       java.net.Socket s = new java.net.Socket();
	       s.connect(server.getAddress(), 15);
	       s.close();
			if(server.getPlayers().size() == 1)
				return server.getPlayers().size() + " connecté";
			else
				return server.getPlayers().size() + " connectés";
	     }catch(IOException e){
			return "Hors ligne";
	    }
	}

	public static BungeePlayer getPlayer(ProxiedPlayer p){
		return getPlayer(p.getName());
	}
	public static BungeePlayer getPlayer(String name){
		BungeePlayer bp = playerCache.get(name);
		if(bp==null){
			bp = new BungeePlayer();
			playerCache.put(name, bp);
			try{
				ResultSet rs = sql.fastSelectUnsafe("SELECT discord_id FROM global WHERE name=?", name);
				if(rs.next()){

				}
			}catch(SQLException e){
				e.printStackTrace();
				sql.broadcastError();
			}
		}
		return bp;
	}
}
