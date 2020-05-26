package fr.entasia.logintools.commands;

import fr.entasia.logintools.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginPlCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if(p.isOp()){
			if(args.length>0){
				if(args[0].equalsIgnoreCase("reload")){
					Main.loadConfig();
					p.sendMessage("§aConfiguration rechargée !");
				}
			}else p.sendMessage("§cMet un argument ! ( rlstaffs )");
		}else p.sendMessage("&cTu n'as pas accès à cette commande !");
		return true;
	}
}