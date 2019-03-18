package arenesolo;
import java.awt.Point;
/*
 * Code minimal pour crÃ©er son propre joueur.
 */
import java.util.ArrayList;

import jeu.Joueur;
import jeu.Plateau;
import jeu.astar.Node;

public class MonJoueur extends jeu.Joueur {
    
    public MonJoueur(String nom) { super(nom); }
    
    
    @Override
    public Action faitUneAction(Plateau etatDuJeu) {
    
    	Point maPos = this.donnePosition();
    	ResultCherche res = new ResultCherche(etatDuJeu.cherche(maPos, 10, Plateau.CHERCHE_CHAMP));
    	
    	System.out.println(maPos.toString());
    	
    	Point point = res.map.get(2).get(0);
    	ArrayList<Node> chemin = etatDuJeu.donneCheminEntre(maPos, point);
    	
    	Point prochaineCase = new Point(chemin.get(0).getPosX(), chemin.get(0).getPosY());
    	
    	
    	return MonJoueur.donneDirection(maPos, prochaineCase);
//    	System.out.println("Mon point " + maPos);
//    	System.out.println(chemin.toString());
    	
    	
      
    }
    
//    public boolean assezDeVigueur() {
//    	if(this.donneVigueur() < 20)
//    }
    
    public static Joueur.Action donneDirection(Point depart, Point arrivée) {
    	if(depart.getX() < arrivée.getX()) {
    		return Joueur.Action.DROITE;
    	}
    	if(depart.getX() > arrivée.getX()) {
    		return Joueur.Action.GAUCHE;
    	}
    	if(depart.getY() < arrivée.getY()) {
    		return Joueur.Action.BAS;
    	}
    	if(depart.getY() > arrivée.getY()) {
    		return Joueur.Action.HAUT;
    	}
    	
    	return null;
    }
}