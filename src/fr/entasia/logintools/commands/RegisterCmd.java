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

import java.sql.SQLException;

import static fr.entasia.logintools.Main.main;

public class RegisterCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		Player p = (Player) sender;
		LoginData ld = Utils.LoginDater.get(p.getUniqueId());
		if (ld == null)
			p.sendMessage("§cTes données d'authentifications sont en cours de chargement !");
		else if(ld.password == null){
			if(args.length==2) {
				if(args[0].equals(args[1])) {
					if (args[0].length() < 6)
						p.sendMessage("§cTon mot de passe doit faire au minimum 7 caractères !");
					else {
						new BukkitRunnable() {
							@Override
							public void run() {
								String hash;
								try {
									hash = Crypto.hashPassword(args[0]);
								} catch (SecurityException e) {
									e.printStackTrace();
									p.sendMessage("§cUne erreur s'est produite lors de ton enregistrement ! Contacte un membre du Staff");
									return;
								}
								try {
									if(Main.sqlConnection.fastUpdateUnsafe("UPDATE global set passwd=? where uuid =?", Crypto.genBDD(hash), p.getUniqueId())==0){
										throw new SQLException("User not in database");
									}
								} catch (SQLException e) {
									e.printStackTrace();
									p.sendMessage("§cUne erreur s'est produite lors de ton enregistrement ! Contacte un membre du Staff");
									return;
								}
								ld.password = hash;
								ld.auth = true;
								p.sendMessage("§aEnregistrement fait avec succès ! §6Téléportation au lobby..");
								Utils.tpLobby(p);
							}
						}.runTaskAsynchronously(main);
					}
				}else p.sendMessage("§cLes deux mots de passe ne sont pas les mêmes !");
			}else {
				if (args.length == 1)
					p.sendMessage("§cTu dois taper ton mot de passe deux fois après la commande /register !");
				p.sendMessage("§cSyntaxe : /register <mot de passe> <mot de passe>");
			}
		}else{
			if(ld.auth)
				p.sendMessage("§cTu est déja authentifié ! utilise la §eNether Star§c dans ton inventaire pour te téléporter au lobby si tu est coincé ici");
			else
				p.sendMessage("§cTu est déja enregistré ! Utilise la commande /login <mot de passe>");
		}
		return true;
	}
}