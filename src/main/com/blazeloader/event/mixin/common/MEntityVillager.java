package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.api.entity.profession.MatingCondition;
import com.blazeloader.api.privileged.IEntityVillager;
import com.blazeloader.event.handlers.VillagerEventHandler;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;

@Mixin(EntityVillager.class)
public abstract class MEntityVillager extends EntityAgeable implements IMerchant, INpc, IEntityVillager {
	private MEntityVillager() { super(null); }
	
	@Accessor("PROFESSION") public abstract DataParameter<Integer> getProfessionDataParameter();
	
	@Accessor("buyingList") public abstract  MerchantRecipeList getMerchentRecipes();
	@Accessor("buyingList") public abstract void setMerchentRecipes(MerchantRecipeList list);
	
	@Accessor("careerId") public abstract int getCareer();
	@Accessor("careerId") public abstract void setCareer(int career);
	
	@Accessor("careerLevel") public abstract int getCareerLevel();
	@Accessor("careerLevel") public abstract void setCareerLevel(int level);
	
	@Accessor("areAdditionalTasksSet") public abstract boolean areAdditionalTasksSet();
	
	@Inject(method = "onInitialSpawn(Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/entity/IEntityLivingData;)Lnet/minecraft/entity/IEntityLivingData;", at = @At("HEAD"), cancellable = true)
	private void onOnInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata, CallbackInfoReturnable<IEntityLivingData> info) {
		VillagerEventHandler.eventOnInitialSpawn((EntityVillager)(Object)this, info, difficulty, livingdata);
	}
	
	@Inject(method = "setProfession(I)V", at = @At("HEAD"), cancellable = true)
	private void onSetProfession(int professionId, CallbackInfo info) {
		VillagerEventHandler.eventSetProfession((EntityVillager)(Object)this, info, professionId);
	}
	
	@Inject(method = "getProfession()I", at = @At("HEAD"), cancellable = true)
	private void onGetProfession(CallbackInfoReturnable<Integer> info) {
		VillagerEventHandler.eventGetProfession((EntityVillager)(Object)this, info);
	}
	
	@Inject(method = "onGrowingAdult()V", at = @At("HEAD"), cancellable = true)
	private void onOnGrowingAdult(CallbackInfo info) {
		VillagerEventHandler.eventOnGrowingAdult((EntityVillager)(Object)this, info);
	}
	
	@Inject(method = "hasEnoughFoodToBreed()Z", at = @At("HEAD"), cancellable = true)
	private void onHasEnoughItemsForOne(CallbackInfoReturnable<Boolean> info) {
		VillagerEventHandler.eventCheckMatingConditions((EntityVillager)(Object)this, info, MatingCondition.BREED);
	}
	
	@Inject(method = "canAbondonItems()Z", at = @At("HEAD"), cancellable = true)
	private void onHasEnoughItemsForTwo(CallbackInfoReturnable<Boolean> info) {
		VillagerEventHandler.eventCheckMatingConditions((EntityVillager)(Object)this, info, MatingCondition.GIVE);
	}
	
	@Inject(method = "wantsMoreFood()Z", at = @At("HEAD"), cancellable = true)
	private void onCheckMatingConditions(CallbackInfoReturnable<Boolean> info) {
		VillagerEventHandler.eventCheckMatingConditions((EntityVillager)(Object)this, info, MatingCondition.FEED);
	}
	
	@Inject(method = "getDisplayName()Lnet/minecraft/util/text/ITextComponent;", at = @At("HEAD"), cancellable = true)
	private void onGetDisplayName(CallbackInfoReturnable<ITextComponent> info) {
		VillagerEventHandler.eventGetDisplayName((EntityVillager)(Object)this, info);
	}
	
	@Inject(method = "readEntityFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("HEAD"), cancellable = true)
	private void onReadEntityFromNBT(NBTTagCompound tag, CallbackInfo info) {
		VillagerEventHandler.eventSetAdditionalAITasks((EntityVillager)(Object)this, info);
	}
	
	@Inject(method = "populateBuyingList()V", at = @At("HEAD"), cancellable = true)
	private void onPopulateBuyingList(CallbackInfo info) {
		VillagerEventHandler.eventPopulateBuyingList((EntityVillager)(Object)this, info);
	}
}
