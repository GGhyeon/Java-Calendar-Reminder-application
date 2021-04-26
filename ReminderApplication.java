
import reminderController.ReminderController;
import reminderModel.ReminderModel;
import reminderMusic.PlayMusic;
import reminderView.ReminderView;

public class ReminderApplication
{
	public static void main (String[] args)
	{		
		// Create model & view: 
		ReminderModel model = new ReminderModel();
		ReminderView view = new ReminderView(model);
		
		// Music
		String filepath="playMusic.wav";
		
		PlayMusic musicObject=new PlayMusic();
		musicObject.playMusic(filepath);
		
		// Create controller for model & view: 
		ReminderController<ReminderModel, ReminderView> controller;
		controller = new ReminderController<ReminderModel, ReminderView>(model, view);
		
		// Initiate application: 
		controller.initializeApplication();
	}
}
