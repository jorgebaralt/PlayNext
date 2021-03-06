package com.phereapp.phere.phere_handling;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.phereapp.phere.MainActivityUser;
import com.phereapp.phere.R;
import com.phereapp.phere.api.ApiInterface;
import com.phereapp.phere.api.SpotifyWebApiClient;
import com.phereapp.phere.dialog_fragments.PlaylistDialogFragment;
import com.phereapp.phere.helper.SharedPreferencesHelper;
import com.phereapp.phere.pojo.Phere;
import com.phereapp.phere.pojo.PherePlaylist;
import com.phereapp.phere.pojo.SpotifyPlaylist;
import com.phereapp.phere.pojo.SpotifyPlaylistList;
import com.phereapp.phere.pojo.SpotifyPlaylistOwner;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewPhereActivity extends AppCompatActivity implements PlaylistDialogFragment.PlaylistFromDialogFragment {

    private EditText mPhereName;
    private EditText mPhereLocation;
    private RadioGroup mPrivacy;
    private RadioButton mPrivacyChosen;
    private String choosenPrivacy;
    private Button mCreatePhereButton;
    private String phereName, phereLocation, userFullName;
    private String pheresCollection = "pheres";
    private static String TAG = "CreateNewPhereActivity: ";
    private Button mCancelButton;
    private String host;
    private Array trying;
    private Phere newPhere;
    public static Activity mCreateNewPhereActivity;
    private Button mImportPlaylist;
    private SpotifyPlaylist selectedPlaylist;
    private SpotifyPlaylistOwner selectedSpotifyPlaylistOwner;
    private TextView selectedPlaylistText;

    Intent moreInfoIntent;

    //firebase
    private FirebaseFirestore db;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCreateNewPhereActivity = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_create_new_phere);

        mPhereName = (EditText) findViewById(R.id.editTxt_phereName_createPhere);
        mPhereLocation = (EditText) findViewById(R.id.editTxt_location_createPhere);
        mCreatePhereButton = (Button) findViewById(R.id.btn_ok_create_phere);
        mPrivacy = (RadioGroup) findViewById(R.id.radio_choose_createPhere);
        mCancelButton = (Button) findViewById(R.id.btn_cancel_create_phere);
        mImportPlaylist = findViewById(R.id.btn_importPlaylist_createPhere);
        selectedPlaylistText = findViewById(R.id.txt_selectedPlaylist_createPhere);

        final PlaylistDialogFragment playlistDialogFragment = new PlaylistDialogFragment();
        final FragmentManager fragmentManager = getFragmentManager();

        //firebase
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        host = currentUser.getDisplayName();

        //intent
        moreInfoIntent = new Intent(CreateNewPhereActivity.this,MoreInfoCreatePhereActivity.class);
        
        mImportPlaylist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String spotifyToken = SharedPreferencesHelper.getDefaults("authorization",CreateNewPhereActivity.this);
                Log.d(TAG, "onClick: Importing Playlist");
                ApiInterface spotifyInterface = SpotifyWebApiClient.getApiClient().create(ApiInterface.class);
                Call<SpotifyPlaylistList> call = spotifyInterface.getSpotifyPlaylists(spotifyToken);
                call.enqueue(new Callback<SpotifyPlaylistList>() {
                    @Override
                    public void onResponse(@NonNull Call<SpotifyPlaylistList> call, @NonNull Response<SpotifyPlaylistList> response) {
                        if(response.isSuccessful()){
                            SpotifyPlaylistList spotifyPlaylist = response.body();
                            List<SpotifyPlaylist> playlists = spotifyPlaylist.getSpotifyPlaylists();

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("spotifyPlaylists", (Serializable) playlists);
                            playlistDialogFragment.setArguments(bundle);
                            playlistDialogFragment.show(fragmentManager,"Playlist_tag");
                        }
                        else{
                            Log.d(TAG, "onResponse: Error getting response, check on token " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SpotifyPlaylistList> call, @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: Error connecting to api, getting playlists " + t.getMessage());
                    }
                });


            }
        });

        // On click of the OK button
        mCreatePhereButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting all the information of the Phere being created
                phereName = mPhereName.getText().toString().replaceAll("\\s","_");
                phereLocation = mPhereLocation.getText().toString().toLowerCase();
                int privacyId = mPrivacy.getCheckedRadioButtonId();

                if (phereName != null && phereLocation != null && privacyId != -1) {

                    //Get Privacy
                    mPrivacyChosen = (RadioButton) findViewById(privacyId);
                    choosenPrivacy = mPrivacyChosen.getText().toString().toLowerCase();

                    newPhere = new Phere(phereName, phereLocation, choosenPrivacy, host);


                    //pass newPhere info to next activity so we can put it into DB
                    moreInfoIntent.putExtra("NewPhere",newPhere);
                    startActivity(moreInfoIntent);



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


    @Override
    public void playlistFromDialogFragment(SpotifyPlaylist spotifyPlaylist, SpotifyPlaylistOwner spotifyPlaylistOwner) {
        selectedPlaylist = spotifyPlaylist;
        selectedPlaylistText.setText(spotifyPlaylist.getName());
        selectedSpotifyPlaylistOwner = spotifyPlaylistOwner;
        PherePlaylist pherePlaylist = new PherePlaylist(selectedPlaylist.getPlaylistId(),selectedSpotifyPlaylistOwner.getId(),selectedPlaylist.getName());
        moreInfoIntent.putExtra("pherePlaylist", pherePlaylist);
        Log.d(TAG, "playlistFromDialogFragment: playlist id  = " + selectedPlaylist.getPlaylistId() + "and owner id = " + selectedSpotifyPlaylistOwner.getId());

    }
}




