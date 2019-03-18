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
	public MonJoueur(String nom) {super(nom);}
    
	
	
    @Override
    public Action faitUneAction(Plateau etatDuJeu) {
        
    	return Action.BAS;
    }

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
    public Point cherchePlusCourt(HashMap<java.lang.Integer,java.util.ArrayList<java.awt.Point>> listRecherche) {
    	Point maPos=this.donnePosition();
    	distanceMin=20;
    	
    	for(Map.Entry<java.lang.Integer,java.util.ArrayList<java.awt.Point>> entry : listRecherche.entrySet()) {
    	   
    	    ArrayList<Point> value = entry.getValue();
    	    value.forEach((n)->{
    	    	double distance=calculDistance(n,maPos);
    	    	if(distance<distanceMin) {
    	    		distanceMin=distance;
    	    		plusProche=n;
    	    	}
    	    });  
    	}
    	
    	return plusProche;
    }
    
    public double calculDistance(Point poin ,Point posActu) {
    	double x=Math.abs(posActu.getX())-Math.abs(poin.getX());
    	double y=Math.abs(posActu.getY())-Math.abs(poin.getY());
    	double dist=Math.abs(x)+Math.abs(y);
		return dist;
    
    }
}