package com.coresystems.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.coresystems.exceptions.NotFoundException;
import com.coresystems.models.Customers;

/**
 *
 * @author Oratile
 */
public class CustomerService {

	private EntityManager em;

	public CustomerService(EntityManager entityManager) {
		setEntityManager(entityManager);
	}

	public void setEntityManager(EntityManager entityManager) {

		this.em = entityManager;
	}

	// Get all Customers
	@SuppressWarnings("unchecked")
	public List<Customers> getAll() {
		return (List<Customers>) em.createNamedQuery("Customers.findAll").getResultList();
	}

	// Add a Customer
	public boolean save(Customers customer) {

		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			em.persist(customer);
			tx.commit();
			em.close();
		} catch (Exception e) {
			tx.rollback();
			return false;
		} finally {
			tx = null;
		}

		return true;
	}

	// Update a Customer
	public boolean update(Integer id, Customers customer) throws Exception {

		Customers c = findCustomerById(id);
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			c.setName(customer.getName());
			c.setUsername(customer.getUsername());
			c.setAddress(customer.getAddress());
			c.setBalance(customer.getBalance());
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			return false;
		} finally {
			tx = null;
		}
		return true;
	}

	// Delete a Customer
	public boolean delete(Integer id) throws Exception {
		Customers c = findCustomerById(id);
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			em.remove(c);
			tx.commit();
		} catch (Exception e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			return false;
		} finally {
			tx = null;
			em.close();
		}
		return true;
	}
	
	public Customers findCustomerById(Integer id) throws NotFoundException, Exception {
		Customers c;
		try {
			c = (Customers) em.createNamedQuery("Customers.findById").setParameter("customerId", id).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException("record not found");
		}
		return c;
	}

}
