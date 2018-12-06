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
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import mobile.ufc.br.novosispu.R;
import mobile.ufc.br.novosispu.entities.Demand;
import mobile.ufc.br.novosispu.service.DemandService;
import mobile.ufc.br.novosispu.service.LikeService;
import mobile.ufc.br.novosispu.service.UserService;

public class CardViewComponent extends CardView {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView userTextView;
    private ImageView demandImageView;
    private Button likeDemandButton;
    private Button editDemandButton;
    private Button deleteDemandButton;
    private Demand demand;
    private LinearLayout buttonsContentLinearLayout;

    private LikeService likeService;
    private UserService userService;
    private DemandService demandService;

    public CardViewComponent(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        inflate(context, R.layout.component_cardview, this);

        likeService = new LikeService();
        userService = new UserService();
        demandService = new DemandService();

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        userTextView = (TextView) findViewById(R.id.userTextView);
        demandImageView = (ImageView) findViewById(R.id.demandImageView);

        buttonsContentLinearLayout = (LinearLayout) findViewById(R.id.buttonsContentLinearLayout);

        likeDemandButton = (Button) findViewById(R.id.likeDemandButton);
        editDemandButton = (Button) findViewById(R.id.editDemandButton);
        deleteDemandButton = (Button) findViewById(R.id.deleteDemandButton);

        likeDemandButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                likeService.like(demand.getKey(), userService.getCurrentUserKey());
            }
        });

        buttonsContentLinearLayout.removeView(editDemandButton);
        buttonsContentLinearLayout.removeView(deleteDemandButton);
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

    public void editable() {
        buttonsContentLinearLayout.addView(editDemandButton);
        buttonsContentLinearLayout.addView(deleteDemandButton);
        buttonsContentLinearLayout.removeView(likeDemandButton);

        deleteDemandButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                demandService.remove(demand);
            }
        });
    }

    private Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
}
