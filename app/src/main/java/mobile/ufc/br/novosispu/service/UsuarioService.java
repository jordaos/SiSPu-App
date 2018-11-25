package mobile.ufc.br.novosispu.service;

import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;

public class UsuarioService {
    private FirebaseAuth mAuth;
    private Context context;

    public UsuarioService() {
        mAuth = FirebaseAuth.getInstance();
    }
}
