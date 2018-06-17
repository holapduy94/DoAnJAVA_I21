package nhom7.uit.com.moviereview.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.utils.FormatStringUrl;

public class CommentActivity extends AppCompatActivity {
    private static final String MOVIE_CODE = "MOVIECODE";
    private MaterialRatingBar materialRatingBar;
    private TextView ratingTxt;
    private EditText commentEdt;
    private ActionBar actionComment;
    private Toolbar commentToolbar;
    private CircularImageView avatarImg;
    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private String movieCode;
    private String photoUrl;
    private String pointRating = "0";
    String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getReferenceFromXML();
        getInformationIntent();
        mDatabase = FirebaseDatabase.getInstance();
        materialRatingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                pointRating = Float.toString(rating);
                if (rating > 0) {
                    ratingTxt.setText("Ghét");
                    if (rating > 1) {
                        ratingTxt.setText("Không thích");
                        if (rating > 2) {
                            ratingTxt.setText("OK");
                            if (rating > 3) {
                                ratingTxt.setText("Thích");
                                if (rating > 4) {
                                    ratingTxt.setText("Rất Thích");
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void getReferenceFromXML() {
        materialRatingBar = (MaterialRatingBar) findViewById(R.id.material_rating_bar);
        ratingTxt = (TextView) findViewById(R.id.rating_txt);
        commentEdt = (EditText) findViewById(R.id.comment_edt);
        commentToolbar = (Toolbar) findViewById(R.id.comment_toolbar);
        Typeface type = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoCondensed-Light.ttf");
        commentEdt.setTypeface(type);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(commentEdt, InputMethodManager.HIDE_NOT_ALWAYS);
        setSupportActionBar(commentToolbar);
        actionComment = getSupportActionBar();
        actionComment.setDisplayHomeAsUpEnabled(true);
        actionComment.setDisplayShowTitleEnabled(false);
        avatarImg = (CircularImageView) findViewById(R.id.comment_facbook_img);
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    photoUrl = user.getPhotoUrl().toString();
                    Picasso.with(getApplicationContext()).load(photoUrl).into(avatarImg);
                    Log.i("COMMENT", "link avatar:" + photoUrl);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getInformationIntent() {
        Intent intent = getIntent();
        movieCode = intent.getStringExtra(MOVIE_CODE);
        Log.i("COMMENT", "Mã phim:" + movieCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.comment_done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.comment_done:
                commentDoneHandle(movieCode);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void commentDoneHandle(String code) {
        if (user != null) {
            final DatabaseReference mRef = mDatabase.getReference("comments").child(code).child("list").push();
            final DatabaseReference mRef1 = mDatabase.getReference("comments").child(code).child("count");
            final DatabaseReference mRef2 = mDatabase.getReference("comments").child(code).child("score");
            final DatabaseReference mRef3 = mDatabase.getReference("comments").child(code).child("star1");
            final DatabaseReference mRef4 = mDatabase.getReference("comments").child(code).child("star2");
            final DatabaseReference mRef5 = mDatabase.getReference("comments").child(code).child("star3");
            final DatabaseReference mRef6 = mDatabase.getReference("comments").child(code).child("star4");
            final DatabaseReference mRef7 = mDatabase.getReference("comments").child(code).child("star5");
            Map<String, String> map = new HashMap<String, String>();
            map.put("comment", commentEdt.getText().toString());
            map.put("avatar", photoUrl);
            map.put("name", user.getDisplayName());
            map.put("point", pointRating);
            map.put("date", FormatStringUrl.getCurrentDateV2());
            mRef.setValue(map);
            mRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count = (String) dataSnapshot.getValue();
                    if (count == null) {
                        count = "0";
                        count = Integer.toString(Integer.parseInt(count) + 1);
                        mRef1.setValue(count);
                    } else {
                        count = Integer.toString(Integer.parseInt(count) + 1);
                        mRef1.setValue(count);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String score = (String) dataSnapshot.getValue();
                    if (score == null) {
                        score = pointRating;
                        mRef2.setValue(score);
                    } else {
                        Log.i("COMMENT", "count: " + count);
                        score = Float.toString((Float.parseFloat(score) + Float.parseFloat(pointRating)) / 2);
                        mRef2.setValue(score);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            int score = Math.round(Float.parseFloat(pointRating));
            switch (score) {
                case 1:
                    mRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String sum = (String) dataSnapshot.getValue();
                            if (sum == null) {
                                sum = "0";
                                sum = Integer.toString(Integer.parseInt(sum) + 1);
                                mRef3.setValue(sum);
                            } else {
                                sum = Integer.toString(Integer.parseInt(sum) + 1);
                                mRef3.setValue(sum);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;
                case 2:
                    mRef4.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String sum = (String) dataSnapshot.getValue();
                            if (sum == null) {
                                sum = "0";
                                sum = Integer.toString(Integer.parseInt(sum) + 1);
                                mRef4.setValue(sum);
                            } else {
                                sum = Integer.toString(Integer.parseInt(sum) + 1);
                                mRef4.setValue(sum);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;
                case 3:
                    mRef5.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String sum = (String) dataSnapshot.getValue();
                            if (sum == null) {
                                sum = "0";
                                sum = Integer.toString(Integer.parseInt(sum) + 1);
                                mRef5.setValue(sum);
                            } else {
                                sum = Integer.toString(Integer.parseInt(sum) + 1);
                                mRef5.setValue(sum);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;
                case 4:
                    mRef6.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String sum = (String) dataSnapshot.getValue();
                            if (sum == null) {
                                sum = "0";
                                sum = Integer.toString(Integer.parseInt(sum) + 1);
                                mRef6.setValue(sum);
                            } else {
                                sum = Integer.toString(Integer.parseInt(sum) + 1);
                                mRef6.setValue(sum);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;
                case 5:
                    mRef7.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String sum = (String) dataSnapshot.getValue();
                            if (sum == null) {
                                sum = "0";
                                sum = Integer.toString(Integer.parseInt(sum) + 1);
                                mRef7.setValue(sum);
                            } else {
                                sum = Integer.toString(Integer.parseInt(sum) + 1);
                                mRef7.setValue(sum);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;
            }
            finish();
        } else {
            Toast.makeText(this, "wait...", Toast.LENGTH_SHORT).show();
        }
    }
}
