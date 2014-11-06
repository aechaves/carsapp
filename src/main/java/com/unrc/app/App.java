package com.unrc.app;

import org.javalite.activejdbc.*;
import static spark.Spark.*;
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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.unrc.app.models.User;
import com.unrc.app.models.Vehicle;
import com.unrc.app.models.Post;
import com.unrc.app.models.Car;
import com.unrc.app.models.Question;
import com.unrc.app.models.Answer;
import com.unrc.app.models.Address;

import com.unrc.app.controllers.VehicleController;
import com.unrc.app.controllers.AnswerController;
import com.unrc.app.controllers.PostController;
import com.unrc.app.controllers.QuestionController;
import com.unrc.app.controllers.UserController;
import com.unrc.app.controllers.SearchController;

public class App {	
	
	
    public static void main( String[] args ) {
    
        /*try {                           //code to open browser in hello url. problem, it loads without the content of spark
            URI uri = new URI("http://localhost:4567/hello");
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            if (desktop != null){
                desktop.browse(uri);
            }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (URISyntaxException use) {
                use.printStackTrace();
            }*/

        /* - CONTROLLER CREATIONS - */
        UserController userController = new UserController();
        QuestionController question = new QuestionController();
        SearchController searchController = new SearchController();
        VehicleController vehicleController = new VehicleController();
        AnswerController answer = new AnswerController();

        before((request, response) -> {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/carsapp_development", "root", "");
        });

        get("/",(request, response) -> {
            response.redirect("/hello");
            return "Welcome";
        }); 
        
        get("/whoops", (request, response) -> {
            return "error";
        });

        get("/hello",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            return new ModelAndView(attributes, "hello.mustache");
        },
            new MustacheTemplateEngine()
        );

        /*------------------------ USER ROUTES ----------------------------*/

        //Form to create a user
        get("/users/new" , (request, response) ->{
            return userController.addUserForm();
        },
            new MustacheTemplateEngine()
        );

        post ("/users",(request, response) ->{ 
            return userController.addUser(request,response);
        });

        //Form to add a vehicle
        get("/users/add/vehicles" , (request, response) ->{
            return userController.addVehicleForm();
        },
            new MustacheTemplateEngine()
        );

        post("/user/add/vehicles" , (request, response) ->{
            return userController.addVehicle(request);
        },
            new MustacheTemplateEngine()
        );

        // Form to add Addresses
        get("/users/add/addresses" , (request, response) ->{
            return userController.addAddressesForm();
        },
            new MustacheTemplateEngine()
        );

        //From to create a Post
        get("/users/add/post" , (request,response) -> {
            return userController.addPostForm();
        },
            new MustacheTemplateEngine()
        );

        post("/users/add/post" , (request,response) -> {
            return userController.selectVehiclesForm(request); //This form list the vehicles for the post creation
        },
            new MustacheTemplateEngine()
        );

        post("/posts" , (request,response) -> {
            return userController.addPost(request, response);
        });

        //List of users
        get("/users",(request, response) -> {
            return userController.listUsers();
        },
            new MustacheTemplateEngine()
        );

        
        //Show User by the id
        get("/users/:id", (request, response) -> {
            return userController.listUserById(request, response);
        },
            new MustacheTemplateEngine()
        );


        /*---------------------- POST ROUTES -----------------*/

        //create instance PostController
        PostController postController = new PostController();

        //Show Post by id
        get("/posts/:id", (request, response) -> {
            return postController.showById(request, response);
        },
            new MustacheTemplateEngine()
        );

        //Add rating at post
        post("/posts/rate", (request, response) -> {
            return postController.addRate(request,response);
        });

        //List of Posts
        get("/posts",(request, response) -> {
            return postController.show();
        },
            new MustacheTemplateEngine()
        );


        /*---------------------- VEHICLE ROUTES ----------------*/


    	post("/vehicles" , (request, response) ->{
            return vehicleController.addVehicle(request,response);
        });

    	get("/vehicles" , (request, response) -> {
            return vehicleController.listVehicles(request,response);
        },
            new MustacheTemplateEngine()
        );
     
        //Show Vehicles 
    	get("/vehicles/:id" , (request, response) -> {
            return vehicleController.listVehiclesById(request, response);
        });


        /*---------------------- QUESTION ROUTES ----------------*/



        // The form shown in the post details POSTs to this route

        post ("/questions",(request, response) -> { 
            return question.addQuestion(request,response);
        });

        // Form to write the answer to a question
        get("/questions/post/:id" , (request,response) -> {
            return userController.addAnswerForm(request, response);
        },
            new MustacheTemplateEngine()
        );

        /*---------------------- ANSWERS ROUTES ----------------*/
       

        post("/posts/answer",(request, response) -> {
            return answer.answerForm(request);
        }, 
            new MustacheTemplateEngine()
        );
       
        post("/answers", (request,response) -> {
       
            return answer.addAnswer(request,response);
        });

        
        /*---------------------- SEARCH ROUTES ----------------*/

        // Show a search bar
        get("/search/users",(request,response) -> {
            return searchController.searchUsersForm();
        }, 
            new MustacheTemplateEngine()
        );

        // Search for users
        post("/search/users", (request,response) -> {
            return searchController.searchUsers(request,response);
        },
            new MustacheTemplateEngine()
        );


        // Show a search bar for posts
        get("/search/posts",(request,response) -> {
            return searchController.searchPostsForm();
        }, 
            new MustacheTemplateEngine()
        );


        // Search for posts
        post("/search/posts", (request,response) -> {
            return searchController.searchPosts(request,response);
        },
            new MustacheTemplateEngine()
        );

        after((request, response) -> {
            Base.close();    
        });
    }
}
