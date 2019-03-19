package arenesolo;

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
	double distanceMin = 20;
	Point plusProche = new Point();

	public static int tailleMap = 0;
	public boolean init = false;
	public Point maYourte;
	public int nbChamps;
	public ArrayList<Point> mesChamps;
	public Point dest;
	public ArrayList<Node> chemin;

	public MonJoueur(String nom) {
		super(nom);
	}

	@Override
	public Action faitUneAction(Plateau etatDuJeu) {

		Point maPos = this.donnePosition();
		if (this.dest == null) {
			this.dest = maPos;
		}
		if (this.init == false) {
			this.tailleMap=etatDuJeu.donneTaille();
			this.maYourte = cherchePlusCourt(getListeYourtes(etatDuJeu),etatDuJeu);
			this.nbChamps = getNbChamps(etatDuJeu);
			this.mesChamps = ListeChampCoteYourte(this.maYourte, 1, etatDuJeu, this.nbChamps);
			this.init = true;
		}
		// Si on se trouve sur une Yourte
		if (Plateau.contientUneYourte(etatDuJeu.donneContenuCellule(maPos)) && this.donneVigueur() < 100) {
			this.dest = maPos;
		}
		else {
			
			ArrayList<Point> champsLibres = (ArrayList<Point>) this.mesChamps
					.stream().filter(champ -> !this
							.isMine(etatDuJeu.donneContenuCellule((int) champ.getX(), (int) champ.getY()), etatDuJeu))
					.collect(Collectors.toList());
			if (champsLibres.size() > 0) {
				this.dest = pointPlusProche(champsLibres, maPos, etatDuJeu);
				if (!(this.donneVigueur() > 20 + calculDistance(this.dest, maPos, etatDuJeu))) {
					if (isJoueurOnYourte(this.maYourte, etatDuJeu)) {
						System.out.println("1");
						this.dest = maPos;
					}
					else {
						System.out.println("2");
						this.dest = this.maYourte;
					}
				}	
			} 
			else {
				this.dest = this.getChampsPasAMoi(etatDuJeu, this.mesChamps);
				if (!(this.donneVigueur() > 20 + calculDistance(this.dest, maPos, etatDuJeu))) {
					if (isJoueurOnYourte(this.maYourte, etatDuJeu)) {
						System.out.println("3");
						this.dest = maPos;
					}
					else {
						System.out.println("4");
						this.dest = this.maYourte;
					}
				}
			}
			if(this.dest==maPos && maPos.getX()==this.maYourte.getX() && maPos.getY()==this.maYourte.getY()) {
				System.out.println("5");
				this.dest=vaSurCaseVide(maPos,etatDuJeu);
			}
			
		}
		//System.out.println("destination :"+this.dest);
		//System.out.println(" yourte :"+this.maYourte);
		this.chemin = etatDuJeu.donneCheminAvecObstaclesSupplementaires(maPos, this.dest, getJoueurs(etatDuJeu));

		Point nextCase = (this.chemin.size() > 0 ? getPointFromNode(this.chemin.get(0)) : maPos);
		return donneDirection(maPos, nextCase);
	}
	
	public Point vaSurCaseVide(Point maPose,Plateau plateau) {
		int x =(int) maPose.getX();
		int y =(int) maPose.getY();
		if(plateau.joueurPeutAllerIci(x+1, y, true, true)==true) {
			return new Point(x+1,y);
		}else if(plateau.joueurPeutAllerIci(x-1, y, true, true)==true) {
			return new Point(x-1,y);
		}else if(plateau.joueurPeutAllerIci(x, y+1, true, true)==true) {
			return new Point(x,y+1);
		}else if(plateau.joueurPeutAllerIci(x-1, y-1, true, true)==true) {
			return new Point(x,y-1);
		}else {
			return maPose;
		}
		
		
	}

	public java.util.ArrayList<java.awt.Point> verifNosChamps(ArrayList<Point> mesChamps, Plateau plateau) {
		Iterator<Point> iter = mesChamps.iterator();
		ArrayList<java.awt.Point> champsPlusANous = new ArrayList<java.awt.Point>();
		while (iter.hasNext()) {
			Point current = iter.next();
			boolean res = isMine(plateau.donneContenuCellule(current), plateau);
			if (res == false) {
				champsPlusANous.add(current);
			}
		}
		return champsPlusANous;
	}

	public static Joueur.Action donneDirection(Point depart, Point arrivee) {

		if ((depart != null) && (arrivee != null)) {
			if (depart.getX() < arrivee.getX()) {
				return Joueur.Action.DROITE;
			}
			if (depart.getX() > arrivee.getX()) {
				return Joueur.Action.GAUCHE;
			}
			if (depart.getY() < arrivee.getY()) {
				return Joueur.Action.BAS;
			}
			if (depart.getY() > arrivee.getY()) {
				return Joueur.Action.HAUT;
			}
			if ((depart.getY() == arrivee.getY()) && (depart.getX() == depart.getX())) {
				return Joueur.Action.RIEN;
			}
		}

		return null;

	}

	public boolean chercherJoueur(ArrayList<Node> chemin, Plateau plateau) {
		int taille = chemin.size();
		int enemie = 0;
		Point maPos = this.donnePosition();
		Iterator<Node> iter = chemin.iterator();
		while (iter.hasNext()) {
			Point current = getPointFromNode(iter.next());
			HashMap<Integer, ArrayList<Point>> ListJoueur = plateau.cherche(current, 1, Plateau.CHERCHE_JOUEUR);
			if (ListJoueur.get(4).size() != 0) {
				Iterator<Point> iter2 = ListJoueur.get(4).iterator();
				while (iter2.hasNext()) {
					Point current2 = iter2.next();
					if (current2.getX() != maPos.getX() && current2.getY() != maPos.getY()) {
						enemie++;
					}
				}
			}
		}
		if (enemie != 0) {
			return true;
		} else {
			return false;
		}
	}

	public static java.util.ArrayList<java.awt.Point> ListeChampCoteYourte(Point posYourt, int rayon, Plateau plateau,
			int nbMinChamps) {
		rayon = rayon;
		HashMap<Integer, ArrayList<Point>> ListChamps = plateau.cherche(posYourt, rayon, Plateau.CHERCHE_CHAMP);

		while (ListChamps.get(2).size() < nbMinChamps) {
			ListChamps = plateau.cherche(posYourt, rayon, Plateau.CHERCHE_CHAMP);
			rayon++;
		}
		return ListChamps.get(2);

	}

	public Point getChampsPasAMoi(Plateau plateau, ArrayList<Point> mesChamps) {
		Point maPos = this.donnePosition();
		ArrayList<Point> champs = getAllChamps(plateau);
		champs.removeIf(poin -> (mesChamps).contains(poin) || isMine(plateau.donneContenuCellule(poin), plateau));
		
		return pointPlusProche(champs, maPos, plateau);
	}

	public Point cherchePlusCourt(HashMap<java.lang.Integer, java.util.ArrayList<java.awt.Point>> listRecherche,
			Plateau plateau) {
		Point maPos = this.donnePosition();
		distanceMin = tailleMap;
		int numMoi = plateau.donneJoueurCourant();

		for (Map.Entry<java.lang.Integer, java.util.ArrayList<java.awt.Point>> entry : listRecherche.entrySet()) {

			ArrayList<Point> points = entry.getValue();
			points.forEach((point) -> {
				if (Plateau.donneProprietaireDeLObjet(plateau.donneContenuCellule(point)) != numMoi + 1) {
					double distance = calculDistance(point, maPos, plateau);
					if (distance < distanceMin  && 
						distance > 0 &&
						!chercherJoueur(plateau.donneCheminAvecObstaclesSupplementaires(maPos, point, getJoueurs(plateau)), plateau)) {
						distanceMin = distance;
						plusProche = point;
					}
				}
			});
		}
		return plusProche;
	}

	public static double calculDistance(Point poin, Point posActu, Plateau plateau) {
		ArrayList<Node> monArray = plateau.donneCheminAvecObstaclesSupplementaires(posActu, poin, getJoueurs(plateau));
		if(monArray != null && monArray.size() > 0) {
			return monArray.size();
		}
		else {
			return 0;
		}
		

	}

	public static HashMap<Integer, ArrayList<Point>> getListeYourtes(Plateau plateau) {
		return plateau.cherche(new Point(5, 5), tailleMap, Plateau.CHERCHE_YOURTE);
	}

	public static ArrayList<Point> getChampsProches(Plateau plateau, Point point) {
		ArrayList<Point> premiereListe = plateau.cherche(point, 5, Plateau.CHERCHE_CHAMP).get(2);
		System.out.println("Premiere Liste " + premiereListe.toString());
		ArrayList<Point> listeFinale = new ArrayList<Point>();
		premiereListe.forEach((champ) -> {
			if ((calculDistance(point, champ, plateau) <= 10) && (calculDistance(point, champ, plateau) > 0)) {
				listeFinale.add(champ);
			}
		});
		return listeFinale;
	}

	public static ArrayList<Point> getAllChamps(Plateau plateau) {
		return plateau.cherche(new Point(0, 0), tailleMap, Plateau.CHERCHE_CHAMP).get(2);
	}

	public static int getNbChamps(Plateau plateau) {
		return getAllChamps(plateau).size() / 4;
	}

	public boolean isMine(int cellule, Plateau plateau) {

		return (Plateau.donneProprietaireDeLObjet(cellule) == plateau.donneJoueurCourant() + 1);
	}

	public static Point pointPlusProche(ArrayList<Point> liste, Point point, Plateau plateau) {
		Iterator<Point> iter = liste.iterator();
		double min = tailleMap;
		Point pointMin = null;
		while (iter.hasNext()) {
			Point current = iter.next();
			double dist = calculDistance(current, point, plateau);
			if (dist < min && dist > 0) {
				min = dist;
				pointMin = current;
			}
		}
		//System.out.println("Point plus proche" + liste.toString());
		if(pointMin == null) {
			return point;
		}
		else {
			return pointMin;
		}
	}

	public static Point getPointFromNode(Node noeud) {
		return new Point(noeud.getPosX(), noeud.getPosY());
	}

	public static boolean isJoueurOnYourte(Point yourte, Plateau plateau) {
		return ((plateau.donneContenuCellule(yourte) & Plateau.MASQUE_PRESENCE_JOUEUR) != 0);
	}
	
	public static ArrayList<Node> getJoueurs(Plateau plateau) {
		ArrayList<Point >joueursPoints = plateau.cherche(new Point(1, 1), tailleMap, Plateau.CHERCHE_JOUEUR).get(4);
		ArrayList<Node> joueursNode = new ArrayList<Node>();
		joueursPoints.forEach((joueur) -> { joueursNode.add(new Node((int)joueur.getX(), (int)joueur.getY()));
		});
		
		return joueursNode;
	}

}