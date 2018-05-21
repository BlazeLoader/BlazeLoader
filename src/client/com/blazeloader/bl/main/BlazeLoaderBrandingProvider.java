package com.blazeloader.bl.main;

import com.blazeloader.api.client.render.ApiRenderClient;
import com.blazeloader.util.version.Versions;
import com.mumfrey.liteloader.api.BrandingProvider;
import com.mumfrey.liteloader.api.CustomisationProvider;
import com.mumfrey.liteloader.client.api.LiteLoaderBrandingProvider;
import com.mumfrey.liteloader.client.util.render.IconAbsolute;
import com.mumfrey.liteloader.util.render.Icon;
import net.minecraft.util.ResourceLocation;

import java.net.URI;

/**
 * BlazeLoader BrandingProvider
 */
public class BlazeLoaderBrandingProvider implements BrandingProvider {
    private static final BlazeLoaderBrandingProvider instance = new BlazeLoaderBrandingProvider();
    
    private static final ResourceLocation ABOUT_TEXTURE = new ResourceLocation("blazeloader", "textures/gui/about.png");
	private static final IconAbsolute LOGO_COORDS = new IconAbsolute(LiteLoaderBrandingProvider.ABOUT_TEXTURE, "logo", 128, 40, 0, 0, 256, 80);
	//public static final IconAbsolute ICON_COORDS = new IconAbsolute(LiteLoaderBrandingProvider.ABOUT_TEXTURE, "blaze", 32, 45, 0, 80, 64, 170);
	private static final IconAbsolute TWITTER_AVATAR_COORDS = new IconAbsolute(LiteLoaderBrandingProvider.ABOUT_TEXTURE, "twitter_avatar", 32, 32, 192, 80, 256, 144);
	
    private BlazeLoaderBrandingProvider() {
    }
    
    protected static CustomisationProvider instance() {
    	return instance;
    }
    
    @Override
    public int getPriority() {
        return 0;
    }
    
    @Override
    public int getBrandingColour() {
        return ApiRenderClient.getARGB(255, 255, 255, 0);
    }
    
    @Override
    public ResourceLocation getLogoResource() {
        return ABOUT_TEXTURE;
    }
    
    @Override
    public Icon getLogoCoords() {
        return LOGO_COORDS;
    }
    
    @Override
    public ResourceLocation getIconResource() {
        return null;
    }
    
    @Override
    public Icon getIconCoords() {
        return null;
    }
    
    @Override
    public String getDisplayName() {
        return Versions.getBLMainVersion().getName();
    }
    
    @Override
    public String getCopyrightText() {
        return "Copyright (c) acomputerdog 2013-2016";
    }
    
    @Override
    public URI getHomepage() {
    	return URI.create("http://www.blazeloader.com");
    }
    
    @Override
    public String getTwitterUserName() {
        return "acomputerdog";
    }
    
    @Override
    public ResourceLocation getTwitterAvatarResource() {
        return ABOUT_TEXTURE;
    }
    
    @Override
    public Icon getTwitterAvatarCoords() {
        return TWITTER_AVATAR_COORDS;
    }
}
