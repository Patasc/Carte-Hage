package cmi.carte_hage.bataille;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cmi.carte_hage.R;

public class Bataille extends AppCompatActivity {
    private List<Integer> CartesJoueur;
    private List<Integer> CartesBot;

    private TextView scoreJoueur;
    private TextView scoreBot;

    ConstraintLayout layout;

    Animator.AnimatorListener VerificationGagnant = new Animator.AnimatorListener() {
        @Override public void onAnimationStart(Animator animator) {}
        @Override public void onAnimationRepeat(Animator animator) {}
        @Override public void onAnimationCancel(Animator animator) {}

        @Override
        public void onAnimationEnd(Animator animator) {
            verificationGagnantTour();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.bataille);
        System.out.println("Activité Bataille démarrée");

        scoreJoueur = findViewById(R.id.scoreJoueur);
        scoreBot = findViewById(R.id.scoreBot);

        layout = findViewById(R.id.batailleLayout);
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

        for (int i = 0; i < 52; i++){
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



            ObjectAnimator testing = ObjectAnimator.ofFloat(carteJoueur, "TranslationY", -600.F).setDuration(2000);
            testing.addListener(this.VerificationGagnant);
            testing.start();

            ObjectAnimator.ofFloat(carteBot, "TranslationY", 600.F).setDuration(2000).start();


            this.scoreJoueur.setText(String.valueOf(Integer.parseInt((String) this.scoreJoueur.getText()) - 1));
            this.scoreBot.setText(String.valueOf(Integer.parseInt((String) this.scoreBot.getText()) - 1));


        }
    }

    public void pileJoueurClicked(View view){
        view.setClickable(false);
        this.tour();
    }

    public void verificationGagnantTour(){
        System.out.println("Animation terminée !");
    }
}
