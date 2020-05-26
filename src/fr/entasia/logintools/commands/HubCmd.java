package fr.entasia.logintools.commands;

import fr.entasia.logintools.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))return false;
		sender.sendMessage("§6Ah.. Tu es actuellement en zone de login, et non au lobby ! Tu me permet ? je vais essayer de te téléporter au lobby..");
		Utils.tpLobby((Player) sender);
		return true;
	}
}