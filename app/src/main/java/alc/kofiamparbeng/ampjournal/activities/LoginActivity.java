package alc.kofiamparbeng.ampjournal.activities;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.app.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import alc.kofiamparbeng.ampjournal.R;
import alc.kofiamparbeng.ampjournal.data.FirebaseDatabaseConstants;
import alc.kofiamparbeng.ampjournal.sync.JournalSyncUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    //Constants
    private static final String TAG = "kofiamparbeng.journal";
    private static final int RC_SIGN_IN = 12100;

    private TextView mEmailTextView;
    private  TextView mPasswordTextView;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            // User is logged in
            goToMainActivity();
        }

        mEmailTextView = (TextView)findViewById(R.id.tv_email);
        mPasswordTextView=(TextView)findViewById(R.id.tv_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button btnEmailSignIn =(Button)findViewById(R.id.email_sign_in_button);
        btnEmailSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithEmail();
            }
        });

        TextView newSignUpTextView = (TextView)findViewById(R.id.tv_new_sign_up_prompt);
        newSignUpTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                newSignUp();
            }
        });

        //Setup Google Sign In Button
        SignInButton signInButton = findViewById(R.id.google_sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                singInWithGoogle();
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.AMPJOURNAL_SIGNIN_TOKEN_ID))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }


    private void singInWithGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInWithEmail(){
        String email = mEmailTextView.getText().toString();
        final String password = mPasswordTextView.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), R.string.emoty_email_prompt, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), R.string.emoty_password_prompt, Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 3) {
                                mPasswordTextView.setError(getString(R.string.error_invalid_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.error_invalid_password), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            processSuccessfulLoginResponse();
                        }
                    }
                })
        .addOnFailureListener(LoginActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, getString(R.string.error_signing_in), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void processSuccessfulLoginResponse(){
        FirebaseUser currentUser = auth.getCurrentUser();
        DatabaseReference fireDatabase = FirebaseDatabase.getInstance().getReference();
        fireDatabase.child(FirebaseDatabaseConstants.JOURNAL_TABLE_NAME).child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Schedule Sync if user has data
                        JournalSyncUtils.startImmediateCloudFetch(LoginActivity.this);
                        goToMainActivity();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        goToMainActivity();
                    }
                });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void newSignUp(){
        Intent signUpFormIntent = new Intent(this, RegisterActivity.class);
        startActivity(signUpFormIntent);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            goToMainActivity();
                        } else {
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, getString(R.string.error_signing_in), Toast.LENGTH_LONG).show();
        }
    }

    private void updateUI(GoogleSignInAccount signedInAccount) {
        if (signedInAccount != null || 1==1) {
            Intent gotoMainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(gotoMainActivityIntent);
        } else {
            Toast.makeText(this, "Error signing in", Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

    }


}

