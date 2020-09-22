package com.example.vlsm.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vlsm.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mDocRefUsers;

    public Result<LoggedInUser> login(final String username, final String password) {

        final boolean[] objectResult = {false};
        final boolean[] userExists = {false};
        final boolean[] wrongPassword = {false};


        mDocRefUsers =  db.document("vlsm/users");
        try {
            // TODO: handle loggedInUser authentication

            mDocRefUsers.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        HashMap<String,Object> vic = (HashMap<String, Object>) documentSnapshot.get("vic");
                        int a=1;
                        a=2;
                        System.out.println(a);
                        if(vic.get("username")==null){
                            userExists[0] = false;
                        }
                        if(username.equals(vic.get("username"))&&password.equals(vic.get("password"))){
                            objectResult[0] = true;
                            userExists[0] = true;
                        }else{
                            wrongPassword[0] = true;
                        }
                    }else{
                        objectResult[0] = false;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    objectResult[0] = false;
                }
            });

            /*Map<String, Object> user = new HashMap<>();
            user.put("first", "Ada");
            user.put("last", "Lovelace");
            user.put("born", 1815);

            db.collection("vlsm")
                    .document("users")
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Succes" );
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
*/
            if(objectResult[0]==false){
                return new Result.Error(new IOException("Can't get data."));
            }
            if(userExists[0]==false){
                return new Result.Error(new IOException("User doesn't exists."));
            }
            if(wrongPassword[0]==false){
                return new Result.Error(new IOException("Wrong password."));
            }
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            password,
                            username);

            if(objectResult[0]){
                return new Result.Success<>(fakeUser);
            }else{
                return new Result.Error(new IOException("Error something."));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}