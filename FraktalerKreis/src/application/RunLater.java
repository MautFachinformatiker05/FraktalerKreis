package application;

public class RunLater implements Runnable{

	static int prev_size = 0;
	
	public RunLater() {
	}

	@Override
	public void run() {
		if ( (Main2.line_array.size() > 5000) || prev_size == Main2.line_array.size())
		{
			Main2.anhalten = true;
			Main2.root.getChildren().addAll(Main2.line_array);
			Main2.count.set(Main2.count.get()+Main2.line_array.size());
			Main2.line_array.clear();
			System.out.println(Main2.count.get()+"\t\t\t"+Main2.count.get()/Main2.estimate+"\t\t\t"+Main2.estimate);
			Main2.anhalten = false;
		}
		prev_size = Main2.line_array.size();
//		Main2.root.getChildren().add(temp);
//		Main2.count.set(Main2.count.get()+1);
//		System.out.println(Main2.count.get()+"\t\t\t"+Main2.count.get()/Main2.estimate+"\t\t\t"+Main2.estimate);
	}
}
