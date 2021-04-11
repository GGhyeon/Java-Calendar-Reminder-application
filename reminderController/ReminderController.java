package reminderController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.IOException;

import java.util.Observable;
import java.util.Observer;

import reminderAPI.IReminderController;
import reminderAPI.IReminderModel;
import reminderAPI.IReminderView;

public class ReminderController <M extends Observable & IReminderModel, V extends Observer & IReminderView> implements IReminderController
{
	// utility enumerations:
	public static enum comboBoxType {YEAR, MONTH, DAY}; 
	
	// Instance variables:
	private M model; 
	private V view; 
	
	// Constructor: 
	public ReminderController (M model, V view)
	{
		// register model and view in the controller:
		this.model = model;
		this.view = view;
		
		// register view as an observer of the model:
		model.addObserver(view);
		
		// register event handlers for view:
		view.addDayListener(new comboBoxListener(comboBoxType.DAY));
		view.addMonthListener(new comboBoxListener(comboBoxType.MONTH));
		view.addYearListener(new comboBoxListener(comboBoxType.YEAR));
		view.addSelectTextListener(new SelectTextHandler());
		view.addSaveListener(new SaveHandler());
		view.addImportListener(new ImportHandler());
		view.addClearListener(new ClearTextHandler());
		view.addFileNameListener(new fileNameHandler());
	}
	
	// Set default data and initialize GUI
	@Override
	public void initializeApplication() 
	{
		view.setDate(model.getCurrentDate());
		view.setReminderInputText("날짜를 선택하고,\n이곳에 일정을 작성한 후\n파일을 저장하세요.");
		view.initGui();
	}
	
	// Handler for Writing reminders to database file after saving:
	private class SaveHandler implements ActionListener 
	{
	     public void actionPerformed(ActionEvent event) 
	     {
	    	 model.setReminder(view.getInputDate(), view.getInputText());
	    	 String modelFileName = model.getFileName();
	    	 String inputFileName = view.getInputFileName();
	    	 
	    	 if (inputFileName == null || inputFileName.length() == 0)
	    		 view.displayErrorMessage("Error: Cannot save to a file with no name");
	    	 else 
	    	 {
		    	 if (!(modelFileName.equals(inputFileName)))
		    			 model.setFileName(inputFileName);
		    	 try
		    	 {	 
		    		 model.WriteCalendarToDatabase();
		    		 view.setStatusBar(String.format("%s에 대한 일정이 성공적으로 파일 \"%s\"에 저장되었습니다.", view.getInputDate(), model.getFileName()));
		    	 } 
		    	 catch (IOException e) 
		    	 {
					view.displayErrorMessage("데이터를 저장하는 데 실패했습니다!");
		    	 }
	    	 }
	     }
	}
	
	// Handler for loading data from database file after importing:
	private class ImportHandler implements ActionListener 
	{
	     public void actionPerformed(ActionEvent event) 
	     {
	    	 try 
	    	 {
				if(model.readCalendarFromDatabase())
				{
					view.setReminderInputText(model.getReminder(view.getInputDate()));
					view.setStatusBar(String.format("%s 파일을 성공적으로 불러왔습니다.", model.getFileName()));
				}
			 }
	    	 catch (ClassNotFoundException e)
	    	 {
	    		 view.displayErrorMessage("Error: Invalid file format");
			 } 
	    	 catch (IOException e)
	    	 {
	    		 view.displayErrorMessage("Error: Could not open file");
			 }
	    	 finally
	    	 {
				view.setInputFileName(model.getFileName());
	    	 }
	     }
	}
	
	// Handler for retrieving text for selected date in view:
	private class SelectTextHandler implements ActionListener 
	{
	     public void actionPerformed(ActionEvent event) 
	     {
	    	 view.setReminderInputText(model.getReminder(view.getInputDate()));
	    	 view.setStatusBar(String.format("%s 일정이 선택되었습니다.", view.getInputDate()));
	     }
	}
	
	// Handler for clearing text area content for selected date:
	private class ClearTextHandler implements ActionListener
	{
	     public void actionPerformed(ActionEvent event) 
	     {
	    	 view.setReminderInputText(null);
	     }
	}
	
	// Handler for setting a new desired file name for database:
	private class fileNameHandler implements ActionListener
	{
	     public void actionPerformed(ActionEvent event) 
	     {
	    	 String newFileName = view.getInputFileName();
	    	 if (newFileName != null && newFileName.length() != 0)
	    	 {
	    		model.setFileName(newFileName);
	    		view.setStatusBar(String.format("File name Successfully changed to %s", newFileName));
	    	 }
	    	 else 
	    	 {
	    		 view.setInputFileName(model.getFileName());
	    		 view.displayErrorMessage("You cannot enter empty file name!\nFile name is restored to previous one");
	    	 }
	     }
	}
	
	// Handler for date components combo boxes:  
	private class comboBoxListener implements ItemListener
	{
		private comboBoxType changedComboBox; 
		
		public comboBoxListener (comboBoxType changedComboBox)
		{
			this.changedComboBox = changedComboBox;
		}
		
		@Override
		public void itemStateChanged(ItemEvent event)
		{// Adjust day list and clear old text
			if (event.getStateChange() == ItemEvent.SELECTED)
			{
				if(this.changedComboBox != comboBoxType.DAY) 
					view.setDayList(model.getValidDays( view.getInputDate()));
				view.setReminderInputText(null);
			}			
		}
	}
}
