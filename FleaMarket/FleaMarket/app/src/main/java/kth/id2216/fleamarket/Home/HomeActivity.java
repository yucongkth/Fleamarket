package kth.id2216.fleamarket.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import kth.id2216.fleamarket.R;
import kth.id2216.fleamarket.Utils.BottomNavigationViewHelper;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;

    private Context mContext = HomeActivity.this;

    private RecyclerView mItemList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "OnCreate: starting.");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Item");
        mDatabase.keepSynced(true);

        mItemList = (RecyclerView) findViewById(R.id.item_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mItemList.setHasFixedSize(true);
        mItemList.setLayoutManager(new LinearLayoutManager(this));

        setupBottomNavigationView();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Item, ItemViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(
                Item.class,
                R.layout.item_row,
                ItemViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, Item model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(),model.getImage());
            }
        };


        mItemList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title) {
            TextView itemTitle = (TextView) mView.findViewById(R.id.itemTitle);
            itemTitle.setText(title);
        }

        public void setPrice(String price) {
            TextView itemPrice = (TextView) mView.findViewById(R.id.itemPrice);
            itemPrice.setText(price);
        }

        public void setImage(Context ctx, String image){
            ImageButton itemImage = (ImageButton) mView.findViewById(R.id.itemImage);
            Picasso.with(ctx).load(image).into(itemImage);
        }
    }


    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
