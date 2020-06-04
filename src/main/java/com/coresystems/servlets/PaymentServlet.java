package com.coresystems.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
import com.coresystems.models.Customers;
import com.coresystems.models.Payments;
import com.coresystems.models.Products;
import com.coresystems.services.PaymentService;
import com.coresystems.utils.ServletUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {

	private static final long serialVersionUID = 3L;
	private static Logger logger = Logger.getLogger(PaymentServlet.class.getName());

	private EntityManagerFactory emFactory;
	private EntityManager entityManager;
	private PaymentService service;

	public PaymentServlet() {
		emFactory = Persistence.createEntityManagerFactory("store");
		entityManager = emFactory.createEntityManager();
		service = new PaymentService(entityManager);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		reEstablishConnection();

		PrintWriter out = response.getWriter();
		Gson json = new GsonBuilder().enableComplexMapKeySerialization().excludeFieldsWithoutExposeAnnotation().create();
		
		String id = request.getParameter("id");

		try {
			
			if (id != null) {
					out.print(json.toJson(service.findOrderById(Integer.parseInt(id))));
			} else {
				out.print(json.toJson(service.getAllOrders()).concat("total: "));
			}
		} catch (NotFoundException e) {
			ServletUtil.sendError(e.getMessage(), response, HttpServletResponse.SC_NOT_FOUND);
		}
		catch (Exception exception) {
			ServletUtil.sendError("System Error", response, HttpServletResponse.SC_BAD_REQUEST);
			exception.printStackTrace();
			logger.info(exception.getMessage());
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("appication/json");
		reEstablishConnection();

		PrintWriter out = response.getWriter();
		Gson json = new GsonBuilder().enableComplexMapKeySerialization().excludeFieldsWithoutExposeAnnotation().create();
		JsonObject jsonObject = json.fromJson(request.getReader(), JsonObject.class);
		
//		product_id.forEach( id ->  {
//		payment.getP().add(new Products((Integer)id.getAsInt()));
//		System.out.println( (Integer)id.getAsInt() );
//		});
		
		try {
			
			
//			Integer id = jsonObject.get("id").getAsInt();
			String strId = request.getParameter("id");
			Integer customer_id = jsonObject.get("customer_id").getAsInt();
			JsonArray product_id = jsonObject.get("product_id").getAsJsonArray();
			Integer amount = jsonObject.get("amount").getAsInt();
			
			Payments payment = new Payments();
			List<Products> p = new ArrayList<>();
		
			product_id.forEach( id ->  {
				p.add( new Products((Integer)id.getAsInt()));
			});
			
			payment.setCustomer(new Customers(customer_id));			
			payment.setAmount(amount);
			payment.setP(p);

			if(strId != null ){
				service.updatePayment(Integer.parseInt(strId), payment);
				ServletUtil.sendResponse("record updated", response);
			}else {
				if (service.addPayment(payment)) {
					ServletUtil.sendResponse("added payment", response);
				} else {
					ServletUtil.sendError("failed to add payment", response, HttpServletResponse.SC_BAD_REQUEST);
				}
			}

		}catch(NotFoundException e) {
			ServletUtil.sendError(e.getMessage(), response, HttpServletResponse.SC_NOT_FOUND);
		}
		catch (Exception e) {
			ServletUtil.sendError("System Error Try Again", response, HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
			logger.warn(e.getMessage());
		}

	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unused")
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		reEstablishConnection();

		PrintWriter out = response.getWriter();
		// use when receiving json data
//		Gson json = new GsonBuilder().enableComplexMapKeySerialization().excludeFieldsWithoutExposeAnnotation().create();
//		json.fromJson(request.getReader(), JsonObject.class);

		Integer id = Integer.parseInt(request.getParameter("id"));

		try {
			if (id != null) {
				service.delete(id);
				ServletUtil.sendResponse("record deleted", response);
			}
		}catch (NotFoundException e) {
			ServletUtil.sendError(e.getMessage(), response, HttpServletResponse.SC_NOT_FOUND);
		}
		catch (Exception e) {
			ServletUtil.sendError("System Error Try Again", response, HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
			logger.info(e.getMessage());
		}

	}
	
	private void reEstablishConnection() {

		if (!entityManager.isOpen()) {

			entityManager = emFactory.createEntityManager();
			service.setEntityManager(entityManager);
		}
	}

}
