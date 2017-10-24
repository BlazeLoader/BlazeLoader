package com.blazeloader.api.client.render;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;

/**
 * Blocks with this interface provide custom logic for rendering a TileEntity for their item representation.
 * <p>
 *  If they provide a TileEntity when placed in the world (see @code{BlockContainer}) this will specify logic for rendering there as well.
 */
public interface ITileEntityRendered<T extends TileEntity> extends ITileEntityProvider {
	/**
	 * Gets the TileRenderer for this class.
	 */
	public ITileRenderer<T> getTileRenderer();
}
