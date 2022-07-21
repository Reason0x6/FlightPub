package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.UserSession;
import com.FlightPub.Services.LocationServices;
import com.FlightPub.Services.UserAccountServices;
import com.FlightPub.Services.UserGroupServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class GroupsController {
    private UserAccountServices usrServices;
    private LocationServices locationServices;
    private UserGroupServices groupServices;

    @Autowired
    @Qualifier(value = "UserGroupServices")
    public void setUserGroupServices(UserGroupServices userGroupServices) {
        this.groupServices = userGroupServices;
    }

    @Autowired
    @Qualifier(value = "UserAccountServices")
    public void setUserService(UserAccountServices usrService) {
        this.usrServices = usrService;
    }

    @Autowired
    @Qualifier(value = "LocationServices")
    public void setLocationsServices(LocationServices locService) {
        this.locationServices = locService;
    }

    @RequestMapping("/groups")
    public String group(@RequestParam String groupId, Model model, HttpSession session){
        if(!getSession(session).isLoggedIn()){
            return "redirect:login";
        }

        // Load group from database
        groupServices.loadUserGroup(groupId);

        String userId = getSession(session).getEmail();

        // Check if current user is actually in group
        if(!groupServices.isUserInGroup(userId)) {
            // Check if user has been invited to group
            if(groupServices.isUserInvited(userId)) {
                // Move user from invite list to user list
                groupServices.removeInvite(userId);
                groupServices.addUser(userId);

                model.addAttribute("accepted", true);
            }
            else {
                model.addAttribute("usr", getSession(session));
                model.addAttribute("Error", "Not in group");
                return "Error/404";
            }
        }

        // Check if user is admin
        if(groupServices.isAdmin(userId)) {
            model.addAttribute("isAdmin", true);
        }

        // Add all group users
        model.addAttribute("groupUsers", groupServices.listAllUsers());
        model.addAttribute("groupId", groupId);

        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("usr", getSession(session));
        return "User/Group";
    }

    @PostMapping("/invite_list")
    public String loadInviteList(@RequestParam("inviteUser") String inviteUser, @RequestParam("groupId") String groupId, Model model, HttpSession session) {
        // Ensure that correct group is selected when loading page
        groupServices.loadUserGroup(groupId);

        // Ensure that the user sending post request is actually a member of the group
        if (!groupServices.isUserInGroup(getSession(session).getEmail())) {
            model.addAttribute("Error", "Not in group");
            return "redirect:/Error/404";
        }

        // If invited user is already in group
        if (groupServices.isUserInGroup(inviteUser)) {
            // TODO send to this front end
            System.out.println("User is already in group");
        }
        // If valid user
        else if (usrServices.getById(inviteUser) != null) {
            groupServices.addInvite(inviteUser);

            // TODO send a notification to user that they have been invited to group
            //  notification would include group link, group name
        }
        // If incoming user email is loading just update users
        else if (inviteUser.equals("loading")){
            System.out.println("Loading invite list for group: " + groupId);
        }
        // If these checks fail not a valid user
        else {
            // TODO send this to the front end instead
            System.out.println("Not a valid user");
        }

        // Get list of all invited users
        model.addAttribute("inviteUsers", groupServices.listAllInvitedUsers());

        return "Fragments/InviteList :: invite_list_fragment";
    }

    private UserSession getSession(HttpSession session){
        UserSession sessionUser = null;
        try{
            sessionUser = (UserSession) session.getAttribute("User");
        } catch(Exception ignored){}

        if(sessionUser == null){
            sessionUser =  new UserSession(null);
            session.setAttribute("User", sessionUser);
        }

        return sessionUser;
    }
}
