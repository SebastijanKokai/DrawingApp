package command.commands;

import command.Command;
import geometry.Rectangle;

public class RectangleModify  implements Command {

	private Rectangle oldState;
	private Rectangle newState;
	private Rectangle original = new Rectangle();
	
	public RectangleModify(Rectangle oldState, Rectangle newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public void execute() {
		
		try {
			original.setInnerColor(oldState.getInnerColor());
			original.setOuterColor(oldState.getOuterColor());
			original.setWidth(oldState.getWidth());
			original.setHeight(oldState.getHeight());
			original.getUpperLeftPoint().setX(oldState.getUpperLeftPoint().getX());
			original.getUpperLeftPoint().setY(oldState.getUpperLeftPoint().getY());
			
			oldState.setInnerColor(newState.getInnerColor());
			oldState.setOuterColor(newState.getOuterColor());
			oldState.setWidth(newState.getWidth());
			oldState.setHeight(newState.getHeight());
			oldState.getUpperLeftPoint().setX(newState.getUpperLeftPoint().getX());
			oldState.getUpperLeftPoint().setY(newState.getUpperLeftPoint().getY());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void unexecute() {
		try {
			oldState.setInnerColor(original.getInnerColor());
			oldState.setOuterColor(original.getOuterColor());
			oldState.setWidth(original.getWidth());
			oldState.setHeight(original.getHeight());
			oldState.getUpperLeftPoint().setX(original.getUpperLeftPoint().getX());
			oldState.getUpperLeftPoint().setY(original.getUpperLeftPoint().getY());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
