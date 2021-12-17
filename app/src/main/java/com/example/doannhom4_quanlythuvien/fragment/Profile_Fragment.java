package com.example.doannhom4_quanlythuvien.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.MainActivity;
import com.example.doannhom4_quanlythuvien.helpers.*;
import com.example.doannhom4_quanlythuvien.ui.*;

import com.example.doannhom4_quanlythuvien.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //khai bao
    View view;
    private Button btnlogout;
    private ImageView  kimcuong;
    private CircleImageView avatar;
    private TextView tvname, tvphone, tvemail, tvsex, edit;
    private String url_avatar;

    public Profile_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile_Fragment newInstance(String param1, String param2) {
        Profile_Fragment fragment = new Profile_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment

        setControl();
        setEnvet();
        return view;
    }

    private void setControl() {
        StaticConfig.items = 1;
        kimcuong = view.findViewById(R.id.kimcuong);
        btnlogout = view.findViewById(R.id.btnlogout);
        avatar = view.findViewById(R.id.avatar);
        tvname = view.findViewById(R.id.tv_name);
        tvphone = view.findViewById(R.id.tv_phone);
        tvemail = view.findViewById(R.id.tv_email);
        tvsex = view.findViewById(R.id.tv_sex);
        edit = view.findViewById(R.id.edit);
        kimcuong.setVisibility(View.INVISIBLE);
        //load du lieu cua user hien tai
        Query profile = StaticConfig.mUser.orderByChild("id").equalTo(StaticConfig.currentuser);
        profile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.exists()) {
                        url_avatar = ds.child("pic").getValue(String.class);
                        Picasso.get()
                                .load(url_avatar)
                                .placeholder(R.drawable.no_image)
                                .fit()
//                                .transform(transformation)
                                .into(avatar);
                        tvname.setText(ds.child("name").getValue(String.class));
                        tvphone.setText(ds.child("phone").getValue(String.class));
                        tvemail.setText(ds.child("email").getValue(String.class));
                        tvsex.setText(ds.child("sex").getValue(String.class));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "fail to load", Toast.LENGTH_SHORT).show();
            }
        });
        //kiểm tra người dùng hiện tại có phải admin k
        StaticConfig.mAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.d("ad id", ds.child("id").getValue(String.class));
                    Log.d("id", StaticConfig.currentuser);
                    if (StaticConfig.currentuser.equals(ds.child("id").getValue(String.class))) {
                        kimcuong.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

    }

    private void setEnvet() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Edit_profile.class));
            }
        });
        kimcuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticConfig.items = 1;
                startActivity(new Intent(getContext(), Menu_admin.class));
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to sign out of the app??")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                StaticConfig.fAuth.signOut();
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        })


                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}