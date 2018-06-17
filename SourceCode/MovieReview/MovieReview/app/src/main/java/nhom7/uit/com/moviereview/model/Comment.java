package nhom7.uit.com.moviereview.model;

/**
 * Created by PATRICKLAGGER on 5/28/2017.
 */

public class Comment {
    private String userName;
    private String urlAvatar;
    private String mComment;
    private String mPoint;
    private String mDate;

    public Comment() {
        // do nothing;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }

    public String getPoint() {
        return mPoint;
    }

    public void setPoint(String mPoint) {
        this.mPoint = mPoint;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }
}
