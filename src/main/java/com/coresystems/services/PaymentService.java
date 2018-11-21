package com.coresystems.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.coresystems.models.Payments;

public class PaymentService {

	   private EntityManager em;
	    
	    public PaymentService(EntityManager entityManager)
	    {
	        this.em = entityManager;
	    }
	    
	    @SuppressWarnings("unchecked")
		public List<Payments> getAllOrders(){
	    	return (List<Payments>)em.createNamedQuery("Payments.findAll").getResultList();
	    }
	    
	    public boolean addPayment(Payments payment) {
	    	
	    	EntityTransaction tx = null;
	    	
	    	try 
	    	{
	    		tx = em.getTransaction();
	    		tx.begin();
	    		em.persist(payment);
	    		tx.commit();
	    		
	    		
	    	}catch(Exception e) 
	    	{
	    		tx.rollback();
	    		return false;
	    	}
	    	finally {
	    		tx = null;
	    	}
	    	
	    	return true;
	    }
	
	
	
}
