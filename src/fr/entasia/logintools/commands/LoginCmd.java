package fr.entasia.logintools.commands;

import fr.entasia.logintools.Main;
import fr.entasia.logintools.Utils;
import fr.entasia.logintools.utils.Crypto;
import fr.entasia.logintools.utils.LoginData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static fr.entasia.logintools.Main.main;

public class LoginCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		Player p = (Player) sender;
		LoginData ld = Utils.LoginDater.get(p.getName());
		if (ld == null) p.sendMessage("§cTes données d'authentifications sont en cours de chargement !");
		else if (ld.password == null)
			p.sendMessage("§cTu n'es pas inscrit sur §bEnta§7sia §c ! Utilise /register <mot de passe> <mot de passe>");
		else if (ld.auth)
			p.sendMessage("§cTu est déja authentifié ! utilise la §eNether Star§c dans ton inventaire pour te téléporter au lobby si tu est coincé ici");
		else if (args.length == 1) {
			if(Utils.ATF.containsKey(p.getName().toLowerCase())&&!Arrays.equals(Utils.ATF.get(p.getName().toLowerCase()), p.getAddress().getAddress().getAddress())) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH.dd", new Locale("fr", "FR"));
				if(args[0].equals(simpleDateFormat.format(new Date()))){
					p.sendMessage("§aLe mot de passe de protection est valide ! Tu peux maintenant entrer ton mot de passe");
					Utils.ATF.put(p.getName().toLowerCase(), p.getAddress().getAddress().getAddress());
				}else p.sendMessage("§c§lCe compte est protégé. Merci d'entrer le mot de passe de protection !");
			}else{
				new BukkitRunnable() {
					@Override
					public void run() {
						if(Crypto.comparePassword(ld.password, args[0])){
							ld.auth = true;
							p.sendMessage("§aMot de passe correct ! §6Téléportation au lobby..");
							Utils.tpLobby(p);
						}else p.sendMessage("§cLe mot de passe est incorrect !");
					}
				}.runTaskAsynchronously(main);
			}
		}else p.sendMessage("§cSyntaxe : /login <mot de passe>");
		return true;
	}
}