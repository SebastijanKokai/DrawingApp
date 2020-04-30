package command.commands;

import command.Command;
import geometry.Shape;
import mvc.DrawingModel;

public class CommandRemove  implements Command {
	
	private DrawingModel model;
	private Shape shape;
	private String nameString;
	private int index;
	
	public CommandRemove(DrawingModel model, Shape shape, int index) {
		this.model = model;
		this.shape = shape;
		this.index = index;
	}

	@Override
	public void execute() {
		model.remove(shape);
	}

	@Override
	public void unexecute() {
		model.getShapes().add(index, shape);
	}

	@Override
	public String getName() {
		return nameString;
	}

}
