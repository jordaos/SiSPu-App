package mobile.ufc.br.novosispu.service;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

import mobile.ufc.br.novosispu.entities.Demand;
import mobile.ufc.br.novosispu.entities.Like;

import static mobile.ufc.br.novosispu.Constants.FIREBASE_CHILD_LIKES;

public class LikeService {
    private FirebaseDatabase database;
    private DatabaseReference likesRef;

    public LikeService() {
        database = FirebaseDatabase.getInstance();
        likesRef = database.getReference(FIREBASE_CHILD_LIKES);
    }

    public void save(Like like) {
        String key = like.getKey();
        if(key == null || key.equals("")) {
            key = likesRef.push().getKey();
            like.setKey(key);
        }
        likesRef.child(key).setValue(like);
    }

    public DatabaseReference getLikesRef() {
        return likesRef;
    }

    public void like(final String demandKey, final String userKey) {
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean liked = false;
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Like like = item.getValue(Like.class);
                    if (like.getDemandKey().equals(demandKey) && like.getUserKey().equals(userKey)) {
                        liked = true;
                        break;
                    }
                }

                if (!liked) {
                    Like newLike = new Like(userKey, demandKey);
                    save(newLike);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
