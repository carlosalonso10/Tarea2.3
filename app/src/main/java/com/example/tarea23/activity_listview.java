package com.example.tarea23;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
public class activity_listview extends AppCompatActivity {

    private ListView listView;
    private CustomAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        listView = findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);

        ArrayList<Photograph> photographs = (ArrayList<Photograph>) databaseHelper.getAllPhotographs();

        adapter = new CustomAdapter(this, photographs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {

            Photograph selectedPhotograph = (Photograph) parent.getItemAtPosition(position);


        });
    }
}
