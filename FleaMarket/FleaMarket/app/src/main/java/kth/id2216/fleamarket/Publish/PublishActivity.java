package kth.id2216.fleamarket.Publish;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import kth.id2216.fleamarket.Home.HomeActivity;
import kth.id2216.fleamarket.R;
import kth.id2216.fleamarket.Utils.BottomNavigationViewHelper;

/**
 * Created by Xiao on 15/02/18.
 */

public class PublishActivity extends AppCompatActivity {
    private static final String TAG = "PublishActivity";
    private static final int ACTIVITY_NUM = 2;

    private ImageButton mSelectImage;
    private EditText mTitle;
    private EditText mDesc;
    private EditText mPrice;
    private Button mConfirmBtn;
    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    private Context mContext = PublishActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        Log.d(TAG, "onCreate: started.");

        setupBottomNavigationView();

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Item");

        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);
        mTitle = (EditText) findViewById(R.id.titleField);
        mDesc = (EditText) findViewById(R.id.descField);
        mPrice = (EditText) findViewById(R.id.priceField);
        mConfirmBtn = (Button) findViewById(R.id.confirmBtn);
        mProgress = new ProgressDialog(this);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPublish();
            }
        });
    }

    private void startPublish() {

        mProgress.setMessage("Publishing your item...");
        mProgress.show();

        final String title_val = mTitle.getText().toString().trim();
        final String desc_val = mDesc.getText().toString().trim();
        final String price_val = mPrice.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) &&
                !TextUtils.isEmpty(price_val) && mImageUri != null){

            StorageReference filepath = mStorage.child("Item_images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newPublishItem = mDatabase.push();
                    newPublishItem.child("title").setValue(title_val);
                    newPublishItem.child("desc").setValue(desc_val);
                    newPublishItem.child("price").setValue(price_val);
                    newPublishItem.child("image").setValue(downloadUrl.toString());

                    mProgress.dismiss();

                    finish();
                    startActivity(new Intent(PublishActivity.this, HomeActivity.class));
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

       if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
           mImageUri = data.getData();
           mSelectImage.setImageURI(mImageUri);
       }

    }


    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
