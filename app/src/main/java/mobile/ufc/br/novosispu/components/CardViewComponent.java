package mobile.ufc.br.novosispu.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import mobile.ufc.br.novosispu.R;

public class CardViewComponent extends CardView {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView demandImageView;

    public CardViewComponent(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.component_cardview, this);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        demandImageView = (ImageView) findViewById(R.id.demandImageView);
    }

    public void setTitle(String text) {
        titleTextView.setText(text);
    }

    public void setDescription(String text) {
        descriptionTextView.setText(text);
    }

    public void setDemandImage(String imageUrl) {
        try {
            Bitmap imageBitmap = decodeFromFirebaseBase64(imageUrl);
            demandImageView.setImageBitmap(imageBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
}
