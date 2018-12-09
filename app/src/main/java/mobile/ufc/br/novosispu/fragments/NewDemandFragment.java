package mobile.ufc.br.novosispu.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import mobile.ufc.br.novosispu.MainActivity;
import mobile.ufc.br.novosispu.R;
import mobile.ufc.br.novosispu.entities.Demand;
import mobile.ufc.br.novosispu.entities.User;
import mobile.ufc.br.novosispu.service.DemandService;
import mobile.ufc.br.novosispu.service.UserService;

import static mobile.ufc.br.novosispu.Constants.DEMAND_KEY_ID;
import static mobile.ufc.br.novosispu.Constants.FRAGMENT_HOME_ID;
import static mobile.ufc.br.novosispu.Constants.TAG;

public class NewDemandFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 111;

    private Button newDemandButton;
    private Button capturePhotoButton;
    private EditText descriptionNewDemandEditText;
    private EditText titleNewDemandEditText;
    private DemandService demandService;
    private UserService userService;

    private ImageView mImageLabel;
    private Location location;

    private Bitmap imageBitmap;

    private FusedLocationProviderClient mFusedLocationClient;

    Demand demand;

    public NewDemandFragment() {
        demand = null;
    }

    public static NewDemandFragment newInstance() {
        NewDemandFragment fragment = new NewDemandFragment();
        return fragment;
    }

    public static NewDemandFragment newInstance(String demandKey) {
        NewDemandFragment fragment = new NewDemandFragment();
        Bundle args = new Bundle();
        args.putString(DEMAND_KEY_ID, demandKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demandService = new DemandService();
        userService = new UserService();

        initLocationListner();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_demand, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        newDemandButton = (Button) getView().findViewById(R.id.newDemandButton);
        capturePhotoButton = (Button) getView().findViewById(R.id.capturePhotoButton);
        titleNewDemandEditText = (EditText) getView().findViewById(R.id.titleNewDemandEditText);
        descriptionNewDemandEditText = (EditText) getView().findViewById(R.id.descriptionNewDemandEditText);
        mImageLabel = (ImageView) getView().findViewById(R.id.imageFromCamera);

        newDemandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference userRef = userService.getUsersRef();
                userRef.child(userService.getCurrentUserKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if(demand == null) {
                            demand = new Demand();
                            demand.setTime(new Date().getTime());
                            demand.setUser(user);
                            demand.setLat(location.getLatitude());
                            demand.setLng(location.getLongitude());
                        }
                        demand.setTitle(titleNewDemandEditText.getText().toString());
                        demand.setDescription(descriptionNewDemandEditText.getText().toString());

                        // Image
                        if(imageBitmap != null) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

                            demand.setImageUrl(imageEncoded);
                        }

                        demandService.save(demand);
                        clearForm();

                        ((MainActivity)getActivity()).changeFragmentTo(FRAGMENT_HOME_ID);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        capturePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLaunchCamera();
            }
        });

        Bundle args = getArguments();
        if(args != null) {
            String demandKey = args.getString(DEMAND_KEY_ID, null);
            if (demandKey != null) {
                loadDemand(demandKey);
            }
        }
    }

    private void clearForm() {
        descriptionNewDemandEditText.setText("");
        titleNewDemandEditText.setText("");
        mImageLabel.setImageResource(0);
        imageBitmap = null;
        demand = null;
    }

    public void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            mImageLabel.setImageBitmap(imageBitmap);
            mImageLabel.getLayoutParams().height = 500;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initLocationListner() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location loc) {
                        // Got last known location. In some rare situations this can be null.
                        if (loc != null) {
                            location = loc;
                        }
                    }
                });
    }

    public void loadDemand(String demandKey) {
        demandService.getDemandRef().child(demandKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                demand = dataSnapshot.getValue(Demand.class);
                Log.d(TAG, demand.toString());
                titleNewDemandEditText.setText(demand.getTitle());
                descriptionNewDemandEditText.setText(demand.getDescription());
                if (demand.getImageUrl() != null) {
                    try {
                        Bitmap imageBitmap = decodeFromFirebaseBase64(demand.getImageUrl());
                        mImageLabel.setImageBitmap(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
}
