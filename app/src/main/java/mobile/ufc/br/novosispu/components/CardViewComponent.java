package mobile.ufc.br.novosispu.components;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import mobile.ufc.br.novosispu.R;

public class CardViewComponent extends CardView {

    private TextView titleTextView;
    private TextView descriptionTextView;

    public CardViewComponent(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.component_cardview, this);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
    }

    public void setTitle(String text) {
        titleTextView.setText(text);
    }

    public void setDescription(String text) {
        descriptionTextView.setText(text);
    }


}
