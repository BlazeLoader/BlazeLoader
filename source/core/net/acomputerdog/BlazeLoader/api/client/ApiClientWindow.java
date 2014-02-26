package net.acomputerdog.BlazeLoader.api.client;

import net.acomputerdog.BlazeLoader.util.javaUtils.ClassPath;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ApiClientWindow {
	private static ArrayList<String> brands = new ArrayList<String>();
	
	/**
	 * Sets the window icon from a mod resource pack
	 * 
	 * icons are loaded from the following location:
	 * asses/ {resource_pack_name} /icon/icon_16x16.png
	 * asses/ {resource_pack_name} /icon/icon_32x32.png
	 * 
	 * at least one of these images must be present
	 * 
	 * @param resourcePack Name of resource pack
	 */
	public static void setIcon(String resourcePack) {
		File icon16x = ClassPath.getResource(resourcePack, "icon/icon_16x16.png");
		File icon32x = ClassPath.getResource(resourcePack, "icon/icon_32x32.png");
		
		if (icon16x != null && icon32x == null) icon32x = icon16x;
		if (icon16x == null && icon32x != null) icon16x = icon32x;
		if (icon16x != null) {
			try {
	    		Display.setIcon(new ByteBuffer[] {getIcon(icon16x), getIcon(icon32x)});
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		}
	}
	
	/**
	 * Sets the window title
	 * @param title
	 */
	public static void setTitle(String title) {
		Display.setTitle(title);
	}
	
	/**
	 * Adds a mod to the client brand
	 * @param brand Brand Name
	 */
	public static void setClientBrand(String brand) {
		if (!brand.isEmpty() && !brands.contains(brand)) {
			brands.add(brand);
		}
	}
	
	/**
	 * Gets the formatted Client brand name
	 */
	public static String getClientBrand() {
		if (brands.size() == 0) {
			return "BlazeLoader";
		}
		
		String result = "";
		
		for (String i : brands) {
			if (result != "") {
				result += ", ";
			}
			result += i;
		}
		return "blazeloader (" + result + ")";
	}
	
	private static ByteBuffer getIcon(File par1File) throws IOException {
        BufferedImage var2 = ImageIO.read(par1File);
        int[] var3 = var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), (int[])null, 0, var2.getWidth());
        ByteBuffer var4 = ByteBuffer.allocate(4 * var3.length);
        int[] var5 = var3;
        int var6 = var3.length;

        for (int var7 = 0; var7 < var6; ++var7) {
            int var8 = var5[var7];
            var4.putInt(var8 << 8 | var8 >> 24 & 255);
        }

        var4.flip();
        return var4;
    }
    
}
