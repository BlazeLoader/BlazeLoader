package com.blazeloader.util.transformers.source;

import com.blazeloader.bl.obf.BLOBF;
import com.blazeloader.util.transformers.AccessLevel;
import com.blazeloader.util.transformers.BLAccessTransformer;
import com.blazeloader.util.transformers.transformations.*;

import net.acomputerdog.OBFUtil.util.TargetType;
import net.acomputerdog.core.java.Patterns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class BLATSource extends TransformationSource {

    private final List<Transformation> transformations;

    public BLATSource(InputStream in) throws IOException {
        transformations = new LinkedList<Transformation>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    if (line.startsWith("#") || line.isEmpty()) {
                        continue;
                    }

                    String[] sections = line.split(Patterns.NUMBERSIGN);
                    String[] parts = sections[0].trim().split(Patterns.SPACE);

                    if (parts.length < 3) {
                        System.err.println("Malformed Line: " + line);
                        continue;
                    }
                    String targetPart = parts[1].trim();
                    if (targetPart.equalsIgnoreCase("METHOD")) {
                        if (parts.length < 4) {
                            System.err.println("Malformed Line: " + line);
                            continue;
                        }
                        addMethodTransformation(parts[0].trim(), getOBF(parts[2].trim().concat(" ").concat(parts[3].trim()), TargetType.METHOD));
                    } else if (targetPart.equalsIgnoreCase("FIELD")) {
                        addFieldTransformation(parts[0].trim(), getOBF(parts[2].trim(), TargetType.FIELD));
                    } else {
                        System.err.println("Unknown transformation type: " + line);
                    }
                } catch (Exception e) {
                    System.err.println("Exception loading line: " + line);
                    e.printStackTrace();
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private void addMethodTransformation(String changes, String name) {
        System.out.println("Adding transformation to: " + name);
        int lastDot = name.lastIndexOf('.');
        String clName = name.substring(0, lastDot);
        String mName = name.substring(lastDot + 1, name.length()).replace('/', '.');
        String[] changeArr = changes.split(Patterns.COMMA);
        for (String change : changeArr) {
            if ("f".equalsIgnoreCase(change)) {
                transformations.add(new MethodFinalTransformation(clName, mName, true));
            } else if ("-f".equalsIgnoreCase(change)) {
                transformations.add(new MethodFinalTransformation(clName, mName, true));
            } else if ("public".equalsIgnoreCase(change)) {
                transformations.add(new MethodPublicTransformation(clName, mName, AccessLevel.PUBLIC));
            } else if ("private".equalsIgnoreCase(change)) {
                transformations.add(new MethodPublicTransformation(clName, mName, AccessLevel.PRIVATE));
            } else if ("protected".equalsIgnoreCase(change)) {
                transformations.add(new MethodPublicTransformation(clName, mName, AccessLevel.PROTECTED));
            } else if ("package".equalsIgnoreCase(change)) {
                transformations.add(new MethodPublicTransformation(clName, mName, AccessLevel.PACKAGE));
            } else {
                System.err.println("Invalid transformation: ".concat(change));
            }
        }

    }

    private void addFieldTransformation(String changes, String name) {
        System.out.println("Adding transformation to: " + name);
        int lastDot = name.lastIndexOf('.');
        String clName = name.substring(0, lastDot);
        String fName = name.substring(lastDot + 1, name.length());
        String[] changeArr = changes.split(Patterns.COMMA);
        for (String change : changeArr) {
            if ("f".equalsIgnoreCase(change)) {
                transformations.add(new FieldFinalTransformation(clName, fName, true));
            } else if ("-f".equalsIgnoreCase(change)) {
                transformations.add(new FieldFinalTransformation(clName, fName, true));
            } else if ("public".equalsIgnoreCase(change)) {
                transformations.add(new FieldPublicTransformation(clName, fName, AccessLevel.PUBLIC));
            } else if ("private".equalsIgnoreCase(change)) {
                transformations.add(new FieldPublicTransformation(clName, fName, AccessLevel.PRIVATE));
            } else if ("protected".equalsIgnoreCase(change)) {
                transformations.add(new FieldPublicTransformation(clName, fName, AccessLevel.PROTECTED));
            } else if ("package".equalsIgnoreCase(change)) {
                transformations.add(new FieldPublicTransformation(clName, fName, AccessLevel.PACKAGE));
            } else {
                System.err.println("Invalid transformation: ".concat(change));
            }
        }
    }

    private String getOBF(String name, TargetType type) {
        if (name == null) {
            return null;
        }
        String[] parts = name.split(Patterns.AT);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Malformed name: " + name);
        }
        String obfType = parts[0];
        String[] obfName = splitGlobal(parts[1]);
        BLOBF blobf;
        if (obfType.equalsIgnoreCase("obf")) {
            blobf = BLOBF.getOBF(obfName[0], obfName.length > 1 ? TargetType.CLASS : type);
        } else if (obfType.equalsIgnoreCase("srg")) {
            blobf = BLOBF.getSRG(obfName[0], obfName.length > 1 ? TargetType.CLASS : type);
        } else if (obfType.equalsIgnoreCase("mcp")) {
            blobf = BLOBF.getMCP(obfName[0], obfName.length > 1 ? TargetType.CLASS : type);
        } else {
            throw new IllegalArgumentException("Unknown OBF type: " + obfType);
        }
        if (blobf == null) {
            throw new IllegalArgumentException("Undefined " + type.name() + " mapping: \"" + obfName[0] + "\",\"" + ((obfName.length >= 2) ? obfName[1] : "NULL") + "\"");
        }
        if (obfName.length > 1) {
            return blobf.getValue().concat(obfName[1]);
        }
        return blobf.getValue();
    }

    private String[] splitGlobal(String obfName) {
        if (obfName == null) {
            return null;
        }
        int asterisk = obfName.indexOf('*');
        if (asterisk != -1) {
            return new String[]{obfName.substring(0, asterisk - 1).trim(), ".*"};
        } else {
            return new String[]{obfName.trim()};
        }
    }

    @Override
    public void provideTransformations(BLAccessTransformer transformer) {
        for (Transformation trans : transformations) {
            if (trans.isGlobal) {
                transformer.addGlobalTransformation(trans);
            } else {
                transformer.addTransformation(trans);
            }
        }
    }

}