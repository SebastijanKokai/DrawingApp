package strategy;

import java.io.File;

import mvc.DrawingModel;

public class SaveManager implements SaveStrategy {
	private SaveStrategy saveStrategy;

	public SaveManager(SaveStrategy saveStrategy) {
		this.saveStrategy=saveStrategy;
		
	}
	@Override
	public void save(Object o, File file) {
		saveStrategy.save(o,file);
	}

}
