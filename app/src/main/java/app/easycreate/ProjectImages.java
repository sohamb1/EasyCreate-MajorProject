package app.easycreate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Soham Bhattacharya on 28-03-2016.
 */
public class ProjectImages extends AppCompatActivity{
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    GridView grid;
    GridViewAdapter adapter;
    File file;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_images);

        // Check for SD Card
            // Locate the image folder in your SD Card
            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "Example 1");

        if (file.isDirectory()) {
            listFile = file.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }
        }

        // Locate the GridView in gridview_main.xml
        grid = (GridView) findViewById(R.id.gridview);
        // Pass String arrays to LazyAdapter Class
        adapter = new GridViewAdapter(this, FilePathStrings, FileNameStrings);
        // Set the LazyAdapter to the GridView
        grid.setAdapter(adapter);

        // Capture gridview item click
//        grid.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                Intent i = new Intent(MainActivity.this, ViewImage.class);
//                // Pass String arrays FilePathStrings
//                i.putExtra("filepath", FilePathStrings);
//                // Pass String arrays FileNameStrings
//                i.putExtra("filename", FileNameStrings);
//                // Pass click position
//                i.putExtra("position", position);
//                startActivity(i);
//            }
//
//        });
    }
}
