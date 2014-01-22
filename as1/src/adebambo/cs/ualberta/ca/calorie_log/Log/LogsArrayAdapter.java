package adebambo.cs.ualberta.ca.calorie_log.Log;

import java.util.List;

import adebambo.cs.ualberta.ca.calorie_log.UIElements.LogRow;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Array adapter that accesses log database and renders it as a list. This is so
 * the Activity classes can display the log entries as a list item
 * 
 */
public class LogsArrayAdapter extends ArrayAdapter<Log>
{

	Context mContext;
	List<Log> mLogs;

	public LogsArrayAdapter(Context context, int textViewResourceId,
			List<Log> logs)
	{

		super(context, textViewResourceId);

		mContext = context;
		mLogs = logs;
	}

	public void setLogs(List<Log> logs)
	{

		mLogs = logs;
	}

	public List<Log> getLogs()
	{

		return mLogs;
	}

	public void add(Log log)
	{

		mLogs.add(log);
	}

	public void remove(Log log)
	{

		mLogs.remove(log);
	}

	/**
	 * returns the number of logs that will appear in the listview
	 * 
	 * @return [int] number of logs
	 */
	public int getCount()
	{

		return mLogs.size();
	}

	/**
	 * returns the log found at the index of the position parameter
	 * 
	 * @return [Log]
	 */
	public Log getItem(int position)
	{

		return mLogs.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{

		LogRow view = (LogRow) convertView;

		if (view == null)
		{
			view = new LogRow(mContext);
		}

		Log log = getItem(position);
		view.setLog(log);
		return view;
	}
}
