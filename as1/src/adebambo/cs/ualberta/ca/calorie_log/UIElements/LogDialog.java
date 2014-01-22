package adebambo.cs.ualberta.ca.calorie_log.UIElements;

import java.util.Calendar;
import java.util.Date;

import adebambo.cs.ualberta.ca.calorie_log.R;
import adebambo.cs.ualberta.ca.calorie_log.Log.Log;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This is the class that implements the edit log or create a new log dialog box
 */
public class LogDialog
{

	Context mContext;

	LogDialogListener mListener;

	Log mLog;
	Date mDate;

	/**
	 * @param context
	 *            required to create and display the dialog
	 * @param listener
	 *            the callback listener that will receive the newly created log
	 */
	public LogDialog(Context context, LogDialogListener listener)
	{

		mContext = context;
		mListener = listener;
		mDate = new Date();
	}

	/**
	 * Interface that handles log creation, deletion, or log edit Notifies the
	 * activity class that the log has been modified
	 */
	public interface LogDialogListener
	{

		/**
		 * passes the newly created log back to the activity
		 * 
		 * @param log
		 */
		public abstract void logCreated(Log log);

		public abstract void logEdited(Log log);

		public abstract void logDeleted(Log log);
	}

	/**
	 * assigns mLog to log for methods
	 * 
	 * @param log
	 */
	public void setLog(Log log)
	{

		mLog = log;
	}

	public void setDate(Date date)
	{

		mDate = date;
	}

	/**
	 * Creates an alert dialog using a layout inflator then displays the alert
	 * dialog using mContext
	 */
	public void show()
	{

		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.log_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		final EditText logMealEdit = (EditText) view
				.findViewById(R.id.log_meal);
		final EditText logCaloriesEdit = (EditText) view
				.findViewById(R.id.log_calories);
		final EditText calPerEdit = (EditText) view
				.findViewById(R.id.cal_per_serv);
		final EditText perServingEdit = (EditText) view
				.findViewById(R.id.per_serving);
		final EditText servAmountEdit = (EditText) view
				.findViewById(R.id.serv_amount);
		if (mLog != null)
		{
			// This is for editing an existing log
			logMealEdit.setText(mLog.getMealName());
			logCaloriesEdit.setText(mLog.getCalorieCount() + "");
			builder.setTitle(R.string.edit_log_title);
			builder.setNegativeButton(R.string.create_log_negative_button,
					new OnClickListener()
					{

						public void onClick(DialogInterface dialog, int which)
						{

							deleteLog();
						}
					});
		}

		else

		{
			builder.setTitle(R.string.create_log_title);
		}

		/**
		 * TextWatcher implementation to monitor the EditText fields with
		 * method. Update total calories field using serving amount, and
		 * calories per serving
		 */
		TextWatcher watcher1 = new TextWatcher()
		{

			public void afterTextChanged(Editable s)
			{

				int caloriesPer = 0;
				float perServing = 0;
				float amount = 0;

				int totCal = 0;
				try
				{
					amount = Float.parseFloat(servAmountEdit.getText()
							.toString());
					caloriesPer = Integer.parseInt(calPerEdit.getText()
							.toString());
					perServing = Float.parseFloat(perServingEdit.getText()
							.toString());
				} catch (NumberFormatException e)
				{
					logCaloriesEdit.setText("0");
					// logCaloriesEdit.hasFocus();
					// logCaloriesEdit.selectAll();
				}

				if (amount != 0 & caloriesPer != 0 & perServing != 0)
				{
					totCal = (int) (caloriesPer / perServing * amount);
				} else
				{
					totCal = 0;
				}
				logCaloriesEdit.setText("" + (totCal));
			}

			public void onTextChanged(CharSequence s, int start, int count,
					int after)
			{

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{

			}
		};

		servAmountEdit.addTextChangedListener(watcher1);
		calPerEdit.addTextChangedListener(watcher1);
		perServingEdit.addTextChangedListener(watcher1);

		builder.setView(view);
		builder.setPositiveButton(R.string.create_log_positive_button,
				new OnClickListener()
				{

					/**
					 * OnClickListener implementation that will create a new log
					 * then notifies the mlistener that the new log has been
					 * created
					 */

					public void onClick(DialogInterface dialog, int which)
					{

						// get the text from the meal name field
						String mealname = logMealEdit.getText().toString();
						int calories = 0;
						// get the text from the calorie field
						try
						{
							calories = Integer.parseInt(logCaloriesEdit
									.getText().toString());
						} catch (NumberFormatException e)
						{
							logCaloriesEdit.setText("0");

						}
						if (calories != 0)
						{
							// edit old log
							if (mLog != null)
							{
								editLog(mealname, calories);
							}

							else
							// create new log
							{
								createNewLog(mealname, calories);
							}
						} else
						{
							Toast.makeText(mContext, "Invalid Entry",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		builder.setNeutralButton(R.string.create_log_neutral_button, null);
		builder.create().show();
	}

	/**
	 * Deletes the already created log. Then notifies the dialog listener that
	 * the log has been deleted.
	 */
	private void deleteLog()
	{

		mLog.delete();
		if (mListener != null)
		{
			mListener.logDeleted(mLog);
		}
	}

	/**
	 * Edits the already created log. Then notifies the dialog listener that the
	 * log has been edited
	 * 
	 * @param newName
	 * @param newCalories
	 */
	private void editLog(String newName, int newCalories)
	{

		mLog.setMealName(newName);
		mLog.setCalorieCount(newCalories);
		mLog.edit();
		/*
		 * Date date1 = mLog.getDate(); this.deleteLog();
		 * //createNewLog(newName, newCalories);
		 * 
		 * mLog = Log.create(newName, newCalories, date1); //mLog.save(); if
		 * (mListener != null) { mListener.logCreated(mLog); }
		 */
		if (mListener != null)
		{
			mListener.logEdited(mLog);
		}
	}

	/**
	 * Creates a new log using values from the dialog. Then notifies the dialog
	 * listener that a new log has been created.
	 * 
	 * @param newName
	 * @param newCalories
	 */
	private void createNewLog(String newName, int newCalories)
	{

		Date date2 = new Date();
		/*
		 * //DEPRECATED if (mDate.getDate() == date2.getDate() &
		 * mDate.getMonth() == date2.getMonth() & mDate.getYear() ==
		 * date2.getYear()) { mDate = date2; }
		 */
		if (sameDay(mDate, date2))
		{	//update the time for today
			mDate = date2;
		}
		mLog = Log.create(newName, newCalories, mDate);
		if (mListener != null)
		{
			mListener.logCreated(mLog);
		}
	}

	private boolean sameDay(Date date1, Date date2)
	{

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
						.get(Calendar.DAY_OF_YEAR);
		return sameDay;
	}

}
