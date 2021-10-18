package com.example.doannhom4_quanlythuvien.helpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StaticConfig {
    public static FirebaseDatabase Database = FirebaseDatabase.getInstance();
    public static FirebaseAuth fAuth = FirebaseAuth.getInstance();
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static FirebaseUser updateUser = FirebaseAuth.getInstance().getCurrentUser();
    public static StorageReference storageReference = storage.getReference();
    public static String currentuser = "";
    public static DatabaseReference mUser = Database.getReference("User");
    public static DatabaseReference mCategory = Database.getReference("Category");
    public static DatabaseReference mAdmin = Database.getReference("Admin");
    public static DatabaseReference mBook = Database.getReference("Book");
    public static DatabaseReference mLibrary = Database.getReference("Library");
    public static final int PICK_IMAGE_REQUEST = 10;
    public static String Default_avatar = "https://firebasestorage.googleapis.com/v0/b/quan-ly-thu-vien-eac1a.appspot.com/o/Default%20avatar%2Fuser.jpg?alt=media&token=22760e3b-7970-4cc5-b4a8-cb0331569f5a";
    public static String Test_link = "https://firebasestorage.googleapis.com/v0/b/quan-ly-thu-vien-eac1a.appspot.com/o/SDS_DoAnQuanLyKhachSan_Nhom7.docx?alt=media&token=e2857ec8-7a70-495b-8673-97602e83b3d5";
    //timestamp
    public static float timestamp = System.currentTimeMillis() / 1000;

}
