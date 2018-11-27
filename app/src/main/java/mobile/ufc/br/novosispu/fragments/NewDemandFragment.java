package mobile.ufc.br.novosispu.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import mobile.ufc.br.novosispu.R;
import mobile.ufc.br.novosispu.entities.Demand;
import mobile.ufc.br.novosispu.service.DemandService;
import mobile.ufc.br.novosispu.service.UserService;

public class NewDemandFragment extends Fragment {

    private Button newDemandButton;
    private EditText descriptionNewDemandEditText;
    private EditText titleNewDemandEditText;
    private DemandService demandService;
    private UserService userService;

    public NewDemandFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewDemandFragment newInstance(String param1, String param2) {
        NewDemandFragment fragment = new NewDemandFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demandService = new DemandService();
        userService = new UserService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_demand, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        newDemandButton = (Button) getView().findViewById(R.id.newDemandButton);
        titleNewDemandEditText = (EditText) getView().findViewById(R.id.titleNewDemandEditText);
        descriptionNewDemandEditText = (EditText) getView().findViewById(R.id.descriptionNewDemandEditText);

        newDemandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Demand demand = new Demand();
                demand.setTitle(titleNewDemandEditText.getText().toString());
                demand.setDescription(descriptionNewDemandEditText.getText().toString());
                demand.setTime(new Date().getTime());
                demand.setUserKey(userService.getCurrentUserKey());

                demandService.save(demand);

                HomeFragment homeFragment = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, homeFragment);
                fragmentTransaction.commit();
            }
        });
    }
}
