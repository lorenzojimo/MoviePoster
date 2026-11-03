package com.example.poster1019;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {

    // Creates the Contact Us page using the fragment_contact.xml layout.
    public ContactFragment(){
        super(R.layout.fragment_contact);
    }

    // Runs once the page's views exist: wires up the email row and the intro button.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tapping the email row opens the user's email app.
        view.findViewById(R.id.layoutEmail).setOnClickListener(new View.OnClickListener() {
            // Opens an email draft addressed to the contact address.
            @Override
            public void onClick(View view) {
                openEmailApp(getString(R.string.contact_email));
            }
        });

        // Tapping the button replaces the description text with my personal introduction.
        TextView textIntro = view.findViewById(R.id.textIntro);
        view.findViewById(R.id.buttonIntro).setOnClickListener(v ->
                textIntro.setText(R.string.personal_intro));
    }

    // Starts an email app with a draft to the given address, or shows a message if none is installed.
    private void openEmailApp(String emailAddress){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+emailAddress));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(),R.string.no_email_app,Toast.LENGTH_SHORT).show();
        }
    }
}
