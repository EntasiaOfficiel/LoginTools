package fr.entasia.logintools;

import fr.entasia.apis.socket.SocketClient;
import fr.entasia.logintools.utils.LoginData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

import static fr.entasia.logintools.Main.main;

public class Utils {

	public static HashMap<String, LoginData> LoginDater = new HashMap<>();
	public static HashMap<String, byte[]> ATF = new HashMap<>();

	public static Location spawn;


	public static void tpLobby(Player p){
		SocketClient.sendData("BungeeCord login "+p.getName());
		new BukkitRunnable() {
			public void run() {
				ItemStack a = new ItemStack(Material.NETHER_STAR, 1);
				ItemMeta b = a.getItemMeta();
				b.setDisplayName("§eTéléportation au lobby");
				a.setItemMeta(b);
				p.getInventory().setItem(4, a);
				p.sendMessage("§6Oh ! Tu ne sembles pas avoir été téléporté au lobby ! Voici une §eNether Star§6 qui retentera de t'y téléporter "+
						"dès que tu cliquera dessus ! Si l'attente dure trop longtemps, contacte un membre du Staff");
			}
		}.runTaskLater(main, 40);
	}


}
