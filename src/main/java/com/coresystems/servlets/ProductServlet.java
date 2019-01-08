package com.coresystems.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.coresystems.exceptions.NotFoundException;
import com.coresystems.models.Products;
import com.coresystems.services.ProductService;
import com.coresystems.utils.ServletUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


/**
 * Servlet implementation class ProductServlet
 */
@WebServlet(description = "This servlet deals with all the operations done on the products", urlPatterns = {
		"/ProductServlet" })
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(PaymentServlet.class.getName());

	private EntityManagerFactory emFactory;
	private EntityManager entityManager;
	private ProductService service;

	public ProductServlet() {
		emFactory = Persistence.createEntityManagerFactory("store");
		entityManager = emFactory.createEntityManager();
		service = new ProductService(entityManager);
	}

	/**
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("application/json");
		reEstablishConnection();
		
		

		PrintWriter out = response.getWriter();
		Gson json = new GsonBuilder().enableComplexMapKeySerialization().excludeFieldsWithoutExposeAnnotation().create();
		
		String id = request.getParameter("id");
		
		try {
			if (id != null) {
				out.print(json.toJson(service.findProductbyID(Integer.parseInt(id))));
			} else {
				out.print(json.toJson(service.getAllProducts()));
			}
			
		}catch (NotFoundException e) {
			ServletUtil.sendError(e.getMessage(), response, HttpServletResponse.SC_NOT_FOUND);
		}
		catch (Exception e) {
			ServletUtil.sendError("no records returned", response, HttpServletResponse.SC_BAD_REQUEST);
			logger.info(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("application/json");
		reEstablishConnection();
		PrintWriter out = response.getWriter();
		Gson json = new GsonBuilder().enableComplexMapKeySerialization().excludeFieldsWithoutExposeAnnotation().create();
		JsonObject jsonObject = json.fromJson(request.getReader(), JsonObject.class);
		
		String product_name = jsonObject.get("product_name").getAsString();
		Double amount = jsonObject.get("amount").getAsDouble();
		
		String strId = request.getParameter("id");
		
		Products p = new Products();
		p.setProductName(product_name);
		p.setAmount(amount);
		
		try {
			 if(strId != null ){
				 service.updateProduct(Integer.parseInt(strId), new Products(product_name, amount));
				 ServletUtil.sendResponse("record updated", response);
			 }else {
				 if(service.addProduct(p))
					 ServletUtil.sendResponse("record added", response);
				 else
					 ServletUtil.sendError("record not added", response, HttpServletResponse.SC_BAD_REQUEST);
			 }
			
		}catch (NotFoundException e) {
			ServletUtil.sendError(e.getMessage(), response, HttpServletResponse.SC_NOT_FOUND);
		}
		catch (Exception e) {
			ServletUtil.sendError("something went wrong", response, HttpServletResponse.SC_BAD_REQUEST);
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("application/json");
		reEstablishConnection();
		PrintWriter out = response.getWriter();
//		Gson json = new GsonBuilder().enableComplexMapKeySerialization().excludeFieldsWithoutExposeAnnotation().create();
//		JsonObject jsonObject = json.fromJson(request.getReader(), JsonObject.class);
		
//		Integer id = (Integer)jsonObject.get("product_id").getAsInt();		
		Integer id = Integer.parseInt(request.getParameter("id"));
		
		try {
			 if(id != null){
				 if(service.deleteProduct(id))
					 ServletUtil.sendResponse("record deleted", response);
				 else
					 ServletUtil.sendResponse("failed to delete record", response);
			 }
			
		} catch (NotFoundException e) {
			ServletUtil.sendError(e.getMessage(), response, HttpServletResponse.SC_NOT_FOUND);
		}
		catch (Exception e) {
			ServletUtil.sendError("something went wrong", response, HttpServletResponse.SC_BAD_REQUEST);
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		
		
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}

	private void reEstablishConnection() {

		if (!entityManager.isOpen()) {

			entityManager = emFactory.createEntityManager();
			service.setEntityManager(entityManager);
		}
	}
	
	
	
}


