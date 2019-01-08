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
import com.coresystems.models.Customers;
import com.coresystems.services.CustomerService;
import com.coresystems.utils.ServletUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {

	private static final long serialVersionUID = -1500805539391979323L;
	private static Logger logger = Logger.getLogger(CustomerServlet.class.getName());

	private EntityManagerFactory emFactory;
	private EntityManager entityManager;
	private CustomerService service;

	public CustomerServlet() {
		emFactory = Persistence.createEntityManagerFactory("store");
		entityManager = emFactory.createEntityManager();
		service = new CustomerService(entityManager);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		reEstablishConnection();

		PrintWriter out = response.getWriter();
		Gson json = new GsonBuilder().enableComplexMapKeySerialization().excludeFieldsWithoutExposeAnnotation().create();
//            JsonObject jsonObject = json.fromJson(request.getReader(), JsonObject.class);

//            Integer id = jsonObject.get("id").getAsInt();

		String id = request.getParameter("id");

		try {
			if (id != null) {
				out.print(json.toJson(service.findCustomerById(Integer.parseInt(id))));
			} else {
				out.print(json.toJson(service.getAll()));
			}

		} catch (NotFoundException exception) {
			ServletUtil.sendError(exception.getMessage(), response, HttpServletResponse.SC_NOT_FOUND);
		}catch (Exception e) {
			ServletUtil.sendError("System error", response, HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
			logger.info(e.getMessage());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		reEstablishConnection();

		try {

			Gson json = new Gson();
			JsonObject jsonObject = json.fromJson(request.getReader(), JsonObject.class);

			String name = jsonObject.get("name").getAsString();
			String address = jsonObject.get("address").getAsString();
			Integer balance = jsonObject.get("balance").getAsInt();
			String username = jsonObject.get("username").getAsString();


			Customers customer = new Customers();
			customer.setName(name);
			customer.setAddress(address);
			customer.setBalance(balance);
			customer.setUsername(username);

			// if requestType is update then modify the specific record
			if (Integer.toString(jsonObject.get("id").getAsInt()).isEmpty()) {

				Integer id = (Integer) jsonObject.get("id").getAsInt();
				customer.setId(id);

				if (service.update(id, customer)) {
					ServletUtil.sendResponse("record updated", response);

				} else {
					ServletUtil.sendError("Failed to Update Record", response, HttpServletResponse.SC_BAD_REQUEST);
				}

			}
			else {
				if (service.save(customer)) {
					ServletUtil.sendResponse("Captured", response);
				} else {
					ServletUtil.sendError("Failed to Capture Record", response, HttpServletResponse.SC_BAD_REQUEST);
				}
			}

		} catch (JsonSyntaxException e) {
			ServletUtil.sendError("Failed to Parse Json", response, HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			ServletUtil.sendError("System Error Try Again", response, HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
			logger.warn(e.getMessage());
		}

	}
	

	@SuppressWarnings("unused")
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		reEstablishConnection();
		
		PrintWriter out = response.getWriter();
		Gson json = new GsonBuilder().enableComplexMapKeySerialization().excludeFieldsWithoutExposeAnnotation().create();
		JsonObject jsonObject = json.fromJson(request.getReader(), JsonObject.class);
		
		Integer id = Integer.parseInt(request.getParameter("id"));
		
		
		try {
			
			if(id != null) {
				service.delete(id);
				ServletUtil.sendResponse("record deleted", response);
			}else {
				ServletUtil.sendError("failed to delete Parameter required", response, HttpServletResponse.SC_BAD_REQUEST);
			}
			
		}
		catch(NotFoundException e) {
			ServletUtil.sendError("record not found", response, HttpServletResponse.SC_NOT_FOUND);
		}
		catch(Exception e)
		{
			ServletUtil.sendError("System Error", response, HttpServletResponse.SC_BAD_REQUEST);
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
