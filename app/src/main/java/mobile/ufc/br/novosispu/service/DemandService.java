package mobile.ufc.br.novosispu.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mobile.ufc.br.novosispu.entities.Demand;

public class DemandService {
    private FirebaseDatabase database;
    private DatabaseReference demandRef;

    public DemandService() {
        database = FirebaseDatabase.getInstance();
        demandRef = database.getReference("demands");
    }

    public void save(Demand demand) {
        String key = demand.getKey();
        if(key.equals("") || key == null) {
            key = demandRef.push().getKey();
            demand.setKey(key);
        }

        demandRef.child(key).setValue(demand);
    }

    public DatabaseReference getDemandRef() {
        return demandRef;
    }
}
