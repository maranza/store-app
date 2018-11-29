package com.coresystems.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.coresystems.models.Payments;


/**
 * @author master
 * @version 1.0
 *
 */
public class PaymentService {

	private EntityManager em;

	public PaymentService(EntityManager entityManager) {
		this.em = entityManager;
	}

	/**
	 * Gets all payment records
	 *
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Payments> getAllOrders() {
		return (List<Payments>) em.createNamedQuery("Payments.findAll").getResultList();
	}

	/**
	 * Adds a Payment to records
	 * 
	 * @param Payment Object
	 * @return boolean
	 */
	public boolean addPayment(Payments payment) {

		EntityTransaction tx = null;

		try {
			tx = em.getTransaction();
			tx.begin();
			em.persist(payment);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			return false;
		} finally {
			tx = null;
			em.close();
		}

		return true;
	}

	/**
	 * Remove a single Payment from records
	 * 
	 * @param Integer id
	 * @return boolean
	 * @throws exception
	 * 
	 */
	public boolean delete(Integer id) throws Exception {
		Payments p = (Payments) em.createNamedQuery("Payments.findById").setParameter("id", id).getSingleResult();

		if (p == null)
			throw new Exception("No record found");

		EntityTransaction tx = null;

		try {
			tx = em.getTransaction();
			tx.begin();
			em.remove(p);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			return false;
		} finally {
			tx = null;
			em.close();
		}
		return true;
	}


}
