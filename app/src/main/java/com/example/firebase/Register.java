package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    Button register, button4;
    EditText fullname;
    EditText password;
    EditText email;
    EditText age;
   private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    User user;
    private Boolean validateName(){
        String val= fullname.getText().toString();

        if(val.isEmpty()){
            fullname.setError("Field Cannot be Empty");
            return false;
        }
        else {
            fullname.setError(null);
            return true;
        }
    }
    private Boolean validateAge(){
        String val= age.getText().toString();

        if(val.isEmpty()){
            age.setError("Field Cannot be Empty");
            return false;
        }
        else {
            age.setError(null);
            return true;
        }
    }
    private Boolean validatePass(){
        String val= password.getText().toString();

        if(val.isEmpty()){
            password.setError("Field Cannot be Empty");
            return false;
        }
        else if (val.length()<=7){
            password.setError("Minimum Length is 8");
            return false;
        }else{
            password.setError(null);
            return true;
        }
    }


    private Boolean validateEmail(){
        String val2=  email.getText().toString();
        //String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern="[a-zA-z]+.[a-zA-Z]+@[a-zA-Z]+.[a-zA-Z]+.[a-zA-Z.]";
        if(val2.isEmpty()){
            email.setError("Field Cannot be Empty");
            return false;
        }else if(!val2.matches(emailPattern)){
            email.setError("Invalid Email Address");
            return false;
        }
        else {
            email.setError(null);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser currentuser=firebaseAuth.getCurrentUser();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processRegister();
            }
        });

        fullname = findViewById(R.id.fullname);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        password= findViewById(R.id.password);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("user");
        user = new User();
    }
    private void getValue(){
        user.setFullname(fullname.getText().toString());
        user.setEmail(email.getText().toString());
        user.setAge(age.getText().toString());
    }


    private void processRegister() {

        if(!validateName() |!validateEmail() |!validatePass() | !validateAge()){
            return;
        }
        String Name=fullname.getText().toString().trim();
        String Pass=password.getText().toString().trim();
        String Age=age.getText().toString().trim();
        String Email=email.getText().toString().trim();
        firebaseAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("Name", Name);//fullname.getText().toString());
                    map.put("Age", Age);//age.getText().toString());
                    map.put("Email", Email);//email.getText().toString());
                    FirebaseDatabase.getInstance().getReference("User")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this,"User Registered Successfully",Toast.LENGTH_LONG).show();
                                //startActivity(new Intent(getApplicationContext(),AddLecturer.class));
                            }
                            else {
                                Toast.makeText(Register.this,"Failed  Register! Try Again",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                   Toast.makeText(Register.this,"Failed to Register! Try Again",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}