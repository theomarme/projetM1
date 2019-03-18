package arenesolo;
import java.awt.Point;
/*
 * Code minimal pour crÃ©er son propre joueur.
 */
import java.util.ArrayList;

import jeu.Joueur;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jeu.Joueur;
import jeu.Plateau;
import jeu.astar.Node;

public class MonJoueur extends jeu.Joueur {
	double distanceMin=20;
	Point plusProche=new Point();
	public MonJoueur(String nom) {super(nom);
	}
    
	
	
    @Override
    public Action faitUneAction(Plateau etatDuJeu) {
        
    	Point maPos=this.donnePosition();
    	cherchePlusCourt(etatDuJeu.cherche(maPos, 10,Plateau.CHERCHE_CHAMP),etatDuJeu);
    	
    	return Action.BAS;
    }

    public static Joueur.Action donneDirection(Point depart, Point arrivee) {
    	if(depart.getX() < arrivee.getX()) {
    		return Joueur.Action.DROITE;
    	}
    	if(depart.getX() > arrivee.getX()) {
    		return Joueur.Action.GAUCHE;
    	}
    	if(depart.getY() < arrivee.getY()) {
    		return Joueur.Action.BAS;
    	}
    	if(depart.getY() > arrivee.getY()) {
    		return Joueur.Action.HAUT;
    	}
    	
    	return null;
    }
    
    public java.util.ArrayList<java.awt.Point> ListeChampCoteYourte(Point posYourt,int rayon,Plateau plateau,int nbMinChamps){
    	HashMap<Integer, ArrayList<Point>> ListChamps=plateau.cherche(posYourt, rayon, Plateau.CHERCHE_CHAMP);
    	
    	if(ListChamps.get(2).size()<nbMinChamps) {
    		
    		ListeChampCoteYourte(posYourt,rayon+1,plateau,nbMinChamps);
    	}
    	return ListChamps.get(2);
    	
    }
    
    public Point cherchePlusCourt(HashMap<java.lang.Integer,java.util.ArrayList<java.awt.Point>> listRecherche,Plateau plateau) {
    	Point maPos=this.donnePosition();
    	distanceMin=10;
    	int numMoi=plateau.donneJoueurCourant();
    	
    	for(Map.Entry<java.lang.Integer,java.util.ArrayList<java.awt.Point>> entry : listRecherche.entrySet()) {
    	   
    	    ArrayList<Point> value = entry.getValue();
    	    value.forEach((n)->{
    	    	if(Plateau.donneProprietaireDuSite(plateau.donneContenuCellule(n))!= numMoi+1) {
    	    		double distance=calculDistance(n,maPos,plateau);
	    	    	if(distance<distanceMin) {
		    	    	distanceMin=distance;
		    	    	plusProche=n;
		    	    }
    	    	}

    	    });
    	}
    	return plusProche;
    }
    
    public double calculDistance(Point poin ,Point posActu,Plateau plateau) {
    	ArrayList<Node> monArray=plateau.donneCheminEntre(posActu,poin);
		return monArray.size();
    
    }
}