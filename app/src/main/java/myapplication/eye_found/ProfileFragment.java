package myapplication.eye_found;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class ProfileFragment extends Fragment {
    private TextView fullname, phone;
    private View view;
    private Button btnLogout;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        //sessionManager = new SessionManager(getContext());
        //sessionManager.checkLogin();

        fullname = view.findViewById(R.id.textView12);
        phone = view.findViewById(R.id.textView13);
        btnLogout = view.findViewById(R.id.button4);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SessionManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String uPhone = sharedPreferences.getString(SessionManager.PHONE_SHARED_PREF,"");
        String uName = sharedPreferences.getString(SessionManager.NAME_SHARED_PREF,"");

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting out sharedpreferences
                SharedPreferences preferences = getActivity().getSharedPreferences(SessionManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                //Getting editor
                SharedPreferences.Editor editor = preferences.edit();

                //Puting the value false for loggedin
                editor.putBoolean(SessionManager.LOGGEDIN_SHARED_PREF_NAME, false);

                //Putting blank value to email
                editor.putString(SessionManager.PHONE_SHARED_PREF, "");

                //Saving the sharedpreferences
                editor.commit();

                //Starting login activity
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        //String fullnameint = getActivity().getIntent().getExtras().getString("fullname");
        //String phoneint = getActivity().getIntent().getExtras().getString("phone");
        //Integer iduser = getActivity().getIntent().getExtras().getInt("iduser");

        fullname.setText(uName);
        phone.setText(uPhone);

        return view;
    }
}
