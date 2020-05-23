package command.commands;

import command.Command;
import geometry.Shape;
import mvc.DrawingModel;

public class CommandRemove  implements Command {
	
	private DrawingModel model;
	private Shape shape;
	private String nameString;
	private int index;
	private boolean selected;
	
	public CommandRemove(DrawingModel model, Shape shape, int index, String nameString) {
		this.model = model;
		this.shape = shape;
		this.index = index;
		this.nameString = nameString;
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
