package nhom7.uit.com.moviereview.model;

/**
 * Created by phuocthang on 10/19/2017.
 */

public class Actor {
    private String mName; // tên
    private String mRole; // vai trò
    private String mProfileImage; // ảnh đại diện
    private String mId; // id của diễn viên.


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmRole() {
        return mRole;
    }

    public void setmRole(String mRole) {
        this.mRole = mRole;
    }

    public String getmProfileImage() {
        return mProfileImage;
    }

    public void setmProfileImage(String mProfileImage) {
        this.mProfileImage = mProfileImage;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }
}
