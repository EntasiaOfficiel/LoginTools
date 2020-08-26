package fr.entasia.logintools;

import fr.entasia.apis.sql.SQLConnection;
import fr.entasia.logintools.commands.*;
import fr.entasia.logintools.listeners.BaseListeners;
import fr.entasia.logintools.listeners.ProtectionListeners;
import fr.entasia.logintools.utils.ConsoleFilter;
import fr.entasia.logintools.utils.Crypto;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

	public static Main main;
	public static SQLConnection sqlConnection;

	public static HashMap<Character, ArrayList<String>> easyPasses = new HashMap<>();

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

			File p = new File(getDataFolder(), "easyPasses.txt");
			if(p.exists()){
				BufferedReader reader = new BufferedReader(new FileReader(p));
				String line;
				ArrayList<String> list;
				while((line = reader.readLine())!=null){
					list = easyPasses.computeIfAbsent(line.charAt(0), k -> new ArrayList<>());
					list.add(line);
				}
			}else{
				getLogger().warning("Création du fichier easyPasses.txt");
				p.createNewFile();
			}

			getServer().getPluginManager().registerEvents(new BaseListeners(), this);
			getServer().getPluginManager().registerEvents(new ProtectionListeners(), this);

			new TaskMsg().runTaskTimer(this, 0, 140); // 7 * 20 = 140

			org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
			logger.addFilter(new ConsoleFilter());

			delFiles("playerdata");
			delFiles("stats");
			delFiles("advancements");

			getLogger().info("Plugin chargé avec succès !");
		}catch(Throwable e){
			e.printStackTrace();
			getLogger().info("LE SERVEUR VA S'ARRETER");
			Bukkit.getServer().shutdown();
		}
	}

	public static void delFiles(String dir) throws IOException {
		File[] files  = new File(Bukkit.getWorlds().get(0).getName()+"/"+dir).listFiles();
		if(files!=null){
			for(File lf : files){
				if(!lf.delete())throw new IOException("File not deleted");
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