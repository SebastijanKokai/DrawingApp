package command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CommandManager {
	
    private static CommandManager instance = null;
    private Stack<Command> stackNormal;
    private Stack<Command> stackReverse;

    private List<String> commandHistory;

    public static CommandManager getInstance(){
        if(instance == null)
        	instance = new CommandManager();
            return instance;
    }

    private CommandManager() {
        stackNormal = new Stack<>();
        stackReverse = new Stack<>();
        commandHistory = new ArrayList<>();
    }

    public void execute(Command command){
        command.execute();
        stackNormal.push(command);
        commandHistory.add(command.getName());
        stackReverse.clear();
    }

	public void undo() {
		if (!stackNormal.isEmpty()) {
			Command cmd = stackNormal.pop();
			stackReverse.push(cmd);
			commandHistory.add(cmd.getName() + " - undo");
			cmd.unexecute();
		}
	}

    public void redo() {
    	if(!stackReverse.isEmpty()) {
    	Command cmd = stackReverse.pop();
    	stackNormal.push(cmd);
    	commandHistory.add(cmd.getName() + " - redo");
    	cmd.execute();
    	}
    }

    public void clearNormal() {
        stackNormal.clear();
    }

    public void clearReverse() {
        stackReverse.clear();
    }

    public List<String> getActionHistory() {
        return commandHistory;
    }
    
    public int sizeNormal() {
    	return stackNormal.size();
    }
    
    public int sizeReverse() {
    	return stackReverse.size();
    }
}
