package cmi.carte_hage.blackjack;

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
import java.util.List;
import java.util.Random;

import cmi.carte_hage.R;

public class Blackjack extends AppCompatActivity {
    // Liste d'objets imageView qui stocke toutes les cartes à afficher / affichés et sert de main

    private List<ImageView> mainCroupier = new ArrayList<ImageView>();
    private List<ImageView> mainJoueur = new ArrayList<ImageView>();

    // Générateur de nomrbe aléatoire, initialisé ici par souci de pratique
    private Random generateurCarte = new Random();
    // Stockage du layout afin de faciliter l'accès à, et réduire la taille des expressions
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.blackjack);
        layout = findViewById(R.id.blackjackLayout);

        this.start();
    }

    // Lorsque une animation est fini, elle peut (si paramétrée pour) appeler cette fonction
    private Animator.AnimatorListener suite_demarrage_blackjack = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {}
        @Override
        public void onAnimationCancel(Animator animator) {}
        @Override
        public void onAnimationRepeat(Animator animator) {}

        @Override
        public void onAnimationEnd(Animator animator) {
            // Puisque nous avons fini toutes les animations, il faut redonner la possibilité de jouer
            (findViewById(R.id.hitButton)).setClickable(true);
            (findViewById(R.id.foldButton)).setClickable(true);
        }
    };

    private void start(){
        /*
        * Fonction servant de point d'entrée pour le jeu, initialise les variables nécessaires,
        * S'assure que tout soit prêt à démarrer, avant de laisser le joueur prendre la main
        */

        // Au cas où il ne s'agit pas de la première partie, il faut nettoyer l'écran
        // Sale manière de faire, puisque nous abusons le fait que lors de la première partie,
        // mainCroupier et mainJoueur sont initialisé à des listes vides
        for (int i = 0; i < this.mainCroupier.size(); i++){
            layout.removeView(this.mainCroupier.get(i));
        }

        for (int i = 0; i < this.mainJoueur.size(); i++){
            layout.removeView(this.mainJoueur.get(i));
        }

        // Au cas où des cartes s'y trouvent, le nettoyage étant fini, on réinitialise les listes
        mainCroupier = new ArrayList<ImageView>();
        mainJoueur  = new ArrayList<ImageView>();

        // Distribution et affichage des cartes initiales
        this.donnerCarte(true, 2);
        this.donnerCarte(false, 2);

        this.afficherCartes(true);
        this.afficherCartes(false);

        // On les déplace hors de l'écran afin de pouvoir faire une animation,servant de transition
        (this.mainCroupier.get(0)).setTranslationY(-500.F);
        (this.mainCroupier.get(1)).setTranslationY(-500.F);

        (this.mainJoueur.get(0)).setTranslationY(500.F);
        (this.mainJoueur.get(1)).setTranslationY(500.F);

        // Initialisation et démarrage des animations, ainsi que le paramétrage de la fonction à
        // Appeler lorsque l'animation sera fini
        ObjectAnimator animationPremiereCaCroupier = ObjectAnimator.ofFloat(this.mainCroupier.get(0), "TranslationY", 0.F).setDuration(1000);
        animationPremiereCaCroupier.addListener(suite_demarrage_blackjack);
        animationPremiereCaCroupier.start();

        ObjectAnimator.ofFloat(this.mainCroupier.get(1), "TranslationY", 0.F).setDuration(1000).start();
        ObjectAnimator.ofFloat(this.mainJoueur.get(0), "TranslationY", 0.F).setDuration(1000).start();
        ObjectAnimator.ofFloat(this.mainJoueur.get(1), "TranslationY", 0.F).setDuration(1000).start();
    }

    public void hitButton(View view){
        /*
            Méthode appelée lorsque le bouton 'HIT' est appuyé, gère la distribution de la carte
            Ainsi que l'animation pour les introduire
         */

        // On empêche le joueur de faire d'autre actions le temps de l'animation
        view.setClickable(false);
        findViewById(R.id.foldButton).setClickable(false);


        // Intialisation du code à exécuter lorsque l'animation sera terminée
        Animator.AnimatorListener final_animation_hit = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                // L'animation étant fini, on laisse le joueur jouer de nouveau
                layout.findViewById(R.id.hitButton).setClickable(true);
                layout.findViewById(R.id.foldButton).setClickable(true);

                if (calculScore(false) > 21){
                    foldButton(null);
                }
            }
        };

        Animator.AnimatorListener suite_animation_hit = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                // Maintenant hors de l'écran, on distribue la nouvelle carte
                donnerCarte(false);
                afficherCartes(false);

                mainJoueur.get(0).setTranslationY(500.F);

                ObjectAnimator animationCarteJoueur = ObjectAnimator.ofFloat(mainJoueur.get(0), "TranslationY", 0.F).setDuration(1000);
                animationCarteJoueur.addListener(final_animation_hit);
                animationCarteJoueur.start();

                for (int i = 1; i < mainJoueur.size(); i++){
                    mainJoueur.get(i).setTranslationY(500.F);
                    ObjectAnimator.ofFloat(mainJoueur.get(i), "TranslationY", 0.F).setDuration(1000).start();
                }


            }
        };

        // Animation de l'envoie des cartes hors de l'écran
        ObjectAnimator animationCarteJoueur = ObjectAnimator.ofFloat(this.mainJoueur.get(0), "TranslationY", 500.F).setDuration(1000);
        animationCarteJoueur.addListener(suite_animation_hit);
        animationCarteJoueur.start();

        for (int i = 1; i < this.mainJoueur.size(); i++){
            ObjectAnimator.ofFloat(this.mainJoueur.get(i), "TranslationY", 500.F).setDuration(1000).start();
        }
    }

    private Integer calculScore(boolean Croupier){
        /*
        Méthode servant à calculer le score total d'une main
         */
        List<ImageView> mainActuelle;

        if (! Croupier) {
            mainActuelle = this.mainJoueur;
        }
        else {
            mainActuelle = this.mainCroupier;
        }

        Integer Somme = 0;

        for (int i = 0; i < mainActuelle.size(); i++){
            // Puisque toute carte correspond à un chiffre entre 0 et 51, on récupère le type en
            // Faisant + 1 % 13, et la valeur avec % 13 + 1
            // 1 - 52  aurait été mieux.
            Integer valeurCarte = ((Integer) mainActuelle.get(i).getTag()) % 13 + 1;

            // Les têtes valent toutes 10
            if (valeurCarte > 10){
                valeurCarte = 10;
            }

            Somme += valeurCarte;
        }

        return Somme;
    }

    private void piocherCroupier(){
        /*
        Méthode animant la distribution de carte pour le croupier, ainsi que leur distribution
         */
        Animator.AnimatorListener fin_animation_fold = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                foldButton(null);
            }
        };

        Animator.AnimatorListener suite_animation_fold = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                donnerCarte(true);
                afficherCartes(true);

                int rIdJoueur = (getResources()).getIdentifier("c" + String.valueOf(mainCroupier.get(0).getTag()), "drawable", getPackageName());
                mainCroupier.get(0).setImageResource(rIdJoueur);

                for (int i = 0; i < mainCroupier.size(); i++){
                    mainCroupier.get(i).setTranslationY(-500.F);
                }

                ObjectAnimator animation_carte_croupier = ObjectAnimator.ofFloat(mainCroupier.get(0), "TranslationY", 0.F).setDuration(1000);
                animation_carte_croupier.addListener(fin_animation_fold);
                animation_carte_croupier.start();


                for (int i = 1; i < mainCroupier.size(); i++){
                    ObjectAnimator.ofFloat(mainCroupier.get(i), "TranslationY", 0.F).setDuration(1000).start();
                }

            }
        };

        ObjectAnimator animation_carte_croupier = ObjectAnimator.ofFloat(mainCroupier.get(0), "TranslationY", -500.F).setDuration(1000);
        animation_carte_croupier.addListener(suite_animation_fold);
        animation_carte_croupier.start();


        for (int i = 1; i < this.mainCroupier.size(); i++){
            ObjectAnimator.ofFloat(mainCroupier.get(i), "TranslationY", -500.F).setDuration(1000).start();
        }
    }

    public void foldButton(View view){
        /*
        Méthode appelé pour terminer la partie, soit à l'initiative du joueur, soit à seule du jeu
        En raison d'un score du joueur dépassant 21
         */

        // Si le score du croupier est inférieur ou égal à 21,
        // Et que le score de la main est inférieure à seule du joueur, alors le croupier pioche
        // Bien sur si le score du joueur dépasse 21, alors le croupier ne pioche pas
        if ((this.calculScore(true) < 22 && this.calculScore(true) < this.calculScore(false)) && this.calculScore(false) < 21){
            piocherCroupier();
            return;
        }

        // Juste au cas où le croupier gagne sans piocher, on modifie l'image (sans animation)
        // De la carte face caché afin de donner au joueur le contexte complet du jeu
        int rIdJoueur = (getResources()).getIdentifier("c" + String.valueOf(mainCroupier.get(0).getTag()), "drawable", getPackageName());
        mainCroupier.get(0).setImageResource(rIdJoueur);

        // Initialisation et affichage du message de fin, et d'un bouton pour recommencer
        TextView messageFin = new TextView(this); messageFin.setId(View.generateViewId());
        messageFin.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        messageFin.setTextSize(34);
        Button boutonRecommencer = new Button(this); boutonRecommencer.setId(View.generateViewId());

        boutonRecommencer.setText("Recommencer ?");

        // Si et le croupier, et le joueur dépasse 21, le joueur perd (et non égalité donc)
        // Sinon si les scores sont les memes, il y a égalité
        // Autrement, le joueur gagne
        if ((this.calculScore(false) < this.calculScore(true) && this.calculScore(true) < 22) || this.calculScore(false) > 21){
            messageFin.setText("Défaite !");
        }

        else if (this.calculScore(false).equals(this.calculScore(true))){
            messageFin.setText("Egalité !");
        }

        else {
            messageFin.setText("Victoire !");
        }

        // Affichage des objets
        layout.addView(messageFin);
        layout.addView(boutonRecommencer);

        ConstraintSet anchors = new ConstraintSet();
        anchors.clone(this.layout);

        anchors.connect(messageFin.getId(), ConstraintSet.LEFT, 0, ConstraintSet.LEFT);
        anchors.connect(messageFin.getId(), ConstraintSet.RIGHT, 0, ConstraintSet.RIGHT);
        anchors.connect(messageFin.getId(), ConstraintSet.TOP, 0, ConstraintSet.TOP, 256);
        anchors.connect(messageFin.getId(), ConstraintSet.BOTTOM, R.id.hitButton, ConstraintSet.TOP);

        anchors.connect(boutonRecommencer.getId(), ConstraintSet.LEFT, 0, ConstraintSet.LEFT);
        anchors.connect(boutonRecommencer.getId(), ConstraintSet.RIGHT, 0, ConstraintSet.RIGHT);
        anchors.connect(boutonRecommencer.getId(), ConstraintSet.BOTTOM, 0, ConstraintSet.BOTTOM, 256);
        anchors.connect(boutonRecommencer.getId(), ConstraintSet.TOP, R.id.hitButton, ConstraintSet.BOTTOM);

        anchors.applyTo(layout);

        findViewById(R.id.hitButton).setClickable(false);
        findViewById(R.id.foldButton).setClickable(false);

        boutonRecommencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Nettoyage rapide de l'écran, et début d'un nouveau cycle
                // On ne réinitialise rien, il est de la fonction start de s'assurer du bon
                // Fonctionnement
                layout.removeView(boutonRecommencer);
                layout.removeView(messageFin);
                start();
            }
        });
    }

    private Boolean carteDejaDistribuee(Integer valeur){
        // Afin d'éviter d'avoir un joueur avec 21 As, on s'assure que la carte n'a pas encore été
        // Distribuée
        for (int i = 0; i < this.mainJoueur.size(); i++){
            if (valeur.equals(this.mainJoueur.get(i).getTag())){
                return true;
            }
        }

        for (int i = 0; i < this.mainCroupier.size(); i++){
            if (valeur.equals(this.mainCroupier.get(i).getTag())){
                return true;
            }
        }

        return false;
    }

    private void donnerCarte(Boolean Croupier, Integer montant){
        /* Méthode qui surcharge 'donnerCarte' afin de simplifier la distribution de plus d'une
           Carte à la fois (lors de la distribution initiale)
        */

        for (int i = 0; i < montant; i++){
            this.donnerCarte(Croupier);
        }
    }
    
    private void donnerCarte(Boolean Croupier){
        /*
        Méthode distribuant une carte
        Intialise également tout les paramètres de la carte, pour son bon affichage
         */
        Integer chiffreChoisi;

        do {
            chiffreChoisi = this.generateurCarte.nextInt(52);
        } while (this.carteDejaDistribuee(chiffreChoisi));

        ImageView nouvelleCarte = new ImageView(this);
        nouvelleCarte.setId(View.generateViewId());
        nouvelleCarte.setTag(chiffreChoisi);
        nouvelleCarte.setScaleX(2.F); nouvelleCarte.setScaleY(2.F);

        if (Croupier && this.mainCroupier.size() == 0){
            nouvelleCarte.setImageResource(R.drawable.dos_carte);
        }
        else {
            int rIdJoueur = (this.getResources()).getIdentifier("c" + String.valueOf(nouvelleCarte.getTag()), "drawable", this.getPackageName());
            nouvelleCarte.setImageResource(rIdJoueur);
        }

        if (! Croupier){
            this.mainJoueur.add(nouvelleCarte);
        }
        else {
            this.mainCroupier.add(nouvelleCarte);
        }
    }

    private void afficherCartes(Boolean Croupier){
        /*
        Méthode servant à afficher les cartes d'une main, fait pour simplifier le placement des
        Cartes
         */
        List<ImageView> mainActuelle;

        if (! Croupier) {
            mainActuelle = this.mainJoueur;
        }
        else {
            mainActuelle = this.mainCroupier;
        }

        for (int i = 0; i < mainActuelle.size(); i++){
            this.layout.removeView(mainActuelle.get(i));
        }

        for (int i = 0; i < mainActuelle.size(); i++){
            ImageView imageCarte = mainActuelle.get(i);

            this.layout.addView(imageCarte);

            // Au delà de 5 cartes, il devient difficile de lire les cartes
            if (mainActuelle.size() >= 5){
                imageCarte.setScaleX(1.5F);
                imageCarte.setScaleY(1.5F);
            }
        }

        // Code long servant qu'à positionner les cartes, soit en bas, soit en haut,
        // Et de partager l'espace horizontal équitablement avec les autres cartes
        ConstraintSet anchors = new ConstraintSet();
        anchors.clone(this.layout);

        // La première carte est spéciale, puisque son coté gauche est connecté pas à une carte mais
        // Au coté de l'écran
        if (! Croupier) {
            anchors.connect(mainActuelle.get(0).getId(), ConstraintSet.BOTTOM, 0, ConstraintSet.BOTTOM, 168);
        }
        else {
            anchors.connect(mainActuelle.get(0).getId(), ConstraintSet.TOP, 0, ConstraintSet.TOP, 168);
        }

        anchors.connect(mainActuelle.get(0).getId(), ConstraintSet.LEFT, 0, ConstraintSet.LEFT);
        anchors.connect(mainActuelle.get(0).getId(), ConstraintSet.RIGHT, mainActuelle.get(1).getId(), ConstraintSet.LEFT);

        for (int i = 1; i < mainActuelle.size() - 1; i++){
            ImageView imageCarte = mainActuelle.get(i);
            ImageView imageCartePrecedante = mainActuelle.get(i - 1);
            ImageView imageCarteSuivante = mainActuelle.get(i + 1);

            if (! Croupier) {
                anchors.connect(imageCarte.getId(), ConstraintSet.BOTTOM, 0, ConstraintSet.BOTTOM, 168);
            }
            else {
                anchors.connect(imageCarte.getId(), ConstraintSet.TOP, 0, ConstraintSet.TOP, 168);
            }
            anchors.connect(imageCarte.getId(), ConstraintSet.LEFT, imageCartePrecedante.getId(), ConstraintSet.RIGHT);
            anchors.connect(imageCarte.getId(), ConstraintSet.RIGHT, imageCarteSuivante.getId(), ConstraintSet.LEFT);
        }


        // La dernière carte est également spéciale, puisque son coté droit est connecté au coté
        // Droit de l'écran
        if (!Croupier) {
            anchors.connect(mainActuelle.get(mainActuelle.size() - 1).getId(), ConstraintSet.BOTTOM, 0, ConstraintSet.BOTTOM, 168);
        }
        else {
            anchors.connect(mainActuelle.get(mainActuelle.size() - 1).getId(), ConstraintSet.TOP, 0, ConstraintSet.TOP, 168);
        }

        anchors.connect(mainActuelle.get(mainActuelle.size() - 1).getId(), ConstraintSet.RIGHT, 0, ConstraintSet.RIGHT);
        anchors.connect(mainActuelle.get(mainActuelle.size() - 1).getId(), ConstraintSet.LEFT, mainActuelle.get(mainActuelle.size() - 2).getId(), ConstraintSet.RIGHT);

        anchors.applyTo(this.layout);
    }

}
