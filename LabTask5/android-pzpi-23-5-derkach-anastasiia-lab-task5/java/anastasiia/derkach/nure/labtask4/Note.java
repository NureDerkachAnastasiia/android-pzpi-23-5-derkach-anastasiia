package anastasiia.derkach.nure.labtask4;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Note implements Serializable {
    private final String title;
    private final long time;
    private final int iconResourceId;
    private final int priorityIconResourceId;
    private final String description;
    private final String imageUri;
    private final PriorityLevel priorityLevel;

    public enum PriorityLevel {
        LOW, MEDIUM, HIGH
    }

    public Note(String title, long time, int iconResourceId, int priorityIconResourceId,
                String description, String imageUri, PriorityLevel priorityLevel) {
        this.title = title;
        this.time = time;
        this.iconResourceId = iconResourceId;
        this.priorityIconResourceId = priorityIconResourceId;
        this.description = description;
        this.imageUri = imageUri;
        this.priorityLevel = priorityLevel;
    }

    public String getTitle() {
        return title;
    }

    public long getTime() {
        return time;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }
    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(time));
    }
}



