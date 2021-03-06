package application;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Scene scene = new Scene(root,800,800,false,SceneAntialiasing.BALANCED);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			root.setTranslateX(400);
			root.setTranslateY(400);
			
			Stage newWindow = new Stage();	
			VBox vbox1 = new VBox(5);
			Scene scene2 = new Scene(vbox1,300,180);
			newWindow.setScene(scene2);
			newWindow.setX(primaryStage.getX() + 700);
			newWindow.setY(primaryStage.getY() + 100);

			angle.set(30);
			branches.set(2);
			modifier.set(0.66);
			modifier2.set(1);
			modifier3.set(1);
			modifier4.set(1);
			modifier5.set(1);
			modifier6.set(1);

			Slider angle_slider = new Slider(0,360,30);
			angle_slider.setMajorTickUnit(30);
			angle_slider.setShowTickMarks(true);
			Slider branch_slider = new Slider(2,8,2);
			branch_slider.setMajorTickUnit(1);
			branch_slider.setShowTickMarks(true);
			Slider modifier_slider = new Slider(0.1,0.9,0.66);
			Slider modifier2_slider = new Slider(0,1,1);
			modifier2_slider.setMajorTickUnit(0.25);
			modifier2_slider.setShowTickMarks(true);
			Slider modifier3_slider = new Slider(0,2,1);
			Slider modifier4_slider = new Slider(0,2,1);
			Slider modifier5_slider = new Slider(0,2,1);
			Slider modifier6_slider = new Slider(0,2,1);
			vbox1.getChildren().addAll(angle_slider, branch_slider, modifier_slider, modifier2_slider, modifier3_slider, modifier4_slider, modifier5_slider, modifier6_slider);
			
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
			
			newWindow.show();
			
			long old_time = System.currentTimeMillis();
			drawrecursive(0,200,200,0);
			long new_time = System.currentTimeMillis();
			System.out.println("Insgesamt wurden "+count+" Linien in "+(new_time-old_time)+" Millisekunden gezeichnet.");

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void drawrecursive(double startX, double startY, double length,double prev_angle) {

		count++;
		if (length<10)
			return;
		else
		{
			if (length==200)
			{
				drawrecursive(0, 400, length*modifier.get(),0);		// überbleibsel alten Codes
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
					Line temp = new Line();
					temp.setStartX(startX);
					temp.setStartY(startY);
					temp.setEndX((modifier3.get()*startX*Math.cos(this_angle)-modifier4.get()*startY*Math.sin(this_angle)));
					temp.setEndY((modifier5.get()*startX*Math.sin(this_angle)+modifier6.get()*startY*Math.cos(this_angle)));
					temp.setStrokeWidth(length/50);
					root.getChildren().add(temp);
					drawrecursive(temp.getEndX(), temp.getEndY(), length*modifier.get(), this_angle);
				}
				return;
			}
		}
	}

	BorderPane root = new BorderPane();
	private DoubleProperty angle = new SimpleDoubleProperty();
	private IntegerProperty branches = new SimpleIntegerProperty();
	private DoubleProperty modifier = new SimpleDoubleProperty();
	private DoubleProperty modifier2 = new SimpleDoubleProperty();
	private DoubleProperty modifier3 = new SimpleDoubleProperty();
	private DoubleProperty modifier4 = new SimpleDoubleProperty();
	private DoubleProperty modifier5 = new SimpleDoubleProperty();
	private DoubleProperty modifier6 = new SimpleDoubleProperty();
	int count = 0;

	public void addListenerToSlider(BorderPane root, Slider sl) {
		sl.valueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> ov,Number old_val, Number new_val) {
				root.getChildren().clear();	// Löschen des vorherigen Kreises
				long old_time = System.currentTimeMillis();
				drawrecursive(0,200,200,0);
				long new_time = System.currentTimeMillis();
				System.out.println("Insgesamt wurden "+count+" Linien in "+(new_time-old_time)+" Millisekunden gezeichnet.");
			}

		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
