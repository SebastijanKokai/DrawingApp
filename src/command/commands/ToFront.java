package command.commands;

import command.Command;
import geometry.Shape;
import mvc.DrawingModel;

public class ToFront implements Command {

	Shape shape;
	DrawingModel model;
	int index;

	public ToFront(DrawingModel model, Shape shape, int index) {
		this.model = model;
		this.shape = shape;
		this.index = index;
	}

	@Override
	public void execute() {
		index++;
		if (index < model.getShapes().size()) {
			model.getShapes().remove(shape);
			model.getShapes().add(index, shape);
		}
	}

	@Override
	public void unexecute() {
		index--;
		model.getShapes().remove(shape);
		model.getShapes().add(index, shape);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
