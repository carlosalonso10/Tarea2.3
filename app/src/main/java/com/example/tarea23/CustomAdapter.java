package com.example.tarea23;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Photograph> {

    private Context context;
    private List<Photograph> photographs;

    public CustomAdapter(Context context, List<Photograph> photographs) {
        super(context, R.layout.list_item_photograph, photographs);
        this.context = context;
        this.photographs = photographs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.list_item_photograph, parent, false);

        ImageView imageView = rowView.findViewById(R.id.imageView);
        TextView descriptionTextView = rowView.findViewById(R.id.descriptionTextView);

        byte[] imageBytes = photographs.get(position).getImage(); // Obtener el array de bytes de la imagen
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length); // Convertir el array de bytes a Bitmap
        imageView.setImageBitmap(bitmap); // Establecer la imagen en el ImageView

        descriptionTextView.setText(photographs.get(position).getDescription()); // Establecer la descripción en el TextView

        return rowView;
    }
}
