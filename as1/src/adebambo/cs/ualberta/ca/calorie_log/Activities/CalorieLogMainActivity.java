package adebambo.cs.ualberta.ca.calorie_log.Activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import adebambo.cs.ualberta.ca.calorie_log.CalorieLogApplication;
import adebambo.cs.ualberta.ca.calorie_log.R;
import adebambo.cs.ualberta.ca.calorie_log.Log.Log;
import adebambo.cs.ualberta.ca.calorie_log.Log.LogsArrayAdapter;
import adebambo.cs.ualberta.ca.calorie_log.UIElements.LogDialog;
import adebambo.cs.ualberta.ca.calorie_log.UIElements.LogDialog.LogDialogListener;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Launcher screen. It displays the list of entries for a particular date,
 * default it current date. Also shows daily calorie count and instantiates the
 * other activity class (CompleteLogActivity)
 * 
 */
public class CalorieLogMainActivity extends Activity implements
		OnItemClickListener, OnClickListener, LogDialogListener,
		OnDateSetListener
{

	private TextView currentDateTextView;
	private TextView noLogsTextView;
	private Date currentDate = new Date();

	private ListView logsList;
	private LogsArrayAdapter logsAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		Button b = (Button) findViewById(R.id.view_comp_log);
		b.setOnClickListener(this);

		b = (Button) findViewById(R.id.add_log_button);
		b.setOnClickListener(this);

		logsList = (ListView) findViewById(R.id.log_list);
		logsList.setOnItemClickListener(this);

		// set the current date to today.
		currentDateTextView = (TextView) findViewById(R.id.current_date);
		currentDateTextView.setText(formatDate(currentDate));
		currentDateTextView.setOnClickListener(this);
		noLogsTextView = (TextView) findViewById(R.id.no_logs);

	}

	@Override
	protected void onResume()
	{

		// TODO Auto-generated method stub
		super.onResume();
		// Set the date to today
		// currentDate = new Date();
		// currentDateTextView.setText(formatDate(currentDate));

		// setup the list adapter
		// logs = Log.logsByDate(currentDate);
		logsAdapter = new LogsArrayAdapter(this, 0, Log.logsByDate(currentDate));
		logsList.setAdapter(logsAdapter);

		updateList();
	}

	/**
	 * Should be called any time the logs list gets updated and the listview
	 * should be refreshed to reflect those changes
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
	}

	/**
	 * Loops through the current logs and calculates the total number of
	 * calories for the day. Then updates the UI with the total
	 */
	private void setTotal()
	{

		TextView tv = (TextView) findViewById(R.id.total_calories);
		int total = 0;
		for (Log log : logsAdapter.getLogs())
		{
			total += log.getCalorieCount();
		}
		tv.setText("" + total);
	}

	/**
	 * Opens a new dialog where the user will be able to create a new log
	 */
	private void show_create_log_dialog()
	{

		LogDialog dialog = new LogDialog(this, this);
		dialog.setDate(currentDate);
		dialog.show();
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
	 * Opens the a date picker dialog. Once the user picks a date, the log list
	 * will be updated with logs from that day
	 */
	private void show_datepicker_dialog()
	{

		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dialog = new DatePickerDialog(this, this, year, month,
				day);
		dialog.show();
	}

	/**
	 * Starts a new activity that displays all the logs saved by the user
	 */
	private void show_complete_log()
	{

		Intent myIntent = new Intent(this, CompleteLogActivity.class);
		this.startActivity(myIntent);
	}

	public void onClick(View v)
	{

		switch (v.getId())
		{
			case R.id.add_log_button:
				show_create_log_dialog();
				break;
			case R.id.current_date:
				show_datepicker_dialog();
				break;
			case R.id.view_comp_log:
				show_complete_log();
				break;
		// case R.id.view_stat: show_stats(); break;
		}
	}

	/**
	 * formats a java.util.date object to a string for displaying to the user
	 * 
	 * @param date
	 * @return String
	 */
	public static String formatDate(Date date)
	{

		Context context = CalorieLogApplication.context();
		String dateFormat = context.getResources().getString(
				R.string.date_format);
		return new SimpleDateFormat(dateFormat).format(date);
	}

	/**************************************************************************
	 * 
	 * LogDialogListener Method Implementation
	 * 
	 *************************************************************************/

	/**
	 * 
	 * Callback after a log is created from a LogDialogLIstener. Add the new log
	 * to the list and refresh the loglist
	 * 
	 * @param [Log] log
	 */
	public void logCreated(Log log)
	{

		if (areDatesEqual(log.getDate(), currentDate))
		{
			// logs.add(log);
			logsAdapter.add(log);
			updateList();
		}
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

	/**
	 * Compares Two date and returns whether the two dates are the same day
	 * 
	 * @param [Date] a
	 * @param [Date] b
	 * @return
	 */
	private boolean areDatesEqual(Date a, Date b)
	{

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(a);
		cal2.setTime(b);

		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
						.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * Refreshes the list of logs being displayed when the date is changed from
	 * the date picker dialog
	 */
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth)
	{

		Calendar cal = Calendar.getInstance();
		cal.set(year, monthOfYear, dayOfMonth);
		currentDate = cal.getTime();

		String date_string = formatDate(currentDate);
		currentDateTextView.setText(date_string);

		logsAdapter.setLogs(Log.logsByDate(currentDate));

		updateList();
	}

	/**
	 * Calls the method to display the edit/create dialog box when a list item
	 * is clicked
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{

		Log log = logsAdapter.getItem(arg2);
		show_create_log_dialog(log);
	}

}