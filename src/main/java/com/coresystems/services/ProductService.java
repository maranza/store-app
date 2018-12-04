/**
 * 
 */
package com.coresystems.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.coresystems.models.Products;
import com.google.gson.JsonElement;

/**
 * @author master
 *
 */
public class ProductService {

	private EntityManager em;

	public ProductService(EntityManager entityManager) {
		setEntityManager(entityManager);
	}
	
	public void setEntityManager(EntityManager entityManager) {
		
		this.em = entityManager;
	}

	public List<Products> getAllProducts() {
		return (List<Products>) em.createNamedQuery("Products.findAll").getResultList();
	}
	
	public boolean addProduct(Products products) {
		EntityTransaction tx = null;
		
		try {
			tx = em.getTransaction();
			tx.begin();
			em.persist(products);
			tx.commit();
			
		} catch (Exception e) {
			if(tx != null && tx.isActive())
				tx.rollback();
			e.printStackTrace();
			return false;
		} finally {
			tx = null;
			em.close();
		}
		return true;
	}

	public boolean updateProduct(Integer productId, Products products) throws Exception {

		EntityTransaction tx = em.getTransaction();
		Products p = findProductbyID(productId);
		try {
			tx.begin();
			p.setProductName(products.getProductName());
			p.setAmount(products.getAmount());
			tx.commit();
		} catch (Exception e) {
			if(tx != null && tx.isActive())
				tx.rollback();
			e.printStackTrace();
			return false;
		} finally {
			tx = null;
			em.close();
		}
		return true;
	}

	public boolean deleteProduct(Integer id) throws Exception {

		EntityTransaction tx = em.getTransaction();
		Products p = findProductbyID(id);
		try {
			tx.begin();
			em.remove(p);
			tx.commit();
		} catch (Exception e) {
			if(tx != null && tx.isActive())
				tx.rollback();
			e.printStackTrace();
			return false;
		} finally {
			tx = null;
			em.close();
		}
		return true;
	}

	public Products findProductbyID(Integer id) throws Exception {
		Products p;
		try {
			p = (Products)em.createNamedQuery("Products.findByProductId").setParameter("productId", id).getSingleResult();
		}catch(Exception e) {			
			throw new Exception("record not found");
		}
		
		return p;
	}

}
