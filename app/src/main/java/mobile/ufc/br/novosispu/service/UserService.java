package mobile.ufc.br.novosispu.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mobile.ufc.br.novosispu.entities.Demand;
import mobile.ufc.br.novosispu.entities.User;

import static mobile.ufc.br.novosispu.Constants.FIREBASE_CHILD_USERS;

public class UserService {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    public UserService() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference(FIREBASE_CHILD_USERS);
    }

    public String getCurrentUserKey() {
        FirebaseUser user = mAuth.getCurrentUser();
        return user.getUid();
    }

    public void save(User user) {
        String key = user.getKey();
        if(key == null || key.equals("")) {
            key = usersRef.push().getKey();
            user.setKey(key);
        }

        usersRef.child(key).setValue(user);
    }

    public DatabaseReference getUsersRef() {
        return usersRef;
    }

}
