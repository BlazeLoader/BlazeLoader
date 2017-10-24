package com.blazeloader.api.entity.profession;

import java.util.ArrayList;
import java.util.Random;

import com.blazeloader.api.privileged.IEntityVillager;
import com.blazeloader.api.privileged.ITasked;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipeList;

public final class Professions {
	private ArrayList<IProfession> professions = new ArrayList<IProfession>();
	
	private static final Professions instance = new Professions();
	
	public static Professions instance() {
		return instance;
	}
	
	private Professions() { }
	
	public int randomProfessionId(Random rand) {
		return rand.nextInt(professions.size() + 6);
	}
	
	public boolean hasId(int id) {
		return id - 6 < professions.size();
	}
	
	public IProfession get(int id) {
		if (!hasId(id) || id < 6) return null;
		return professions.get(id - 6);
	}
	
	public void add(IProfession profession) {
		professions.add(profession);
	}
	
	public static int getProfessionId(EntityVillager villager) {
		return Math.max((Integer)villager.getDataManager().get(((IEntityVillager)villager).getProfessionDataParameter()),0);
	}
	
	public Boolean checkMatingConditions(EntityVillager villager, boolean fixedMarkiplier, int markiplier) {
		IProfession profession = get(getProfessionId(villager));
		if (profession == null) return null;
		InventoryBasic inventory = villager.getVillagerInventory();
		int i = inventory.getSizeInventory();
		if (!fixedMarkiplier) {
			markiplier = profession.getStackMultiplier();
		}
		while (i-- > 0) {
			if (profession.checkMatingConditions(inventory.getStackInSlot(i), markiplier)) {
				return true;
			}
		}
		return false;
	}
	
	public ITextComponent getDisplayName(EntityVillager villager) {
		IProfession profession = get(getProfessionId(villager));
		if (profession == null) return null;

		String nameTag = villager.getCustomNameTag();
		if (nameTag != null && nameTag.length() > 0) {
			return new TextComponentString(nameTag);
		}
		
		villager.getRecipes(null);
		return profession.getDisplayName();
	}
	
	public ResourceLocation getResourceLocation(EntityVillager villager) {
		IProfession profession = get(getProfessionId(villager));
		return profession == null ? null : profession.getResourceLocation();
	}
	
	public boolean onGrownUp(EntityVillager villager) {
		IProfession profession = get(getProfessionId(villager));
		if (profession == null) return false;
		profession.setAdditionalAItasks(((ITasked)villager).getAITasks());
		return true;
	}
	
	public boolean populateBuyingList(EntityVillager villager) {
		IProfession profession = get(getProfessionId(villager));
		if (profession == null) return false;
		IEntityVillager pVillager = null;
		try {
			pVillager = (IEntityVillager)villager;
		} catch (Throwable e) {e.printStackTrace();}
		
		int careerId = pVillager.getCareer();
		int careerLevel = pVillager.getCareerLevel();
		
        if (careerId != 0 && careerLevel != 0) {
            careerLevel++;
        } else {
            careerId = profession.getNewCareer(villager.getRNG());
            pVillager.setCareer(careerId);
            careerLevel = 1;
        }
        pVillager.setCareerLevel(careerLevel);
        
        MerchantRecipeList buyingList = pVillager.getMerchentRecipes();
        
        if (buyingList == null) {
            buyingList = new MerchantRecipeList();
            pVillager.setMerchentRecipes(buyingList);
        }
        
        ITradeList[] trades = profession.getTradeList(careerId, careerLevel);
        for (ITradeList i : trades) {
            i.modifyMerchantRecipeList(buyingList, villager.getRNG());
        }
		
		return true;
	}
	
	public boolean setAdditionalAITasks(EntityVillager villager) {
		IProfession profession = get(getProfessionId(villager));
		if (profession == null) return false;
		if (!((IEntityVillager)villager).areAdditionalTasksSet()) {
			profession.setAdditionalAItasks(((ITasked)villager).getAITasks());
		}
		return true;
	}
}
