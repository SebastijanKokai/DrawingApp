package command.commands;

import java.awt.Color;

import command.Command;
import geometry.Donut;
import geometry.Point;

public class DonutModify implements Command {

	private Donut oldState;
	private Donut newState;
	private Donut original = new Donut();
	
	public DonutModify(Donut oldState, Donut newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public void execute() {
		original.setInnerColor(oldState.getInnerColor());
		original.setOuterColor(oldState.getOuterColor());
		try {
			original.setRadius(oldState.getRadius());
			original.setInnerRadius(oldState.getInnerRadius());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		original.getCenter().setX(oldState.getCenter().getX());
		original.getCenter().setY(oldState.getCenter().getY());
		
		System.out.println(original.toString());
		
		
		oldState.setInnerColor(newState.getInnerColor());
		oldState.setOuterColor(newState.getOuterColor());
		try {
			oldState.setRadius(newState.getRadius());
			oldState.setInnerRadius(newState.getInnerRadius());
		} catch (Exception e) {
			e.printStackTrace();
		}
		oldState.getCenter().setX(newState.getCenter().getX());
		oldState.getCenter().setY(newState.getCenter().getY());
		
		System.out.println(oldState.toString());

	}

	@Override
	public void unexecute() {
		oldState.setInnerColor(original.getInnerColor());
		oldState.setOuterColor(original.getOuterColor());
		try {
			oldState.setRadius(original.getRadius());
			oldState.setInnerRadius(original.getInnerRadius());
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
