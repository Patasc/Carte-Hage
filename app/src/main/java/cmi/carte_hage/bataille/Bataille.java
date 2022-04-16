package cmi.carte_hage.bataille;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private TextView annonceFin;

    private ImageView imageJoueur;
    private ImageView imageBot;

    private ConstraintLayout layout;
    private ImageView pileJoueur;

    private Integer cartesEnStock = 0;

    private ArrayList<Integer> defausse = new ArrayList<Integer>();

    Animator.AnimatorListener VerificationGagnant = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            verificationGagnantTour();
        }
    };

    Animator.AnimatorListener SuiteTour = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            suite_tour();
        }
    };

    Animator.AnimatorListener TourSuivantNonEgalite = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            tourSuivantNonEgalite();
        }
    };

    Animator.AnimatorListener TourSuivantEgalite1 = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            cas_egalite();
        }
    };

    Animator.AnimatorListener TourSuivantEgalite = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            suite_anim_egalite();
        }
    };

    Animator.AnimatorListener TourSuivantEgalite2 = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            fin_anim_egalite();
        }
    };

    private View.OnClickListener recommencer = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            layout.removeView(view);
            layout.removeView(annonceFin);

            demarrage();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        System.out.println("CALLED !");
        // setContentView(this.getResources().getIdentifier("layout", "bataille", this.getPackageName()));
        setContentView(R.layout.bataille);
        System.out.println("Activité Bataille démarrée");

        scoreJoueur = findViewById(R.id.scoreJoueur);
        scoreBot = findViewById(R.id.scoreBot);

        layout = findViewById(R.id.batailleLayout);
        pileJoueur = findViewById(R.id.pileJoueur);
        this.demarrage();

    }

    public void distribution_cartes() {
        this.CartesJoueur = new ArrayList<Integer>();
        this.CartesBot = new ArrayList<Integer>();
        this.cartesEnStock = 0;

        List<Integer> store = new ArrayList<Integer>();
        for (int i = 0; i < 52; i++) {
            store.add(i);
        }

        Collections.shuffle(store);

        for (int i = 0; i < store.size(); i++) {
            if (i % 2 == 0) {
                this.CartesJoueur.add(store.get(i));
                this.scoreJoueur.setText(String.valueOf(this.CartesJoueur.size()));
            } else {
                this.CartesBot.add(store.get(i));
                this.scoreBot.setText(String.valueOf(this.CartesBot.size()));
            }
        }
    }


    public void demarrage() {
        this.distribution_cartes();
        this.pileJoueur.setClickable(true);
    }

    public void suite_tour() {
        ObjectAnimator testing = ObjectAnimator.ofFloat(this.imageJoueur, "TranslationY", -600.F).setDuration(2000);
        testing.addListener(this.VerificationGagnant);
        testing.start();

        ObjectAnimator.ofFloat(this.imageBot, "TranslationY", 600.F).setDuration(2000).start();
    }

    public void fin_anim_egalite(){
        this.tourSuivantNonEgalite();
    }

    public void suite_anim_egalite(){
        ObjectAnimator testing = ObjectAnimator.ofFloat(this.imageJoueur, "TranslationX", -1000.F).setDuration(800);
        testing.addListener(TourSuivantEgalite2);
        testing.start();

        ObjectAnimator.ofFloat(this.imageBot, "TranslationX", 1000.F).setDuration(800).start();
    }

    public void cas_egalite(){
        this.layout.removeView(this.imageBot);
        this.layout.removeView(this.imageJoueur);

        this.scoreJoueur.setText(String.valueOf(Integer.parseInt((String) this.scoreJoueur.getText()) - 1));
        this.scoreBot.setText(String.valueOf(Integer.parseInt((String) this.scoreBot.getText()) - 1));
        this.cartesEnStock += 2;

        Integer botPicked = this.CartesBot.get(0);
        this.CartesBot.remove(0);
        this.defausse.add(botPicked);

        this.imageBot = new ImageView(this);
        this.imageBot.setTag(botPicked);
        this.imageBot.setImageResource(R.drawable.dos_carte);
        this.imageBot.setId(View.generateViewId());
        this.imageBot.setScaleX(2);
        this.imageBot.setScaleY(2);

        Integer playerPicked = this.CartesJoueur.get(0);
        this.CartesJoueur.remove(0);
        this.defausse.add(playerPicked);

        this.imageJoueur = new ImageView(this);
        this.imageJoueur.setTag(playerPicked);
        this.imageJoueur.setImageResource(R.drawable.dos_carte);
        this.imageJoueur.setId(View.generateViewId());
        this.imageJoueur.setScaleX(2);
        this.imageJoueur.setScaleY(2);

        if (this.CartesBot.size() == 0 || this.CartesJoueur.size() == 0){
            this.defaite();
            return;
        }

        this.layout.addView(this.imageJoueur);
        this.layout.addView(this.imageBot);

        ConstraintSet anchorCarte = new ConstraintSet();
        anchorCarte.clone(layout);

        anchorCarte.connect(this.imageBot.getId(), ConstraintSet.LEFT, R.id.scoreBot, ConstraintSet.LEFT);
        anchorCarte.connect(this.imageBot.getId(), ConstraintSet.RIGHT, R.id.scoreBot, ConstraintSet.RIGHT);
        anchorCarte.connect(this.imageBot.getId(), ConstraintSet.TOP, 0, ConstraintSet.TOP, 300);

        anchorCarte.connect(this.imageJoueur.getId(), ConstraintSet.RIGHT, R.id.scoreJoueur, ConstraintSet.RIGHT);
        anchorCarte.connect(this.imageJoueur.getId(), ConstraintSet.LEFT, R.id.scoreJoueur, ConstraintSet.LEFT);
        anchorCarte.connect(this.imageJoueur.getId(), ConstraintSet.BOTTOM, 0, ConstraintSet.BOTTOM, 300);

        anchorCarte.applyTo(layout);

        ObjectAnimator testing = ObjectAnimator.ofFloat(this.imageJoueur, "TranslationY", -600.F).setDuration(800);
        testing.addListener(TourSuivantEgalite);
        testing.start();

        ObjectAnimator.ofFloat(this.imageBot, "TranslationY", 600.F).setDuration(800).start();
    }

    public void tour() {
        this.scoreJoueur.setText(String.valueOf(Integer.parseInt((String) this.scoreJoueur.getText()) - 1));
        this.scoreBot.setText(String.valueOf(Integer.parseInt((String) this.scoreBot.getText()) - 1));
        this.cartesEnStock += 2;

        Integer botPicked = this.CartesBot.get(0);
        this.CartesBot.remove(0);
        this.defausse.add(botPicked);

        this.imageBot = new ImageView(this);
        this.imageBot.setTag(botPicked);
        int rId = (this.getResources()).getIdentifier("c" + String.valueOf(botPicked), "drawable", this.getPackageName());
        this.imageBot.setImageResource(rId);
        this.imageBot.setId(View.generateViewId());
        this.imageBot.setScaleX(2);
        this.imageBot.setScaleY(2);
        this.imageBot.setAlpha(0.F);

        Integer playerPicked = this.CartesJoueur.get(0);
        this.CartesJoueur.remove(0);
        this.defausse.add(playerPicked);

        this.imageJoueur = new ImageView(this);
        this.imageJoueur.setTag(playerPicked);
        int rIdJoueur = (this.getResources()).getIdentifier("c" + String.valueOf(playerPicked), "drawable", this.getPackageName());
        this.imageJoueur.setImageResource(rIdJoueur);
        this.imageJoueur.setId(View.generateViewId());
        this.imageJoueur.setScaleX(2);
        this.imageJoueur.setScaleY(2);
        this.imageJoueur.setAlpha(0.F);

        this.layout.addView(this.imageJoueur);
        this.layout.addView(this.imageBot);

        ConstraintSet anchorCarte = new ConstraintSet();
        anchorCarte.clone(layout);

        anchorCarte.connect(this.imageBot.getId(), ConstraintSet.LEFT, R.id.scoreBot, ConstraintSet.RIGHT);
        anchorCarte.connect(this.imageBot.getId(), ConstraintSet.RIGHT, 0, ConstraintSet.RIGHT);
        anchorCarte.connect(this.imageBot.getId(), ConstraintSet.TOP, 0, ConstraintSet.TOP, 176);

        anchorCarte.connect(this.imageJoueur.getId(), ConstraintSet.RIGHT, R.id.scoreJoueur, ConstraintSet.LEFT);
        anchorCarte.connect(this.imageJoueur.getId(), ConstraintSet.LEFT, 0, ConstraintSet.LEFT);
        anchorCarte.connect(this.imageJoueur.getId(), ConstraintSet.BOTTOM, 0, ConstraintSet.BOTTOM, 176);

        anchorCarte.applyTo(layout);

        ObjectAnimator testing = ObjectAnimator.ofFloat(this.imageJoueur, "Alpha", 1.F).setDuration(800);
        testing.addListener(SuiteTour);
        testing.start();

        ObjectAnimator.ofFloat(this.imageBot, "Alpha", 1.F).setDuration(800).start();

    }

    public void pileJoueurClicked(View view) {
        this.pileJoueur.setClickable(false);
        this.tour();
    }

    public void verificationGagnantTour() {
        Integer valeurCarteBot = ((Integer) this.imageBot.getTag()) % 13;
        Integer valeurCarteJoueur = ((Integer) this.imageJoueur.getTag()) % 13;

        if (valeurCarteBot.equals(valeurCarteJoueur)) {
            ObjectAnimator testing = ObjectAnimator.ofFloat(this.imageJoueur, "TranslationX", 1000.F).setDuration(500);
            testing.addListener(TourSuivantEgalite1);
            testing.start();

            ObjectAnimator.ofFloat(this.imageBot, "TranslationX", -1000.F).setDuration(500).start();

        } else if (valeurCarteBot > valeurCarteJoueur) {
            ObjectAnimator testing = ObjectAnimator.ofFloat(this.imageJoueur, "TranslationY", -2500.F).setDuration(500);
            testing.addListener(TourSuivantNonEgalite);
            testing.start();

            ObjectAnimator.ofFloat(this.imageBot, "TranslationY", -500.F).setDuration(500).start();

            this.scoreBot.setText(String.valueOf(Integer.parseInt((String) this.scoreBot.getText()) + this.cartesEnStock));

            this.CartesBot.addAll(this.defausse);
            this.defausse.clear();
            this.cartesEnStock = 0;
        } else {
            ObjectAnimator testing = ObjectAnimator.ofFloat(this.imageJoueur, "TranslationY", 500.F).setDuration(500);
            testing.addListener(TourSuivantNonEgalite);
            testing.start();

            ObjectAnimator.ofFloat(this.imageBot, "TranslationY", 2500.F).setDuration(500).start();

            this.scoreJoueur.setText(String.valueOf(Integer.parseInt((String) this.scoreJoueur.getText()) + this.cartesEnStock));

            // Pour que ce soit carte gagnt puis perdant dans pile
            for (int i = 0; i < this.defausse.size(); i += 2) {
                if (i + 1 < this.defausse.size()) this.CartesJoueur.add(this.defausse.get(i + 1));
                this.CartesJoueur.add(this.defausse.get(i));
            }

            this.defausse.clear();
            this.cartesEnStock = 0;
        }
    }

    public void defaite(){
        this.annonceFin = new TextView(this);
        this.annonceFin.setTextSize(34.F);
        this.annonceFin.setId(View.generateViewId());
        this.annonceFin.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.layout.addView(this.annonceFin);

        ConstraintSet anchorCarte = new ConstraintSet();
        anchorCarte.clone(layout);

        anchorCarte.connect(this.annonceFin.getId(), ConstraintSet.LEFT, 0, ConstraintSet.LEFT);
        anchorCarte.connect(this.annonceFin.getId(), ConstraintSet.RIGHT, 0, ConstraintSet.RIGHT);
        anchorCarte.connect(this.annonceFin.getId(), ConstraintSet.BOTTOM, 0, ConstraintSet.BOTTOM);
        anchorCarte.connect(this.annonceFin.getId(), ConstraintSet.TOP, 0, ConstraintSet.TOP);

        anchorCarte.applyTo(layout);

        if (this.CartesBot.size() == 0 && this.CartesJoueur.size() == 0){
            this.annonceFin.setText("Egalité !");
        }

        else if (this.CartesJoueur.size() == 0){
            this.annonceFin.setText("Défaite !");
        }

        else {
            this.annonceFin.setText("Victoire ! Félicitations");
        }

        Button bouton = new Button(this);
        bouton.setId(View.generateViewId());
        bouton.setOnClickListener(recommencer);
        bouton.setText("Recommencer ?");
        bouton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.layout.addView(bouton);

        ConstraintSet anchorTexte = new ConstraintSet();
        anchorTexte.clone(layout);

        anchorTexte.connect(bouton.getId(), ConstraintSet.LEFT, 0, ConstraintSet.LEFT);
        anchorTexte.connect(bouton.getId(), ConstraintSet.RIGHT, 0, ConstraintSet.RIGHT);
        anchorTexte.connect(bouton.getId(), ConstraintSet.TOP, this.annonceFin.getId(), ConstraintSet.TOP, 164);

        anchorTexte.applyTo(layout);
    }

    public void tourSuivantNonEgalite() {
        this.layout.removeView(this.imageBot);
        this.layout.removeView(this.imageJoueur);

        if (this.CartesBot.size() == 0 || this.CartesJoueur.size() == 0){
            this.defaite();
        }

        else {
            this.pileJoueur.setClickable(true);
        }
    }

}
