package command.commands;

import command.Command;
import geometry.Shape;
import mvc.DrawingModel;

public class BringFront implements Command {

	Shape shape;
	DrawingModel model;
	int index;
	int length;
	int indexSave;

	public BringFront(DrawingModel model, Shape shape, int index, int length) {
		this.model = model;
		this.shape = shape;
		this.index = index;
		this.length = length;
	}

	@Override
	public void execute() {
		indexSave = model.getShapes().indexOf(shape);
		if (index < length) {
			model.getShapes().remove(shape);
			model.getShapes().add(shape);
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
