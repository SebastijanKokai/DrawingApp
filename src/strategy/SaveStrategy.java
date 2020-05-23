package strategy;

import java.io.File;

import mvc.DrawingModel;

public interface SaveStrategy {

	void save(Object o, File f);

}
