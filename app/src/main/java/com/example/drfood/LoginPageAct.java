package com.example.drfood;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginPageAct extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private static final int RC_SIGN_IN = 10;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Boolean UserExgist;
    private String UserUid;
    private String UserName;
    private String UserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1061305272353-jph2miao034qttglvevvnig9nqe6k24r.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginPageAct.this, gso);

        SignInButton btn = (SignInButton)findViewById(R.id.login_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.e("오류시작", "1");
            try {
                // Google Sign In was successful, authenticate with Firebase
                Log.e("오류시작","1-2");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e("오류시작", account.getId());
                Log.e("오류시작", account.getEmail());
                Log.e("오류시작", account.getIdToken());
                firebaseAuthWithGoogle(account);
                Log.e("오류시작", "3");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Errore", "Google sign in failed", e);
                // [START_EXCLUDE]

                // updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("ID", "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]
        Log.d("맞나", "제발");
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Log.d("맞나", "제발1");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("맞나", "제발2");
                        if (task.isSuccessful()) {
                            Log.d("맞나", "제발3");
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Success", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserUid = user.getUid();
                            UserName = user.getDisplayName();
                            UserEmail = user.getEmail();
                            UserExgist = false;
                            Log.d("UserUid", UserUid);
                            Log.d("UserName",UserName);
                            Log.d("UserEmail", UserEmail);


                            //사용자가 DB에 있는 사용자인지 없는 사용자읹지 알기위해
                            ChildEventListener childEventListener = new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Log.d("여기까지","됬나?");
                                    if(dataSnapshot.getKey().equals(UserUid)){
                                        UserExgist = true;
                                        Log.d("여기까지","됬나?");
                                        //추후에 하자
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };
                            mDatabase.child("people").addChildEventListener(childEventListener);

                            if(!UserExgist){
                                Log.d("여기까지","됬나?");
                                mDatabase.child("people").child(UserUid).child("name").setValue(UserName);
                                mDatabase.child("people").child(UserUid).child("E-mail").setValue(UserEmail);
                                //mDatabase.child("people").child(UserUid).child()
                            }

                            //updateUI(user);
                            Intent intent = new Intent();
                            intent.putExtra("UserUid",UserUid);
                            intent.putExtra("UserName", UserName);
                            intent.putExtra("UserEmail",UserEmail);

                            setResult(3000,intent);
                            finish();
                        } else {
                            Log.d("맞나", "제발4");
                            // If sign in fails, display a message to the user.
                            Log.w("Errore", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginPageAct.this,"인증실패",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]
}
