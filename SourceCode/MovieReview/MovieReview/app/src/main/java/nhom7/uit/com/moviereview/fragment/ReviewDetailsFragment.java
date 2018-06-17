package nhom7.uit.com.moviereview.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import nhom7.uit.com.moviereview.R;
import nhom7.uit.com.moviereview.adapter.CommentListAdapter;
import nhom7.uit.com.moviereview.controller.CommentActivity;
import nhom7.uit.com.moviereview.controller.DetailsMovieActivity;
import nhom7.uit.com.moviereview.controller.LoginActivity;
import nhom7.uit.com.moviereview.model.Comment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewDetailsFragment extends Fragment {
    private static final String LINK_DETAIL_EXTRAS = "id";
    private static final String MOVIE_CODE = "MOVIECODE";
    private static final int REQUEST_CODE = 2;
    private RecyclerView commentRecycler;
    private FirebaseDatabase mDatabase;
    private DatabaseReference dataComment;
    private String movieCode;
    private ArrayList<Comment> commentList;
    private CommentListAdapter commentListAdapter;
    private TextView pointTxt;
    private TextView countTxt;
    private TextView warningTxt;
    private TextView star1;
    private TextView star2;
    private TextView star3;
    private TextView star4;
    private TextView star5;
    private View reviewScrim;
    private CircularProgressView circularProgressView;
    private RatingBar mRatingbar;
    FloatingActionButton commentFab;
    AlertDialog alertDialog;
    public static ReviewDetailsFragment newInstance(Bundle b) {
        ReviewDetailsFragment fragment = new ReviewDetailsFragment();
        fragment.setArguments(b);
        return fragment;
    }

    public ReviewDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Typeface type = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Light.ttf");
        View rootView = inflater.inflate(R.layout.fragment_review_details, container, false);
        Bundle b = getArguments();
        movieCode = b.getString(LINK_DETAIL_EXTRAS);
        commentRecycler = (RecyclerView) rootView.findViewById(R.id.comment_recycler_view);
        pointTxt = (TextView) rootView.findViewById(R.id.point_medium_txt);
        countTxt = (TextView) rootView.findViewById(R.id.count_people_txt);
        reviewScrim = rootView.findViewById(R.id.review_scrim);
        circularProgressView = (CircularProgressView) rootView.findViewById(R.id.progress_review_view);
        mRatingbar = (RatingBar) rootView.findViewById(R.id.review_rating_bar);
        warningTxt = (TextView) rootView.findViewById(R.id.warning_txt);
        star1 = (TextView) rootView.findViewById(R.id.star_1_text_view);
        star2 = (TextView) rootView.findViewById(R.id.star_2_text_view);
        star3 = (TextView) rootView.findViewById(R.id.star_3_text_view);
        star4 = (TextView) rootView.findViewById(R.id.star_4_text_view);
        star5 = (TextView) rootView.findViewById(R.id.star_5_text_view);
        commentFab = (FloatingActionButton) rootView.findViewById(R.id.comment_fab);
        warningTxt.setTypeface(type);
        commentRecycler.setNestedScrollingEnabled(false);
        commentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    Intent intent = new Intent(getActivity(), CommentActivity.class);
                    intent.putExtra(MOVIE_CODE, movieCode);
                    startActivity(intent);
                } else {
                    createAlertDialog();
                    alertDialog.show();
                }

            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        super.onActivityCreated(savedInstanceState);
        commentList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance();
        dataComment = mDatabase.getReference("comments");
        getCommentFromFirebase(movieCode);
        DatabaseReference mRef1 = mDatabase.getReference("comments").child(movieCode);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("count") && dataSnapshot.hasChild("score")) {
                    if(!dataSnapshot.child("score").getValue().toString().equals("0")){
                        pointTxt.setText(dataSnapshot.child("score").getValue().toString().substring(0,3));
                        mRatingbar.setRating(Float.parseFloat(dataSnapshot.child("score").getValue().toString().substring(0,3)));
                    }else {
                        Log.d("COMMENT", "co chay");
                        pointTxt.setText("0");
                    }

                    countTxt.setText(dataSnapshot.child("count").getValue().toString());
                }else {
                    pointTxt.setText("0");
                    countTxt.setText("0");
                    reviewScrim.setVisibility(View.GONE);
                    circularProgressView.setVisibility(View.GONE);
                }
                // add star diagram
                if(dataSnapshot.hasChild("star1")){
                    GridLayout.LayoutParams params = (GridLayout.LayoutParams) star1.getLayoutParams();
                    params.width = Math.round(5*scale*Integer.parseInt((String)dataSnapshot.child("star1").getValue()) + 10*scale + 0.5f);
                    star1.setLayoutParams(params);
                }
                if(dataSnapshot.hasChild("star2")){
                    GridLayout.LayoutParams params = (GridLayout.LayoutParams) star2.getLayoutParams();
                    params.width = Math.round(5*scale*Integer.parseInt((String)dataSnapshot.child("star2").getValue()) + 10*scale + 0.5f);
                    star2.setLayoutParams(params);
                }
                if(dataSnapshot.hasChild("star3")){
                    GridLayout.LayoutParams params = (GridLayout.LayoutParams) star3.getLayoutParams();
                    params.width = Math.round(5*scale*Integer.parseInt((String)dataSnapshot.child("star3").getValue()) + 10*scale + 0.5f);
                    star3.setLayoutParams(params);
                }
                if(dataSnapshot.hasChild("star4")){
                    GridLayout.LayoutParams params = (GridLayout.LayoutParams) star4.getLayoutParams();
                    params.width = Math.round(5*scale*Integer.parseInt((String)dataSnapshot.child("star4").getValue()) + 10*scale + 0.5f);
                    star4.setLayoutParams(params);
                }
                if(dataSnapshot.hasChild("star5")){
                    GridLayout.LayoutParams params = (GridLayout.LayoutParams) star5.getLayoutParams();
                    params.width = Math.round(5*scale*Integer.parseInt((String)dataSnapshot.child("star5").getValue()) + 10*scale + 0.5f);
                    star5.setLayoutParams(params);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef1.addValueEventListener(valueEventListener);

    }

    public void getCommentFromFirebase(String code){
        DatabaseReference mRef = dataComment.child(code).child("list");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comment comment = new Comment();
                comment.setUserName((String)dataSnapshot.child("name").getValue());
                comment.setComment((String)dataSnapshot.child("comment").getValue());
                comment.setPoint((String)dataSnapshot.child("point").getValue());
                comment.setUrlAvatar((String)dataSnapshot.child("avatar").getValue());
                comment.setDate((String)dataSnapshot.child("date").getValue());
                Log.i("COMMENT", comment.getUserName() + " " +
                comment.getUrlAvatar() + " " + comment.getComment() +
                " " + comment.getPoint());
                commentList.add(0, comment);
                commentListAdapter = new CommentListAdapter(commentList, getContext());
                commentRecycler.setAdapter(commentListAdapter);
                RecyclerView.LayoutManager glm = new GridLayoutManager(getContext(), 1);
                commentRecycler.setLayoutManager(glm);
                reviewScrim.setVisibility(View.GONE);
                warningTxt.setVisibility(View.GONE);
                circularProgressView.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.addChildEventListener(childEventListener);
    }
    public void createAlertDialog(){
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage("Bạn cần đăng nhập để tiếp tục bình luận");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Đồng Ý",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
    }
}
