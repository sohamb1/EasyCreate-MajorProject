package app.easycreate;

/**
 * Created by Soham Bhattacharya on 14-02-2016.
 */
public class ProjectSetter {
    //private variables
    int _id;
    String name;
    String description;
    String width;
    String height;

    // Empty constructor
    public ProjectSetter() {

    }

    // constructor
    public ProjectSetter(int id, String targedialogues,
                         String startdate,
                         String enddate,
                         String completeddialogues) {
        this._id = id;
        this.name = targedialogues;
        this.description = startdate;
        this.width = enddate;
        this.height = completeddialogues;
    }

    public ProjectSetter(String targedialogues,
                         String startdate,
                         String enddate,
                         String completeddialogues) {
        this.name = targedialogues;
        this.description = startdate;
        this.width = enddate;
        this.height = completeddialogues;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting name
    public String getName() {
        return this.name;
    }

    // setting name
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }


    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

}
