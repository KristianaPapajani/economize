import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Observable;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

/**
 * This class encapsulates the data model of a very simple data collection
 * protocol. The model has three possible states: init state (has an associated
 * sting containing initial instructions), elicitation state (has an associated
 * ordered set of prompts), and end state (has an associated final message).
 * This class provides services for iterating over the ordered set of prompts.
 * 
 * This Model is an Observable.
 * 
 * @author mb
 *
 */
public class Model extends Observable {

	public static final int STATE_UNASSIGNED = 6;//3
	public static final int INIT_STATE       = 0;
	public static final int ELICIT_STATE     = 1;
	public static final int END_STATE        = 2;
	public static final int SELECT_MOVIE     = 1;
	public static final int SELECT_TIME      = 2;
	public static final int SELECT_TICKET    = 3;
	public static final int SELECT_COMBO     = 4;
	public static final int PAYMENT          = 5;
	public static final int CONFIRMATION = 6;
	public static final int 	MOVIE_IMG_HEIGHT = 225;
	public static final int 	MOVIE_IMG_WITDH = 150;
	
	private PrintStream myOut = System.out;

	private String[] prompts = { "Press the button", "Press the button", "Press the button", "Press the button",
			"Press the button", "Press the button" };
	private String[] moviePaths = {""};
	private String endMsg = "Task Complete.";
	//contains the path for the images.
	private String movieSelected = "";
	private String initMsg = "Instructions go here.  Button will appear after a delay.  Press to continue.";
	private String[] movieImgPath = {"src/movies/A_Bad_Mom_Christmas.jpg","src/movies/Blade_Runner.jpg","src/movies/Coco.jpg",
			"src/movies/Wonder.jpg","src/movies/Thor.jpg", "src/movies/The_Star.jpg", "src/movies/Justice_League.jpg",
			"src/movies/Daddys_Home_2.jpg"};
	private String[] comboImgPath = {};// to do
	private String[] theaters = {"Bay Theatre","Bayview Theatre","Beach Alliance Atlantis","Carlton Theatre","Cineplex Odeon","Coronet Theatre",
			"Downtown Theatre","Eastwood Theatre","Eglinton Theatre","Hollywood Theatre","Kingsway Theatre"};
	private String SelectTheaterStr = "Please Select Theater";
	private String SelectDayStr="Select Day";
	private String SelectTimeStr="Select time";
	private String[] Day= {"Today","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
	private String[] Time= {"5:00 PM","7:30 PM","8:00 PM","9:25 PM","10:00 PM","10:30 PM","11:00 PM"};
	private BufferedImage[] movieImgArray;
	private BufferedImage[] comboImgArray;
	private int currPromptIdx;
	
	private int currentState;

	private long startTimestamp;
	private long stopTimestamp;

	/**
	 * Create an instance of this model. The iterator over the prompts has not
	 * been initialized.
	 */
	public Model() {
		currPromptIdx = 0;
		//construct an ImageBuffer array from MovieImgPath
		 movieImgArray = loadImgArray(movieImgPath);
		//construct an ImageBuffer array from ComboImgPath
		//comboImgArray = loadImgArray(ComboImgPath); .. to do
		this.setState(Model.STATE_UNASSIGNED);
	}

	private void modelNotify(Object o) {
		// print something to the console, for the sake of tracing program
		// control flow
		// myOut.println("Change happened to the data model");
		// indicate that the state of this Observable has changed
		setChanged();
		// System.out.println(hasChanged());
		// notify the observers that the state has changed
		notifyObservers(o);
	}

	/**
	 * @return the prompt that is current in the iteration.
	 */
	public String getCurrentPrompt() {
		return prompts[currPromptIdx];
	}

	/**
	 * @return a string which indicates, via a stylized string, the position of
	 *         the prompt that is current in the iteration, relative to all
	 *         prompts. For example: "(Prompt 2/9): " The numerator of the
	 *         fraction will be less than or equal to the denominator.
	 */
	public String getPromptRelativePositionString() {
		return "(Prompt " + (currPromptIdx + 1) + "/" + (prompts.length) + "): ";
	}

	/**
	 * Advances the iterator to the next prompt. Should ensure first that the
	 * iterator has not reached past the end of the set of prompts.
	 */
	public void setPromptToNext() {
		currPromptIdx++;
		modelNotify(currPromptIdx);
	}
	/**
	 * String to propt user to select a theater.
	 */
	public String getSelectTheaterStr() {
		return SelectTheaterStr;
	}
	
	public String getSelectDayStr() {
		return SelectDayStr;
	}
	
	public String getSelectTimeStr() {
		return SelectTimeStr;
	}
	public void setMovieSelected(String movie) {
		this.movieSelected = movie;
	}
	/**
	 * @return whether the iterator has reached past the end of the set of
	 *         prompts.
	 */
	public boolean isPromptsRemaining() {
		return currPromptIdx < prompts.length - 1;
	}

	/**
	 * Causes a message to printed to a PrintStream with current prompt and
	 * elapsed time.
	 * 
	 * Pre - the method recordStopTimeStamp() has been invoked after
	 * recordStartTimeStamp()
	 */
	public void recordDuration() {
		myOut.println(getCurrentPrompt() + "\tTime Elapsed (msec): " + (stopTimestamp - startTimestamp));
	}

	/**
	 * @param modelState
	 *            the state for the model. Passed parameter must be one of the
	 *            class fields.
	 * 
	 *            Mutate the current state of this model.
	 */
	public void setState(int modelState) {
		// currentState = 9;
		currentState = modelState;
		modelNotify(currentState);
	}

	/**
	 * @return the current state of this model (value will be one of the class
	 *         fields).
	 */
	public int getCurrentState() {
		return currentState;
	}

	/**
	 * @return the initialization message associated with the INIT_STATE of this
	 *         model.
	 */
	public String getInitMsg() {
		return initMsg;
	}

	/**
	 * Initiates the iterator over the prompts.
	 */
	public void setPromptToFirst() {
		currPromptIdx = 0;
		modelNotify(currPromptIdx);
	}

	/**
	 * @return the finish message associated with the END_STATE of this model.
	 */
	public String getEndMsg() {
		return endMsg;
	}

	/**
	 * Tell this model to start recording elapsed time.
	 */
	public void recordStopTimeStamp() {
		stopTimestamp = System.currentTimeMillis();
	}

	/**
	 * Tell this model to stop recording elapsed time.
	 */
	public void recordStartTimeStamp() {
		startTimestamp = System.currentTimeMillis();
	}
	
	/**
	 * 
	 * @param index the location of the movie image that is being retrieved 
	 * @return BufferedImage representing the movie at provided index location
	 */
	public BufferedImage getMovieImdAt(int index) {
		return movieImgArray[index];
	}
	/**
	 * 
	 * @param index the location of the combo image that is being retrieved 
	 * @return BufferedImage representing the combo at provided index location
	 */
	public BufferedImage getComboImgAt(int index) {
		return comboImgArray[index];
	}
	/**
	 * 
	 * @param index the location of the theater name that is being retrieved 
	 * @return String representing the theater at provided index location
	 */
	public String getTheaterNameAt(int index) {
		return theaters[index];
	}
	/**
	 * 
	 * @return total number of theaters on file
	 */
	public int getNumberOfTheaters() {
		return theaters.length;
	}
	/**
	 * 
	 * @param index the location of the weekdays name that is being retrieved 
	 * @return String representing the weekday at provided index location
	 */
	public String getWeekDaysNameAt(int index) {
		return Day[index];
	}
	
	/**
	 * @return total number of weekdays on file
	 */
	
	
	public int getNumberofWeekDays() {
		return Day.length;
	}
	
	
	
	/**
	 * 
	 * @param index the location of the times name that is being retrieved 
	 * @return String representing the time at provided index location
	 */
	public String getTimeNameAt(int index) {
		return Time[index];
	}
	
	/**
	 * @return total number of time scheduled on file
	 */
	public int getNumberofTime() {
		return Time.length;
	}

	/**
	 * 
	 * @return total number of movies on file
	 */
	
	
	public int getNumberOfMovies() {
		return movieImgArray.length;
	}
	/**
	 * 
	 * @return total number of combos on file
	 */
	public int getNumberOfCombos() {
		return comboImgArray.length;
	}
	/**
	 * 
	 * @param Integer index represent the location for desire name.
	 * @return the name of the movie at provided index
	 */
	public String getMovieNameAt(int index) {
		String str = movieImgPath[index];
		str = str.replace('_',' ');
		str = str.substring(str.lastIndexOf("/"), (str.indexOf(".") - 1));
		System.out.println(str);
		return "";
	}
	// iterate through imagePath array to create a BufferImage for each path and then add BufferImage to an array 
	private BufferedImage[] loadImgArray(String[] imagesPath) {
		BufferedImage[] tmp = new BufferedImage[imagesPath.length];
		for(int i = 0; i < imagesPath.length; i++) {
			try {
			BufferedImage tmpBufImg = ImageIO.read(new File(imagesPath[i]));
			tmp[i] = tmpBufImg;
			}catch(IOException e) {
				System.out.println("Image not found");
			}
		}
		return tmp;
	}
	private class tupple{
		//might need it 
	}
}