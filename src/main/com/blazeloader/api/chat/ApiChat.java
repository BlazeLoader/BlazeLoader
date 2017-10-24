package com.blazeloader.api.chat;

import net.minecraft.command.ICommandSender;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

/**
 * API for chat-related functions.
 */
public class ApiChat {

    /**
     * Sends a raw chat to a command user.
     *
     * @param user    	The command user to send the chat to.
     * @param message 	The message to send.
     */
    public static void sendRawChat(ICommandSender user, String message) {
        user.sendMessage(new TextComponentString(message));
    }
    
    /**
     * Constructs a message from the given text and styling objects and sends the result to the user.
     * <p>
     * Parses the given arguments as such:
     * 	Any instances of {@code EnumChatFormatting, ClickEvent, or HoverEvent} appearing in succession are collapsed into
     *  a single {@code ChatStyle} to be then applied by to the string content immediately following.
     *  <p>
     *  For any instances of {@code ChatStyle} appearing in the arguments array an attempt will be made to merge it into any existing style
     *  by method of overlay and will be applied to the continent immediately following.
     *  <p>
     *  {@code IChatComponent} are treated the same as text and will be appended directly with the preceding style applied to them if any.
     *  <p>
     *  Any other objects are simply converted via {@code toString()} and appended to the message.
     * 
     * @param user    	The command user to send the chat to.
     * @param args		An array of components to make up the message.
     */
    public static void sendChat(ICommandSender user, Object... args) {
		TextComponentString message = new TextComponentString("");
		Style style = null;
		for (Object o : args) {
			if (o instanceof TextFormatting || o instanceof ChatColor) {
				TextFormatting[] codes = ChatColor.getEnumChatColor(o);
				for (TextFormatting code : codes) {
					if (style == null) {
						style = new Style();
					}
					switch (code) {
						case OBFUSCATED:
							style.setObfuscated(true);
							break;
						case BOLD:
							style.setBold(true);
							break;
						case STRIKETHROUGH:
							style.setStrikethrough(true);
							break;
						case UNDERLINE:
							style.setUnderlined(true);
							break;
						case ITALIC:
							style.setItalic(true);
							break;
						case RESET:
							style = null;
							break;
						default:
							style.setColor(code);
					}
				}
			} else if (o instanceof ClickEvent) {
				if (style == null) style = new Style();
				style.setClickEvent((ClickEvent)o);
			} else if (o instanceof HoverEvent) {
				if (style == null) style = new Style();
				style.setHoverEvent((HoverEvent)o);
			} else if (o instanceof ITextComponent) {
				if (style != null) {
					((ITextComponent)o).setStyle(style);
					style = null;
				}
				message.appendSibling((ITextComponent)o);
			} else if (o instanceof Style) {
				if (!((Style)o).isEmpty()) {
					if (style != null) {
						inheritFlat((Style)o, style);
					}
					style = ((Style)o);
				}
			} else {
				ITextComponent line = o instanceof String ? new TextComponentTranslation((String)o) : new TextComponentString(o.toString());
				if (style != null) {
					line.setStyle(style);
					style = null;
				}
				message.appendSibling(line);
			}
		}
		
		user.sendMessage(message);
	}
    
    /**
     * Merges the given child ChatStyle into the given parent preserving hierarchical inheritance.
     * 
     * @param parent	The parent to inherit style information
     * @param child		The child style who's properties will override those in the parent
     */
    public static void inheritFlat(Style parent, Style child) {
		if ((parent.getBold() != child.getBold()) && child.getBold()) {
			parent.setBold(true);
		}
		if ((parent.getItalic() != child.getItalic()) && child.getItalic()) {
			parent.setItalic(true);
		}
		if ((parent.getStrikethrough() != child.getStrikethrough()) && child.getStrikethrough()) {
			parent.setStrikethrough(true);
		}
		if ((parent.getUnderlined() != child.getUnderlined()) && child.getUnderlined()) {
			parent.setUnderlined(true);
		}
		if ((parent.getObfuscated() != child.getObfuscated()) && child.getObfuscated()) {
			parent.setObfuscated(true);
		}
        
        Object temp;
        if ((temp = child.getColor()) != null) {
        	parent.setColor((TextFormatting)temp);
        }
        if ((temp = child.getClickEvent()) != null) {
        	parent.setClickEvent((ClickEvent)temp);
        }
        if ((temp = child.getHoverEvent()) != null) {
        	parent.setHoverEvent((HoverEvent)temp);
        }
        if ((temp = child.getInsertion()) != null) {
        	parent.setInsertion((String)temp);
        }
    }
}
