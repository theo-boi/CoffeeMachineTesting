package coffeemachine.coffeemachine.controller;

import coffeemachine.coffeemachine.model.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

//Check quantities to know if you may order
public class CheckQuantities {
	
	public boolean checkDrinkQuantities(EntityManager em, int drink_id) {
		Drink drink = em.find(Drink.class, drink_id);
		if (drink.getQuantity() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean checkWaterQuantities(EntityManager em, double size) {
		Amounts amounts = em.find(Amounts.class, 1);
		if (amounts.getWater() > size) {
			return true;
		}
		return false;
	}
	
	public boolean checkPaperCupQuantities(EntityManager em, double size) {
		PaperCup paperCup = em.find(PaperCup.class, size);
		if (paperCup.getQuantity() > 0) {
			return true;
		}
		return false;		
	}
	
	public boolean checkSugarQuantities(EntityManager em, int quantity) {
		Amounts amounts = em.find(Amounts.class, 1);
		if (amounts.getSugar() > quantity*0.005) {
			return true;
		}
		return false;
	}
	
	public boolean checkQuantitiesRequiredToOrder(EntityManager em) {
		Query query = em.createQuery("from Drink d where d.quantity > 0");
		List<Drink> drinksAvailable = query.getResultList();
		if(drinksAvailable.isEmpty()) {
			return false;
		}
		query = em.createQuery("from PaperCup");
		List<PaperCup> paperCups = query.getResultList();
		List<PaperCup> sizesAvailable = new ArrayList<PaperCup>();
		for(int i = 0; i < paperCups.size(); i++) {
			if(this.checkWaterQuantities(em, paperCups.get(i).getSize())) {
				sizesAvailable.add(paperCups.get(i));
			}
		}
		if(sizesAvailable.isEmpty()) {
			return false;
		}
		return true; 
	}

}
