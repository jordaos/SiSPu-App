package mobile.ufc.br.novosispu.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import mobile.ufc.br.novosispu.R;
import mobile.ufc.br.novosispu.entities.Demand;
import mobile.ufc.br.novosispu.service.LikeService;
import mobile.ufc.br.novosispu.service.UserService;

public class CardViewComponent extends CardView {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView userTextView;
    private ImageView demandImageView;
    private Button likeDemandButton;
    private Demand demand;

    private LikeService likeService;
    private UserService userService;

    public CardViewComponent(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        inflate(context, R.layout.component_cardview, this);

        likeService = new LikeService();
        userService = new UserService();

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        userTextView = (TextView) findViewById(R.id.userTextView);
        demandImageView = (ImageView) findViewById(R.id.demandImageView);

        likeDemandButton = (Button) findViewById(R.id.likeDemandButton);

        likeDemandButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                likeService.like(demand.getKey(), userService.getCurrentUserKey());
            }
        });
    }

    public void setTitle(String text) {
        titleTextView.setText(text);
    }

    public void setDescription(String text) {
        descriptionTextView.setText(text);
    }

    public void setLikeDemandButton(int qtd) {
        likeDemandButton.setText("Like (" + qtd + ")");
    }

    public void setUser(String userName) {
        userTextView.setText("Por: " + userName);
    }

    public void setDemandImage(String imageUrl) {
        try {
            Bitmap imageBitmap = decodeFromFirebaseBase64(imageUrl);
            demandImageView.setImageBitmap(imageBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
        setTitle(demand.getTitle());
        setDescription(demand.getDescription());
        setUser(demand.getUser().getName());

        if(demand.getImageUrl() != null && !demand.getImageUrl().equals("")) {
            setDemandImage(demand.getImageUrl());
        }
    }

    private Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
}
