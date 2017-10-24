package com.blazeloader.api.command;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.command.CommandHandler;
import net.minecraft.command.CommandHelp;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandPrivateHelp extends CommandHelp {
	
	private final CommandHandler handler;
	
	public CommandPrivateHelp(CommandHandler commands) {
		super();
		handler = commands;
	}
	
	@Override
	protected List<ICommand> getSortedPossibleCommands(ICommandSender sender, MinecraftServer server) {
        List<ICommand> var2 = handler.getPossibleCommands(sender);
        Collections.sort(var2);
        return var2;
    }
	
	@Override
	protected Map<String, ICommand> getCommandMap(MinecraftServer server) {
        return handler.getCommands();
    }
}
