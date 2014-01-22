package adebambo.cs.ualberta.ca.calorie_log;

import java.util.ArrayList;
import java.util.List;

import com.orm.androrm.DatabaseAdapter;
import com.orm.androrm.Model;

import adebambo.cs.ualberta.ca.calorie_log.R;
import adebambo.cs.ualberta.ca.calorie_log.Log.Log;
import android.app.Application;
import android.content.Context;

/**
 * 
 * Works Referenced:
 * Referred to newboston.com for tutorial on android application development, 
 * Got background for log_dialog.xml layout from all-wallpapers.net,
 * Used androrm package (http://androrm.the-pixelpla.net/documentation/beginners/tutorials/)
 *  to handle log database,
 * Referenced Justin Taylor's calorie counter app on github. I modelled my app and most of my
 * code around his (https://github.com/justin-taylor/Calorie-Counter)
 */

/**
 * The original developer of a substantial portion of code used in this android
 * app is Justin Taylor. (https://github.com/justin-taylor)
 * 
 */

public class CalorieLogApplication extends Application
{

	private static CalorieLogApplication appContext;

	public void onCreate()
	{

		super.onCreate();

		appContext = this;
		initializeDatabase();
	}

	/**
	 * An accessor method to make it easier to access the app context from
	 * classes that are not activities
	 * 
	 * @return Context the application context
	 */
	public static Context context()
	{

		return appContext;
	}

	private void initializeDatabase()
	{

		// setup the database
		List<Class<? extends Model>> models = new ArrayList<Class<? extends Model>>(
				1);
		models.add(Log.class);

		String dbName = this.getResources().getString(R.string.database_name);
		DatabaseAdapter.setDatabaseName(dbName);
		DatabaseAdapter adapter = new DatabaseAdapter(appContext);
		adapter.setModels(models);
	}
}
