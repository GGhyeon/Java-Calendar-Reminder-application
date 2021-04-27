package reminderModel;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Observable;

import javax.swing.JFileChooser;

import date.Date;
import reminderAPI.IReminderModel;

public class ReminderModel extends Observable implements IReminderModel, Serializable
{
	// Static variables: 
	private static final long serialVersionUID = 1L;
	
	// Instance variables:
	private Hashtable<Date, String> calendar; 
	private String fileName;
	
	// Constructor 
	public ReminderModel()
	{
		super();
		calendar = new Hashtable<Date, String>();
		fileName = "캘린더.ser"; // default file name 
	}

	// Basic Getters: 
	public String getReminder(Date date) {return calendar.get(date);}
	public void setReminder(Date date, String text) {calendar.put(date, text);}
	public Hashtable<Date, String> getCalendar(){return calendar;}
	public String getFileName() {return fileName;}
	public Date getCurrentDate() {return new Date();}

	
	// Basic Setters: 
	public void setCalendar(Hashtable<Date, String> databaseCalendar) {this.calendar = databaseCalendar;}
	public void setFileName(String fileName) 
	{
		if ((fileName != null) && (fileName.length() != 0))
			this.fileName = fileName;
	}
		
	// Utility Getters for Initialization of a calendar: 
	
	// Get list of years: 
	public ArrayList<Integer> getYears() 
	{
		int statingYear = 2000;
		int maxYear = 2050;
		ArrayList <Integer>years = new ArrayList<Integer>(maxYear);
		for (int year = statingYear; year <= maxYear; year++)
			years.add(year);
		return years;
	}
	
	// Get list of months: 
	public ArrayList<String> getMonths() 
	{
		String[] monthsArray = {"1월", "2월", "3월", "4월", "5월", "6월",
						 "7월", "8월", "9월", "10월", "11월", "12월"};
		ArrayList <String>monthList = new ArrayList<String>(Arrays.asList(monthsArray));
		return monthList;
	}
	
	// Get list of days:
	public ArrayList<Integer> getValidDays(Date date)
	{
		int maxDay = Date.maxDayOfMonthAndYear(date.getYear(), date.getMonth());
		ArrayList <Integer> validDays = new ArrayList<Integer>(maxDay);
		
		for (int day = 1; day <= maxDay; day++)
			validDays.add(day);
		
		return validDays;
	}

	// Save all reminders data from all dates to database file:
	public void WriteCalendarToDatabase() throws IOException
	{
		ObjectOutputStream outputfile;
		outputfile = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)));
		outputfile.writeObject(calendar);
		outputfile.close();
	}

	// Get a database file and and read all it's data to model:
	@SuppressWarnings("unchecked")
	public boolean readCalendarFromDatabase() throws IOException, ClassNotFoundException 
	{
		File inputFile;
		ObjectInputStream inputStream;

   	 	JFileChooser fileChooser = new JFileChooser();
   	 	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
   	 	if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
   	 	{
   	 		inputFile = fileChooser.getSelectedFile();
   	 		this.fileName = inputFile.getName();
   	 		inputStream = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)));
   	 		Object data = inputStream.readObject();
   	 		if (data instanceof Hashtable)
   	 		{
   	 			this.calendar = (Hashtable<Date, String>)data;
   	 			this.setChanged();
   	 			this.notifyObservers(fileName);
   	 			inputStream.close();
   	 			return true;
   	 		}
   	 		else 
   	 			throw new ClassNotFoundException("파일을 해석할 수 없습니다.");
   		}
   	 	else return false;
	}


}
