package mobile.ufc.br.novosispu.service;

import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserService {
    private FirebaseAuth mAuth;

    public UserService() {
        mAuth = FirebaseAuth.getInstance();
    }

    public String getCurrentUserKey() {
        FirebaseUser user = mAuth.getCurrentUser();
        return user.getUid();
    }
}
