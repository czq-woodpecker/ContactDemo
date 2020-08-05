package com.thoughtworks.contactdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectContactActivity extends AppCompatActivity {
    public static final String TAG = "SelectContactActivity";
    public static final int REQUEST_SELECT_CONTACT = 1;
    private Button selectContactButton;
    private Button lifecycleDemoButton;
    private TextView contactInfoTextView;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);

        selectContactButton = findViewById(R.id.select_contact_button);
        lifecycleDemoButton = findViewById(R.id.lifecycle_demo_button);
        contactInfoTextView = findViewById(R.id.contact_info_text_view);
        selectContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectContact();
            }
        });
        lifecycleDemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLifecycleDemoActivity();
            }
        });
    }

    private void openLifecycleDemoActivity() {
        Intent intent = new Intent(this, LifecycleDemoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            uri = data.getData();

            Cursor cursor = getContentResolver().query(uri, null, null, null);
            if(cursor != null && cursor.moveToFirst()) {
                int displayNameX = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String displayName = cursor.getString(displayNameX);

                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                cursor.close();
                Log.d(TAG, "displayName=" + displayName);
                Log.d(TAG, "phoneNumber=" + phoneNumber);

                String formattedPhoneNumber = formatPhoneNumber(phoneNumber);

                contactInfoTextView.setText(displayName + " " + formattedPhoneNumber);
            }
        }
    }

    private String formatPhoneNumber(String phoneNumber) {
        if(phoneNumber == null || phoneNumber.isEmpty()) {
            return "";
        }
        return phoneNumber.replace("-", "").replace(" ", "");
    }

    public void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, REQUEST_SELECT_CONTACT);
    }
}
