package fr.entasia.logintools.listeners;

import fr.entasia.apis.ChatComponent;
import fr.entasia.apis.PlayerUtils;
import fr.entasia.apis.socket.SocketClient;
import fr.entasia.logintools.Main;
import fr.entasia.logintools.TaskMsg;
import fr.entasia.logintools.Utils;
import fr.entasia.logintools.utils.Crypto;
import fr.entasia.logintools.utils.LoginData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static fr.entasia.logintools.Main.main;

public class BaseListeners implements Listener {

	private static double random() {
		return (double)Math.round(Math.random()*160)/10;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		Bukkit.broadcastMessage("Login §8» §7"+e.getPlayer().getName()+" §8» §r"+e.getMessage());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPreJoin(AsyncPlayerPreLoginEvent e) {
		try{
			ResultSet rs = Main.sqlConnection.fastSelectUnsafe("SELECT passwd from global where uuid=?", e.getUniqueId());
			if(rs.next()){
				LoginData ld = new LoginData();
				ld.password = rs.getString("passwd");
				if(ld.password!=null)ld.password = Crypto.getfromBDD(ld.password);
				Utils.LoginDater.put(e.getName(), ld);
			}else{
				e.setKickMessage("§cErreur lors de l'enregistrement/chargement de votre profil ! # 2");
				e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			}
		}catch(SQLException e2){
			e.setKickMessage("§cErreur lors de l'enregistrement/chargement de votre profil ! # 1");
			e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			e2.printStackTrace();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage("");
		Player p = e.getPlayer();
		Location nsp = Utils.spawn.clone();
		nsp.setX(nsp.getX()+random()-8);
		nsp.setZ(nsp.getZ()+random()-8);
		nsp.setY(nsp.getY()+1);
		if(nsp.getBlock().getType()==Material.AIR){
			nsp.setY(nsp.getY()-1);
			p.teleport(nsp);
		}
		else p.teleport(Utils.spawn);
		p.setPlayerListHeaderFooter(ChatComponent.create("§3Bienvenue sur §bEnta§7sia!\n Ping : §b"+ PlayerUtils.getPing(p, false) +" \n §7§m-------------------§r"),
				ChatComponent.create("§7§m-------------------§r\n §3Discord: §b/discord"));
		p.setPlayerListName("§7"+p.getName());
		p.getInventory().clear();
		TaskMsg.sendMsg(p);
		LoginData ld = Utils.LoginDater.get(p.getName());

		new BukkitRunnable() {
			public void run() {
				if(ld==null)return;
				cancel();
				new BukkitRunnable() {
					public void run() {
						LoginData ldd = Utils.LoginDater.get(p.getName());
						if(ldd!=null&&ld==ldd&&!ld.auth)
							p.kickPlayer("§7Tu as mis trop longtemps à te login ! §cMot de passe oublié ? §7Passe sur Discord§b https://discord.gg/7U5E2yQ");
					}
				}.runTaskLater(main, 1000); // 50*20 = 1000

//				if(ld.password==null){
//					ld.puuid = UUIDChanging.getpremiumUUID(p.getName());
//					if(ld.puuid!=null) Inventories.openCrackPremInv(p);
//				}
			}
		}.runTaskTimerAsynchronously(main, 0, 15);



	}
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage("");
		Utils.LoginDater.remove(e.getPlayer().getName());
	}


	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		ItemStack it = e.getItem();
		if (it != null && it.getType() == Material.NETHER_STAR && it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equals("§eTéléportation au lobby")){
			LoginData ld = Utils.LoginDater.get(e.getPlayer().getName());
			if(ld==null)return;
			long b = (new Date().getTime()-ld.itemcd);
			if (b < 6000) {
				b = (6000-b)/1000;
				if (b == 0) e.getPlayer().sendMessage("§cCooldown ! Attend encore 1 seconde");
				else e.getPlayer().sendMessage("§cCooldown ! Attend encore " + (b+1) + " secondes");
			}else{
				e.getPlayer().sendMessage("§eTentative de téléportation au lobby..");
				SocketClient.sendData("BungeeCord send "+e.getPlayer().getName()+" hub");
				ld.itemcd = new Date().getTime();

			}
		}
	}

//	@EventHandler
//	public void invClick(InventoryClickEvent e) {
//		if(e.getWhoClicked() instanceof Player&&e.getInventory().getName()!=null&&e.getInventory().getName().equals("§3Compte Premium/Craqué")){
//			e.setCancelled(true);
//			if(e.getCurrentItem()!=null&&e.getCurrentItem().getType()==Material.STAINED_CLAY&&e.getCurrentItem().getDurability()==5){
//				LoginData ld = Utils.LoginDater.get(e.getWhoClicked().getUniqueId());
//				if(ld==null)e.getWhoClicked().sendMessage("§cErreur lors du chargement de tes données ! ):");
//				else{
//					switch(UUIDChanging.changeUUID(e.getWhoClicked().getUniqueId().toString(), ld.puuid)){
//						case OKAY:
//							((Player) e.getWhoClicked()).kickPlayer("§aPassage en compte premium effectué ! Tu peux te reconnecter, et profiter du jeu !");
//							break;
//						case BANNED:
//							e.getWhoClicked().sendMessage("§cCe compte premium à été banni du serveur !");
//							break;
//						default:
//							e.getWhoClicked().sendMessage("§cOops , une erreur inattendue s'est produite ! Contacte un membre du Staff !");
//					}
//				}
//			}
//		}
//	}
}