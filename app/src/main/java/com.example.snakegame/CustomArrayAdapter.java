package com.example.snakegame;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import java.util.List;
public class CustomArrayAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> playerScores;

    public CustomArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mContext = context;
        playerScores = objects; // Initialize the playerScores list with the objects passed
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        String currentPlayer = getItem(position); // Correctly retrieve the current player score

        TextView textView = listItem.findViewById(android.R.id.text1);
        textView.setText(currentPlayer);
        textView.setTypeface(ResourcesCompat.getFont(mContext, R.font.munro)); // Load and set the custom font
        textView.setTextColor(Color.CYAN); // Set the text color to cyan

        return listItem;
    }
}
