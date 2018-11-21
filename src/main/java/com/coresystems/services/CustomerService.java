package com.coresystems.services;

import com.coresystems.models.Customers;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Oratile
 */
public class CustomerService {

	private EntityManager em;

	public CustomerService(EntityManager entityManager) {
		this.em = entityManager;
	}

	// Get all Customers
	@SuppressWarnings("unchecked")
	public List<Customers> getAll() {
		return (List<Customers>) em.createNamedQuery("Customers.findAll").getResultList();
	}

	// Get a Customer by Id
	public Customers getOne(Integer id) throws Exception {
		Customers customer = (Customers) em.createNamedQuery("Customers.findById").setParameter("id", id)
				.getSingleResult();

		if (customer == null) {
			throw new Exception("No Records found with that Id");
		}

		return customer;
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

		Customers c = (Customers) em.createNamedQuery("Customers.findById").setParameter("id", id).getSingleResult();

		if (c == null) {
			throw new Exception("No Records found with that Id");
		}

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
		Customers c = (Customers) em.createNamedQuery("Customers.findById").setParameter("id", id).getSingleResult();

		if (c == null) {
			throw new Exception("No Records found with that Id");
		}

		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			em.remove(c);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			return false;
		} finally {
			tx = null;
		}
		return true;
	}

}
