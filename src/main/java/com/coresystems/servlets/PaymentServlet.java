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

import com.coresystems.models.Customers;
import com.coresystems.models.Payments;
import com.coresystems.models.Products;
import com.coresystems.services.PaymentService;
import com.coresystems.services.ProductService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
		Gson json = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		String id = request.getParameter("id");

		try {
			
			if (id != null) {
					out.print(json.toJson(service.findOrderById(Integer.parseInt(id))));
			} else {
				out.print(json.toJson(service.getAllOrders()));
			}
		} catch (Exception exception) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("{\"msg\":\"System Error\"}");
			logger.info(exception.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("appication/json");
		reEstablishConnection();

		PrintWriter out = response.getWriter();
		Gson json = new Gson();
		JsonObject jsonObject = json.fromJson(request.getReader(), JsonObject.class);
		

		try {
			
			
//			Integer id = jsonObject.get("id").getAsInt();
			String strId = request.getParameter("id");
			Integer customer_id = jsonObject.get("customer_id").getAsInt();
			Integer product_id = jsonObject.get("product_id").getAsInt();
			Integer amount = jsonObject.get("amount").getAsInt();

			Payments payment = new Payments();
		
			payment.setCustomerId(new Customers(customer_id));
			payment.getP().add(new Products(product_id));
			payment.setAmount(amount);

			if(strId != null ){
				service.updatePayment(Integer.parseInt(strId), payment);
				out.print(" {\"msg\" : \"record updated\"} ");
			}else {
				if (service.addPayment(payment)) {
					out.println(" { \"msg\" : \"added payment\" } ");

				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					out.println(" { \"msg\" : \"failed to add payment\" } ");
				}
			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
			out.println("{\"msg\" : \"System Error Try Again\"}");
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
//		Gson json = new Gson();
//		json.fromJson(request.getReader(), JsonObject.class);

		Integer id = Integer.parseInt(request.getParameter("id"));

		try {
			if (id != null) {
				service.delete(id);
				out.println(" {\"msg\" : \"deleted\"} ");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.println(" {\"msg\" : \"failed to detele no such order\"} ");
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.println(" {\"msg\" : \"System Error\"} ");
			logger.info(e.getMessage());
		}

	}
	
	@SuppressWarnings("unused")
	private void reEstablishConnection() {

		if (!entityManager.isOpen()) {

			entityManager = emFactory.createEntityManager();
			service.setEntityManager(entityManager);
		}
	}

}
