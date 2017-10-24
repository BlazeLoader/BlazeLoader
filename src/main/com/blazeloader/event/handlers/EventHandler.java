package com.blazeloader.event.handlers;

import java.net.SocketAddress;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.api.ApiServer;
import com.blazeloader.api.world.gen.UnpopulatedChunksQ;
import com.blazeloader.api.privileged.IWorld;
import com.blazeloader.event.listeners.*;
import com.blazeloader.event.listeners.args.BlockEventArgs;
import com.blazeloader.event.listeners.args.EntitySpawnEventArgs;
import com.blazeloader.event.listeners.args.FallEventArgs;
import com.blazeloader.event.listeners.args.InventoryEventArgs;
import com.blazeloader.event.listeners.args.LoginEventArgs;
import com.mojang.authlib.GameProfile;
import com.mumfrey.liteloader.core.event.HandlerList;
import com.mumfrey.liteloader.core.event.HandlerList.ReturnLogicOp;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

/**
 * Side-independent event handler
 */
public class EventHandler {
	/**
	 * Used where there is a chance a mod can call the method that triggers the event.
	 * Added to prevent infinite recursion inside an event.
	 */
	protected static boolean isInEvent = false;
	
    public static final HandlerList<StartupListener> modEventHandlers = new HandlerList<StartupListener>(StartupListener.class);
    public static final HandlerList<InventoryListener> inventoryEventHandlers = new HandlerList<InventoryListener>(InventoryListener.class, ReturnLogicOp.OR_BREAK_ON_TRUE);
    public static final HandlerList<TickListener> tickEventHandlers = new HandlerList<TickListener>(TickListener.class);
    public static final HandlerList<WorldListener> worldEventHandlers = new HandlerList<WorldListener>(WorldListener.class);
    public static final HandlerList<BlockChangedListener> blockEventHandlers = new HandlerList<BlockChangedListener>(BlockChangedListener.class, ReturnLogicOp.AND_BREAK_ON_FALSE);
    public static final HandlerList<PlayerListener> playerEventHandlers = new HandlerList<PlayerListener>(PlayerListener.class, ReturnLogicOp.OR_BREAK_ON_TRUE);
    public static final HandlerList<ChunkListener> chunkEventHandlers = new HandlerList<ChunkListener>(ChunkListener.class);
    public static final HandlerList<EntityConstructingListener> entityEventHandlers = new HandlerList<EntityConstructingListener>(EntityConstructingListener.class);
    public static final HandlerList<EntityTrackingListener> entityTrackings = new HandlerList<EntityTrackingListener>(EntityTrackingListener.class, ReturnLogicOp.OR_BREAK_ON_TRUE);
    public static final HandlerList<ProfilerListener> profilerHandlers = new HandlerList<ProfilerListener>(ProfilerListener.class);
    
    public static void eventTick() {
        tickEventHandlers.all().onTick();
    }

    public static void eventStart() {
        modEventHandlers.all().start();
    }

    public static void eventEnd() {
        modEventHandlers.all().stop();
    }
    
    public static void eventInit(WorldServer sender) {
    	worldEventHandlers.all().onWorldInit(sender);
    }
    
    public static void eventStartSection(Profiler sender, String name) {
        profilerHandlers.all().onSectionStart(sender, name);
    }

    public static void eventEndSection(Profiler sender) {
        profilerHandlers.all().onSectionEnd(sender, sender.getNameOfLastSection());
    }
    
    private static boolean eventSpawnEntityInWorld = false;
    public static void eventSpawnEntityInWorld(CallbackInfoReturnable<Boolean> info, World sender, Entity entity) {
    	if (!eventSpawnEntityInWorld && worldEventHandlers.size() > 0) {
    		if (!(entity instanceof EntityPlayer)) {
    			eventSpawnEntityInWorld = true;
    			EntitySpawnEventArgs args = new EntitySpawnEventArgs(entity);
    			worldEventHandlers.all().onEntitySpawned(sender, args);
    			if (args.getEntityChanged()) {
    				entity = args.getEntity();
    			}
    			if (args.getForced() != entity.forceSpawn) {
    				entity.forceSpawn = args.getForced();
    				info.setReturnValue(sender.spawnEntity(entity));
    				entity.forceSpawn = !entity.forceSpawn;
    			} else if (args.getEntityChanged()) {
    				info.setReturnValue(sender.spawnEntity(entity));
    			}
    			eventSpawnEntityInWorld = false;
    		}
    	}
    }
    
    private static boolean eventSetBlockState = false;
    public static void eventSetBlockState(CallbackInfoReturnable<Boolean> info, World sender, BlockPos pos, IBlockState state) {
    	if (!eventSetBlockState && blockEventHandlers.size() > 0) {
    		if (((IWorld)sender).isCoordValid(pos)) {
	    		IBlockState old = sender.getBlockState(pos);
	    		if (!old.equals(state)) {
	    			BlockEventArgs args = new BlockEventArgs(old, state);
		    		eventSetBlockState = true;
		    		blockEventHandlers.all().onBlockChanged(sender, pos, args);
		    		if (args.isCancelled()) {
		    			info.setReturnValue(false);
		    		} else if (!state.equals(args.getNewState())) {
		    			info.setReturnValue(sender.setBlockState(pos, args.getNewState()));
		    		}
		    		eventSetBlockState = false;
	    		}
    		}
    	}
    }
    
    public static void eventTryHarvestBlock(PlayerInteractionManager sender, CallbackInfoReturnable<Boolean> info, BlockPos pos) {
    	if (info.getReturnValue() && blockEventHandlers.size() > 0) {
    		World w = sender.world;
    		IBlockState state = w.getBlockState(pos);
    		blockEventHandlers.all().onBreakBlock(sender.player, w, state, pos, EnumHand.MAIN_HAND);
    	}
    }
    
    public static void eventOnItemUse(CallbackInfoReturnable<EnumActionResult> info, EntityPlayer player, World world, BlockPos pos, EnumFacing side, EnumHand hand) {
    	if (info.getReturnValue() == EnumActionResult.SUCCESS && blockEventHandlers.size() > 0) {
    		blockEventHandlers.all().onPlaceBlock(player, world, world.getBlockState(pos), pos, side, hand);
    	}
    }
    
    public static void eventPlayerLoggedIn(PlayerList sender, EntityPlayerMP player) {
        playerEventHandlers.all().onPlayerLoginMP(sender, player);
    }

    public static void eventPlayerLoggedOut(PlayerList sender, EntityPlayerMP player) {
        playerEventHandlers.all().onPlayerLogoutMP(sender, player);
    }

    public static <ReturnType> void eventRecreatePlayerEntity(PlayerList sender, EntityPlayerMP oldPlayer, int dimension, boolean didWin) {
        playerEventHandlers.all().onPlayerRespawnMP(sender, oldPlayer, dimension, !didWin);
    }
    
    public static void eventAllowUserToConnect(CallbackInfoReturnable<String> info, SocketAddress address, GameProfile profile) {
    	if (playerEventHandlers.size() > 0) {
    		String errorMessage = info.getReturnValue();
    		if (errorMessage == null) {
    			LoginEventArgs args = new LoginEventArgs(profile);
    			for (PlayerListener i : playerEventHandlers) {
    				i.onPlayerTryLoginMP(args);
    				if (args.isBlocked()) {
        				info.setReturnValue(args.getMessage());
        				return;
        			}
    			}
    		}
    	}
    }
    
    public static void eventOnChunkLoad(Chunk sender) {
        if (!sender.isTerrainPopulated()) {
            UnpopulatedChunksQ.instance().push(sender);
        }
        chunkEventHandlers.all().onChunkLoad(sender);
    }

    public static void eventOnChunkUnload(Chunk sender) {
        UnpopulatedChunksQ.instance().pop(sender);
        chunkEventHandlers.all().onChunkUnload(sender);
    }
    
    public static void initEntity(Entity sender, World w) {
    	if (!(sender instanceof EntityPlayer)) {
	    	entityEventHandlers.all().onEntityConstructed(sender);
    	}
    }
    
    public static void initEntityPlayer(EntityPlayer sender, World w, GameProfile profile) {
    	entityEventHandlers.all().onEntityConstructed(sender);
    }
    
    public static void eventCollideWithPlayer(EntityPlayer sender, CallbackInfo info, Entity entity) {
    	if (playerEventHandlers.size() > 0 && !playerEventHandlers.all().onEntityCollideWithPlayer(entity, sender)) {
    		info.cancel();
    	}
    }
    
    public static void eventDoBlockCollisions(Object sender) {
    	if (playerEventHandlers.size() > 0) {
    		EntityPlayer player = (EntityPlayer)sender;
    		if (player.isEntityAlive() && !player.isPlayerSleeping() && !player.noClip) {
	    		BlockPos var1 = new BlockPos(player.getEntityBoundingBox().minX + 0.001D, player.getEntityBoundingBox().minY + 0.001D, player.getEntityBoundingBox().minZ + 0.001D);
	            BlockPos var2 = new BlockPos(player.getEntityBoundingBox().maxX - 0.001D, player.getEntityBoundingBox().maxY - 0.001D, player.getEntityBoundingBox().maxZ - 0.001D);
	            if (player.getEntityWorld().isAreaLoaded(var1, var2)) {
	                for (int var3 = var1.getX(); var3 <= var2.getX(); ++var3) {
	                    for (int var4 = var1.getY(); var4 <= var2.getY(); ++var4) {
	                        for (int var5 = var1.getZ(); var5 <= var2.getZ(); ++var5) {
	                            BlockPos pos = new BlockPos(var3, var4, var5);
	                            IBlockState state = player.getEntityWorld().getBlockState(pos);
	                            if (state.isOpaqueCube()) {
	                            	playerEventHandlers.all().onPlayerCollideWithBlock(state, pos, player);
	                            }
	                        }
	                    }
	                }
	            }
    		}
    	}
    }
    
    private static boolean inEvent = false;
    public static void eventSetCurrentItem(InventoryPlayer sender, ItemStack stack) {
    	if (inventoryEventHandlers.size() > 0) {
    		if (!inEvent) {
    			inEvent = true;
				inventoryEventHandlers.all().onSlotSelectionChanged(sender.player, stack, sender.currentItem);
				inEvent = false;
    		}
    	}
    }
    
    public static void eventChangeCurrentItem(InventoryPlayer sender, CallbackInfo info, int increment) {
    	if (inventoryEventHandlers.size() > 0) {
	    	int newIndex = sender.currentItem + (increment > 0 ? 1 : increment < 0 ? -1 : 0);
	    	if (newIndex > -1) {
	    		for (newIndex -= increment; newIndex < 0; newIndex += InventoryPlayer.getHotbarSize());
	    		newIndex = newIndex % InventoryPlayer.getHotbarSize();
				if (!inventoryEventHandlers.all().onSlotSelectionChanged(sender.player, sender.getStackInSlot(newIndex), newIndex)) {
					info.cancel();
				}
	    	}
    	}
    }
    
    /*public static void eventOnItemPickup(EventInfo<EntityLivingBase> event, Entity itemEntity, int amount) {
    	if (inventoryEventHandlers.size() > 0 && !itemEntity.isDead && !event.getSource().worldObj.isRemote) {
    		inventoryEventHandlers.all().onItemPickup(event.getSource(), itemEntity, amount);
    	}
    }*/
    
    public static void eventUpdateEquipmentIfNeeded(EntityLiving sender, CallbackInfo info, EntityItem entityItem) {
    	if (inventoryEventHandlers.size() > 0) {
	    	ItemStack pickedUp = entityItem.getItem();
	    	InventoryEventArgs args = new InventoryEventArgs(pickedUp);
	    	inventoryEventHandlers.all().onEntityEquipItem(sender, entityItem, args);
	    	if (args.isCancelled()) {
	    		info.cancel();
	    	} else {
	    		if (args.isDirty()) {
	    			entityItem.setItem(args.getItemStack());
	    		}
	    	}
    	}
    }
    
    public static void eventEntityDropItem(Entity sender, CallbackInfoReturnable<EntityItem> info, ItemStack droppedItem, float yOffset) {
    	if (!isInEvent && droppedItem != null && droppedItem.getCount() > 0) {
    		if (inventoryEventHandlers.size() > 0) {
	    		InventoryEventArgs args = new InventoryEventArgs(droppedItem);
	    		inventoryEventHandlers.all().onDropItem(sender, false, false, args);
		    	if (args.isCancelled()) {
		    		info.setReturnValue(null);
		    	} else {
		    		if (args.isDirty()) {
		    			isInEvent = true;
		    			info.setReturnValue(sender.entityDropItem(args.getItemStack(), yOffset));
		    			isInEvent = false;
		    		}
		    	}
	    	}
    	}
    }
    
    //Because otherwise items held in an inventory get deleted when a mod cancels the drop.
    private static ItemStack savedHeldItemStack;
    public static void eventDropItem(EntityPlayer sender, CallbackInfoReturnable<EntityItem> info, ItemStack droppedItem, boolean dropAround, boolean traceItem) {
    	savedHeldItemStack = null;
    	if (!isInEvent && droppedItem != null && droppedItem.getCount() > 0) {
    		if (inventoryEventHandlers.size() > 0) {
	    		ItemStack held = sender.inventory.getItemStack();
	    		InventoryEventArgs args = new InventoryEventArgs(droppedItem);
	    		inventoryEventHandlers.all().onDropItem(sender, dropAround, traceItem, args);
		    	if (args.isCancelled()) {
		    		info.setReturnValue(null);
		    		if (held != null) {
			    		if (!held.equals(droppedItem)) {
			    			held.setCount(held.getCount() + droppedItem.getCount());
			    		} else {
			    			savedHeldItemStack = held;
			    		}
		    		}
		    	} else {
		    		if (args.isDirty()) {
		    			isInEvent = true;
		    			info.setReturnValue(sender.dropItem(args.getItemStack(), dropAround, traceItem));
		    			isInEvent = false;
		    		}
		    	}
    		}
    	}
    }
    
    //Now we then have to put the item back
    public static void eventSlotClick(CallbackInfoReturnable<?> info, EntityPlayer player) {
    	if (savedHeldItemStack != null) {
	    	if (info.getReturnValue() == null && player.inventory.getItemStack() == null) {
	    		player.inventory.setItemStack(savedHeldItemStack);
	    		savedHeldItemStack = null;
	    	}
    	}
    }
    
    public static void eventDropOneItem(EntityPlayer sender, CallbackInfoReturnable<EntityItem> info, boolean dropAll) {
    	if (inventoryEventHandlers.size() > 0) {
	    	ItemStack droppedItem = sender.inventory.getCurrentItem().copy();
	    	if (droppedItem != null) {
		    	if (!dropAll) droppedItem.setCount(1);
		    	InventoryEventArgs args = new InventoryEventArgs(droppedItem);
		    	inventoryEventHandlers.all().onDropOneItem(sender, dropAll, args);
		    	if (args.isCancelled()) {
		    		info.setReturnValue(null);
		    	}
		    	if (args.isDirty()) {
		    		info.setReturnValue(sender.dropItem(args.getItemStack(), false, true));
		    		sender.inventory.decrStackSize(sender.inventory.currentItem, droppedItem.getCount());
		    	}
	    	}
    	}
    }
    
    public static void eventFall(EntityPlayer sender, CallbackInfo info, float distance, float multiplier) {
    	if (playerEventHandlers.size() > 0) {
    		if (!isInEvent) {
    			FallEventArgs args = new FallEventArgs(distance, multiplier);
    			playerEventHandlers.all().onPlayerFall(sender, args);
    			if (!args.isCancelled()) {
					distance = args.getFallDistance();
					multiplier = args.getDamageMultiplier();
					isInEvent = true;
					sender.fall(distance, multiplier);
					isInEvent = false;
    			}
    			info.cancel();
        	}
    	}
    }
    
    public static void initS0DPacketCollectItem(int itemId, int entityId) {
    	if (inventoryEventHandlers.size() > 0) {
	    	MinecraftServer server = ApiServer.getServer();
	    	Entity owner = null;
	    	for (WorldServer i : server.worlds) {
	    		owner = i.getEntityByID(entityId);
	    		if (owner != null) break;
	    	}
	    	Entity item = null;
	    	for (WorldServer i : server.worlds) {
	    		item = i.getEntityByID(itemId);
	    		if (item != null) break;
	    	}
	    	if (item != null && owner != null) {
	    		int amount = 1;
	    		if (item instanceof EntityItem) {
	    			amount = ((EntityItem)item).getItem().getCount();
	    		}
	    		inventoryEventHandlers.all().onItemPickup(owner, item, amount);
	    	}
    	}
    }
    
    public static void eventGetSpawnPacket(EntityTrackerEntry sender, CallbackInfoReturnable<Packet<?>> info) {
    	Entity track = sender.getTrackedEntity();
    	if (!track.isDead) {
    		SPacketSpawnObject packet = null;
            for (EntityTrackingListener mod : entityTrackings) {
                SPacketSpawnObject modPacket = mod.onCreateSpawnPacket(track, packet != null);
                if (modPacket != null) packet = modPacket;
            }
            if (packet != null) {
            	info.setReturnValue(packet);
            }
        }
    }
    
    public static void eventTrackEntity(EntityTracker sender, Entity entity) {
    	if (!isInEvent) {
	    	isInEvent = true;
	    	boolean isHandled = false;
	        for (EntityTrackingListener mod : entityTrackings) {
	            isHandled |= mod.onAddEntityToTracker(sender, entity, isHandled);
	        }
	        isInEvent = false;
    	}
    }
    
}
