package adebambo.cs.ualberta.ca.calorie_log.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adebambo.cs.ualberta.ca.calorie_log.CalorieLogApplication;
import android.content.Context;

import com.orm.androrm.CharField;
import com.orm.androrm.DateField;
import com.orm.androrm.Filter;
import com.orm.androrm.IntegerField;
import com.orm.androrm.Model;
import com.orm.androrm.QuerySet;

/**
 * This class is the database for storing the users entry information. The log
 * is a database object from the androrm package
 * 
 */
public class Log extends Model
{

	protected CharField name;
	protected IntegerField calories;

	protected DateField date;

	public Log()
	{

		super(true);
		name = new CharField(80);
		calories = new IntegerField();
		date = new DateField();
	}

	/**
	 * Meal Constructor that takes the name of the meal and the number of
	 * calories the meal is
	 * 
	 * @param meal
	 * @param calorie_count
	 */
	public static Log create(String meal, int calorie_count, Date logDate)
	{

		Log log = new Log();

		log.setMealName(meal);
		log.setCalorieCount(calorie_count);
		log.setDate(logDate);
		log.save();

		return log;
	}

	/**
	 * @return [String] this log's meal name
	 */
	public String getMealName()
	{

		return name.get();
	}

	/**
	 * @param [String] meal the new meal name for this log
	 */
	public void setMealName(String meal)
	{

		name.set(meal);
	}

	/**
	 * @return [int] this log's calorie count
	 */
	public int getCalorieCount()
	{

		return calories.get().intValue();
	}

	/**
	 * Sets the value of the calorie field
	 */
	public void setCalorieCount(int count)
	{

		calories.set(count);
	}

	public Date getDate()

 	{

		return date.get();
	}

	/**
	 * 
	 * @param [Date] date the new date
	 */
	public void setDate(Date d)
	{

		date.set(d);
	}

	public boolean save()
	{

		int id = Log.objects(context(), Log.class).all().count() + 1;
		return this.save(context(), id);
	}

	public boolean edit()
	{

		return this.save(context());
	}

	public boolean delete()
	{

		return this.delete(context());
	}

	/**
	 * Returns a list of all logs in a given date
	 * 
	 * @param date
	 * @return
	 */
	public static List<Log> logsByDate(Date date)
	{

		String query_string = formatDateForQuery(date);
		Filter filter = new Filter();
		filter.contains("date", query_string);

		return Log.objects().filter(filter).orderBy("-date").toList();
	}

	/**
	 * Sorts all logs by date in ascending order and returns the list
	 * 
	 * @return
	 */
	public static List<Log> orderByDate()
	{

		return Log.objects().all().orderBy("date").toList();
	}

	/**
	 * Formats a date object into a string that that it can be used to query the
	 * androrm database. This is kind of ugly because this requires a knowledge
	 * of the inner working of androrm.
	 * 
	 * @param [Date] date the Date that will be queried
	 * 
	 * @return [String] string formatted like androm dates
	 * 
	 */
	private static String formatDateForQuery(Date date)
	{

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String dayString = String.valueOf(day);
		String monthString = String.valueOf(month);
		if (day < 10)
		{
			dayString = "0" + dayString;
		}

		if (month < 10)
		{
			monthString = "0" + monthString;
		}

		return year + "-" + monthString + "-" + dayString;
	}

	/**
	 * Returns a list of all Logs sorted in descending order of date
	 * 
	 * @return
	 */
	public static List<Log> all()
	{

		return Log.objects().all().orderBy("-date").toList();
	}

	/**
	 * Builds up query for logs database
	 * 
	 * @return
	 */
	public static QuerySet<Log> objects()
	{

		return Log.objects(context(), Log.class);
	}

	/**
	 * Get application context
	 * 
	 * @return
	 */
	private static Context context()
	{

		return CalorieLogApplication.context();
	}
}
