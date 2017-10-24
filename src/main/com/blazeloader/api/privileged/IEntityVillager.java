package com.blazeloader.api.privileged;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.village.MerchantRecipeList;

/**
 * Accessor methods for private fields on EntityVillager.
 */
public interface IEntityVillager {
	
	public DataParameter<Integer> getProfessionDataParameter();
	
	/**Returns true if this villager has performed its initial setup of AI Tasks and tradelists.*/
	public boolean areAdditionalTasksSet();
	
	/**Gets the current level of trade achieved by this villager.*/
	public int getCareerLevel();
	
	/**Sets this villager's trade level to the given value.
	 * @param level Trade value, must be positive.*/
	public void setCareerLevel(int level);
	
	/**Gets the id for this villager's current career.*/
	public int getCareer();
	
	/**Sets this villager's career to the given id.*/
	public void setCareer(int career);
	
	/**Gets the collection of recipes this villager currently has to offer.*/
	public MerchantRecipeList getMerchentRecipes();
	
	/**Sets this villager's recipe list.*/
	public void setMerchentRecipes(MerchantRecipeList list);
}
