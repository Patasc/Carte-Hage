package cmi.carte_hage.bataille;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cmi.carte_hage.R;

public class Bataille extends AppCompatActivity {
    private List<Integer> CartesJoueur;
    private List<Integer> CartesBot;

    private TextView scoreJoueur;
    private TextView scoreBot;

    ConstraintLayout layout;

    AnimatorSet animation;
    AnimatorSet tour_up;
    AnimatorSet tour_down;


    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.bataille);
        System.out.println("Activité Bataille démarrée");

        scoreJoueur = findViewById(R.id.scoreJoueur);
        scoreBot = findViewById(R.id.scoreBot);

        layout = findViewById(R.id.batailleLayout);
         animation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.testing);
        tour_up = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.bataille_tour_up);
        tour_down = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.bataille_tour_down);

        // Animation animation = AnimationUtils.loadAnimation(this, R.anim.secondtest);
        /*


        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.dos_carte);
        image.setId(View.generateViewId());
        image.setScaleX(5);
        image.setScaleY(5);

        v.addView(image);


        c.clone(v);

        c.connect(image.getId(), ConstraintSet.LEFT, 0, ConstraintSet.LEFT);
        c.connect(image.getId(), ConstraintSet.RIGHT, 0, ConstraintSet.RIGHT);
        c.connect(image.getId(), ConstraintSet.BOTTOM, 0, ConstraintSet.BOTTOM, 50);

        c.applyTo(v);


        // image.startAnimation(animation);

        AnimatorSet animation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.testing);
        animation.setTarget(image);
        animation.start();

        */

        this.demarrage();

    }

    public void distribution_cartes(){
        CartesJoueur = new ArrayList<Integer>();
        CartesBot = new ArrayList<Integer>();

        List<Integer> store = new ArrayList<Integer>();
        for (int i = 0; i < 52; i++){
            store.add(i);
        }

        Collections.shuffle(store);
        System.out.println("Number 1 !");

        for (int i = 0; i < 52; i++){
            System.out.println(i);
            if (i % 2 == 0){
                CartesJoueur.add(store.get(i));
                this.scoreJoueur.setText(String.valueOf(Integer.parseInt((String) this.scoreJoueur.getText()) + 1));
            }
            else {
                CartesBot.add(store.get(i));
                this.scoreBot.setText(String.valueOf(Integer.parseInt((String) this.scoreBot.getText()) + 1));
            }
        }

        System.out.println("Done");

    }


    public void demarrage(){
        this.distribution_cartes();
    }

    public void tour(){
        if (this.CartesBot.size() == 0 || this.CartesJoueur.size() == 0){
            System.out.println("Defaite !!");
        }

        else {
            Integer botPicked = this.CartesBot.get(0);

            ImageView carteBot = new ImageView(this);
            int rId = (this.getResources()).getIdentifier("c" + String.valueOf(botPicked), "drawable", this.getPackageName());
            carteBot.setImageResource(rId);
            carteBot.setId(View.generateViewId());
            carteBot.setScaleX(2); carteBot.setScaleY(2);

            ConstraintSet anchorCarteBot = new ConstraintSet();

            this.layout.addView(carteBot);

            anchorCarteBot.clone(layout);

            anchorCarteBot.connect(carteBot.getId(), ConstraintSet.LEFT, R.id.scoreBot, ConstraintSet.RIGHT);
            anchorCarteBot.connect(carteBot.getId(), ConstraintSet.RIGHT, 0, ConstraintSet.RIGHT);
            anchorCarteBot.connect(carteBot.getId(), ConstraintSet.TOP, 0, ConstraintSet.TOP, 176);

            anchorCarteBot.applyTo(layout);


            Integer playerPicked = this.CartesJoueur.get(0);

            ImageView carteJoueur = new ImageView(this);
            int rIdJoueur = (this.getResources()).getIdentifier("c" + String.valueOf(playerPicked), "drawable", this.getPackageName());
            carteJoueur.setImageResource(rIdJoueur);
            carteJoueur.setId(View.generateViewId());
            carteJoueur.setScaleX(2); carteJoueur.setScaleY(2);

            ConstraintSet anchorCarteJoueur = new ConstraintSet();

            this.layout.addView(carteJoueur);

            anchorCarteJoueur.clone(layout);

            anchorCarteJoueur.connect(carteJoueur.getId(), ConstraintSet.RIGHT, R.id.scoreJoueur, ConstraintSet.LEFT);
            anchorCarteJoueur.connect(carteJoueur.getId(), ConstraintSet.LEFT, 0, ConstraintSet.LEFT);
            anchorCarteJoueur.connect(carteJoueur.getId(), ConstraintSet.BOTTOM, 0, ConstraintSet.BOTTOM, 176);

            anchorCarteJoueur.applyTo(layout);

            tour_up.setTarget(carteJoueur);
            tour_up.start();

            tour_down.setTarget(carteBot);
            tour_down.start();

            this.scoreJoueur.setText(String.valueOf(Integer.parseInt((String) this.scoreJoueur.getText()) - 1));
            this.scoreBot.setText(String.valueOf(Integer.parseInt((String) this.scoreBot.getText()) - 1));
        }
    }

    public void pileJoueurClicked(View view){
        view.setClickable(false);
        this.tour();
    }
}
