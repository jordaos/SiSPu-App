package mobile.ufc.br.novosispu.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobile.ufc.br.novosispu.R;

public class NewDemandFragment extends Fragment {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_demand, container, false);
    }
}
