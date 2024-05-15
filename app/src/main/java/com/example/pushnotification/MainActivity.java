package com.example.pushnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextMessage, editTextUserToken;
    String title, message, userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextMessage = findViewById(R.id.editTextMessage);
        editTextUserToken = findViewById(R.id.editTextUserToken);

        // For sending notification to all
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        // For sending notification to a particular user
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            // Store/Update this token to database while sign in every time
                            String token = Objects.requireNonNull(task.getResult()).getToken();
                            editTextUserToken.setText(token);
                        }
                    }
                });
//        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
//        mUser.getIdToken(true)
//                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                    public void onComplete(@NonNull Task<GetTokenResult> task) {
//                        if (task.isSuccessful()) {
//                            String idToken = task.getResult().getToken();
//                            // Send token to your backend via HTTPS
//                            // ...
//                        } else {
//                            // Handle error -> task.getException();
//                        }
//                    }
//                });
    }

    // Send notification to a specific user
    public void onSendNotificationToUserButtonClicked(View view) {
        title = editTextTitle.getText().toString();
        message = editTextMessage.getText().toString();
        userToken = editTextUserToken.getText().toString();
        if (!title.isEmpty() && !message.isEmpty() && !userToken.isEmpty()) {
            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(userToken,
                    title, message, getApplicationContext(), MainActivity.this);
            notificationsSender.SendNotifications();
        } else {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
        }
    }

    // Send notification to all users
    public void onSendNotificationToAllButtonClicked(View view) {
        title = editTextTitle.getText().toString();
        message = editTextMessage.getText().toString();
        if (!title.isEmpty() && !message.isEmpty()) {
            FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",
                    title, message, getApplicationContext(), MainActivity.this);
            notificationsSender.SendNotifications();
        } else {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
        }
    }

}