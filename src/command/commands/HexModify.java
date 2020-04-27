package command.commands;

import command.Command;
import geometry.HexagonAdapter;

public class HexModify implements Command {

	private HexagonAdapter oldState;
	private HexagonAdapter newState;
	private HexagonAdapter original = new HexagonAdapter();
	
	public HexModify(HexagonAdapter oldState, HexagonAdapter newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public void execute() {
		try {
			original.setX(oldState.getX());
			original.setY(oldState.getY());
			original.setR(oldState.getR());
			original.setInnerColor(oldState.getInnerColor());
			original.setOuterColor(oldState.getOuterColor());
			
			oldState.setX(newState.getX());
			oldState.setY(newState.getY());
			oldState.setR(newState.getR());
			oldState.setInnerColor(newState.getInnerColor());
			oldState.setOuterColor(newState.getOuterColor());
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void unexecute() {
		try {
			oldState.setX(original.getX());
			oldState.setY(original.getY());
			oldState.setR(original.getR());
			oldState.setInnerColor(original.getInnerColor());
			oldState.setOuterColor(original.getOuterColor());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
