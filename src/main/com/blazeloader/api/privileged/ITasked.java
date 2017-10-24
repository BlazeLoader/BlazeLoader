package com.blazeloader.api.privileged;

import net.minecraft.entity.ai.EntityAITasks;

public interface ITasked {
	/**
	 * Gets the ai tasks related to this entity.
	 */
	EntityAITasks getAITasks();
}
