package command.commands;

import command.Command;
import geometry.Shape;
import mvc.DrawingModel;

public class ToBack implements Command {

	Shape shape;
	DrawingModel model;
	int index;

	public ToBack(DrawingModel model, Shape shape, int index) {
		this.model = model;
		this.shape = shape;
		this.index = index;
	}

	@Override
	public void execute() {
		index--;
		if (index >= 0) {
			model.getShapes().remove(shape);
			model.getShapes().add(index, shape);
		}
	}

	@Override
	public void unexecute() {
		index++;
		model.getShapes().remove(shape);
		model.getShapes().add(index, shape);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
