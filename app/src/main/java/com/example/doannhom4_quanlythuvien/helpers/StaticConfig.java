package com.example.doannhom4_quanlythuvien.helpers;

import com.example.doannhom4_quanlythuvien.model.Book;
import com.example.doannhom4_quanlythuvien.model.Statisticals;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

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
    public static DatabaseReference mPageNumber = Database.getReference("Page");
    public static DatabaseReference mComment = Database.getReference("Comment");
    public static String Default_avatar = "https://firebasestorage.googleapis.com/v0/b/quan-ly-thu-vien-eac1a.appspot.com/o/Default%20avatar%2Fuser.jpg?alt=media&token=22760e3b-7970-4cc5-b4a8-cb0331569f5a";
    //timestamp
    public static Date now = new Date();
    public static long timestamp = now.getTime();

    //array thong ke
    public static ArrayList<Statisticals> ArrayThongke = new ArrayList<>();
    //array mau
    public  static int[] arrayListColer =new int[100];
    //man hinh
    public static int items = 0;
    //arralist check
    public static ArrayList<Book> ArrayCheck = new ArrayList<>();
    //arraylist sach
    public static ArrayList<Book> ArrayBook = new ArrayList<>();
    //kiem tra xoa
    public static boolean is_del = false;
}
