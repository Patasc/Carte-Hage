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
    // Liste stockant des entiers correspondant aux cartes du joueur
    private List<Integer> CartesJoueur;
    private List<Integer> CartesBot;

    // Objets contenant les messages et cartes affichés à l'écran, afin d'en simplifier
    // L'utilisation et la modification de
    private TextView scoreJoueur;
    private TextView scoreBot;
    private TextView annonceFin;

    private ImageView imageJoueur;
    private ImageView imageBot;

    private ConstraintLayout layout;
    private ImageView pileJoueur;

    private Integer cartesEnStock = 0;

    private ArrayList<Integer> defausse = new ArrayList<Integer>();


    // Implémentations des interfaces AnimatorListener afin d'appeler des fonctions lorsqu'une
    // Animation se termine
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
        setContentView(R.layout.bataille);

        // Initialisation des variables et appel de la fonction démarrage, afin de débuter le jeu
        scoreJoueur = findViewById(R.id.scoreJoueur);
        scoreBot = findViewById(R.id.scoreBot);

        layout = findViewById(R.id.batailleLayout);
        pileJoueur = findViewById(R.id.pileJoueur);
        this.demarrage();

    }

    public void distribution_cartes() {
        /*
        Méthode distribuant à chaque joueur 26 cartes aléatoires
         */
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
                // Affichage de la taille de la main du joueur
                this.CartesJoueur.add(store.get(i));
                this.scoreJoueur.setText(String.valueOf(this.CartesJoueur.size()));
            } else {
                // Affichage de la taille de la main du bot adversaire
                this.CartesBot.add(store.get(i));
                this.scoreBot.setText(String.valueOf(this.CartesBot.size()));
            }
        }
    }


    public void demarrage() {
        /*
        En début de partie cette fonction est appelée, elle permet au jeu de commencer et
        Distribue les cartes
         */
        this.distribution_cartes();
        this.pileJoueur.setClickable(true);
    }

    public void suite_tour() {
        /*
        Méthode servant à poursuivre une animation
         */
        ObjectAnimator testing = ObjectAnimator.ofFloat(this.imageJoueur, "TranslationY", -600.F).setDuration(2000);
        testing.addListener(this.VerificationGagnant);
        testing.start();

        ObjectAnimator.ofFloat(this.imageBot, "TranslationY", 600.F).setDuration(2000).start();
    }

    public void fin_anim_egalite(){
        /*
        Une fois les animations de suite_anim_egalite, cette fonction est appelée afin de permettre
        Au tour suivant d'avoir lieu
         */
        this.tourSuivantNonEgalite();
    }

    public void suite_anim_egalite(){
        /*
        Méthode assurant la suite de l'animation, une fois que celles de cas_egalite aient finis
         */
        ObjectAnimator testing = ObjectAnimator.ofFloat(this.imageJoueur, "TranslationX", -1000.F).setDuration(800);
        testing.addListener(TourSuivantEgalite2);
        testing.start();

        ObjectAnimator.ofFloat(this.imageBot, "TranslationX", 1000.F).setDuration(800).start();
    }

    public void cas_egalite(){
        /*
        Méthode affichant et animant les cartes
         */
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
        /*
        Méthode servant à piocher une carte dans les mains de chaque joueur, et affiche les
        Animations pour
         */
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
        /*
        Lorsque le joueur appuie sur la pile pour jouer, cette fonction l'empêche d'appuyer de
        Nouveau et appelle la fonction tour() qui assure les animations du tour
         */
        this.pileJoueur.setClickable(false);
        this.tour();
    }

    public void verificationGagnantTour() {
        /*
        Méthode servant à vérifier le gagnant du jeu (ou s'il y a eu une égalité) et affiche les
        Informations relatives à la fin de partie, et propose au joueur de commencer une nouvelle
        Partie
         */
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

            // Pour que ce soit carte gagant puis perdant dans pile
            for (int i = 0; i < this.defausse.size(); i += 2) {
                if (i + 1 < this.defausse.size()) this.CartesJoueur.add(this.defausse.get(i + 1));
                this.CartesJoueur.add(this.defausse.get(i));
            }

            this.defausse.clear();
            this.cartesEnStock = 0;
        }
    }

    public void defaite(){
        /*
        Méthode servant à, dans le cas d'une défaite, afficher le résultat de la partie et
        Permet au joueur de recommencer, s'il le souhaite
         */
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
        /*
            Cette méthode nettoie l'écran, puis vérifie si un des joueurs a perdu, et si oui
            Arrête le jeu, sinon permet au joueur de passer au tour suivant
         */
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
