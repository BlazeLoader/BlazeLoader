package com.blazeloader.api.gui;

import java.io.IOException;
import java.util.Iterator;

import com.blazeloader.util.version.Versions;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import com.blazeloader.api.privileged.ICreativeMenuForge;

/**
 * Override for the vanilla Creative menu to add support for additional pages of tabs.
 */
public class CreativeTabGui extends GuiContainerCreative {
	
	private static final ResourceLocation CREATIVE_INVENTORY_TEXTURE = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	private static final ResourceLocation RESOURCE_PACKS_TEXTURE = new ResourceLocation("textures/gui/resource_packs.png");
	
	private static final int SCROLL_BUTTON_NEXT_LEFT = 145;
	private static final int SCROLL_BUTTON_PREV_LEFT = 145;
	private static final int SCROLL_BUTTON_NEXT_TOP = -28;
	private static final int SCROLL_BUTTON_PREV_TOP = 132;
	
	private static final int SCROLL_BUTTON_WIDTH = 21;
	private static final int SCROLL_BUTTON_HEIGHT = 32;
	
	private static int pageIndex = 0;
	
	public CreativeTabGui(EntityPlayer player) {
		super(player);
		if (CreativeTabs.HOTBAR.isAlignedRight()) {
			new CreativeTabToolbars();
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		if (Versions.isForgeInstalled()) {
			//Disable forge gui stuff
			((ICreativeMenuForge)this).setPages(0);
			Iterator<GuiButton> iter = buttonList.iterator();
			while (iter.hasNext()) {
				GuiButton i = iter.next();
				if (i.id == 101 || i.id == 102) iter.remove();
			}
		}
	}
	
	public void nextPage() {
		if (canMoveForward()) {
			pageIndex = (pageIndex + 1);
			((ICreativeMenuForge)this).setCurrentTab(getFirstOnPage());
		}
	}
	
	public void prevPage() {
		if (--pageIndex < 0) pageIndex = 0;
		((ICreativeMenuForge)this).setCurrentTab(getFirstOnPage());
	}
	
	public boolean canMoveForward() {
		return pageIndex < (ApiGui.getCreativeTabsRegistry().length - 3)/10;
	}
	
	public boolean canMoveBack() {
		return pageIndex > 0;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0) {
			if (isOverNextPage(mouseX, mouseY)) {
				nextPage();
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
				return;
			}
			if (isOverPrevPage(mouseX, mouseY)) {
				prevPage();
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
				return;
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		mc.getSoundHandler().stopSounds();
	}
	
	public boolean checkTabBounds(CreativeTabs tab) {
		if (tab != CreativeTabs.SEARCH && tab != CreativeTabs.INVENTORY) {
			if (getTabPage(tab) != pageIndex) {
				return false;
			}
		}
		return true;
	}
	
	public int getTabPage(CreativeTabs tab) {
		int index = tab.getTabIndex();
		int page = 0;
		if (index > 11) {
			page = 1 + (index - 12) / 10;
		}
		return page;
	}
	
	public int getPageStart(int page) {
		if (page == 0) return 0;
		page--;
		page *= 10;
		return page + 12;
	}
	
	public int getPageOffset(int index) {
		if (index == 5 || index == 11) return 0;
		if (index < 5) return index;
		if (index < 11) return index - 1;
		index -= 12;
		index = index % 10;
		return index;
	}
	
	public CreativeTabs getFirstOnPage() {
		int current = this.getSelectedTabIndex();
		if (current == 5 || current == 11) return CreativeTabs.CREATIVE_TAB_ARRAY[current];
		
		int index = getPageStart(pageIndex) + getPageOffset(current);
		
		if (index < 11 && index > 5) index++;
		
		if (index >= CreativeTabs.CREATIVE_TAB_ARRAY.length) index = CreativeTabs.CREATIVE_TAB_ARRAY.length - 1;
		return CreativeTabs.CREATIVE_TAB_ARRAY[index];
	}
	
	@Override
	protected boolean isMouseOverTab(CreativeTabs tab, int x, int y) {
		return checkTabBounds(tab) && super.isMouseOverTab(tab, x, y);
	}
	
	@Override
	protected boolean renderCreativeInventoryHoveringText(CreativeTabs tab, int x, int y) {
		if (isOverNextPage(x, y)) {
			drawHoveringText(I18n.format("createWorld.customize.custom.next"), x, y);
			return false;
		}
		if (isOverPrevPage(x, y)) {
			drawHoveringText(I18n.format("createWorld.customize.custom.prev"), x, y);
			return false;
		}
		
		return checkTabBounds(tab) && super.renderCreativeInventoryHoveringText(tab, x, y);
	}
	
	@Override
	protected void drawTab(CreativeTabs tab) {
		if (checkTabBounds(tab)) {
			super.drawTab(tab);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawScrollButtons(mouseX, mouseY);
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}
	
	private boolean isOverNextPage(int mouseX, int mouseY) {
		return canMoveForward() &&
				mouseX > guiLeft + SCROLL_BUTTON_NEXT_LEFT &&
				mouseX < guiLeft + SCROLL_BUTTON_NEXT_LEFT + SCROLL_BUTTON_WIDTH &&
				mouseY > guiTop + SCROLL_BUTTON_NEXT_TOP &&
				mouseY < guiTop + SCROLL_BUTTON_NEXT_TOP + SCROLL_BUTTON_HEIGHT;
	}
	
	private boolean isOverPrevPage(int mouseX, int mouseY) {
		return canMoveBack() && 
				mouseX > guiLeft + SCROLL_BUTTON_PREV_LEFT &&
				mouseX < guiLeft + SCROLL_BUTTON_PREV_LEFT + SCROLL_BUTTON_WIDTH &&
				mouseY > guiTop + SCROLL_BUTTON_PREV_TOP &&
				mouseY < guiTop + SCROLL_BUTTON_PREV_TOP + SCROLL_BUTTON_HEIGHT;
	}
		
	private void drawScrollButtonIcons(int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(RESOURCE_PACKS_TEXTURE);
		if (canMoveForward()) {
			drawModalRectWithCustomSizedTexture(SCROLL_BUTTON_NEXT_LEFT + 4, SCROLL_BUTTON_NEXT_TOP + 8, 2, 0, SCROLL_BUTTON_WIDTH/2, SCROLL_BUTTON_HEIGHT/2, 128, 128);
		}
		if (canMoveBack()) {
			drawModalRectWithCustomSizedTexture(SCROLL_BUTTON_PREV_LEFT + 5, SCROLL_BUTTON_PREV_TOP + 8, 16, 0, SCROLL_BUTTON_WIDTH/2, SCROLL_BUTTON_HEIGHT/2, 128, 128);
		}
	}
	
	private void drawScrollButtons(int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TEXTURE);
		if (canMoveForward()) {
			if (isOverNextPage(mouseX, mouseY)) {
				drawModalRectWithCustomSizedTexture(guiLeft + SCROLL_BUTTON_NEXT_LEFT, guiTop + SCROLL_BUTTON_NEXT_TOP + 2, 0, 32, SCROLL_BUTTON_WIDTH/2, SCROLL_BUTTON_HEIGHT, 256, 256);
				drawModalRectWithCustomSizedTexture(guiLeft + SCROLL_BUTTON_NEXT_LEFT + SCROLL_BUTTON_WIDTH/2, guiTop + SCROLL_BUTTON_NEXT_TOP + 2, 18, 32, SCROLL_BUTTON_WIDTH/2, SCROLL_BUTTON_HEIGHT, 256, 256);
			} else {
				drawModalRectWithCustomSizedTexture(guiLeft + SCROLL_BUTTON_NEXT_LEFT, guiTop + SCROLL_BUTTON_NEXT_TOP, 0, 0, SCROLL_BUTTON_WIDTH/2, SCROLL_BUTTON_HEIGHT, 256, 256);
				drawModalRectWithCustomSizedTexture(guiLeft + SCROLL_BUTTON_NEXT_LEFT + SCROLL_BUTTON_WIDTH/2, guiTop + SCROLL_BUTTON_NEXT_TOP, 18, 0, SCROLL_BUTTON_WIDTH/2, SCROLL_BUTTON_HEIGHT, 256, 256);
			}
		}
		if (canMoveBack()) {
			if (isOverPrevPage(mouseX, mouseY)) {
				drawModalRectWithCustomSizedTexture(guiLeft + SCROLL_BUTTON_PREV_LEFT, guiTop + SCROLL_BUTTON_PREV_TOP, 0, 100, SCROLL_BUTTON_WIDTH/2, SCROLL_BUTTON_HEIGHT, 256, 256);
				drawModalRectWithCustomSizedTexture(guiLeft + SCROLL_BUTTON_PREV_LEFT + SCROLL_BUTTON_WIDTH/2, guiTop + SCROLL_BUTTON_PREV_TOP, 18, 100, SCROLL_BUTTON_WIDTH/2, SCROLL_BUTTON_HEIGHT, 256, 256);
			} else {
				drawModalRectWithCustomSizedTexture(guiLeft + SCROLL_BUTTON_PREV_LEFT, guiTop + SCROLL_BUTTON_PREV_TOP, 0, 64, SCROLL_BUTTON_WIDTH/2, SCROLL_BUTTON_HEIGHT, 256, 256);
				drawModalRectWithCustomSizedTexture(guiLeft + SCROLL_BUTTON_PREV_LEFT + SCROLL_BUTTON_WIDTH/2, guiTop + SCROLL_BUTTON_PREV_TOP, 18, 64, SCROLL_BUTTON_WIDTH/2, SCROLL_BUTTON_HEIGHT, 256, 256);
			}
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		drawScrollButtonIcons(mouseX, mouseY);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
}




