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
import javax.swing.JTextField;
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

	private static final long serialVersionUID           = 2L; // needed by serializers
	private static final int  labelTickettArraySize      = 8;
	private static final int  ticketTypeArraySize        = 6; 
	private static final int  centerTicketPanelArraySize = 3;
	private static final int  ticketTypeSize             = 50;
	private static final int  centerTicketPanelWidth     = 50;
	private static final int  centerTicketPanelHeigt     = 45;
	private PrintStream myOut = System.out;



	/*
	 * here we maintain a reference to the model so that the view-controller
	 * delegate may query the model about its state, using the model's services
	 * for doing so.
	 */
	private Model theModel;

	private JPanel infoPanel;
	private JPanel mainPanel;
	private JPanel scrollPanel; 
	private JPanel widgetPanel;
	private JPanel scrollTheaterPanel;	
	private JPanel totalTicket;
	
	private JScrollPane scroll;
	private JScrollPane scrollTheater;
	

	private JLabel paymentPanel;	
	private JLabel ShowDayTitle;
	private JLabel ShowTimeTitle;
	private JLabel adultTicketPanel;
	private JLabel childTicketPanel;
	private JLabel ticketTotalPanel;
	private JLabel seniorTicketPanel;
	private JLabel [] centerTicketPanel;
	private JLabel[] labelTickettArray;
	
	private JButton btnBack;
	private JButton theButton;
	private JButton appButton;
	private JButton timeButton;
	private JButton moveFoward;
	private JButton TicketTypeContinue;
	private JButton paymentContinue;
	
	private JButton[] ticketType;
	private JButton[] btnsMovieArray;
	private JButton[] btnsComboArray;
	

	private JTextField total;
	private JTextField ZipCode;	
	private JTextField cardNumber;
	private JTextField ExpireDate;
	private JTextField cardHolderName;
	private JTextField SecurityNumber;
	
	private Choice DayNames;	
	private Choice theaterNames;
	private Choice TimeNames;

	private JLabel comboWhiteLbl;
	private JLabel insideWhiteLbl;
	private JLabel redfirstLbl;
	private JLabel redsecondLbl;
	private JLabel redThirdLbl;
	private JLabel redFourLbl;
	
	private JLabel confirm;
	
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
		Dimension thisScreen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(theModel.APP_WIDTH, theModel.APP_HEIGHT);
		this.setLocationByPlatform(true);
		this.setLocationRelativeTo(null);	
		this.getContentPane().setLayout(null);
		this.setTitle(this.getClass().getName());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*
		 * Here we set up the GUI. The first panel is for the prompt and the
		 * second panel is for the button.
		 */
		//infoPanel = new JPanel();
		mainPanel          = new JPanel();
		widgetPanel        = new JPanel();
		totalTicket        = new JPanel();
		paymentPanel       = new JLabel();
		ShowDayTitle       = new JLabel();
		adultTicketPanel   = new JLabel();		
		childTicketPanel   = new JLabel();
		seniorTicketPanel  = new JLabel();
		ticketTotalPanel   = new JLabel();
		ShowTimeTitle      = new JLabel();
		
		centerTicketPanel  = new JLabel[centerTicketPanelArraySize];
		labelTickettArray  = new JLabel[labelTickettArraySize];
		
		total              = new JTextField ();
		cardHolderName     = new JTextField ();
		cardNumber         = new JTextField ();
		ExpireDate         = new JTextField ();
		SecurityNumber     = new JTextField ();
		ZipCode            = new JTextField ();
		
		timeButton         = new JButton();
		appButton          = new JButton();
		moveFoward         = new JButton();
		btnBack            = new JButton();
		paymentContinue    = new JButton("Continue");	
		TicketTypeContinue = new JButton("Continue");	
		theButton          = new JButton("Press to Start");
		ticketType         = new JButton[ticketTypeArraySize];
		btnsMovieArray     = new JButton[theModel.getNumberOfMovies()];
		
		theaterNames       = new Choice();
		DayNames           = new Choice();
		TimeNames          = new Choice();
		
		comboWhiteLbl      = new JLabel();
		insideWhiteLbl     = new JLabel();
		redfirstLbl        = new JLabel();
		redsecondLbl       = new JLabel();
		redThirdLbl        = new JLabel();
		redFourLbl         = new JLabel();
		confirm            = new JLabel();
		
		theButton.setEnabled(true);
		ticketTotalPanel.setIcon( new ImageIcon(theModel.getTicketTotalJP()));
		scrollPanel = new JPanel(new GridLayout(0,3,10,10));
		
		

		widgetPanel.setBackground(Color.WHITE);
		widgetPanel.setLayout(new BorderLayout());
		widgetPanel.add(theButton, BorderLayout.CENTER);
		widgetPanel.setSize(this.getWidth(),this.getHeight());
		widgetPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		mainPanel.setSize(Model.MAIN_PANEL_WIDTH, Model.MAIN_PANEL_HEIGHT);
		mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.setBorder(new LineBorder( Color.RED, 5));
		

		
		for(int i = 0; i < theModel.getNumberOfMovies(); i++) {
			ImageIcon image = new ImageIcon(theModel.getMovieImdAt(i));
			JButton tmp = new JButton();
			tmp.setIcon(image);
			tmp.setSize(Model.MOVIE_IMG_WIDTH, Model.MOVIE_IMG_HEIGHT);
			tmp.addActionListener(this);
			this.btnsMovieArray[i] = tmp;
		}
		for(int i = 0; i < ticketTypeArraySize ; i++) {
			ImageIcon image = new ImageIcon(theModel.getBtnImgAt(i));
			JButton tmpBtm = new JButton();
			tmpBtm.setIcon(image);
			ticketType[i] = tmpBtm;
			ticketType[i].setSize(ticketTypeSize,ticketTypeSize);
			ticketType[i].addActionListener(this);
		}
		for(int i = 0; i < centerTicketPanelArraySize; i++) {
			JLabel tmp = new JLabel(theModel.getTicketCt(i), JLabel.CENTER);
			tmp.setFont(new Font("Arial", Font.BOLD, 20));
			tmp.setBorder(new LineBorder( Color.BLACK, 1));
			tmp.setSize(centerTicketPanelWidth, centerTicketPanelHeigt);
			
			centerTicketPanel[i] = tmp;
		}

		
		// here we install this object (ActionListener) on the button so that we
		// may detect user actions that may be dispatched from it.

		
		
		btnBack.addActionListener(this);
		theButton.addActionListener(this);
		appButton.addActionListener(this);
		timeButton.addActionListener(this);
		moveFoward.addActionListener(this);
		paymentContinue.addActionListener(this);
		TicketTypeContinue.addActionListener(this);
		
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
		this.getContentPane().add(mainPanel);
		this.getContentPane().add(widgetPanel);
	}

	@Override
	public Dimension getPreferredSize() {
		// find the dimensions of the screen and a dimension that is derive one
		// quarter of the size
		Dimension thisScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension targetSize = new Dimension(Model.APP_WIDTH, Model.APP_HEIGHT);
		return targetSize;
	}

	@Override
	public void update(Observable o, Object arg) {
		// the model's state has updated

		if (theModel.getCurrentState() == Model.INIT_STATE) {//Model.INIT_STATE) {
			int appButtonSize     = 150, appButtonX      = 50,  appButtonY = 20,
				widgetPanelHeight = 70,  widgetPanelLocY = 710;
			System.out.println("VIEW1");
			mainPanel.removeAll();
			widgetPanel.remove(theButton);

			 JLabel tmp = new JLabel();
			 
			 tmp.setIcon(new ImageIcon(theModel.getPhnBtmImg()));
			 tmp.setSize(widgetPanel.getWidth(), widgetPanelHeight);
			 
			 appButton.setIcon(new ImageIcon(theModel.getAppImg()));
			 appButton.setSize(appButtonSize,appButtonSize);
			 appButton.setLocation(appButtonX,appButtonY);
			 
			 mainPanel.add(appButton);
			 
			 
			 widgetPanel.setSize(this.getHeight(),widgetPanelHeight);
			 widgetPanel.setLocation(0, widgetPanelLocY);
			 widgetPanel.add(tmp);
			 
			 mainPanel.repaint();

		} else if (theModel.getCurrentState() == Model.ELICIT_STATE) {
			int theatersSelecHeight = 50;
			System.out.println("VIEW2");
			mainPanel.removeAll();
			
			for(int i = 0; i < btnsMovieArray.length; i++) {
				scrollPanel.add(btnsMovieArray[i]);
			}
			
			
			scroll = new JScrollPane(scrollPanel);
			scrollPanel.setSize(this.getWidth(),this.getHeight() );
			scroll.setSize(this.getWidth(), mainPanel.getHeight() - theatersSelecHeight);
			scroll.setLocation(0,theatersSelecHeight);

			
			for(int i = 0; i <= theModel.getNumberOfTheaters(); i++) {
				JLabel tmp = new JLabel();
				if(i == 0) {
					theaterNames.add(theModel.getSelectTheaterStr());
				}else {
					theaterNames.add(theModel.getTheaterNameAt(i-1));
				}
			}


			theaterNames.setSize(this.getWidth(), theatersSelecHeight);
			btnBack.setBounds(20, 10,200, 50);
			
			widgetPanel.add(btnBack);
			mainPanel.remove(appButton);
			mainPanel.add(theaterNames);
			mainPanel.add(scroll);
			this.repaint();

		} else if (theModel.getCurrentState() == Model.SELECT_TIME) {
//			System.out.println("VIEW3");
//			mainPanel.removeAll();
//			mainPanel.repaint();		
//			moveFoward.setSize(this.getWidth(), 70);
//			mainPanel.add(moveFoward);
		
			//--------------------------------------------------------------------------------------------------------

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
					
			DayNames.setBounds(160, 430, 200, 50);
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
					
				
				TimeNames.setBounds(160, 500, 200, 50);
				
				add(TimeNames, BorderLayout.CENTER);
				mainPanel.add(TimeNames);
				mainPanel.setBackground(Color.white);
				DayNames.setBackground(Color.white);
				TimeNames.setBackground(Color.white);
				
				ShowDayTitle.setBackground(Color.BLACK);
				ShowDayTitle.setFont(new Font("Tamil MN", Font.BOLD, 13));
				ShowDayTitle.setBounds(215, 399, 200, 50);
				ShowDayTitle.setText("Show Day");
				mainPanel.add(ShowDayTitle);
				
				ShowTimeTitle.setBackground(Color.BLACK);
			    ShowTimeTitle.setFont(new Font("Tamil MN", Font.BOLD, 13));
			    ShowTimeTitle.setBounds(215, 470, 200, 50);
			    ShowTimeTitle.setText("Show Time");
			    mainPanel.add(ShowTimeTitle);
			    
			    timeButton.setBounds(160, 570, 200, 30);
			    timeButton.setBackground(Color.RED);
			    timeButton.setText("Continue");
			    timeButton.setFont(new Font("Tamil MN", Font.PLAIN, 13));
			    timeButton.setOpaque(true);
			    mainPanel.add(timeButton);
	
			    JLabel ImageOfTitle = new JLabel();
			    ImageOfTitle.setText("Select Time");
			    ImageOfTitle.setFont(new Font("TIMES", Font.BOLD, 20));
			    ImageOfTitle.setBounds(200, 30, 200, 50);
			    ImageOfTitle.setBackground(Color.white);
			   
			   
			   
			    mainPanel.add(ImageOfTitle);
			   
				
				JLabel ImagePanel=new JLabel();
				int location = theModel.getMovieSelectedIndex();
				ImagePanel.setIcon(new ImageIcon(theModel.getMovieImdAt(location)));
				ImagePanel.setBackground(Color.RED);
				ImagePanel.setBounds(180, 100, Model.MOVIE_IMG_WIDTH, Model.MOVIE_IMG_HEIGHT);
				mainPanel.add(ImagePanel);
				ImagePanel.setOpaque(true);
				
				
				 mainPanel.repaint();					
			
			
		
			//--------------------------------------------------------------------------------------------------------
	
		
		} else if (theModel.getCurrentState() == Model.SELECT_TICKET) {
			System.out.println("VIEW4");
			adultTicketPanel.setIcon(new ImageIcon(theModel.getAdultPanelImg()));
			childTicketPanel.setIcon( new ImageIcon(theModel.getChildPanelImg()));
			seniorTicketPanel.setIcon( new ImageIcon(theModel.getSniorPanelImg()));
			mainPanel.removeAll();
			totalTicket.removeAll();
			widgetPanel.add(btnBack);
			totalTicket.setBounds     (52, 507, 447, 120);
			ticketTotalPanel.setBounds(50, 400, 450, 150);
			adultTicketPanel.setBounds(50, 30,200,150);
			childTicketPanel.setBounds(335,30, 200,150);
			seniorTicketPanel.setBounds(50,235,200,150);
			totalTicket.setBackground(Color.WHITE);
			//tmp.setSize(200, 30);
			
			
			int X = 0;
			int Y = 0;
			for(int i = 0; i < 8; i++)
			{
				JLabel tmp;
				
				if(i % 2 == 0) {
					tmp = new JLabel("" ,JLabel.LEFT);
					tmp.setBounds(X, Y, 347, 30);
					X = X + 347;
				}else{
					tmp = new JLabel("" ,JLabel.RIGHT);
					tmp.setBounds(X, Y, 100, 30);
					Y = Y + 30;
					X = 0;
					}
				tmp.setBorder(new LineBorder( Color.BLACK, 1));
				if	   (i == 0) {tmp.setText(theModel.confirmationtAdultTxt());}
				else if(i == 1){tmp.setText(theModel. totalCostAdultTicket());}
				else if(i == 2) {tmp.setText(theModel.confirmationtChildTxt());}
				else if(i == 3) {tmp.setText(theModel.totalCostChildTicket());}
				else if(i == 4) {tmp.setText(theModel.confirmationtSeniorTxt() );}
				else if(i == 5) {tmp.setText(theModel.totalCostSeniorTicket());}
				else if(i == 6) {tmp.setText(theModel.TotalStr());}
				else            {tmp.setText(theModel.TotalTicketCostStr());}
				labelTickettArray[i] = tmp;
				totalTicket.add(tmp);		
			}
			
			mainPanel.repaint();
			TicketTypeContinue.setBounds(47, 635, 456, 30);
			TicketTypeContinue.setBackground(Color.RED);
			TicketTypeContinue.setOpaque(true);
			mainPanel.add(adultTicketPanel);
			mainPanel.add(totalTicket);
			mainPanel.add(childTicketPanel);
			mainPanel.add(seniorTicketPanel);
			mainPanel.setBackground(Color.WHITE);
			mainPanel.add(ticketTotalPanel);
			mainPanel.add(TicketTypeContinue);

			int locX = 50, locY = 175, shitX = 185, shitY = 205,
				    btnWidth = ticketType[0].getWidth(),
					cntWidth = centerTicketPanel[0].getWidth();
			
			for(int i = 0; i < ticketType.length; i++) {
				mainPanel.add(ticketType[i]);
				
				if(i % 2 == 0) {
					centerTicketPanel[(i%3)].setText(theModel.getTicketCt(i%3));
					
					mainPanel.add(centerTicketPanel[(i%3)]);
					ticketType[i].setLocation(locX, locY);
					centerTicketPanel[(i%3)].setLocation(locX + btnWidth, locY + 3);
					locX = locX + cntWidth + btnWidth;
				}else {
					ticketType[i].setLocation(locX, locY);
					locX = locX + shitX;
					if(i == 3) { 
						locX = 50; 
						locY = shitY + locY;
						System.out.println(locX + " "+ locY);
					}
						
					
					
				}
			}
			mainPanel.repaint();
		} else if (theModel.getCurrentState() == Model.SELECT_COMBO) {
			mainPanel.removeAll();
			mainPanel.repaint();
			System.out.println("VIEW3");
			mainPanel.add(moveFoward);
			moveFoward.setBounds(400, 570, 75, 50);
			moveFoward.setFont(new Font("Tamil MN", Font.BOLD, 13));
			moveFoward.setBorder(new LineBorder( Color.RED, 3));
	      
			
			comboWhiteLbl.setBackground(Color.WHITE);
			comboWhiteLbl.setBounds(190, 50, 150, 50);
			comboWhiteLbl.setFont(new Font("Tamil MN", Font.BOLD, 13));
			comboWhiteLbl.setBorder(new LineBorder( Color.RED, 4));
			mainPanel.add(comboWhiteLbl);
			comboWhiteLbl.setText(" Choose Food Combo");
			comboWhiteLbl.setOpaque(true);
			
			
			insideWhiteLbl.setBackground(Color.WHITE);
			insideWhiteLbl.setBounds(55, 500, 430, 400);
			insideWhiteLbl.setBorder(new LineBorder( Color.RED, 4));
			mainPanel.add(insideWhiteLbl);
			insideWhiteLbl.setOpaque(true);
			
			insideWhiteLbl.add(redfirstLbl);
			insideWhiteLbl.add(redsecondLbl);
			insideWhiteLbl.add(redThirdLbl);
			insideWhiteLbl.add(redFourLbl);
			
			redfirstLbl.setBackground(Color.WHITE);
			redfirstLbl.setBounds(0, 0, 215, 200);
			redfirstLbl.setBorder(new LineBorder( Color.RED, 4));
			redfirstLbl.setOpaque(true);
	       

			
			redsecondLbl.setBackground(Color.WHITE);
			redsecondLbl.setBounds(215, 0, 215, 200);
			redsecondLbl.setBorder(new LineBorder( Color.RED, 4));
			redsecondLbl.setOpaque(true);
			
			redThirdLbl.setBackground(Color.WHITE);
			redThirdLbl.setBounds(0, 200, 215, 200);
			redThirdLbl.setBorder(new LineBorder( Color.RED, 4));
			redThirdLbl.setOpaque(true);
			
			redFourLbl.setBackground(Color.WHITE);
			redFourLbl.setBounds(215, 200, 215, 200);
			redFourLbl.setBorder(new LineBorder( Color.RED, 4));
			redFourLbl.setOpaque(true); 
			
			redfirstLbl.setIcon(new ImageIcon(theModel.getCombo1PanelImg()));
			redsecondLbl.setIcon( new ImageIcon(theModel.getCombo2PanelImg()));
			redThirdLbl.setIcon( new ImageIcon(theModel.getCombo3PanelImg()));
			redFourLbl.setIcon( new ImageIcon(theModel.getCombo4PanelImg()));
			
			 
				
		
		
			

			
		
	}
			 
			

	 
			
			
			
			
				

				 else if (theModel.getCurrentState() == Model.PAYMENT) {
			
			mainPanel.removeAll();
			mainPanel.repaint();	
			mainPanel.setBackground(Color.WHITE);
			paymentPanel.setIcon(new ImageIcon(theModel.getPaymentImg()));
			total.setText(theModel.TotalTicketCostStr());
			  
			ExpireDate.setBackground(Color.decode    ("#f1f1f1")); 
			SecurityNumber.setBackground(Color.decode("#f1f1f1")); 
			ZipCode.setBackground(Color.decode       ("#f1f1f1"));
			cardNumber.setBackground(Color.decode    ("#f1f1f1")); 
			cardHolderName.setBackground(Color.decode("#f1f1f1"));
			paymentContinue.setBackground         (Color.RED );
			
			total.setBounds             (125, 150,  70, 20);
			paymentPanel.setBounds      ( 5,   0, 540, 600);
			cardHolderName.setBounds    (37, 258, 200, 25);
			cardNumber.setBounds        (37, 317, 200, 25);
			ExpireDate.setBounds        (37, 373,  65, 25);
			SecurityNumber.setBounds    (37, 428,  65, 25); 
			ZipCode.setBounds           (37, 525,  90, 25);	
			paymentContinue.setBounds   (47, 620, 456, 30);
			
			
			paymentContinue.setOpaque(true);
			SecurityNumber.setOpaque(true); 			
			cardHolderName.setOpaque(true);
			ExpireDate.setOpaque(true);
			cardNumber.setOpaque(true);
			ZipCode.setOpaque(true);
			
			paymentPanel.add(total);
			paymentPanel.add(cardHolderName);
			paymentPanel.add(cardNumber);
			paymentPanel.add(ExpireDate);
			paymentPanel.add(SecurityNumber);
			paymentPanel.add(ZipCode);
			mainPanel.add(paymentContinue);
			mainPanel.add(paymentPanel);
			widgetPanel.add(btnBack);
			System.out.println("VIEW5");
		}else if (theModel.getCurrentState() == Model.CONFIRMATION) {
			mainPanel.removeAll();
			mainPanel.repaint();	
			mainPanel.add(confirm);
			confirm.setBounds(55, 500, 430, 400);
			confirm.setIcon( new ImageIcon(theModel.getConfirm()));
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
				if (e.getSource().equals(btnBack)) {
					theModel.setState( Model.INIT_STATE);
				}else {
					theModel.setState(Model.SELECT_MOVIE);
				}
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
							//String movie = theModel.getMovieNameAt(i);
							//theModel.setMovieSelected(movie);
							theModel.setMovieSelected(i);
							theModel.setState(Model.SELECT_TIME);
						}
					}
				}
				
				
					//theModel.setState(Model.ELICIT_STATE);
				
					//theModel.setState(Model.END_STATE);
				
			} else if(theModel.getCurrentState() == Model.SELECT_TIME ) {
				System.out.print("btnPressed4->");
				if (e.getSource().equals(btnBack)) {
					theModel.setState(Model.SELECT_MOVIE);
					System.out.println("back btn pressed");
				}else {
					System.out.println(DayNames.getSelectedItem() + "=" + theModel.getSelectDayStr());
					System.out.println(TimeNames.getSelectedItem() + "=" + theModel.getSelectTimeStr());
					if(DayNames.getSelectedItem().equals(theModel.getSelectDayStr())) {
						DayNames.setBackground(Color.RED);
						theModel.setState(Model.SELECT_TIME);
					} else if(TimeNames.getSelectedItem().equals(theModel.getSelectTimeStr())) {
						TimeNames.setBackground(Color.RED);	
						theModel.setState(Model.SELECT_TIME);
					}else {
						theModel.setState(Model.SELECT_TICKET);
					}
					
				}
					
					
					
					
				
				
			} else if (theModel.getCurrentState() == Model.SELECT_TICKET) {
				if (e.getSource().equals(btnBack)) {
					theModel.setState(Model.SELECT_TIME);
				}else if(e.getSource().equals(TicketTypeContinue)) {	
					boolean notEmpty = false;
					for(int i = 0;i < 3;i++ ) {
						if(!theModel.getTicketCt(i).equals("0")) {
							notEmpty = true;
						}
					}
					if(notEmpty) {
						theModel.setState(Model.SELECT_COMBO);
						
					}else {
						for(int i = 0; i < 3; i++) {
							centerTicketPanel[i].setBorder(new LineBorder( Color.RED, 1));
							centerTicketPanel[i].setBackground(Color.RED);
							centerTicketPanel[i].setOpaque(true);
							
						}
						theModel.setState(Model.SELECT_TICKET);
					}
					
				}else{
					for(int i = 0; i < ticketType.length; i++) {
						if (e.getSource().equals(ticketType[i])) {
							if(i % 2 == 0) {
								theModel.decreaseTicketCount(i%3);
						
								System.out.println("left " + i%3);
								
							}else {
								theModel.increaseTicketCount((i - 1)%3);
								System.out.println("right " + (i - 1)%3);
							}
							
							theModel.setState(Model.SELECT_TICKET);
						}
						centerTicketPanel[i%3].setBackground(Color.WHITE);
						centerTicketPanel[i%3].setOpaque(true);
					}
				}	
				
			}
			else if(theModel.getCurrentState() == Model.SELECT_COMBO ) {
				
				if(e.getSource().equals(moveFoward));{
					System.out.println("payment");
					
					theModel.setState(Model.PAYMENT);
				}
				if(e.getSource().equals(btnBack)) {
					theModel.setState(Model.SELECT_TICKET);
				}
			}
			else if(theModel.getCurrentState() == Model.PAYMENT) {
				if(e.getSource().equals(btnBack)) {
					System.out.println("back");
					theModel.setState(Model.SELECT_TICKET);
				}
				else if(e.getSource().equals(paymentContinue)){
					
					boolean isEmpty = false;

					if (cardHolderName.getText().isEmpty()) {
						isEmpty = true;
						cardHolderName.setBorder(new LineBorder( Color.RED, 2));
					}
					if (cardNumber.getText().isEmpty()) {
						isEmpty = true;
						cardNumber.setBorder(new LineBorder( Color.RED, 2));
					}
					if (ExpireDate.getText().isEmpty()) {
						isEmpty = true;
						ExpireDate.setBorder(new LineBorder( Color.RED, 2));
					}
					if (SecurityNumber.getText().isEmpty()) {
						isEmpty = true;
						SecurityNumber.setBorder(new LineBorder( Color.RED, 2));
					}
					if(ZipCode.getText().isEmpty()) {
						isEmpty = true;
						ZipCode.setBorder(new LineBorder( Color.RED, 2));
					}
					if(isEmpty) {
						theModel.setState(Model.PAYMENT);
					}else {
						theModel.setState(Model.CONFIRMATION);
					}

				}
					
					
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
}