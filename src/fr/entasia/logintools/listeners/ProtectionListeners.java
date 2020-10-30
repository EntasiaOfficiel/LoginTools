package fr.entasia.logintools.listeners;

import fr.entasia.logintools.Utils;
import fr.entasia.logintools.utils.LoginData;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.util.Vector;

public class ProtectionListeners implements Listener {



	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onWeather(WeatherChangeEvent e) {
		e.setCancelled(e.toWeatherState()); // en gros si c'est true c'est la pluie
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(e.getPlayer().getGameMode()== GameMode.CREATIVE&&e.getPlayer().isOp()){
			LoginData ld = Utils.loginDatas.get(e.getPlayer().getName());
			e.setCancelled(ld==null||!ld.auth);
		}else e.setCancelled(true);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(e.getPlayer().getGameMode()== GameMode.CREATIVE&&e.getPlayer().isOp()){
			LoginData ld = Utils.loginDatas.get(e.getPlayer().getName());
			e.setCancelled(ld==null||!ld.auth);
		}else e.setCancelled(true);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}


	public static String[] commands = new String[]{"l", "lo", "login", "reg", "register"};

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		LoginData ld = Utils.loginDatas.get(e.getPlayer().getName());
		if(ld==null||!ld.auth){
			String cmd = e.getMessage().split(" ")[0].substring(1).toLowerCase();
			for(String i : commands){
				if(i.equals(cmd))return;
			}
			e.setCancelled(true);
		}
	}


//
//	@EventHandler
//	public void InventoryClick(InventoryClickEvent e) {
//		if(!(e.getWhoClicked() instanceof Player)||e.getAction() == InventoryAction.NOTHING)return;
//		Player p = (Player) e.getWhoClicked();
//		LoginData ld = Utils.LoginDater.get(p.getUniqueId());
//		if (e.getInventory().getName()!=null&&e.getInventory().getName().equalsIgnoreCase("§3Compte Premium/Craqué")&&e.getCurrentItem() != null&&e.getCurrentItem().hasItemMeta()&&e.getCurrentItem().getItemMeta().hasDisplayName()) {
//			e.setCancelled(true);
//			if(ld==null){
//				p.closeInventory();
//				p.sendMessage("§cTes données d'authentification sont encore en cours de chargement !");
//				return;
//			}
//			switch(e.getCurrentItem().getItemMeta().getDisplayName()){
//				case "§6Rester sur un compte Craqué":
//					p.closeInventory();
//					p.sendMessage("§6Tu as choisi une authentification craqué ! Tu dois maintenant t'enregistrer sur le serveur");
//					p.sendMessage("§6Tu voulais choisir une authentification premium ? Déconnecte et reconnecte toi sans t'enregistrer pour faire réapparaitre le menu");
//					break;
//				case "§2Passer sur un compte Premium":
//					p.closeInventory();
//					p.sendMessage("§2Passage sur un compte premium en cours.. ( vous allez être kick du serveur )");
//					try {
//						SQLConnection.checkConnect();
//						PreparedStatement ps = SQLConnection.connection.prepareStatement("update playerdata.global set ArgonSize = ? where uuid=?");
//	//					ps.setString(1, Utils.PToUid.get(p.getName()));
//						ps.setString(2, p.getUniqueId().toString());
//						ps.executeQuery();
//					} catch (SQLException e2) {
//						e2.printStackTrace();
//					}
//					break;
//				case "§2Passer sur un compte Premium§a":
//					p.closeInventory();
//					p.sendMessage("§2Passage sur un compte premium en cours.. ( tu vas être kick du serveur )");
//	//				if(UUIDChanging.changeUUID(p.getUniqueId().toString(), Utils.PToUid.get(p.getName()))) p.kickPlayer("§aMise à jour de ton compte effectuée !");
//	//				else p.sendMessage("§4Erreur : §cLa version premium de ton compte est bannie !");
//					break;
//			}
//		}else e.setCancelled(ld==null||!ld.auth);
//	}

	@EventHandler
	public void anyMovement(PlayerMoveEvent e){
		if(e.getTo().distance(Utils.spawn)>50){
			e.setCancelled(true);
			Vector vec = new Vector(Utils.spawn.getX()-e.getTo().getX(), Utils.spawn.getY()-e.getTo().getY(), Utils.spawn.getZ()-e.getTo().getZ());
			vec.multiply(0.06);
			vec.setY(vec.getY()*4);
			e.getPlayer().setVelocity(vec);
		}

	}

}
