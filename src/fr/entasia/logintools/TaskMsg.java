package fr.entasia.logintools;

import fr.entasia.logintools.utils.LoginData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class TaskMsg extends BukkitRunnable {

	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()){
			sendMsg(p);
		}
	}

	public static void sendMsg(Player p){
		LoginData ld = Utils.loginDatas.get(p.getName());
		if(ld!=null&&!ld.auth) {
			if (Utils.ATF.containsKey(p.getName().toLowerCase()) && !Arrays.equals(Utils.ATF.get(p.getName().toLowerCase()), p.getAddress().getAddress().getAddress())) {
				p.sendMessage("§4§lCe compte est protégé.");
				p.sendMessage("§c§lMerci d'entrer le mot de passe de protection ! §c( fourni dans les annonces staff , messages épinglés )");
			} else {
				if (ld.password == null)
					p.sendMessage("§bMerci de te register avec cette commande : §7/register <mot de passe> <mot de passe>");
				else p.sendMessage("§bMerci de te login avec cette commande : §7/login <mot de passe>");
			}
		}
	}

}
