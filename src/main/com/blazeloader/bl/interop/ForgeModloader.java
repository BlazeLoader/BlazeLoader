package com.blazeloader.bl.interop;

import com.blazeloader.util.reflect.Lamda;
import com.blazeloader.util.reflect.Reflect;
import com.blazeloader.util.reflect.Func;
import com.blazeloader.util.version.Versions;

/**
 * Indirection class for FMLCommonHandler.
 * <br>
 * This is necessary because forge is a total control freak.
 */
public final class ForgeModloader {
	private static boolean init = false;
	private static ForgeMLAccess access;
	
	public static ForgeMLAccess instance() {
		if (Versions.isForgeInstalled() && !init) {
			init = true;
			/*access = new ForgeMLAccess() {
				public void exitJava(int arg0) {
					net.minecraftforge.fml.common.FMLCommonHandler.instance().exitJava(arg0);
				}
			}*/
			Func<?, ?> _getInstance = Reflect.lookupStaticMethod("net.minecraftforge.fml.common.FMLCommonHandler.instance ()Lnet/minecraftforge/fml/common/FMLCommonHandler;");
			Lamda<Object, ForgeMLAccess, Void> _exitJava = Reflect.lookupMethod(ForgeMLAccess.class, "net.minecraftforge.fml.common.FMLCommonHandler.exitJava (IZ)V");
			if (_getInstance.valid() && _exitJava.valid()) {
				try {
					access = _exitJava.getLambda(_getInstance.call());
				} catch (Throwable e) {
					access = null;
				}
			}
		}
		if (access == null) access = new ForgeMLAccess() {
			@Override
			public void exitJava(int exitCode, boolean hardExit) {
				System.exit(exitCode);
			}
		};
		return access;
	}
}
