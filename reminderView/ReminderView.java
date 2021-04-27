package reminderView;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import date.Date;
import reminderAPI.IReminderModel;
import reminderAPI.IReminderView;




public class ReminderView extends JFrame implements IReminderView
{
	// Static variables: 
	private static final long serialVersionUID = 2526677148315437465L;
	
	// GUI Instance variables: 
	
	// Panels
	private final JPanel northPanel;
	private final JPanel southPanel;
	private final JPanel eastPanel;
	private final JPanel westPanel;
	private final JPanel centerPanel;
	private final JPanel controllsPanel;
	private final JPanel dateSelectorPanel;
	private final JPanel fileNamePanel;

	// ComboBoxes for selecting a date 
	private final JComboBox<Integer> dayComboBox;
	private final JComboBox<String> monthComboBox;
	private final JComboBox<Integer> yearComboBox;
	
	// Buttons & Controls:
	private JButton selectTextButton;
	private JButton saveButton;
	private JButton importButton;
	private JButton clearTextButton;
	private JButton ColorChooseButton;
	private final JTextArea reminderText;
	private final JTextField fileName;
	
	// Labels:
	private final JLabel ApplicationHeaderLabel; 
	private final JLabel yearLabel;
	private final JLabel monthLabel;
	private final JLabel dayLabel;
	private final JLabel fileNameLabel;
	private final JLabel statusBar;
	
	// Icons:
	private final Icon calendarIcon;
	private final Icon selectIcon;
	private final Icon saveIcon;
	private final Icon importIcon;
	private final Icon clearTextIcon;

	
	
	// Constructor - Initialize all GUI components:
	public ReminderView (IReminderModel model)
	{
		super("캘린더 프로그램");
		this.setSize(600, 500);
		
		// Create panels
		northPanel = new JPanel(new BorderLayout());
		southPanel = new JPanel(new BorderLayout());
		eastPanel = new JPanel(new BorderLayout());
		westPanel = new JPanel(new BorderLayout());
		centerPanel = new JPanel(new BorderLayout());	
		controllsPanel = new JPanel(new GridLayout(0,1,5,5));
		fileNamePanel = new JPanel(new GridLayout(1,2,1,1));
		
		// Set Panels locations:
		this.add(northPanel, BorderLayout.NORTH);
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(westPanel, BorderLayout.WEST);
		this.add(centerPanel, BorderLayout.CENTER);
		
		// Application Title 
		calendarIcon = new ImageIcon(getClass().getResource("calendar.png"));
		ApplicationHeaderLabel = new JLabel(" 캘린더 프로그램", calendarIcon, SwingConstants.LEFT);
		ApplicationHeaderLabel.setFont(new Font("바탕", Font.BOLD, 17));
		ApplicationHeaderLabel.setBackground(new Color (159,239,229));
		ApplicationHeaderLabel.setOpaque(true);
		northPanel.add(ApplicationHeaderLabel, BorderLayout.PAGE_START);
		
		// Date selector section: combo boxes, labels, select button etc.
		yearLabel = new JLabel("연도");
		yearLabel.setOpaque(true);
		yearLabel.setBackground(new Color(100,216,202));
		yearLabel.setFont(new Font("바탕", Font.BOLD, 13));
		yearLabel.setToolTipText("연도 선택");
		monthLabel = new JLabel("월");
		monthLabel.setOpaque(true);
		monthLabel.setBackground(new Color(124,228,216));
		monthLabel.setFont(new Font("바탕", Font.BOLD, 13));
		monthLabel.setToolTipText("달 선택");
		dayLabel = new JLabel("일");
		dayLabel.setOpaque(true);
		dayLabel.setBackground(new Color(159,239,229));
		dayLabel.setFont(new Font("바탕", Font.BOLD, 13));
		dayLabel.setToolTipText("일 선택");
		
		//model.getYear() + "-" + (model.getMonth() + 1) + "-" + model.getDay();


	
		Integer[] yearArray = new Integer[IReminderModel.maxYears];
		yearArray = model.getYears().toArray(yearArray);
		String[] monthArray = new String[IReminderModel.maxMonths];
		model.getMonths().toArray(monthArray);
		Integer[] dayArray = new Integer[IReminderModel.maxDays]; 
		dayArray = model.getValidDays(new Date(1,1,2018)).toArray(dayArray);
	
		yearComboBox = new JComboBox<Integer>(yearArray);
		yearComboBox.setBackground(new Color(255,153,190));
		yearComboBox.setFont(new Font("바탕", Font.PLAIN, 13));
		monthComboBox = new JComboBox<String>(monthArray);
		monthComboBox.setBackground(new Color(255,186,211));
		monthComboBox.setFont(new Font("바탕", Font.PLAIN, 13));
		dayComboBox = new JComboBox<Integer>(dayArray);
		dayComboBox.setBackground(new Color(255,218,231));
		dayComboBox.setFont(new Font("바탕", Font.PLAIN, 13));
		
		dateSelectorPanel = new JPanel(new GridLayout(0, 2, 2, 2));
		dateSelectorPanel.setOpaque(true);
		dateSelectorPanel.setBackground(Color.BLACK);
		dateSelectorPanel.add(yearLabel);
		dateSelectorPanel.add(yearComboBox);
		dateSelectorPanel.add(monthLabel);
		dateSelectorPanel.add(monthComboBox);
		dateSelectorPanel.add(dayLabel);
		dateSelectorPanel.add(dayComboBox);
		
		selectIcon = new ImageIcon(getClass().getResource("search.png"));
		selectTextButton = new JButton("선택한 날짜의 일정 확인", selectIcon);
		selectTextButton.setAlignmentY(LEFT_ALIGNMENT);
		selectTextButton.setBackground(new Color(255,174,174));
		selectTextButton.setFont(new Font("바탕", Font.BOLD, 14));
		selectTextButton.setToolTipText("현재 선택한 날짜에 저장된 텍스트를 불러옵니다.");
		
		northPanel.add(dateSelectorPanel, BorderLayout.CENTER);
		northPanel.add(selectTextButton, BorderLayout.PAGE_END);
		
		// Text Area section
		String defaultText = "메모를 작성하세요...";
		reminderText = new JTextArea(defaultText, 15, 20);
		reminderText.setBackground(new Color(255,255,225));
		reminderText.setFont(new Font("바탕", Font.PLAIN, 20));
		centerPanel.add(new JScrollPane(reminderText), BorderLayout.CENTER);
		
		
		//색깔 고르기
		ColorChooseButton= new JButton("글짜 색깔 지정");
		centerPanel.add(ColorChooseButton, BorderLayout.NORTH);

		
		
		
		// Filename section
		fileName = new JTextField("캘린더.ser");
		fileName.setFont(new Font("돋움", Font.PLAIN, 14));
		fileNameLabel = new JLabel("파일 이름:");
		fileNameLabel.setFont(new Font("돋움", Font.BOLD, 16));
		fileNameLabel.setToolTipText("파일 이름을 작성하세요.");
		fileNamePanel.add(fileNameLabel);
		fileNamePanel.add(fileName);
		centerPanel.add(fileNamePanel, BorderLayout.SOUTH);
		
		// Controls section - save, import, clear, etc. 
		saveIcon = new ImageIcon(getClass().getResource("save.png"));
		saveButton = new JButton("저장", saveIcon); 
		saveButton.setToolTipText("캘린더를 저장합니다.");
		controllsPanel.add(saveButton);
		
		importIcon = new ImageIcon(getClass().getResource("import.png"));
		importButton = new JButton("불러오기", importIcon); 
		importButton.setToolTipText("파일을 불러옵니다.");
		controllsPanel.add(importButton);
		
		clearTextIcon = new ImageIcon(getClass().getResource("clearText.png"));
		clearTextButton = new JButton("일정 삭제", clearTextIcon);
		clearTextButton.setToolTipText("선택된 날짜의 일정을 삭제합니다.");
		controllsPanel.add(clearTextButton);
		
		eastPanel.add(controllsPanel);
		this.add(centerPanel, BorderLayout.CENTER);
		
		// Status bar section:
		statusBar = new JLabel("@Chanan Welt: Maman14 - Question 2");
		statusBar.setFont(new Font("David", Font.PLAIN, 14));
		statusBar.setOpaque(true);
		statusBar.setBackground(Color.LIGHT_GRAY);
		southPanel.add(statusBar, BorderLayout.SOUTH);
	}
	



	// Utility initialization for setting GUI first time:
	public void initGui() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	// Register event handlers for GUI components: 
	public void addSaveListener(ActionListener saveActionListener) 
	{saveButton.addActionListener(saveActionListener);}

	public void addYearListener(ItemListener handler) //여기서부터 콤보박스핸들러
	{yearComboBox.addItemListener(handler);}
	
	public void addMonthListener(ItemListener handler) 
	{monthComboBox.addItemListener(handler);}
	
	public void addDayListener(ItemListener handler) 
	{dayComboBox.addItemListener(handler);}

	public void addSelectTextListener(ActionListener SelectTextHandler) 
	{selectTextButton.addActionListener(SelectTextHandler);}

	public void addImportListener(ActionListener importHandler) 
	{importButton.addActionListener(importHandler);}
	
	public void addClearListener(ActionListener clearActionListener) 
	{clearTextButton.addActionListener(clearActionListener);}

	public void addFileNameListener(ActionListener fileNameHandler)
	{fileName.addActionListener(fileNameHandler);}
	
	public void addColorChooseListener(ActionListener MenuActionListener)
	{ColorChooseButton.addActionListener(MenuActionListener);}
	
	// Getters: 
	public Date getInputDate() {return new Date(getInputDay(), getInputMonth(), getInputYear());}
	public int getInputYear(){return (Integer)yearComboBox.getSelectedItem();}
	public int getInputMonth() {return 1 + monthComboBox.getSelectedIndex();}
	public int getInputDay() {return (Integer)dayComboBox.getSelectedItem();}
	public String getInputText() {return reminderText.getText();}
	public String getInputFileName() {return fileName.getText();}
	public Color getInputTextColor(Color color) {return reminderText.getForeground();}

	
	// Setters: 
	public void setReminderInputText(String newText) {reminderText.setText(newText);}
	public void setInputFileName(String newFileName) {fileName.setText(newFileName);}


	
	public void setDayList(ArrayList<Integer> days)
	{
		dayComboBox.removeAllItems();
		for (int day: days)
			dayComboBox.addItem(day);
	}
	
	public void setDate(Date date)
	{
		yearComboBox.setSelectedItem(date.getYear());	
		monthComboBox.setSelectedIndex(date.getMonth()-1);
		dayComboBox.setSelectedItem(date.getDay());
	}
	
	public void setStatusBar(String message)
	{
		SecureRandom randomNumbers = new SecureRandom();
		int randomRedColor = randomNumbers.nextInt(256);
		int randomBlueColor = randomNumbers.nextInt(256);
		statusBar.setOpaque(true);
		statusBar.setText(message);
		statusBar.setBackground(new Color(255, 170, 182));
	}
	
	// update data according to observed model & changed object:
	public void update(Observable model, Object changedObject) 
	{
		statusBar.setText(String.format("%s 파일을 성공적으로 불러왔습니다.", changedObject));
	}
	
	// Display Error Message:
	public void displayErrorMessage(String message)
	{
		JOptionPane.showMessageDialog(this, message, "캘린더 프로그램", JOptionPane.ERROR_MESSAGE);
	}


	
	
}
