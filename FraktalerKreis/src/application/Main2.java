package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class Main2 extends Application implements Runnable, Serializable {


	private static final long serialVersionUID = 8755233224304753429L;
	ExecutorService executor;
	ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
	double startX;
	double startY;
	double length;
	double prev_angle;
	static ArrayList<SerializableLine> line_array = new ArrayList<SerializableLine>();
	static ArrayList<SerializableLine> line_array_backup = new ArrayList<SerializableLine>();
	static boolean anhalten = false;
	static BorderPane root = new BorderPane(); 
	static private DoubleProperty angle = new SimpleDoubleProperty();
	static private IntegerProperty branches = new SimpleIntegerProperty();
	static private DoubleProperty modifier = new SimpleDoubleProperty();
	static private DoubleProperty modifier2 = new SimpleDoubleProperty();
	static private DoubleProperty modifier3 = new SimpleDoubleProperty();
	static private DoubleProperty modifier4 = new SimpleDoubleProperty();
	static private DoubleProperty modifier5 = new SimpleDoubleProperty();
	static private DoubleProperty modifier6 = new SimpleDoubleProperty();
	static double estimate = 0;
	static IntegerProperty count = new SimpleIntegerProperty();
	static Slider angle_slider;
	static Slider branch_slider;
	static Slider modifier_slider;
	static Slider modifier2_slider;
	static Slider modifier3_slider;
	static Slider modifier4_slider;
	static Slider modifier5_slider;
	static Slider modifier6_slider;
	static ProgressBar progress;
	static Button serialize;
	static Button deserialize;
	boolean loading;

	public Main2() {
		executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		startX = 0;
		startY = 200;
		length = 200;
		prev_angle = 0;
	}

	public Main2(ExecutorService executor, double startX, double startY, double length, double prev_angle) {
		this.executor = executor;
		this.startX = startX;
		this.startY = startY;
		this.length = length;
		this.prev_angle = prev_angle;	
	}

	public void start(Stage primaryStage) {
		try {
			loading = false;
			Scene scene = new Scene(root,800,800,false,SceneAntialiasing.BALANCED);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("FraktalerKreis");
			primaryStage.show();
			root.setTranslateX(400);
			root.setTranslateY(400);

			Stage newWindow = new Stage();	
			VBox vbox1 = new VBox(5);
			Scene scene2 = new Scene(vbox1,300,260);
			newWindow.setScene(scene2);
			newWindow.setTitle("Slider für FraktalerKreis");
			newWindow.setX(primaryStage.getX() + 805);
			newWindow.setY(primaryStage.getY() + 100);
			newWindow.setMaxHeight(300);

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					Platform.exit();
					System.exit(0);
				}
			});

			newWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					Platform.exit();
					System.exit(0);
				}
			});

			angle.set(30);
			branches.set(2);
			modifier.set(0.66);
			modifier2.set(1);
			modifier3.set(1);
			modifier4.set(1);
			modifier5.set(1);
			modifier6.set(1);

			angle_slider = new Slider(0,360,30);
			angle_slider.setMajorTickUnit(30);
			angle_slider.setShowTickMarks(true);
			branch_slider = new Slider(2,8,2);
			branch_slider.setMajorTickUnit(1);
			branch_slider.setShowTickMarks(true);
			modifier_slider = new Slider(0.1,0.9,0.66);
			modifier2_slider = new Slider(0,1,1);
			modifier2_slider.setMajorTickUnit(0.25);
			modifier2_slider.setShowTickMarks(true);
			modifier3_slider = new Slider(0,2,1);
			modifier4_slider = new Slider(0,2,1);
			modifier5_slider = new Slider(0,2,1);
			modifier6_slider = new Slider(0,2,1);
			progress = new ProgressBar();
			progress.setPrefWidth(300);
			serialize = new Button("Serialize");
			serialize.setPrefWidth(300);
			deserialize = new Button("Deserialize");
			deserialize.setPrefWidth(300);

			vbox1.getChildren().addAll(angle_slider, branch_slider, modifier_slider, modifier2_slider, modifier3_slider, modifier4_slider, modifier5_slider, modifier6_slider, progress, serialize, deserialize);

			angle.bind(angle_slider.valueProperty());
			branches.bind(branch_slider.valueProperty());
			modifier.bind(modifier_slider.valueProperty());
			modifier2.bind(modifier2_slider.valueProperty());
			modifier3.bind(modifier3_slider.valueProperty());
			modifier4.bind(modifier4_slider.valueProperty());
			modifier5.bind(modifier5_slider.valueProperty());
			modifier6.bind(modifier6_slider.valueProperty());

			addListenerToSlider(root, angle_slider);
			addListenerToSlider(root, branch_slider);
			addListenerToSlider(root, modifier_slider);
			addListenerToSlider(root, modifier2_slider);
			addListenerToSlider(root, modifier3_slider);
			addListenerToSlider(root, modifier4_slider);
			addListenerToSlider(root, modifier5_slider);
			addListenerToSlider(root, modifier6_slider);
			addListenerToProgressBar(root, progress);
			addListenerToSerialize(root, serialize);
			addListenerToDeserialize(root, deserialize);

			newWindow.show();
			executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			Main2 rekursiv = new Main2(executor,0, 400, 400, 0);
			executor.execute(rekursiv);
			count.set(0);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Draws a few lines and causes other Threads to draw even more of them.
	 */
	private void drawrecursive(double startX, double startY, double length,double prev_angle) {

		if (length<10)
		{
			Platform.runLater(new RunLater());
			return;
		}
		else
		{				
			for(int i=2; i<=branches.get()+1;i++)
			{
				double this_angle;
				if (i==branches.get()+1 && i%2==0)		// middle branch
				{
					this_angle = modifier2.get()*prev_angle;
				}
				else if(i%2==0)					// left branch
				{
					this_angle = (-(i/2) * angle.get()) + modifier2.get()*prev_angle;
				}
				else							// right branch
				{
					this_angle = ((i/2) * angle.get()) + modifier2.get()*prev_angle;
				}
				SerializableLine temp = new SerializableLine(); 
				temp.setStartX(startX);
				temp.setStartY(startY);
				temp.setEndX((modifier3.get()*startX*Math.cos(this_angle)-modifier4.get()*startY*Math.sin(this_angle)));
				temp.setEndY((modifier5.get()*startX*Math.sin(this_angle)+modifier6.get()*startY*Math.cos(this_angle)));
				temp.setStrokeWidth(length/50);
				while(anhalten)
				{
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				line_array.add(temp);
				Platform.runLater(new RunLater());
				Main2 rekursiv = new Main2(executor,temp.getEndX(), temp.getEndY(), length*modifier.get(), this_angle);
				executor.execute(rekursiv);
			}
			return;			
		}
	}



	public void addListenerToSlider(BorderPane root, Slider sl) {
		sl.valueChangingProperty().addListener(new ChangeListener<Boolean>() {

			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging, Boolean changing) {
				if(wasChanging && !loading) {
					root.getChildren().clear();
					line_array_backup.clear();
					count.set(0);
					Main2 rekursiv = new Main2(executor,0, 400, 400, 0);
					estimateLines();
					executor.execute(rekursiv);
				}
			}

		});
	}

	public void addListenerToProgressBar(BorderPane root, ProgressBar progress) {
		count.addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> ov,Number old_val, Number new_val) {

				progress.setProgress(count.get()/estimate);
			}

		});
	}

	public void addListenerToSerialize(BorderPane root, Button serialize) {
		serialize.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

			@Override
			public void handle(javafx.event.ActionEvent event) {
				FileOutputStream fileOut;
				ObjectOutputStream output;
				disableUI(true);
				try {
					fileOut = new FileOutputStream("./stream");
					output = new ObjectOutputStream(fileOut);
//					output.writeObject(Main2);		// slider
					output.writeObject(line_array_backup);
					output.close();
					fileOut.close();
					System.out.println("Settings Saved");
					System.out.println(root.getChildrenUnmodifiable());
				} catch (Exception e) {
					e.printStackTrace();
				}
				disableUI(false);
			}
		});

	}

	public void addListenerToDeserialize(BorderPane root, Button deserialize) {
		deserialize.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(javafx.event.ActionEvent event) {
				FileInputStream fileIn;
				ObjectInputStream input;
				disableUI(true);
				try {
					fileIn = new FileInputStream("./stream");
					input = new ObjectInputStream(fileIn);
					root.getChildren().clear();
//					input.readObject(Main2);
					line_array = ((ArrayList<SerializableLine>) input.readObject());	//?
					root.getChildren().addAll(line_array);
					input.close();
					fileIn.close();
					System.out.println("Settings Loaded");
					System.out.println(root.getChildrenUnmodifiable());
				} catch (Exception e) {
					e.printStackTrace();
				}
				disableUI(false);
			}
		});

	}

	public void disableUI(boolean bool) {

		angle_slider.setDisable(bool);
		branch_slider.setDisable(bool);
		modifier_slider.setDisable(bool);
		modifier2_slider.setDisable(bool);
		modifier3_slider.setDisable(bool);
		modifier4_slider.setDisable(bool);
		modifier5_slider.setDisable(bool);
		modifier6_slider.setDisable(bool);
		serialize.setDisable(bool);
		deserialize.setDisable(bool);
		loading = bool;
	}

	private void writeObject(ObjectOutputStream os) {

		try {
			os.defaultWriteObject();
			os.writeDouble(angle_slider.getValue());
			os.writeDouble(branch_slider.getValue());
			os.writeDouble(modifier_slider.getValue());
			os.writeDouble(modifier2_slider.getValue());
			os.writeDouble(modifier3_slider.getValue());
			os.writeDouble(modifier4_slider.getValue());
			os.writeDouble(modifier5_slider.getValue());
			os.writeDouble(modifier6_slider.getValue());

		} catch (Exception e) { e.printStackTrace(); }
	}

	private void readObject(ObjectInputStream is) {

		try {
			is.defaultReadObject();
			angle_slider.setValue(is.readDouble());
			branch_slider.setValue(is.readDouble());
			modifier_slider.setValue(is.readDouble());
			modifier2_slider.setValue(is.readDouble());
			modifier3_slider.setValue(is.readDouble());
			modifier4_slider.setValue(is.readDouble());
			modifier5_slider.setValue(is.readDouble());
			modifier6_slider.setValue(is.readDouble());

		} catch (Exception e) { e.printStackTrace(); }
	}

	public void estimateLines() {
		int estimate2 = branches.get();
		double len = 400* modifier.get();
		while(len >= 10) 
		{
			estimate2++;
			estimate2 *= branches.get();
			len = len * modifier.get();
		}
		estimate = estimate2*0.97;				// größere Werte sind komplett falsch!!
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void run() {
		drawrecursive(startX, startY, length, prev_angle);
	}
}
