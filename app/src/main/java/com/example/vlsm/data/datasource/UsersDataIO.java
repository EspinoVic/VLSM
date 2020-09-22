package com.example.vlsm.data.datasource;

import com.example.vlsm.data.model.Project;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UsersDataIO {
    FirebaseFirestore db;
    public UsersDataIO() {

         db = FirebaseFirestore.getInstance();

    }

    public ArrayList<Project> getProjects(String username){
        return null;
    }



}
