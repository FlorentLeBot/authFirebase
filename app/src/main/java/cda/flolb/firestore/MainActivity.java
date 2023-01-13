package cda.flolb.firestore;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Déclaration de variables pour l'authentification Firebase
    // Instance de l'objet FirebaseAuth
    // qui permet l'authentification des utilisateurs dans une application
    private FirebaseAuth mAuth;

    // AuthStateListener est une interface fournie par Firebase qui permet d'écouter
    // les changements d'état d'authentification de l'utilisateur.
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Déclaration de variables pour les champs email et mot de passe
    private EditText email;
    private EditText password;

    // Déclaration de variables pour les boutons de connexion et d'inscription
    private AppCompatButton loginBtn;
    private AppCompatButton registerBtn;

    //----------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // On récupère l'instance de l'authentification
        mAuth = FirebaseAuth.getInstance();

        // Déconnexion de l'utilisateur courant
        mAuth.signOut();

        // On récupère les éléments de la vue
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);


        // Ecouteur d'état d'authentification pour surveiller les modifications de l'état de l'authentification de l'utilisateur
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // L'utilisateur est connecté
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {

                    // L'utilisateur est déconnecté
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        // On ajoute un listener (écouteur d'évenement) sur le bouton de connexion
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // On récupère les valeurs des champs
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();

                // On vérifie que les champs ne sont pas vides
                if (emailValue.isEmpty() || passwordValue.isEmpty()) {

                    // On affiche un message d'erreur
                    Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
                else {

                    // On se connecte à l'aide de l'email et du mot de passe via l'authentification email/mot de passe de Firebase
                    login(emailValue, passwordValue);
                }
            }
        });


        // On ajoute un listener (écouteur d'évenement) sur le bouton d'inscription
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // On récupère les valeurs des champs
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();

                // On vérifie que les champs ne sont pas vides
                if (emailValue.isEmpty() || passwordValue.isEmpty()) {

                    // On affiche un message d'erreur
                    Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
                else {

                    // On enregistre l'utilisateur
                    register(emailValue, passwordValue);
                }}
        });
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------

    // Méthode permettant de se connecter en utilisant une adresse e-mail et un mot de passe
    public void login(String emailValue, String passwordValue) {

        // On appelle la méthode signInWithEmailAndPassword de Firebase pour connecter un utilisateur
        mAuth.signInWithEmailAndPassword(emailValue, passwordValue)

                // On ajoute un écouteur pour réagir à la fin de la tâche
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // Si la tâche s'est bien passée (connexion réussie)
                if (task.isSuccessful()) {

                    // On affiche un message de succès
                    Log.d(TAG, "signInWithEmail:success");
                    Toast.makeText(MainActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();

                    // On ouvre une nouvelle activité
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                } else {

                    // On affiche un message d'erreur
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "L'email existe déjà", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------

    // Méthode permettant de s'inscrire en utilisant une adresse e-mail et un mot de passe
    public void register(String emailValue, String passwordValue) {

        // On appelle la méthode createUserWithEmailAndPassword de Firebase pour créer un nouvel utilisateur
        mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)

                // On ajoute un écouteur pour réagir à la fin de la tâche
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // Si ça s'est bien passée (création d'utilisateur réussie)
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    Toast.makeText(MainActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);

                } else {

                    // Sinon on affiche un message d'erreur
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Inscription échouée", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}





