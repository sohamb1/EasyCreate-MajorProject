package app.easycreate;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Soham Bhattacharya on 28-03-2016.
 */
public class GridViewAdapter extends BaseAdapter {

    // Declare variables
    private Activity activity;
    private String[] filepath;
    private String[] filename;

    private static LayoutInflater inflater = null;

    public GridViewAdapter(Activity a, String[] fpath, String[] fname) {
        activity = a;
        filepath = fpath;
        filename = fname;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return filepath.length;

    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.gridview_item, null);
        // Locate the TextView in gridview_item.xml
        // Locate the ImageView in gridview_item.xml
        ImageView image = (ImageView) vi.findViewById(R.id.image);

        try {

            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            switch(metrics.densityDpi){
                case DisplayMetrics.DENSITY_LOW:
                    image.setLayoutParams(new RelativeLayout.LayoutParams(metrics.DENSITY_LOW, metrics.DENSITY_LOW));
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    image.setLayoutParams(new RelativeLayout.LayoutParams(metrics.DENSITY_MEDIUM, metrics.DENSITY_MEDIUM));
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    image.setLayoutParams(new RelativeLayout.LayoutParams(metrics.DENSITY_HIGH, metrics.DENSITY_HIGH));
                    break;
            }
//            Picasso.with(activity)
//                    .load(filepath[position])
//                    .centerCrop()
//                    .into(image);



        } catch (Exception e) {

        }

        // Set file name to the TextView followed by the position

        // Decode the filepath with BitmapFactory followed by the position
        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);

        // Set the decoded bitmap into ImageView
        image.setImageBitmap(bmp);
        return vi;
    }
}
