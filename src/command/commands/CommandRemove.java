package command.commands;

import command.Command;
import geometry.Shape;
import mvc.DrawingModel;

public class CommandRemove  implements Command {
	
	private DrawingModel model;
	private Shape shape;
	private String nameString;
	
	public CommandRemove(DrawingModel model, Shape shape, String nameString) {
		this.model = model;
		this.shape = shape;
		this.nameString = nameString;
	}

	@Override
	public void execute() {
		model.remove(shape);
	}

	@Override
	public void unexecute() {
		model.add(shape);
	}

	@Override
	public String getName() {
		return nameString;
	}

}
