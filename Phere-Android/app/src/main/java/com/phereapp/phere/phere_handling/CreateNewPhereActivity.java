package com.phereapp.phere.phere_handling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.phereapp.phere.MainActivityUser;
import com.phereapp.phere.Phere;
import com.phereapp.phere.R;

public class CreateNewPhereActivity extends AppCompatActivity {

    private EditText mPhereName;
    private EditText mPhereLocation;
    private RadioGroup mPrivacy;
    private RadioButton mPrivacyChosen;
    private String choosenPrivacy;
    private Button mCreatePhereButton;
    private String phereName, phereLocation;
    private String pheresCollection = "pheres";
    private static String TAG = "CreateNewPhereActivity: ";
    private Button mCancelButton;
    private String host;
    //firebase
    private FirebaseFirestore db;
    private FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_create_new_phere);

        mPhereName = (EditText) findViewById(R.id.editTxt_phereName_createPhere);
        mPhereLocation = (EditText) findViewById(R.id.editTxt_location_createPhere);
        mCreatePhereButton = (Button) findViewById(R.id.btn_ok_create_phere);
        mPrivacy = (RadioGroup) findViewById(R.id.radio_choose_createPhere);
        mCancelButton = (Button) findViewById(R.id.btn_cancel_create_phere);

        //firebase
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        host = currentUser.getEmail();


        // On click of the OK button
        mCreatePhereButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting all the information of the Phere being created
                phereName = mPhereName.getText().toString();
                phereLocation = mPhereLocation.getText().toString();
                int selectedId = mPrivacy.getCheckedRadioButtonId();

                if (phereName != null && phereLocation != null && selectedId != -1) {

                    //Get Privacy
                    mPrivacyChosen = (RadioButton) findViewById(selectedId);
                    choosenPrivacy = mPrivacyChosen.getText().toString();

                    //TODO: Import or create Playlist

                    //Add Phere into database
                    addUserReference();
                    Toast.makeText(CreateNewPhereActivity.this, "Your Phere has been created", Toast.LENGTH_SHORT).show();

                    //TODO: next screen depends on import or create playlist.

                }
                else{
                    Toast.makeText(CreateNewPhereActivity.this, "Please fill all values", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent =  new Intent(CreateNewPhereActivity.this,MainActivityUser.class);
                startActivity(backIntent);
                finish();
            }
        });

    }

    public void addUserReference(){
        // create new Phere object to send to database
        Phere newPhere = new Phere(phereName, phereLocation, choosenPrivacy,host);
        // adds the extra information to the document in the database
        db.collection(pheresCollection).document(phereName).set(newPhere);
    }

}



