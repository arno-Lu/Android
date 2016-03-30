package com.example.lu.listviewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Character> characterList = new ArrayList<Character>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCharacter();
        CharacterAdapter adapter = new CharacterAdapter(MainActivity.this,R.layout.character_item,characterList);
        ListView listView =(ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Character character = characterList.get(position);
                Toast.makeText(MainActivity.this,character.getName(),Toast.LENGTH_SHORT).show();
            }
        });

    }
`   
    private  void initCharacter(){

        Character A = new Character("A",R.drawable.a123);
        characterList.add(A);

        Character B = new Character("B",R.drawable.a123);
        characterList.add(B);

        Character C = new Character("C",R.drawable.a123);
        characterList.add(C);

        Character D = new Character("D",R.drawable.a123);
        characterList.add(D);

        Character E = new Character("E",R.drawable.a123);
        characterList.add(E);

        Character F = new Character("F",R.drawable.a123);
        characterList.add(F);

        Character G = new Character("G",R.drawable.a123);
        characterList.add(G);

        Character H = new Character("H",R.drawable.a123);
        characterList.add(H);

        Character I = new Character("I",R.drawable.a123);
        characterList.add(I);

        Character J = new Character("J",R.drawable.a123);
        characterList.add(J);

        Character K = new Character("K",R.drawable.a123);
        characterList.add(K);
    }
}
