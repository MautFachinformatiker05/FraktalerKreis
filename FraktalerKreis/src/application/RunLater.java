package application;

import javafx.scene.shape.Line;

public class RunLater implements Runnable{

	Line temp;
	
	public RunLater(Line temp) {
		this.temp = temp;
	}

	@Override
	public void run() {
		Main2.root.getChildren().add(temp);
	}
}
