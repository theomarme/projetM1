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
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import jeu.Joueur;
import jeu.Plateau;
import jeu.astar.Node;

public class MonJoueur extends jeu.Joueur {
	double distanceMin=20;
	Point plusProche=new Point();
	
	public static int tailleMap = 100;
	public static boolean init = false;
	public Point maYourte;
	public int nbChamps;
	public ArrayList<Point> mesChamps;
	public Point dest;
	public ArrayList<Node> chemin;
	
	public MonJoueur(String nom) {super(nom);}
    
	
	
    @Override
    public Action faitUneAction(Plateau etatDuJeu) {
        
    	Point maPos=this.donnePosition();
    	this.dest = maPos;
    	if(this.donneVigueur() > (20 + calculDistance(this.dest, maPos, etatDuJeu))) {
	    	this.maYourte = cherchePlusCourt(etatDuJeu.cherche(maPos, 10,Plateau.CHERCHE_YOURTE),etatDuJeu);
	    	this.nbChamps = getNbChamps(etatDuJeu);
	    	this.mesChamps = ListeChampCoteYourte(this.maYourte, 1, etatDuJeu, this.nbChamps);
	    	
	    	ArrayList<Point> champsLibres = (ArrayList<Point>) this.mesChamps.stream().filter(
	    			champ -> !isMine(etatDuJeu.donneContenuCellule((int)champ.getX(), (int)champ.getY()), etatDuJeu)).collect(Collectors.toList());
	    	System.out.println(this.dest);
	    	this.dest = pointPlusProche(champsLibres, maPos, etatDuJeu);
	    	this.chemin = etatDuJeu.donneCheminEntre(maPos, this.dest);
    	}
    	else {
    		this.dest = this.maYourte;
    		this.chemin = etatDuJeu.donneCheminEntre(maPos, this.dest);
    	}
    
    		return donneDirection(maPos, getPointFromNode(this.chemin.get(0)));
    	
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
    	if((depart.getY() == arrivee.getY()) &&  (depart.getX() == depart.getX())) {
    		return Joueur.Action.RIEN;
    	}
    	
    	return null;
    }
    
    public boolean chercherJoueur(ArrayList<Node> chemin,	Plateau plateau) {
    	int taille = chemin.size();
    	int enemie=0;
    	Point maPos=this.donnePosition();
    	Iterator<Node> iter = chemin.iterator();
    	while(iter.hasNext()) {
    		Point current = getPointFromNode(iter.next());
    		HashMap<Integer, ArrayList<Point>> ListJoueur=plateau.cherche(current, 1, Plateau.CHERCHE_JOUEUR);
    		if(ListJoueur.get(4).size()!=0) {
    			Iterator<Point> iter2 = ListJoueur.get(4).iterator();
    			while(iter2.hasNext()) {
    				Point current2 = iter2.next();
    				if(current2.getX()!=maPos.getX() && current2.getY()!=maPos.getY()) {	
	    				enemie++;
	    			}
    			}
    		}
    	}
    	if(enemie!=0) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    
    public static java.util.ArrayList<java.awt.Point> ListeChampCoteYourte(Point posYourt,int rayon,Plateau plateau,int nbMinChamps){
    	rayon = rayon;
    	HashMap<Integer, ArrayList<Point>> ListChamps = plateau.cherche(posYourt, rayon, Plateau.CHERCHE_CHAMP);
    	
    	while(ListChamps.get(2).size()<nbMinChamps) {
  
    		ListChamps = plateau.cherche(posYourt, rayon, Plateau.CHERCHE_CHAMP);
    		rayon ++;
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
    public boolean isMine(int cellule, Plateau plateau) {
    	
    	return (Plateau.donneProprietaireDuSite(cellule) != plateau.donneJoueurCourant());
    }
    
    public static Point pointPlusProche(ArrayList<Point> liste, Point point, Plateau plateau) {
    	Iterator<Point> iter = liste.iterator();
    	double min = tailleMap;
    	Point pointMin = null;
    	while(iter.hasNext()) {
    		Point current = iter.next();
    		double dist = calculDistance(current, point, plateau);
    		if(dist < min) {
    			min = dist;
    			pointMin = current;
    		}
    	}
    	return pointMin;
    }
    
    public static Point getPointFromNode(Node noeud) {
    	return new Point(noeud.getPosX(), noeud.getPosY());
    }
   
   
}