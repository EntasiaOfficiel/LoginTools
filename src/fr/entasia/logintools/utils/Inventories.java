package fr.entasia.logintools.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Inventories {


	public static void openCrackPremInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, "§3Compte Premium/Craqué");

		ItemStack item = new ItemStack(Material.STAINED_CLAY,1,(byte)1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6Choisir un compte Craqué");
		List<String> lore = new ArrayList<>();
		lore.add("§eCliquez ici pouravoir un compte en mode");
		lore.add("§eCraqué , et entrer votre mot de passe à chaque connection");
		lore.add("§e( Ceci est le mode par défaut )");

		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(10, item);


		item = new ItemStack(Material.STAINED_CLAY,1,(byte)5);
		meta = item.getItemMeta();
		lore.clear();
		lore.add("§aCliquez ici pour passer votre compte en mode Premium");
		lore.add("§aVous n'aurez plus besoin d'entrer votre mot de passe à la connection");
		lore.add("");
		lore.add("§2vous ne pourrez plus vous connecter sur votre");
		lore.add("§2compte avec des versions non-payées de Minecraft !");
		lore.add("§2Si vous doutez, utilisez un compte Craqué");

		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(16, item);


		item = new ItemStack(Material.BOOK,1,(short)1);
		meta = item.getItemMeta();
		meta.setDisplayName("§9Important :");
		lore.clear();
		lore.add("§3Passer votre compte en mode Premium vous permet de vous connecter sans utiliser de mot de passe");
		lore.add("");
		lore.add("§b§lAttention: §3Si vous activez le mode Premium , vous ne pourrez");
		lore.add("§3plus vous connecter sur votre compte avec des versions non-payées de Minecraft !");
		lore.add("§3Si vous doutez, utilisez un compte Craqué");

		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(22, item);

		p.openInventory(inv);
	}
}
