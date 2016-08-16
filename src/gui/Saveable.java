package gui;

/**
 *  Saveable Interface
 * @author Hafrans
 *
 *	You know that a window have lots of components which having data , When you dispose a window these data need to be saved.
 *  so you should implement this Interface to force program to store their data correctly.
 */
public interface Saveable {
	
	/**	
	 * save method
	 */
	public abstract void save();
	
	/**
	 * save flush 
	 * when a window using save(), if another window have same sections they need updating.
	 */
	public abstract void saveFlush();

}
