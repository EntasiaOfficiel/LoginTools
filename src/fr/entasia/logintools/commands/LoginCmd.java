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

	public static MessageDigest SHA;


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		Player p = (Player) sender;
		LoginData ld = Utils.LoginDater.get(p.getUniqueId());
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
						boolean okay;
						if (ld.ex) {
							String salt = ld.password.split("\\$")[2];
							byte[] passhash = SHA.digest(args[0].getBytes());
							String password =  String.format("%0" + (passhash.length << 1) + "x", new BigInteger(1, passhash));
							passhash = SHA.digest((password+salt).getBytes());
							password =  String.format("%0" + (passhash.length << 1) + "x", new BigInteger(1, passhash));
							okay = ld.password.equals("$SHA$"+salt+"$"+password);
//							for(byte b : passhash){
//								a = Integer.toHexString(0xff & b);
//								if(a.length() == 1) sb.append(0);
//								sb.append(a);
//
//							}
//							StringBuilder sb = new StringBuilder("$SHA$"+a+"$");
//							System.out.println(sb.toString());
//							System.out.println(ld.password);
//							okay = sb.toString().equals(ld.password);
						}else okay = Crypto.comparePassword(ld.password, args[0]);
						if(okay){
							ld.auth = true;
							p.sendMessage("§aMot de passe correct ! §6Téléportation au lobby..");
							if(ld.ex) {
								p.sendMessage("§6Nous avons remarqué que ton mot de passe est stocké dans notre base de données dans un ancien format. Nous allons le déplacer dans un nouveau format plus sécurisé !");

								ld.password = Crypto.hashPassword(args[0]);
								ld.ex = false;
								try{
									Main.sqlConnection.fastUpdateUnsafe("UPDATE global set expasswd=null, passwd=? WHERE uuid=?", Crypto.genBDD(ld.password), p.getUniqueId());
								}catch(SQLException e){
									e.printStackTrace();
									p.sendMessage("§cErreur lors du déplacement de ton mot de passe ! Ton mot de passe à été gardé dans l'ancien format. Contacte un membre du staff");
								}
							}
							Utils.tpLobby(p);
						}else p.sendMessage("§cLe mot de passe est incorrect !");
					}
				}.runTaskAsynchronously(main);
			}
		}else p.sendMessage("§cSyntaxe : /login <mot de passe>");
		return true;
	}
}