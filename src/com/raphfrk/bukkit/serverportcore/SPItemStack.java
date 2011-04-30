package com.raphfrk.bukkit.serverportcore;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@Entity
public class SPItemStack implements Serializable {

	private static final long serialVersionUID = 1L;

	public SPItemStack() {
		
	}

	public SPItemStack(String playerName, boolean armour, int slot, ItemStack itemStack) {
		this.playerName = playerName;
		this.armour = armour;
		this.slot = slot;
		setStack(itemStack);
	}
	
	@Id
	private int id;
	
	private String playerName;
	
	private int typeId;
	private int amount;
	private short damage;
	private int slot;
	private boolean armour;
	
	@Override
	public String toString() {
		return "[" + playerName + ": " + amount + " of " + typeId + " (" + damage + ") @" + slot + " (" + (armour?("ARMOUR)"):"MAIN)");
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	public int getTypeId() {
		return typeId;
	}
	
	public void setDamage(short damage) {
		this.damage = damage;
	}
	
	public short getDamage() {
		return damage;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public void setArmour(boolean armour) {
		this.armour = armour;
	}
	
	public boolean isArmour() {
		return armour;
	}
	
	public ItemStack getStack() {
		if(typeId == 0) {
			return null;
		}
		return new ItemStack(typeId, amount, damage);
	}
	
	public void setStack(ItemStack itemStack) {
		if(itemStack == null) {
			typeId = 0;
			damage = 0;
			amount = 0;
		} else {
			typeId = itemStack.getTypeId();
			damage = itemStack.getDurability();
			amount = itemStack.getAmount();
		}	
	}
	
	public void copy(SPItemStack itemStack) {
		this.playerName = itemStack.getPlayerName();
		setStack(itemStack.getStack());
	}
	
	public static SPItemStack[] getInventory(Player player) {
		
		String playerName = player.getName();
		PlayerInventory inv = player.getInventory();
		ItemStack[] main = inv.getContents();
		ItemStack[] armour = inv.getArmorContents();
		
		ArrayList<SPItemStack> itemStacks = new ArrayList<SPItemStack>();
		
		for(int cnt=0;cnt<main.length;cnt++) {
			if(main[cnt] != null && main[cnt].getTypeId() != 0) {
				itemStacks.add(new SPItemStack(playerName, false, cnt, main[cnt]));
			}
		}
		
		for(int cnt=0;cnt<armour.length;cnt++) {
			if(armour[cnt] != null && armour[cnt].getTypeId() != 0) {
				itemStacks.add(new SPItemStack(playerName, true, cnt, armour[cnt]));
			}
		}
		
		inv.clear();
		return itemStacks.toArray(new SPItemStack[0]);
	}
	
	public static ItemStack getItemStack(PlayerInventory playerInventory, boolean armour, int slot) {

		if(armour) {
			if(slot==0) {
				return playerInventory.getBoots();
			} else if(slot==1) {
				return playerInventory.getLeggings();
			} else if(slot==2) {
				return playerInventory.getChestplate();
			} else if(slot==3) {
				return playerInventory.getHelmet();
			}
		} else {
			ItemStack[] main = playerInventory.getContents();
			if(slot<0 || slot>=main.length) {
				return null;
			}
			return main[slot];
		}
		return null;
	}
	
	final static ItemStack blankArmour = new ItemStack(0, 0, (short)0);
	
	public static void setItemStack(PlayerInventory playerInventory, ItemStack itemStack, boolean armour, int slot) {
		
		boolean clear = itemStack == null || itemStack.getTypeId() == 0;

		ItemStack armourSetting = clear?blankArmour:itemStack;
		
		if(armour && !clear) {
			if(slot==0) {
				playerInventory.setBoots(armourSetting);
			} else if(slot==1) {
				playerInventory.setLeggings(armourSetting);
			} else if(slot==2) {
				playerInventory.setChestplate(armourSetting);
			} else if(slot==3) {
				playerInventory.setHelmet(armourSetting);
			}
		} else {
			if(armour) { // Hack grr
				slot += playerInventory.getSize();
			}
			if(clear) {
				playerInventory.clear(slot);
			} else {
				playerInventory.setItem(slot, itemStack);
			}
		}
	}
	
	public static boolean addItemStack(Player player, SPItemStack stack) {
		
		if(stack == null || player == null) {
			return false;
		}

		PlayerInventory inv = player.getInventory();
		ItemStack current = getItemStack(inv, stack.isArmour(), stack.getSlot());

		if(current == null || current.getTypeId()==0) {
			setItemStack(inv, stack.getStack(), stack.isArmour(), stack.getSlot());
			return true;
		}
		
		return false;
		
	}
	
	public static SPItemStack[] addInventory(Player player, SPItemStack[] stacks) {
		
		ArrayList<SPItemStack> itemStacks = new ArrayList<SPItemStack>(stacks.length);
		
		for(int cnt=0;cnt<stacks.length;cnt++) {
			if(!addItemStack(player, stacks[cnt])) {
				itemStacks.add(stacks[cnt]);
			}
		}
		
		return itemStacks.toArray(stacks);
	}
	
}
