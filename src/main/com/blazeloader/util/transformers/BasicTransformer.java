package com.blazeloader.util.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import net.minecraft.launchwrapper.IClassTransformer;

public abstract class BasicTransformer implements IClassTransformer {
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (name == null || transformedName == null || bytes == null) {
			return bytes;
		}
		
		ClassNode classNode = new ClassNode();
		new ClassReader(bytes).accept(classNode, 0);
		if (transform(transformedName, classNode)) {
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(writer);
            return writer.toByteArray();
		}
		return bytes;
	}
	
	
	public abstract boolean transform(String transformedName, ClassNode classNode);
}
