import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;

/**
 * This class encapsulates a GUI, a component of which is a view-controller
 * delegate. This object is an observer of the Model.
 * 
 * This GUI presents views that are tailored to the current state of the Model.
 * Changes to the view are driven by state changes to the Model. View
 * initializes with an active button labeled "Press to start". This action
 * launches the first state change to the Model.
 * 
 * @author mb
 *
 */
public class ViewController extends JFrame implements Observer, ActionListener {

	private static final long serialVersionUID = 2L; // needed by serializers

	private PrintStream myOut = System.out;

	private int promptDisplayDuration = 3000; // in msec

	private SwingWorker<Void, Void> displayWorker;

	/*
	 * here we maintain a reference to the model so that the view-controller
	 * delegate may query the model about its state, using the model's services
	 * for doing so.
	 */
	private Model theModel;

	private JPanel infoPanel;
	private JPanel widgetPanel;
	private JPanel mainPanel;
	private JLabel theLabel;
	private JScrollPane scroll;
	private JScrollPane scrollTheater;
	private JPanel scrollPanel; 
	private JPanel scrollTheaterPanel;
	private JButton theButton;
	private JButton appButton;
	private JButton btnBack;
	private JButton timeButton;
	private JButton[] btnsMovieArray;
	private JButton[] btnsComboArray;
	private Choice theaterNames;
	private Choice DayNames;
	private Choice TimeNames;
	private JPanel ImageOfTitle;
	private JPanel ImagePanel;
	private JLabel ShowDayTitle;
	private JLabel ShowTimeTitle;
	
	
	
	/**
	 * Creates the view-controller delegate
	 * 
	 * @param model
	 *            the Model
	 */
	public ViewController(Model model) {
		theModel = model;

		/*
		 * Set up some basic aspects of the frame
		 */
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension thisScreen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(540, 800);
		this.setTitle(this.getClass().getName());
		this.setLocationByPlatform(true);
		// this methods asks the window manager to position the frame in the
		// centre of the screen
		this.setLocationRelativeTo(null);

		/*
		 * Here we set up the GUI. The first panel is for the prompt and the
		 * second panel is for the button.
		 */
		//infoPanel = new JPanel();
		widgetPanel = new JPanel();
		//infoPanel.setBackground(Color.WHITE);
		widgetPanel.setBackground(Color.WHITE);
		
		// the default Layout Manager for JPanel is FlowLayout. Make choice of
		// Layout Manager explicit here
		//infoPanel.setLayout(new BorderLayout());
		
		widgetPanel.setLayout(new BorderLayout());
		this.getContentPane().setLayout(null);
		//BoxLayout boxLayout = 
		btnsMovieArray = new JButton[theModel.getNumberOfMovies()];
		//btnsComboArray = new JButton[theModel.getNumberOfCombos()]; .. to do
		theLabel = new JLabel();
		theLabel.setText("");//
		scrollPanel = new JPanel(new GridLayout(0,3,10,10));
		
		//scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//infoPanel.add(theLabel, BorderLayout.CENTER);
		appButton = new JButton();
		theButton = new JButton("Press to Start");
		theButton.setEnabled(true);
		btnBack = new JButton();
		timeButton=new JButton("Continue");
		
		widgetPanel.add(theButton, BorderLayout.CENTER);
		widgetPanel.setSize(this.getWidth(),this.getHeight());
		widgetPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		mainPanel = new JPanel(null);
		mainPanel.setSize(540,720);
		mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		//mainPanel.setSize( this.getWidth() - 500,this.getHeight() - 500);
		System.out.println(this.getHeight());
		mainPanel.setBorder(new LineBorder( Color.RED, 8));
		System.out.println(this.getWidth() +", "+this.getHeight());
		/* DONT DELETE
		BufferedImage tmpImage = null;
		ImageIcon image = null;
		test.setSize( this.getWidth() - 100,this.getHeight() - 100);
		try {
			 tmpImage = ImageIO.read(new File("src/movies/wonder.jpg"));
			 System.out.println(tmpImage);
			 image = new ImageIcon(tmpImage);
		}catch(IOException e) {
			System.out.println("no image2");
		}
		
		System.out.print(image);
		JLabel imagelabel = new JLabel();
		imagelabel.setIcon(image);
		imagelabel.setSize(300, 300);
		imagelabel.setBackground(Color.red);
		test.add(imagelabel);
		test.setBackground(Color.BLACK);
		test.setBorder(new LineBorder( Color.RED, 8));
		JButton test = new JButton(new ImageIcon(theModel.getMovieImdAt(0)));
		test.setSize(300, 300);
		mainPanel.add(test);
		*/
		//construct an jlabel array that holds all the images 
		//initializing all the choices
		theaterNames = new Choice();
		DayNames= new Choice();
		TimeNames=new Choice();
		//initializing labels and panels
		 
		 ImagePanel= new JPanel();
	     ShowDayTitle= new JLabel();
		 ShowTimeTitle= new JLabel();
		
		for(int i = 0; i < theModel.getNumberOfMovies(); i++) {
			ImageIcon image = new ImageIcon(theModel.getMovieImdAt(i));
			JButton tmp = new JButton();
			tmp.setIcon(image);
			tmp.setSize(Model.MOVIE_IMG_WITDH, Model.MOVIE_IMG_HEIGHT);
			tmp.addActionListener(this);
			this.btnsMovieArray[i] = tmp;
		}
		
		this.getContentPane().add(mainPanel);
		//this.getContentPane().add(infoPanel, BorderLayout.CENTER);
		this.getContentPane().add(widgetPanel);
		
		// here we install this object (ActionListener) on the button so that we
		// may detect user actions that may be dispatched from it.
		theButton.addActionListener(this);
		appButton.addActionListener(this);
		btnBack.addActionListener(this);
		/*
		 * Here we install the view as a listener on the model. This way, when
		 * the model changes, the view will be notified and the view will know
		 * to redraw itself. This statement is presently commented out. We
		 * instead exposed this statement in the Runnable for pedagogical
		 * purposes.
		 */
		// model.addObserver(this);

		this.setVisible(true);

		// this method asks the frame layout manager to size the frame so that
		// all its contents are at or above their preferred sizes
		this.pack();
		// make this component visible (do not assume that it will be visible by
		// default)
		this.setVisible(true);
	}

	@Override
	public Dimension getPreferredSize() {
		// find the dimensions of the screen and a dimension that is derive one
		// quarter of the size
		Dimension thisScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension targetSize = new Dimension(540, 800);
		return targetSize;
	}

	@Override
	public void update(Observable o, Object arg) {
		// the model's state has updated
		// myOut.println("model observer detected change: " +
		// theModel.getCurrentState());
		if (theModel.getCurrentState() == Model.INIT_STATE) {//Model.INIT_STATE) {
			mainPanel.removeAll();
			 try {
				 
				 ImageIcon appImg = new ImageIcon(ImageIO.read(new File("src/images/appLogo.png")));
				 ImageIcon btmImg = new ImageIcon(ImageIO.read(new File("src/images/phnBtmImg.png")));
				 //btmImg.
				 JLabel tmp = new JLabel();
				 System.out.println("VIEW1");
				 appButton.setIcon(appImg);
				 appButton.setSize(150,150);
				 appButton.setLocation(50,20);
				 mainPanel.add(appButton);
				 mainPanel.repaint();
				 tmp.setIcon(btmImg);
				 tmp.setSize(widgetPanel.getWidth(), 70);
				 widgetPanel.remove(theButton);
				 //tmp.setLocation(0, 733);
				 widgetPanel.setSize(this.getHeight(),70);
				 widgetPanel.setLocation(0, 710);
				 widgetPanel.add(tmp);
				 this.repaint();
				 
			 }catch(IOException e) {
				 System.out.println("not found");
			 }
			
			 
			//displayWorker = this.createWorkerDelayedEnabledButton(promptDisplayDuration);
			//displayWorker.execute();
		} else if (theModel.getCurrentState() == Model.ELICIT_STATE) {
			System.out.println("VIEW2");
			mainPanel.remove(appButton);
			theaterNames.setSize(this.getWidth(),50);
			for(int i = 0; i < btnsMovieArray.length; i++) {
				scrollPanel.add(btnsMovieArray[i]);
			}
			scroll = new JScrollPane(scrollPanel);
			scrollPanel.setSize(this.getWidth(),this.getHeight() );
			scroll.setSize(this.getWidth(), mainPanel.getHeight() - 50);
			scroll.setLocation(0,50);
			//scrollPanel.add( Box.createVerticalStrut(400) );
			
			for(int i = 0; i <= theModel.getNumberOfTheaters(); i++) {
				JLabel tmp = new JLabel();
				if(i == 0) {
					theaterNames.add(theModel.getSelectTheaterStr());
				}else {
					theaterNames.add(theModel.getTheaterNameAt(i-1));
				}

			}
			theaterNames.setSize(this.getWidth(),50);
			mainPanel.remove(appButton);
			mainPanel.add(theaterNames);
			mainPanel.add(scroll);
			
			

			btnBack.setOpaque(false);
			btnBack.setContentAreaFilled(false);
			btnBack.setBorderPainted(false);
			btnBack.setSize(200, 50);
			btnBack.setLocation(20, 0);
			widgetPanel.add(btnBack);
			//theButton.setEnabled(false);
			theModel.recordStartTimeStamp();
			theLabel.setText(theModel.getPromptRelativePositionString() + theModel.getCurrentPrompt());
			this.repaint();
			//displayWorker = this.createWorkerDelayedEnabledButton(promptDisplayDuration);
			//displayWorker.execute();
		} else if (theModel.getCurrentState() == Model.SELECT_TIME) {
			System.out.println("VIEW3");
			mainPanel.removeAll();
			mainPanel.repaint();
			//this part is for the days choice
		for(int i = 0; i <= theModel.getNumberofWeekDays(); i++) {
				JLabel tmp = new JLabel();
				if(i == 0) {
					DayNames.add(theModel.getSelectDayStr());
				}else {
					DayNames.add(theModel.getWeekDaysNameAt(i-1));
				} }
				
		DayNames.setBounds(150, 430, 200, 50);
			add(TimeNames, BorderLayout.CENTER);
			mainPanel.add(DayNames);
			//this part is for the times choice
			for(int i = 0; i <= theModel.getNumberofTime(); i++) {
				JLabel tmp = new JLabel();
				if(i == 0) {
					TimeNames.add(theModel.getSelectTimeStr());
				}else {
					TimeNames.add(theModel.getTimeNameAt(i-1));
				} }
				
			
			TimeNames.setBounds(150, 500, 200, 50);
			
			add(TimeNames, BorderLayout.CENTER);
			mainPanel.add(TimeNames);
			mainPanel.setBackground(Color.white);
			DayNames.setBackground(Color.white);
			TimeNames.setBackground(Color.white);
			
			ShowDayTitle.setBackground(Color.BLACK);
			ShowDayTitle.setFont(new Font("Tamil MN", Font.BOLD, 13));
			ShowDayTitle.setBounds(200, 399, 200, 50);
			ShowDayTitle.setText("Show Day");
			mainPanel.add(ShowDayTitle);
			
			ShowTimeTitle.setBackground(Color.BLACK);
		    ShowTimeTitle.setFont(new Font("Tamil MN", Font.BOLD, 13));
		    ShowTimeTitle.setBounds(200, 470, 200, 50);
		    ShowTimeTitle.setText("Show Time");
		    mainPanel.add(ShowTimeTitle);
		    
		    timeButton.setBounds(400, 570, 75, 50);
		    timeButton.setBackground(Color.RED);
		    timeButton.setFont(new Font("Tamil MN", Font.PLAIN, 13));
		    mainPanel.add(timeButton);
		   /* try {
				ImageIcon imagetimebtn = new ImageIcon(ImageIO.read(new File("red-button.png")));
				 timeButton.setIcon(imagetimebtn);
				    timeButton.setBounds(400, 570, 75, 50);
				    mainPanel.add(timeButton);
				    mainPanel.repaint();
				    JLabel img = new JLabel();
				   img.setIcon(imagetimebtn);
					 img.setBounds(400,570,75,50);
					
				    
			} catch (IOException e) {
				
				System.out.println("not found");
			}
		   
		    */
		   
		   // ImageIcon imageIcon = new ImageIcon("TheTitle.png");
		    JLabel ImageOfTitle = new JLabel();
		    ImageOfTitle.setText("Select Time");
		    ImageOfTitle.setFont(new Font("TIMES", Font.BOLD, 20));
		    ImageOfTitle.setBounds(200, 30, 200, 50);
		    ImageOfTitle.setBackground(Color.white);
		   
		   
		   
		    mainPanel.add(ImageOfTitle);
		    
		    btnBack.setOpaque(false);
			btnBack.setContentAreaFilled(false);
			btnBack.setBorderPainted(false);
			btnBack.setSize(200, 50);
			btnBack.setLocation(20, 0);
			widgetPanel.add(btnBack);
			
			JLabel ImagePanel=new JLabel();
			ImagePanel.setBackground(Color.RED);
			ImagePanel.setBounds(120, 100, 280, 300);
			mainPanel.add(ImagePanel);
			 ImagePanel.setOpaque(true);
			
			
			
			
			
			} else if (theModel.getCurrentState() == Model.END_STATE) {
				System.out.println("VIEW3");
				mainPanel.removeAll();
				mainPanel.repaint();
				theLabel.setText(theModel.getEndMsg());
				theButton.setText("");
				theButton.setEnabled(false);
				
			}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

			/*
			 * in response to button presses, we modify model attributes. We let
			 * this view's redraw of itself be delegated to when it is informed
			 * of the state change
			 */
			if (theModel.getCurrentState() == Model.STATE_UNASSIGNED) {
				System.out.print("btnPressed1->");
				theModel.setState(Model.INIT_STATE);
			
				
			} else if (theModel.getCurrentState() == Model.INIT_STATE) {
				System.out.print("btnPressed2->");
				
				theModel.setState(Model.SELECT_MOVIE);
			} else if (theModel.getCurrentState() == Model.SELECT_MOVIE) {
				System.out.print("btnPressed3->");
				if (e.getSource().equals(btnBack)) {
					theModel.setState(Model.INIT_STATE);
					System.out.println("back btn pressed");
				}else if(theaterNames.getSelectedItem().equals(theModel.getSelectTheaterStr())) {
					theaterNames.setBackground(Color.RED);
				}else {
					for(int i = 0; i < btnsMovieArray.length; i++) {
						if (e.getSource().equals(btnsMovieArray[i])){
							String movie = theModel.getMovieNameAt(i);
							theModel.setMovieSelected(movie);
							theModel.setState(Model.SELECT_TIME);
						}
					}
				} 
				
				
					//theModel.setState(Model.ELICIT_STATE);
				
					//theModel.setState(Model.END_STATE);
			 
			} else if (theModel.getCurrentState() == Model.SELECT_TIME) {
				System.out.print("btnPressed4->");
				if (e.getSource().equals(btnBack)) {
					theModel.setState(Model.INIT_STATE);
					System.out.println("back btn pressed");
				}else if(DayNames.getSelectedItem().equals(theModel.getSelectDayStr())) {
					DayNames.setBackground(Color.RED);
				} else if(TimeNames.getSelectedItem().equals(theModel.getSelectTimeStr())) {
					TimeNames.setBackground(Color.RED);
					
			}
			else if (theModel.getCurrentState() == Model.END_STATE) {
				// control should never arrive here. When in end state, button
				// is not enabled
			}
			// myOut.println("Button press");
		

	}

	/**
	 * Launch thread to wait and then enable button
	 * 
	 * @return
	 *//*
	private SwingWorker<Void, Void> createWorkerDelayedEnabledButton(int delayInMSec) {
		return new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				try {
					Thread.sleep(delayInMSec);
				} catch (InterruptedException e) {
					myOut.println("Error Occurred.");
				}
				theButton.setEnabled(true);
				return null;
			}
		};
	}
*/
	} }