package app.easycreate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.GestureDetector;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawingView myView;
    protected View.OnTouchListener touchListener;
    protected GestureDetector gdt;

    protected ArrayList<Drawable> visibleDrawables = new ArrayList<Drawable>();
    protected Shape currentShape = new OvalShape();
    protected ShapeDrawable currentDrawable = new ShapeDrawable(currentShape);
    protected Path currentPath;

    static int defaultColor = Color.RED;
    static int defaultRadius = 10;
    static int defaultBackground = Color.WHITE;

    static enum ShapeType {
        BRUSH, ERASER, OVAL, RECTANGLE
    }

    protected int color = defaultColor;
    protected int brushRadius = defaultRadius;
    protected int eraseRadius = defaultRadius;
    protected int backgroundColor = defaultBackground;
    protected ShapeType currShape = ShapeType.BRUSH;

    protected String filePath;

    protected LinearLayout layout;

    protected boolean popupVisible = false;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        save = (Button) findViewById(R.id.save);
        layout = (LinearLayout) findViewById(R.id.linearlayout);
        myView = new DrawingView(this, MainActivity.this);

        // disable hardware acceleration
        myView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        layout.addView(myView);

        gdt = new GestureDetector(this, new CanvasGestureListener(
                MainActivity.this));

        touchListener = new CanvasTouchListener(MainActivity.this, gdt);

        myView.setOnTouchListener(touchListener);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitmap();
            }
        });

    }


    @Override
    protected void onNewIntent(Intent intent) {
        boolean clearScreen = intent.getBooleanExtra("clear", false);
        if (clearScreen == true) {
            visibleDrawables.clear();
            myView.invalidate();
        } else {
            color = intent.getIntExtra("color", color);
            backgroundColor = intent.getIntExtra("backgroundColor", backgroundColor);
            brushRadius = intent.getIntExtra("brushRadius", brushRadius);
            eraseRadius = intent.getIntExtra("eraseRadius", eraseRadius);
            currShape = ShapeType.valueOf(intent.getStringExtra("type"));
        }
    }

    protected void showPopup() {
        // fix this code
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("width", (int) (myView.canvasWidth * 0.9));
        intent.putExtra("height", (int) (myView.canvasHeight * 0.9));
        intent.putExtra("background", backgroundColor);
        intent.putExtra("color", color);
        intent.putExtra("brushRadius", brushRadius);
        intent.putExtra("eraseRadius", eraseRadius);
        intent.putExtra("shapeType", currShape.name());
        startActivity(intent);
    }

    protected void setCurrentShape() {
        switch (currShape) {
            case BRUSH:
                setPath(false);
                break;
            case ERASER:
                setPath(true);
                break;
            case OVAL:
                currentShape = new OvalShape();
                currentDrawable = new ShapeDrawable(currentShape);
                break;
            case RECTANGLE:
                currentShape = new RectShape();
                currentDrawable = new ShapeDrawable(currentShape);
                break;
        }
    }

    private void setPath(boolean erase) {
        currentPath = new Path();
        currentShape = new PathShape(currentPath, myView.canvasWidth,
                myView.canvasHeight);
        currentDrawable = new ShapeDrawable(currentShape);
        currentDrawable
                .setBounds(0, 0, myView.canvasWidth, myView.canvasHeight);

        Paint curr = currentDrawable.getPaint();
        curr.setColor(color);
        curr.setStyle(Paint.Style.STROKE);
        curr.setStrokeWidth(brushRadius);
        curr.setStrokeJoin(Paint.Join.ROUND);
        curr.setStrokeCap(Paint.Cap.ROUND);
        curr.setAntiAlias(true);
        curr.setDither(true);

        if (erase == true) {
            curr.setStrokeWidth(eraseRadius);
            curr.setColor(backgroundColor);
        }
    }

    private Boolean saveBitmap() {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Bitmap currentBitmap = myView.getBitmap();
        currentBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        // will create "test.jpg" in sdcard folder.
        File f = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                + File.separator + "test.jpg");

        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        // write the bytes in file
        FileOutputStream fo;
        try {
            fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        filePath = f.getAbsolutePath();
        return true;

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
