package application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.scene.shape.Line;

public class SerializableLine extends Line implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5529625571836590418L;
	
	private void writeObject(ObjectOutputStream os) {
	   
	   try {
	    os.defaultWriteObject();
	    os.writeDouble(getStartX());
	    os.writeDouble(getStartY());
	    os.writeDouble(getEndX());
	    os.writeDouble(getEndY());
	    os.writeDouble(getStrokeWidth());
	   } catch (Exception e) { e.printStackTrace(); }
	  }

	  private void readObject(ObjectInputStream is) {
	    
	   try {
	    is.defaultReadObject();
	    setStartX(is.readDouble());
	    setStartY(is.readDouble());
	    setEndX(is.readDouble());
	    setEndY(is.readDouble());
	    setStrokeWidth(is.readDouble());
	   } catch (Exception e) { e.printStackTrace(); }
	  }
}
