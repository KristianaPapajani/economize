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

	public static final int STATE_UNASSIGNED = 7;//3
	public static final int INIT_STATE       = 0;
	public static final int ELICIT_STATE     = 1;
	public static final int END_STATE        = 2;
	public static final int SELECT_MOVIE     = 1;
	public static final int SELECT_TIME      = 2;
	public static final int SELECT_TICKET    = 3;
	public static final int SELECT_COMBO     = 4;
	public static final int PAYMENT          = 5;
	public static final int CONFIRMATION     = 6;
	public static final int 	MOVIE_IMG_HEIGHT = 225;
	public static final int 	MOVIE_IMG_WIDTH = 150;
	public static final int CHILD_MOVIE_COST = 7;
	public static final int SENIOR_MOVIE_COST = 9;
	public static final int ADULT_MOVIE_COST = 12;
	public static final int COMBO_1_COST= 7;
	public static final int COMBO_2_COST= 11;
	public static final int COMBO_3_COST= 10;
	public static final int COMBO_4_COST= 10;
	public static final int APP_WIDTH = 540;
	public static final int APP_HEIGHT = 800;
	public static final int MAIN_PANEL_WIDTH = 540;
	public static final int MAIN_PANEL_HEIGHT = 720;
	
	private PrintStream myOut = System.out;

	private String SelectDayStr = "Select Day";
	private String SelectTimeStr="Select time";
	private String movieSelected = "";
	
	//contains the path for the images.
	
	private String[] movieImgPath = {"src/movies/A_Bad_Mom_Christmas.jpg","src/movies/Blade_Runner.jpg","src/movies/Coco.jpg",
			"src/movies/Wonder.jpg","src/movies/Thor.jpg", "src/movies/The_Star.jpg", "src/movies/Justice_League.jpg",
			"src/movies/Daddys_Home_2.jpg"};
	private String[] comboImgPath = {};// to do
	private String[] theaters =      {"Bay Theatre","Bayview Theatre","Beach Alliance Atlantis","Carlton Theatre","Cineplex Odeon","Coronet Theatre",
			"Downtown Theatre","Eastwood Theatre","Eglinton Theatre","Hollywood Theatre","Kingsway Theatre"};
	private String[] ticketBtnPath = {"src/images/btmLeft.png", "src/images/btmRight.png"};
	private String[] tiketTypeStr  = {"Adult","Senior","Child"};
	private String[] Day= {"Today","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
	private String[] Time= {"5:00 PM","7:30 PM","8:00 PM","9:25 PM","10:00 PM","10:30 PM","11:00 PM"};
	
	private String SelectTheaterStr     = "Please Select Theater";
	private String adultPanelPath       = "src/images/adultRed-button.jpg";
	private String childPanelPath       = "src/images/childRed-button.png";
	private String seniorPanelPath      = "src/images/seniorRed-button.png";
	private String TicketTotalPanelPath = "src/images/ticketTotalPanel.png";
	private String PaymentImgPath       = "src/images/checkout2.png";
	private String appImgPath = "src/images/appLogo.png";
	private String btmImgPath = "src/images/phnBtmImg.png";
	private String combo1Path= "src/combo/combo1.png";
	private String combo2Path="src/combo/combo2.png";
	private String combo3Path="src/combo/combo3.png";
	private String combo4Path="src/combo/combo4.png";
	private String confirmPath="src.images/ticketconfirm.jpg";
	
;	//private String[] totalTicketsStr =;
	private BufferedImage[] ticketImgArray;
	private BufferedImage[] movieImgArray;
	private BufferedImage[] comboImgArray;
	private int currPromptIdx;
	private int movieSelectedIndex = -1;
	private int currentState;
	private int TicketCt[] = {0,0,0};
	private int childTicketCt;
	private int seniotTicketCt;
	private long startTimestamp;
	private long stopTimestamp;

	/**
	 * Create an instance of this model. The iterator over the prompts has not
	 * been initialized.
	 */
	public Model() {
		currPromptIdx  = 0;
		
		//construct an ImageBuffer array from MovieImgPath
		ticketImgArray = loadTicketBtnImg(ticketBtnPath);
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


	public String confirmationtAdultTxt() {
		return  "Adult  " + TicketCt[0]; 
				
	}
	public String confirmationtChildTxt() {
		return  "Child  " + TicketCt[1];
	}
	public String confirmationtSeniorTxt() {
		return  "Seniot " + TicketCt[2];
	}
	public String TotalStr() {
		return   "Total ";
	}
	public String totalCostAdultTicket() {
		return  "$" +  TicketCt[0]*this.ADULT_MOVIE_COST + ".00";
	}
	public String totalCostChildTicket() {
		return  "$" +  TicketCt[1]*this.CHILD_MOVIE_COST + ".00";
	}
	public String totalCostSeniorTicket() {
		return  "$" +  TicketCt[2]*this.SENIOR_MOVIE_COST + ".00";
	}
	public String TotalTicketCostStr() {
		return   "$" +(TicketCt[0]*this.ADULT_MOVIE_COST + TicketCt[1]*this.CHILD_MOVIE_COST
					 + TicketCt[2]*this.SENIOR_MOVIE_COST) + ".00";
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
	/**
	 * 
	 * @return the index of the selected movie
	 */
	public int getMovieSelectedIndex() {
		return this.movieSelectedIndex;
	}
	/**
	 * TO DO!!
	 * @param movie
	 */
	public void setMovieSelected(int index) {
		this.movieSelected = getMovieNameAt(index);
		this.movieSelectedIndex = index;
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
	 * Increases the count plus 1 to the desired ticket type.
	 * @param index represents the ticket type: 0 for Adult, 1 for Child, 2 for Senior
	 */
	public void increaseTicketCount(int index) {
		TicketCt[index]++;
	}
	/**
	 * Decreases the count plus 1 to the desired ticket type.
	 * @param index represents the ticket type: 0 for Adult, 1 for Child, 2 for Senior
	 */
	public void decreaseTicketCount(int index) {
		if(TicketCt[index] != 0) {
			TicketCt[index]--;
		}
	}

	/**
	 * @return the current state of this model (value will be one of the class
	 *         fields).
	 */
	public int getCurrentState() {
		return currentState;
	}
	/**
	* 
	* @return number of days
	*/
	public int getNumberofWeekDays() {
		return Day.length;
	}
	
	/**
	 * @return total number of time scheduled on file
	 */
	public int getNumberofTime() {
		return Time.length;
	}
	//-------------
	/**
	 * 
	 * @return A string of the selected time
	 */
	public String getSelectTimeStr() {
		return SelectTimeStr;
	}
	
	/**
	 * 
	 * @return A string of the selected date
	 */
	public String getSelectDayStr() {
		return SelectDayStr;
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
	 * 
	 * @param index the location of the weekdays name that is being retrieved 
	 * @return String representing the weekday at provided index location
	 */
	public String getWeekDaysNameAt(int index) {
		return Day[index];
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
	 * @param index of the location of the button image that is being retrieved 
	 * @return BufferedImage representing the button image at provided index location
	 */
	public BufferedImage getBtnImgAt(int index) {
		return ticketImgArray[index];
	}
	
	public BufferedImage getAppImg() {
		BufferedImage tmp = null;
		try {
			tmp =  ImageIO.read(new File(appImgPath));
		}catch(IOException e) {
			
		}
		return tmp;
	}
	public BufferedImage getPhnBtmImg() {
		BufferedImage tmp = null;
		try {
			tmp =  ImageIO.read(new File(btmImgPath));
		}catch(IOException e) {
			
		}
		return tmp;
	}
	
	public BufferedImage getPaymentImg() {
		BufferedImage tmp = null;
		try {
			tmp =  ImageIO.read(new File(PaymentImgPath));
		}catch(IOException e) {
			
		}
		return tmp;
	}
	/**
	 * 
	 * @return BufferedImage for adult ticket panel
	 */
	public BufferedImage getAdultPanelImg() {
		BufferedImage tmp = null;
		try {
			tmp =  ImageIO.read(new File(adultPanelPath));
		}catch(IOException e) {
			
		}
		return tmp;
	}
	/**
	 * 
	 * @return BufferedImage for child ticket panel
	 */
	public BufferedImage getChildPanelImg() {
		BufferedImage tmp = null;
		try {
			tmp =  ImageIO.read(new File(childPanelPath));
		}catch(IOException e) {
			
		}
		return tmp;
	}
	/**
	 * 
	 * @return BufferedImage for senior ticket panel
	 */
	public BufferedImage getSniorPanelImg() {
		BufferedImage tmp = null;
		try {
			tmp =  ImageIO.read(new File(seniorPanelPath));
		}catch(IOException e) {
			
		}
		return tmp;
	}
	
	public BufferedImage getCombo1PanelImg() {
		BufferedImage tmp = null;
		try {
			tmp =  ImageIO.read(new File(combo1Path));
		}catch(IOException e) {
			
		}
		return tmp;
	}
	
	public BufferedImage getCombo2PanelImg() {
		BufferedImage tmp = null;
		try {
			tmp =  ImageIO.read(new File(combo2Path));
		}catch(IOException e) {
			
		}
		return tmp;
	}
	
	public BufferedImage getCombo3PanelImg() {
		BufferedImage tmp = null;
		try {
			tmp =  ImageIO.read(new File(combo3Path));
		}catch(IOException e) {
			
		}
		return tmp;
	}
	
	public BufferedImage getCombo4PanelImg() {
		BufferedImage tmp = null;
		try {
			tmp =  ImageIO.read(new File(combo4Path));
		}catch(IOException e) {
			
		}
		return tmp;
	}
	/**
	 * 
	 * @return
	 */
	public BufferedImage getTicketTotalJP() {
		BufferedImage tmp = null;
		try {
			tmp =  ImageIO.read(new File(TicketTotalPanelPath));
		}catch(IOException e) {
			
		}
		
		
		 return tmp;
	}
	
	public BufferedImage getConfirm() {
		BufferedImage tmp = null;
		try {
			tmp =  ImageIO.read(new File(confirmPath));
		}catch(IOException e) {
			
		}
		
		
		 return tmp;
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
	 * @param index represents the ticket type: 0 for Adult, 1 for Child, 2 for Senior
	 * @return the ticket count for the ticket count for desired ticket type
	 */
	public String getTicketCt(int index) {
		return TicketCt[index] + "";
	}
	/**
	 * 
	 * @param Integer index represent the location for desire ticket type 1:Adult, 2:Senior 3:Child.
	 * @return the name of the movie at provided index
	 */
	public String getTicketTypeStrAt(int index) {
		return tiketTypeStr[index];
	}

	private String getMovieNameAt(int index) {
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
				System.out.println("Movie image not found");
			}
		}
		return tmp;
	}
	private BufferedImage[] loadTicketBtnImg(String[] ticketBtnPath) {
		BufferedImage[] tmp = new BufferedImage[6];
		for(int i = 0; i < 6; i++) {
				try {
					System.out.println(i % 2);
					BufferedImage tmpImg = ImageIO.read(new File(ticketBtnPath[i % 2]));
					tmp[i] = tmpImg;
					
				}catch(IOException e) {
					System.out.println("Image ticket button not found");
				}	
		}
		return tmp;
	}
	
	
	private class tupple{
		//might need it 
	}
}