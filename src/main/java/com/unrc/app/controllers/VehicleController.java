package com.unrc.app.controllers;

import org.javalite.activejdbc.*;
import static spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.ModelAndView;
import spark.TemplateEngine;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import com.unrc.app.MustacheTemplateEngine;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import com.unrc.app.models.User;
import com.unrc.app.models.Vehicle;
import com.unrc.app.models.Post;
import com.unrc.app.models.Car;
import com.unrc.app.models.Question;
import com.unrc.app.models.Answer;
import com.unrc.app.models.Address;

public class VehicleController {
	
	public String addVehicle (Request request, Response response) {

		Post post = Post.findById(request.queryParams("postId"));
		String name = request.queryParams("name");
        String model = request.queryParams("model");
		String km = request.queryParams("km");
		String user = request.queryParams("user");
		String type = request.queryParams("vehicleType");

        User u = User.findFirst("email = ?",user);
           	if (type.equals("car")) {
                	u.addCar(name,model,km,request.queryParams("carType"));
           	}if (type.equals("truck")) {
                	u.addTruck(name,model,km,request.queryParams("truckType"));
           	}if (type.equals("moto")) {
                	u.addMoto(name,model,km,request.queryParams("motoType"));
           	}if (type.equals("other")) {
                	u.addVehicle(name,model,km);
           	}
            response.redirect("/vehicles");
            return "success"; 
	}
    
     //List of vehicles
     public static ModelAndView listVehicles (Request request, Response response) {
        
            Map<String, Object> attributes = new HashMap<>();
            List<Vehicle> vehicles = Vehicle.findAll();
            attributes.put("vehicles_count", vehicles.size());
            attributes.put("vehicles", vehicles);
            return new ModelAndView(attributes, "vehicles.mustache");
		
       }
    
    
    //Show Vehicles 
	public static String listVehiclesById (Request request , Response response) {

            Vehicle v = Vehicle.findById(Integer.parseInt(request.params(":id")));
            if (v == null) {
                response.redirect("/whoops", 404);
                return "not found";
            } else {
                String vehicleName = v.getString("name") +" "+ v.getString("model");
                String km = v.getString("km");
                User u1 = User.findById(v.getInteger("user_id"));
                String userName = u1.getString("first_name");
                return "Vehicle: " + vehicleName+"\n"+"Belongs to: "+userName;
            }
        }	

}

	
