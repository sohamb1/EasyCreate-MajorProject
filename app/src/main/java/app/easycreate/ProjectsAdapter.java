package app.easycreate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;


public class ProjectsAdapter extends BaseAdapter {
    FragmentActivity context;
    ProjectSetter ts;
    private List<ProjectSetter> ProjectSet;
    Typeface tf;
    int id;
    private static LayoutInflater inflater = null;

    public ProjectsAdapter(FragmentActivity activity, List<ProjectSetter> tss) {
        this.context = activity;
        this.ProjectSet = tss;
    }

    @Override
    public int getCount() {

        return ProjectSet.size();
    }

    @Override
    public Object getItem(int location) {
        return ProjectSet.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public class Holder {
        TextView name;
        TextView description;
        TextView width;
        TextView height;
        TextView name_txt;
        TextView description_txt;
        TextView width_txt;
        TextView height_txt;
        TextView id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.project_list_item, null);
        AssetManager am = context.getApplicationContext().getAssets();
        tf = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "CaviarDreams.ttf"));
        holder.name = (TextView) convertView.findViewById(R.id.project_name);
        holder.name_txt = (TextView) convertView.findViewById(R.id.project_name_txt);
        holder.description = (TextView) convertView.findViewById(R.id.description);
        holder.description_txt = (TextView) convertView.findViewById(R.id.description_text);
        holder.width = (TextView) convertView.findViewById(R.id.width);
        holder.width_txt = (TextView) convertView.findViewById(R.id.width_text);
        holder.id = (TextView) convertView.findViewById(R.id.id);
        holder.height = (TextView) convertView.findViewById(R.id.height);
        holder.height_txt = (TextView) convertView.findViewById(R.id.height_text);


        holder.name.setTypeface(tf);
        holder.name_txt.setTypeface(tf);
        holder.description.setTypeface(tf);
        holder.description_txt.setTypeface(tf);
        holder.width.setTypeface(tf);
        holder.width_txt.setTypeface(tf);
        holder.height.setTypeface(tf);
        holder.height_txt.setTypeface(tf);
        ts = ProjectSet.get(position);
        holder.name.setText(ts.getName());
        holder.description.setText(ts.getDescription());
        holder.id.setText(ts.getID() + "");
        holder.width.setText(ts.getWidth());
        holder.height.setText(ts.getHeight());

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i=new Intent("app.easycreate.ProjectImages");
                context.startActivity(i);
            }
//                });
//            }
        });
        return convertView;
    }

}//endGetView
