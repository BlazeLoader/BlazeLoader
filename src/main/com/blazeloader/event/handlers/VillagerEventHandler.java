package com.blazeloader.event.handlers;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.api.entity.profession.Professions;
import com.blazeloader.event.mixin.Mix;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DifficultyInstance;

public class VillagerEventHandler {
	private static boolean lockProfession = false;
		
	public static void eventOnInitialSpawn(EntityVillager sender, CallbackInfoReturnable<IEntityLivingData> info, DifficultyInstance difficulty, IEntityLivingData livingdata) {
		if (lockProfession) return;
		lockProfession = true;
		info.setReturnValue(sender.onInitialSpawn(difficulty, livingdata));
		lockProfession = false;
	}
	
	public static void eventGetProfession(EntityVillager sender, CallbackInfoReturnable<Integer> info) {
		info.setReturnValue(Professions.getProfessionId(sender));
	}
	
	public static void eventSetProfession(EntityVillager sender, CallbackInfo info, int professionId) {
		if (!lockProfession) return;
		lockProfession = false;
		info.cancel();
		sender.setProfession(Professions.instance().randomProfessionId(sender.getEntityWorld().rand));
	}
	
	public static void eventPopulateBuyingList(EntityVillager sender, CallbackInfo info) {
		Mix.intercept(Professions.instance().populateBuyingList(sender), info);
	}
	
	public static void eventOnGrowingAdult(EntityVillager sender, CallbackInfo info) {
		Mix.intercept(Professions.instance().onGrownUp(sender), info);
	}
	
    public static void eventCheckMatingConditions(EntityVillager sender, CallbackInfoReturnable<Boolean> info, boolean fixed, int markiplier) {
    	Mix.intercept(Professions.instance().checkMatingConditions(sender, fixed, markiplier), info);
    }
    
    public static void eventSetAdditionalAITasks(EntityVillager sender, CallbackInfo info) {
    	Mix.intercept(Professions.instance().setAdditionalAITasks(sender), info);
    }
    
    public static void eventGetDisplayName(EntityVillager sender, CallbackInfoReturnable<ITextComponent> info) {
    	Mix.intercept(Professions.instance().getDisplayName(sender), info);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void eventGetEntityTexture(EntityVillager sender, CallbackInfoReturnable info) {
		Mix.intercept(Professions.instance().getResourceLocation(sender), info);
    }
}
