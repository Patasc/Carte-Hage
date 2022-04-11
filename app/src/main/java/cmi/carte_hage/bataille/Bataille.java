package cmi.carte_hage.bataille;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import cmi.carte_hage.R;

public class Bataille extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.bataille);
        System.out.println("I'm alive in create !");
    }

    public void main(View view) {
        System.out.println("I'm alive !");
    }
}
