package com.surya.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.surya.entity.Entry;
import com.surya.entity.User;
import com.surya.service.EntryService;
import com.surya.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService; 
	
	@Autowired
	HttpSession session;
	
	
	public UserService getUserService() {
		return userService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	@Autowired
	private EntryService entryService;
	
	public EntryService getEntryService() {
		return entryService;
	}


	public void setEntryService(EntryService entryService) {
		this.entryService = entryService;
	}
	

	@GetMapping("home")
	public String homepage()
	{
		
		String model = new String("loginpage");
		
		
		
		return model;
	}
	
	
	@GetMapping("register")
	public String registrationpage()
	{
		
		String model = new String("registrationpage");
		
		
		
		return model;
	}
	
	
	@PostMapping(value="saveuser")
	public String saveuser(@ModelAttribute("user") User user)
	{
		//code to save the user details in the database
		
		
        String model = new String("registersuccess");
		
        userService.saveUser(user);	
        
        
		return model;
		
	}
	
	@PostMapping(value="/authenticate")
	public String authenticateUser(@ModelAttribute("user") User user, Model model) {
	    String viewName = "loginpage";
	    
	    User user1 = userService.findByUsername(user.getUsername());
	    
	    if (user1 != null && user.getPassword().equals(user1.getPassword())) {
	        viewName = "userhomepage";
	        model.addAttribute("user", user1);
	        
	        session.setAttribute("user", user1);
	        
	        try {
	            List<Entry> entries = entryService.findByUserid(user1.getId());
	            model.addAttribute("entrieslist", entries);
	        } catch (Exception e) {
	            e.printStackTrace();
	            // Handle exception as required
	        }
	    }
	    
	    return viewName;
	}

	
	@GetMapping("addentry")
	public String addEntry(HttpSession session, Model model) {
	    // Retrieve the logged-in user from the session
	    User loggedInUser = (User) session.getAttribute("user");

	    // Check if the user is logged in
	    if (loggedInUser != null) {
	        // Use the logged-in user information
	        model.addAttribute("user", loggedInUser); // Add user to the model
	        return "addentryform"; // Show the form for adding an entry
	    } else {
	        // Redirect to login page if user is not logged in
	        return "redirect:/login"; // Or another appropriate action
	    }
	}
	
	@PostMapping("saveentry")
	public String saveentry(@ModelAttribute("entry") Entry entry, Model model, HttpSession session) {
	    String viewname="userhomepage";
	    
	    entryService.saveEntry(entry);
	    
	    User user1=(User)session.getAttribute("user");
	    
	    List<Entry> entries=null;
	    
	    try {
	        entries=entryService.findByUserid(user1.getId());
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    
	    model.addAttribute("entrieslist", entries);
	    model.addAttribute("user", user1); // Add the user to the model
	    
	    return viewname;
	}
	
	
	@GetMapping("viewentry")
	public String viewentry(@RequestParam("id") int id, Model model)
	{
		String viewname="displayentry";
		
		Entry entry = entryService.findById(id);
		
		model.addAttribute("entry", entry);
		
		return viewname;
	}
	
	
	@GetMapping("userhome")
	public String userhomepage(Model model)
	{
		
		String viewname="userhomepage";
		User user1=(User)session.getAttribute("user");
		
		List<Entry> entries=null;
		
		try {
			entries=entryService.findByUserid(user1.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("entrieslist", entries);
		
		return viewname;
	}
	
	
	
	
	@GetMapping("updateentry")
	public String updateentry(@RequestParam("id") int id, Model model, HttpSession session) {
	    String viewname="displayupdateentry";
	    
	    Entry entry = entryService.findById(id);
	    
	    model.addAttribute("entry", entry);
	    
	    User user1=(User)session.getAttribute("user");
	    
	    if(user1==null)
	        viewname="loginpage";
	    else
	        model.addAttribute("user", user1); // Add the user to the model
	    
	    return viewname;
	}

	@PostMapping("processentryupdate")
	public String processentryupdate(@ModelAttribute("entry") Entry entry, Model model, HttpSession session) {
	    String viewname="userhomepage";
	    
	    entryService.updateEntry(entry);
	    
	    User user1=(User)session.getAttribute("user");
	    
	    List<Entry> entries=null;
	    
	    try {
	        entries=entryService.findByUserid(user1.getId());
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    
	    model.addAttribute("entrieslist", entries);
	    model.addAttribute("user", user1); // Add the user to the model
	    
	    return viewname;
	}
	

	@GetMapping("deleteentry")
	public String deleteentry(@RequestParam("id") int id, Model model)
	{
	    String viewname="userhomepage";
	    
	    User user1=(User)session.getAttribute("user");
	    
	    Entry entry = entryService.findById(id);
	    
	    if(user1==null)
	        viewname="loginpage";
	    else
	        entryService.deleteEntry(entry);
	    
	    List<Entry> entries=null;
	    
	    try {
	        entries=entryService.findByUserid(user1.getId());
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    
	    model.addAttribute("entrieslist", entries);
	    model.addAttribute("user", user1); // Add this line
	    
	    return viewname;
	}
	
	/*
	 * @GetMapping("deleteentry") public String deleteentry(@RequestParam("id") int
	 * id, Model model) { String viewname = "userhomepage";
	 * 
	 * User user1 = (User) session.getAttribute("user");
	 * 
	 * Entry entry = entryService.findById(id);
	 * 
	 * if (user1 == null) viewname = "loginpage"; else
	 * entryService.deleteEntry(entry);
	 * 
	 * List<Entry> entries = null;
	 * 
	 * try { entries = entryService.findByUserid(user1.getId()); } catch (Exception
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * model.addAttribute("entrieslist", entries);
	 * 
	 * return viewname; }
	 */
	
	
	@GetMapping("signout")
	public String signout()
	{
		
		String model = new String("loginpage");
		
		session.invalidate();
		
		
		return model;
	}
	

}
