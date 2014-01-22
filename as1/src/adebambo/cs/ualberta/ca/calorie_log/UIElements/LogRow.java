package adebambo.cs.ualberta.ca.calorie_log.UIElements;

import adebambo.cs.ualberta.ca.calorie_log.R;
import adebambo.cs.ualberta.ca.calorie_log.Log.Log;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

// Handles the display for rows of saved calorie logs in a list
public class LogRow extends LinearLayout
{

	Context mContext;

	Log mLog;

	public LogRow(Context context)
	{

		super(context);

		mContext = context;
		setup();
	}

	public LogRow(Context context, AttributeSet attrs)
	{

		super(context, attrs);

		mContext = context;
		setup();
	}

	private void setup()
	{

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.log_row, this);
	}

	public void setLog(Log log)
	{

		mLog = log;
		TextView tv = (TextView) findViewById(R.id.log_meal_name);
		tv.setText(mLog.getMealName() + "");

		tv = (TextView) findViewById(R.id.log_calories);
		tv.setText(mLog.getCalorieCount() + "");

		tv = (TextView) findViewById(R.id.log_date);
		tv.setText(mLog.getDate() + "");
	}
}