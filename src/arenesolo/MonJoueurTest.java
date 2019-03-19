package arenesolo;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;



import org.junit.jupiter.api.Test;

import jeu.Joueur.Action;
import jeu.astar.Node;

class MonJoueurTest {

	@Test
	void testDonneDirection() {
		Point p1 = new Point(6,5);
		Point p2 = new Point(5,5);
		Point p3 = new Point(6,4);
		assertEquals(Action.GAUCHE, MonJoueur.donneDirection(p1, p2));
		assertEquals(Action.DROITE, MonJoueur.donneDirection(p2, p1));
		assertEquals(Action.HAUT, MonJoueur.donneDirection(p1, p3));
		assertEquals(Action.BAS, MonJoueur.donneDirection(p3, p1));
		assertEquals(Action.RIEN, MonJoueur.donneDirection(p1, p1));
		assertEquals(null, MonJoueur.donneDirection(p1, null));
	}
	
	@Test
	void testgetPointFromNode() {
		int x=10;
		int y=5;
		Node monNode=new Node(x,y);
		Point monPoint=new Point(x,y);
		assertEquals(monPoint, MonJoueur.getPointFromNode(monNode));
	}
	
	

}
