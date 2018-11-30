package com.coresystems.main;

import com.coresystems.models.Customers;
import com.coresystems.services.CustomerService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

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

@WebServlet("/hello")
public class Hello extends HttpServlet {

	private static final long serialVersionUID = -1500805539391979323L;
	private static Logger logger = Logger.getLogger(Hello.class.getName());

	private static EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("store");
	EntityManager manager = emFactory.createEntityManager();
	private CustomerService service = new CustomerService(manager);

	@Override
	public void init() {

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");

		PrintWriter out = response.getWriter();
		Gson json = new Gson();
//            JsonObject jsonObject = json.fromJson(request.getReader(), JsonObject.class);

//            Integer id = jsonObject.get("id").getAsInt();

		String id = request.getParameter("id");

//		try {

//			if (id != null) {
//				out.print(json.toJson(service.getOne(Integer.parseInt(id))));
//			} else {
				out.print(json.toJson(service.getAll()));
//			}

//		} catch (Exception exception) {
//
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			response.getWriter().println("{ \"msg\" : \" " + exception.getMessage() + " \" }");
//			logger.info(exception.getMessage());
//		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		doGet(request, response);

		response.setContentType("application/json");

		PrintWriter out = response.getWriter();
//            Gson json = new Gson();

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
					out.println("{\"msg\":\"Updated\"}");

				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					out.println("{\"msg\":\"Failed to Update Record\"}");

				}

			}
//    			
			else {
				if (service.save(customer)) {
					out.println("{\"msg\":\"Captured\"}");

				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					out.println("{\"msg\":\"Faisssled to Captusssre Recorddd\"}");
//    					
				}
			}

		} catch (JsonSyntaxException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.println("{\"msg\" : \"Failed to Parse Json\"}");

		} catch (Exception e) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
			out.println("{\"msg\" : \"System Error Try Again\"}");
			logger.warn(e.getMessage());
		}

	}
	

	@SuppressWarnings("unused")
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		
		PrintWriter out = response.getWriter();
		Gson json = new Gson();
		JsonObject jsonObject = json.fromJson(request.getReader(), JsonObject.class);
		
		Integer id = Integer.parseInt(request.getParameter("id"));
		
		
		try {
			
			if(id != null) {
				service.delete(id);
				out.println(" {\"msg\" : \"deleted\"} ");
			}else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.println(" {\"msg\" : \"failed to delete Parameter required\"} ");
			}
			
		}catch(Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.println("{\"msg\":\"System Error \"}");
			logger.info(e.getMessage());
		}
		
		
	}
	
	
	
	
	

}
