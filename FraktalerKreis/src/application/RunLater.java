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
		Main2.count.set(Main2.count.get()+1);
		System.out.println(Main2.count.get()+"\t\t\t"+Main2.count.get()/Main2.estimate+"\t\t\t"+Main2.estimate);
	}
}
