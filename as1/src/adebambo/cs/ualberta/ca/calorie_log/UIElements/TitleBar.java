package adebambo.cs.ualberta.ca.calorie_log.UIElements;


import adebambo.cs.ualberta.ca.calorie_log.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//Layout class for main Activity title bar
public class TitleBar extends RelativeLayout{

    public TitleBar(Context context, AttributeSet attrs) {
    	super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.title_bar, this);

    }   

    public void setTitle(String title) {
    	TextView titleView = (TextView) findViewById(R.id.title_string);
    	titleView.setText(title);
    }
    
    public void setTitleResource(int resource){
    	ImageView titleimage = (ImageView) findViewById(R.id.title_image);
        titleimage.setImageResource(resource);
    }   

}