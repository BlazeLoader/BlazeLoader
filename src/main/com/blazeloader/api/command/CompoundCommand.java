package com.blazeloader.api.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.blazeloader.api.ApiServer;

import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

/**
 * A special type of command that encapsulates a private set of commands and its own /help.
 */
public abstract class CompoundCommand extends BLCommandBase implements ICommandManager {

	protected final CommandHandler handler = new CommandHandler() {
		@Override
		protected MinecraftServer getServer() {
			return ApiServer.getServer();
		}
	};
	
	private final String name;
	
	public CompoundCommand(String nm, boolean includeHelp, boolean autoRegister) {
		super(autoRegister);
		name = nm;
		if (includeHelp) {
			registerCommand(new CommandPrivateHelp(handler));
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "/" + getName() + " /{command} {argumants}";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws WrongUsageException {
		if (args.length > 0) {
			String raw = "";
			for (int i = 0; i < args.length; i++) {
				if (raw != "") raw += " ";
				raw += args[i];
			}
			handler.executeCommand(sender, raw);
		} else {
			throw new WrongUsageException(getUsage(sender));
		}
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    	if (args.length < 2) {
	    	if (args.length == 0) {
	    		List<ICommand> commands = handler.getPossibleCommands(sender);
	    		Collections.sort(commands);
		    	List<String> result = new ArrayList<String>();
		    	for (ICommand i : commands) {
		    		result.add("/" + i.getName());
		    	}
		        return result;
	    	} else {
	    		for (int i = 0; i < args.length; i++) args[i] = args[i].replace("/", "");
	    		return handler.getTabCompletions(sender, args[0], pos);
	    	}
	    	
    	}
    	
		ICommand command = getCommands().get(args[0].substring(1));
		if (command != null && command.checkPermission(server, sender)) {
			return command.getTabCompletions(server, sender, dropFirstString(args), pos);
		}
    	return null;
    }
	
	/**
	 * Executes one of the contained commands with a raw input string.
	 * 
	 * @param sender		Entity executing this command
	 * @param rawCommand	Command string to execute.
	 * 
	 * @return	Number of executions.
	 */
	@Override
	public int executeCommand(ICommandSender sender, String rawCommand) {
		return handler.executeCommand(sender, rawCommand);
	}
	
	/**
	 * Returns all the commands from this CompoundCommand that a given sender can use. 
	 * @param sender	An entity that can execute commands
	 * @return	List of possible commands.
	 */
	@Override
	public List<ICommand> getPossibleCommands(ICommandSender sender) {
		return handler.getPossibleCommands(sender);
	}
	
    /**
     * returns a map of string to commads. All commands are returned, not just ones which someone has permission to use.
     */
	@Override
    public Map<String, ICommand> getCommands() {
        return handler.getCommands();
    }
	
    /**
     * Registers a full set of new commands to this collection.
     */
	public void registerCommands(ICommand... commands) {
		for (ICommand i : commands) {
			registerCommand(i);
		}
	}
    
    /**
     * Register a new command to this collection.
     * @return The command object that has been registered.
     */
	public ICommand registerCommand(ICommand command) {
		return handler.registerCommand(command);
	}
}