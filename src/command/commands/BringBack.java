package command.commands;

import command.Command;
import geometry.Shape;
import mvc.DrawingModel;

public class BringBack implements Command {

	Shape shape;
	DrawingModel model;
	int index;
	int indexSave;

	public BringBack(DrawingModel model, Shape shape, int index) {
		this.model = model;
		this.shape = shape;
		this.index = index;
	}

	@Override
	public void execute() {
		indexSave = model.getShapes().indexOf(shape);
		if (index != 0) {
			model.getShapes().remove(shape);
			model.getShapes().add(0, shape);
		}
	}

	@Override
	public void unexecute() {
		model.getShapes().remove(shape);
		model.getShapes().add(indexSave, shape);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
