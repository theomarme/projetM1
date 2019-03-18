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
	
	public static int tailleMap = 100;
	public static boolean init = false;
	
	public MonJoueur(String nom) {super(nom);}
    
	
	
    @Override
    public Action faitUneAction(Plateau etatDuJeu) {
        System.out.println(getListeYourtes(etatDuJeu).toString());
     
        System.out.println(getNbChamps(etatDuJeu));
        
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
    
    public static double calculDistance(Point poin ,Point posActu,Plateau plateau) {
    	ArrayList<Node> monArray=plateau.donneCheminEntre(posActu,poin);
		return monArray.size();
    
    }
    
    public static ArrayList<Point> getListeYourtes(Plateau plateau) {
    	return plateau.cherche(new Point(5,5), 100, Plateau.CHERCHE_YOURTE).get(1);
    }
    
    public static ArrayList<Point> getChampsProches(Plateau plateau, Point point){
    	ArrayList<Point> premiereListe =  plateau.cherche(point, 5,Plateau.CHERCHE_CHAMP).get(2);
    	System.out.println("Premiere Liste " + premiereListe.toString());
    	ArrayList<Point> listeFinale = new ArrayList<Point>();
    	premiereListe.forEach(
    			(champ)->{
    				if(calculDistance(point, champ, plateau) <= 10) {
    					listeFinale.add(champ);
    				}
    	});
    	return listeFinale;
    }
    
   
   
    public static ArrayList<Point> getAllChamps(Plateau plateau){
    	return plateau.cherche(new Point(0,0), tailleMap, Plateau.CHERCHE_CHAMP).get(2);
    }
    
    public static int getNbChamps(Plateau plateau) {
    	return getAllChamps(plateau).size() / 4;
    }
   
}