package com.blazeloader.api.achievement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;

public class ApiAchievement {
    /**
     * Unlocks an achievement for the given player.
     *
     * @param player      Player who has achieved
     * @param achievement What the player has achieved
     */
    public static void unlockAchievement(EntityPlayer player, Advancement achievement) {
        
        if (player instanceof EntityPlayerMP) {
        	PlayerAdvancements advancements = ((EntityPlayerMP) player).getAdvancements();
        	AdvancementProgress progress = advancements.getProgress(achievement);
        	
        	if (progress.isDone()) return;
        	for (String s : progress.getRemaningCriteria()) {
                advancements.grantCriterion(achievement, s);
            }
        }
    }
    
    /**
     * The opposite of unlock achievement. If the player has previously unlocked the achievement will reset it to its initial state.
     * 
     * @param player		The player who has achieved
     * @param achievement	What the player has achieved
     */
    public static void lockAchievement(EntityPlayerMP player, Advancement achievement) {
		PlayerAdvancements advancements = ((EntityPlayerMP) player).getAdvancements();
		AdvancementProgress progress = advancements.getProgress(achievement);
		
		if (!progress.hasProgress()) return;
		for (String s : progress.getCompletedCriteria()) {
            advancements.revokeCriterion(achievement, s);
        }
    }
    
    /**
     * Checks if the player has unlocked the given achievement.
     * 
     * @param player		The player to check
     * @param achievement	The achievement to check for
     * @return	True if the player has that achievement, false otherwise.
     */
    public static boolean hasAchievementUnlocked(EntityPlayerMP player, Advancement achievement) {
    	return player.getAdvancements().getProgress(achievement).isDone();
    }
}
