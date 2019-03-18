package arenesolo;
/*
 * Code minimal pour crÃ©er son propre joueur.
 */


import jeu.Plateau;

public class MonJoueur extends jeu.Joueur {
    
    public MonJoueur(String nom) { super(nom); }
    
    @Override
    public Action faitUneAction(Plateau etatDuJeu) {
        return super.faitUneAction(etatDuJeu);
    }
}