package adebambo.cs.ualberta.ca.calorie_log.Activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adebambo.cs.ualberta.ca.calorie_log.R;
import adebambo.cs.ualberta.ca.calorie_log.Log.Log;
import adebambo.cs.ualberta.ca.calorie_log.Log.LogsArrayAdapter;
import adebambo.cs.ualberta.ca.calorie_log.UIElements.LogDialog;
import adebambo.cs.ualberta.ca.calorie_log.UIElements.LogDialog.LogDialogListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity Class that displays a list of all saved log entries It also shows
 * other log information like the average calories consumed per day, the total
 * calories consumed from all entries and the total days logged
 */
public class CompleteLogActivity extends Activity implements LogDialogListener,
		OnItemClickListener
{

	private TextView noLogsTextView;
	private ListView logsList;

	private LogsArrayAdapter logsAdapter;
	float daysLogged;
	int total_cal;
	int average_cal;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_comp_list);

		noLogsTextView = (TextView) findViewById(R.id.no_logs_ever);
		logsList = (ListView) findViewById(R.id.log_list2);
		logsList.setOnItemClickListener(this);
		logsAdapter = new LogsArrayAdapter(this, 0, Log.all());
		logsList.setAdapter(logsAdapter);

	}

	@Override
	protected void onResume()
	{

		// TODO Auto-generated method stub
		super.onResume();
		super.onStart();
		// logsList.setSelection(logsAdapter.getCount());
		// logsList.smoothScrollToPosition((logsAdapter.getCount()));
		updateList();
	}

	/**
	 * Is called any time the logs list gets updated and the listview is to be
	 * refreshed to reflect those changes
	 */
	private void updateList()
	{

		// Toggle the no logs textview if no logs available
		if (logsAdapter.getLogs().size() == 0)
		{
			noLogsTextView.setVisibility(View.VISIBLE);
		}

		else
		{
			noLogsTextView.setVisibility(View.GONE);
		}

		logsAdapter.notifyDataSetChanged();
		setTotal();
		setdaysLogged();
		setAverageIntake();
	}

	/**
	 * Calculates the sum of all calories in saved log entries and displays the
	 * value
	 */
	private void setTotal()
	{

		TextView tv = (TextView) findViewById(R.id.total_calories_consumed);
		total_cal = 0;
		for (Log log : logsAdapter.getLogs())
		{
			total_cal += log.getCalorieCount();
		}
		tv.setText("" + total_cal);
	}

	/**
	 * calculates the span of days of saved logs i.e (most recent log date) -
	 * (oldest log date)
	 */
	private void setdaysLogged()
	{

		// date objects are automatically set to current date
		Date minDate = new Date();
		Date maxDate = new Date();
		float diff = 0;
		List<Date> myList = new ArrayList<Date>();

		TextView tv = (TextView) findViewById(R.id.days_logged);

		// First check check the size of logs list to see if empty
		if (logsAdapter.getLogs().size() == 0)
		{
			daysLogged = 0;
		} else
		{
			for (Log log : logsAdapter.getLogs())
			{
				myList.add(log.getDate());
			}
			minDate = min(myList);
			maxDate = max(myList);
			diff = (maxDate.getTime()) - (minDate.getTime());
			// Don't forget to convert from milliseconds to days
			daysLogged = diff / (24 * 60 * 60 * 1000);

		}

		DecimalFormat form = new DecimalFormat();
		form.setMaximumFractionDigits(1);
		tv.setText("" + form.format(daysLogged));

	}

	/**
	 * Calculates the average intake of calories using total calories and number
	 * of days logged
	 */
	private void setAverageIntake()
	{

		// Check to make sure denominator is not zero
		if (daysLogged > 0)
		{
			average_cal = (int) (total_cal / daysLogged);

		} else
		{
			average_cal = 0;
		}
		TextView tv = (TextView) findViewById(R.id.average_calories);
		DecimalFormat form = new DecimalFormat();
		form.setMaximumFractionDigits(0);
		tv.setText("" + form.format(average_cal));
	}

	/**
	 * Finds and returns the minimum date in a list of dates
	 * 
	 * @param dates
	 * @return
	 */
	public static Date min(List<Date> dates)
	{

		long min = Long.MAX_VALUE;
		Date minDate = null;
		for (Date value : dates)
		{
			long v = value.getTime();
			if (v < min)
			{
				min = v;
				minDate = value;
			}
		}
		return minDate;
	}

	/**
	 * Finds and returns the maximum date in a list of dates
	 * 
	 * @param dates
	 * @return
	 */
	public static Date max(List<Date> dates)
	{

		long max = 0;
		Date maxDate = null;
		for (Date value : dates)
		{
			long v = value.getTime();
			if (v > max)
			{
				max = v;
				maxDate = value;
			}
		}
		return maxDate;
	}

	/**
	 * Opens a new dialog passing the log into the LogDialog. This is used for
	 * editing a single log
	 * 
	 * @param log
	 */
	private void show_create_log_dialog(Log log)
	{

		LogDialog dialog = new LogDialog(this, this);
		dialog.setLog(log);
		dialog.show();
		
	}

	/**
	 * 
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{

		Log log = logsAdapter.getItem(arg2);
		show_create_log_dialog(log);
	}

	/**
	 * 
	 * Callback after a log is created from a LogDialogLIstener. Add the new log
	 * to the list and refresh the loglist
	 * 
	 * @param [Log] log
	 */
	public void logCreated(Log log)
	{
		logsAdapter.add(log);
		updateList();
		logEdited(log);
	}

	/**
	 * 
	 * Callback after a log is edited from a LogDialogLIstener. Refreshes the
	 * log list
	 * 
	 * @param [Log] log
	 */
	public void logEdited(Log log)
	{

		updateList();
	}

	/**
	 * 
	 * Callback after a log is Deleted from a LogDialogLIstener. Refreshes the
	 * log list
	 * 
	 * @param [Log] log
	 */
	public void logDeleted(Log log)
	{

		logsAdapter.remove(log);
		updateList();
	}

}
