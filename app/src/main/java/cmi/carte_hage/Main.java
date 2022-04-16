package cmi.carte_hage;

import cmi.carte_hage.bataille.Bataille;
import cmi.carte_hage.blackjack.Blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Dictionary;
import java.util.Hashtable;

public class Main extends AppCompatActivity {
    Dictionary<String, Class> ListeDeJeux = new Hashtable<String, Class>();

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);

        setContentView(R.layout.menu);

        ListeDeJeux.put("Bataille", Bataille.class);
        ListeDeJeux.put("Blackjack", Blackjack.class);

    }

    public void onClickHandler(View view){
        Intent i = new Intent(this, ListeDeJeux.get(view.getTag()));
        startActivity(i);
    }
}
