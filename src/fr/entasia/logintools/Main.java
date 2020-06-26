package fr.entasia.logintools;

import fr.entasia.apis.sql.SQLConnection;
import fr.entasia.logintools.commands.*;
import fr.entasia.logintools.listeners.BaseListeners;
import fr.entasia.logintools.listeners.ProtectionListeners;
import fr.entasia.logintools.utils.ConsoleFilter;
import fr.entasia.logintools.utils.Crypto;
import org.apache.commons.codec.digest.Crypt;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.security.MessageDigest;

public class Main extends JavaPlugin {

	public static Main main;
	public static SQLConnection sqlConnection;

	@Override
	public void onEnable() {
		try{

			main = this;
			saveDefaultConfig();
			loadConfig();

			Crypto.genSalt();

			getCommand("loginpl").setExecutor(new LoginPlCmd());
			getCommand("login").setExecutor(new LoginCmd());
			getCommand("register").setExecutor(new RegisterCmd());
			getCommand("spawn").setExecutor(new SpawnCmd());
			getCommand("hub").setExecutor(new HubCmd());

			sqlConnection = new SQLConnection("login", "playerdata");

			getServer().getPluginManager().registerEvents(new BaseListeners(), this);
			getServer().getPluginManager().registerEvents(new ProtectionListeners(), this);

			new TaskMsg().runTaskTimer(this, 0, 140); // 7 * 20 = 140

			getLogger().info("Plugin chargé avec succès !");
		}catch(Throwable e){
			e.printStackTrace();
			getLogger().info("LE SERVEUR VA S'ARRETER");
			Bukkit.getServer().shutdown();
		}

		org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
		logger.addFilter(new ConsoleFilter());

		File[] files  = new File(Bukkit.getWorlds().get(0).getName()+"/playerdata").listFiles();
		if(files!=null){
			for(File lf : files){
				lf.delete();
			}
		}
	}

	public static void loadConfig(){
		main.reloadConfig();
		Utils.spawn = new Location(Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")), Main.main.getConfig().getInt("spawn.x")+0.5,
						Main.main.getConfig().getInt("spawn.y")+0.2, Main.main.getConfig().getInt("spawn.z")+0.5);

		Utils.ATF.clear();
		for(String i : Main.main.getConfig().getStringList("staffs")){
			Utils.ATF.put(i.toLowerCase(), null);
		}
	}
}