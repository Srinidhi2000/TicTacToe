package com.example.android.tic_tac_toe;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class viewTime extends AppCompatActivity {
    display_besttime db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_besttime);
        ListView list=(ListView)findViewById(R.id.list);
        db=new display_besttime(this);
        ArrayList<String> storetime=new ArrayList<>();
          Cursor data=db.getcontents();
        if (data.getCount()==0)
        {Toast.makeText(getApplicationContext(),"Database is empty",Toast.LENGTH_SHORT).show(); }
        else
        {
            while(data.moveToNext())
            {storetime.add(data.getString(1)); }
            ListAdapter listAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,storetime);
            list.setAdapter(listAdapter);
            }
    }
}
