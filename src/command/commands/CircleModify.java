package command.commands;

import command.Command;
import geometry.Circle;

public class CircleModify implements Command {

	private Circle oldState;
	private Circle newState;
	private Circle original = new Circle();
	
	public CircleModify(Circle oldState, Circle newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public void execute() {
		original.setInnerColor(oldState.getInnerColor());
		original.setOuterColor(oldState.getOuterColor());
		try {
			original.setRadius(oldState.getRadius());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		original.getCenter().setX(oldState.getCenter().getX());
		original.getCenter().setY(oldState.getCenter().getY());
		
		
		oldState.setInnerColor(newState.getInnerColor());
		oldState.setOuterColor(newState.getOuterColor());
		try {
			oldState.setRadius(newState.getRadius());
		} catch (Exception e) {
			e.printStackTrace();
		}
		oldState.getCenter().setX(newState.getCenter().getX());
		oldState.getCenter().setY(newState.getCenter().getY());

	}

	@Override
	public void unexecute() {
		oldState.setInnerColor(original.getInnerColor());
		oldState.setOuterColor(original.getOuterColor());
		try {
			oldState.setRadius(original.getRadius());
		} catch (Exception e) {
			e.printStackTrace();
		}
		oldState.getCenter().setX(original.getCenter().getX());
		oldState.getCenter().setY(original.getCenter().getY());
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
