package nhom7.uit.com.moviereview.model;

/**
 * Created by phuocthang on 10/15/2017.
 */

public class MovieData {
    private String mID;
    private String mTitle;       // tiêu đề phim
    private String mVote;        // điểm IMDb
    private String mDescription; // mô tả phim
    private String mPosterPath;  // hình poster
    private String mPosterPanel; // hình baner
    private String mLinkDetail;  // đường dẫn chi tiết
    private String mDate;        // ngày công chiếu
    private String mTimeRemain;  // thời lượng phim
    private String mTrailer;     // mã trailer youtube

    public MovieData() {
        //do nothing
    }

    public String getmVote() {
        return mVote;
    }

    public void setmVote(String mVote) {
        this.mVote = mVote;
    }

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }


    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getPosterPanel() {
        return mPosterPanel;
    }

    public void setPosterPanel(String mPosterPanel) {
        this.mPosterPanel = mPosterPanel;
    }

    public String getLinkDetail() {
        return mLinkDetail;
    }

    public void setLinkDetail(String mLinkDetail) {
        this.mLinkDetail = mLinkDetail;
    }

    public String getTimeRemain() {
        return mTimeRemain;
    }

    public void setTimeRemain(String mTimeRemain) {
        this.mTimeRemain = mTimeRemain;
    }

    public String getTrailer() {
        return mTrailer;
    }

    public void setTrailer(String mTrailer) {
        this.mTrailer = mTrailer;
    }
}
