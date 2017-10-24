package com.blazeloader.api.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import org.apache.commons.lang3.ArrayUtils;

import com.blazeloader.api.ApiServer;
import com.blazeloader.api.chat.ApiChat;
import com.blazeloader.api.chat.ChatColor;

/**
 * A superclass for commands that wish to include BL utilities.
 */
public abstract class BLCommandBase extends CommandBase {

    /**
     * Returns a copy of the given string array with the first item removed.
     */
    public static String[] dropFirstString(String[] input) {
    	return ArrayUtils.subarray(input, 1, input.length - 1);
    }
	
    /**
     * Creates a new BLCommandBase without automatically registering it.
     */
    public BLCommandBase() {
        this(false);
    }

    /**
     * Creates a new BLCommandBase, optionally auto-registering it in the command list.
     *
     * @param autoRegister If set to true, command will be automatically registered.
     */
    public BLCommandBase(boolean autoRegister) {
        super();
        if (autoRegister) {
            ApiCommand.registerCommand(this);
        }
    }
    
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    	if (getIgnorePermissionLevel()) {
    		//EntityPlayerMP is hard wired to return false for anyone that is not an OP for any command except for /help
    		//It's a terrible design
    		return sender.canUseCommand(getRequiredPermissionLevel(), "help");
    	}
    	return super.checkPermission(server, sender);
    }

    /**
     * Sends chat to a command user.
     *
     * @param target  The user to send the chat to.
     * @param message The message to send.
     */
    protected void sendChat(ICommandSender target, String message) {
        ApiChat.sendChat(target, message);
    }

    /**
     * Sends chat to a command sender, followed by a format_reset marker.
     *
     * @param target  The user to send the chat to.
     * @param message The message to send.
     */
    protected void sendChatLine(ICommandSender target, String message) {
    	ApiChat.sendChat(target, ChatColor.RESET, message);
    }
    
    /**
     * Sends a chat message constructed form the given unlocalised identifier.
     * 
     * @param target  		The user to send the chat to.
     * @param translate		The unlocalised string identifier
     * @param params		Optional objects to be inserted in place of variables in the string during localisation
     */
    protected void sendChatTranslation(ICommandSender target, String translate, Object params) {
    	ApiChat.sendChat(target, new TextComponentTranslation(translate, params));
    }
    
    /**
     * Utility method to get usernames for players on the server.
     */
    protected static String[] getListOfPlayerUsernames() {
        return ApiServer.getServer().getOnlinePlayerNames();
    }
    
    /**
     * Returns the name of this command.
     *
     * @return Returns BLCommandBase.getCommandName();
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public abstract int getRequiredPermissionLevel();
    
    /**
     * Return whether this command is usable by non-opped users on a server, like /help
     */
    public abstract boolean getIgnorePermissionLevel();
}
