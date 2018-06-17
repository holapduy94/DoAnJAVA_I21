package nhom7.uit.com.moviereview.model;

import nhom7.uit.com.moviereview.utils.Constants;

/**
 * Created by phuocthang on 10/27/2017.
 */

public class MovieVideo {
    private String id, key, name,size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImageURL() {
        return Constants.YOUTUBE_IMG_URL + getKey() + "/0.jpg";
    }
}
