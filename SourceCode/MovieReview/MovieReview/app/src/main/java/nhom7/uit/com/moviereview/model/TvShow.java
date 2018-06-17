package nhom7.uit.com.moviereview.model;

/**
 * Created by phuocthang on 10/18/2017.
 */

public class TvShow {

    private int mID;
    private String tvID;
    private String nameShow;
    private String yearOnAir;
    private String posterPath;
    private String backgroundPath;




    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    private int imageDrawable;

    public TvShow() {
        // please do something
    }

    public TvShow(String nameShow, String yearOnAir, int imageDrawable) {
        this.nameShow = nameShow;
        this.yearOnAir = yearOnAir;
        this.imageDrawable = imageDrawable;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public String getTvID() {
        return tvID;
    }

    public void setTvID(String tvID) {
        this.tvID = tvID;
    }

    public String getNameShow() {
        return nameShow;
    }

    public void setNameShow(String nameShow) {
        this.nameShow = nameShow;
    }

    public String getYearOnAir() {
        return yearOnAir;
    }

    public void setYearOnAir(String yearOnAir) {
        this.yearOnAir = yearOnAir;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

}
