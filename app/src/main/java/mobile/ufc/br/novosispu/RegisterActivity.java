package mobile.ufc.br.novosispu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mobile.ufc.br.novosispu.entities.User;
import mobile.ufc.br.novosispu.service.UserService;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Button salvarButton;
    private Button goToLoginButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;

    private FirebaseAuth mAuth;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userService = new UserService();
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        salvarButton = (Button) findViewById(R.id.cadastrarButton);
        goToLoginButton = (Button) findViewById(R.id.goToLoginButton);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);

        salvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userEmail = emailEditText.getText().toString();
                final String userPass = passwordEditText.getText().toString();
                final String userName = nameEditText.getText().toString();
                mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userKey = mAuth.getCurrentUser().getUid();
                                    User user = new User(userKey, userName, userEmail);
                                    userService.save(user);
                                    Toast.makeText(RegisterActivity.this, "Cadastro efetuado com sucesso.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "E-mail ou senha inválido.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
