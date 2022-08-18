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

/**
 * Controller for handing user groups
 */
@Controller
public class GroupsController {
    // Database services
    private UserGroupServices groupServices;
    @Autowired
    @Qualifier(value = "UserGroupServices")
    public void setUserGroupServices(UserGroupServices userGroupServices) {
        this.groupServices = userGroupServices;
    }

    private UserAccountServices usrServices;
    @Autowired
    @Qualifier(value = "UserAccountServices")
    public void setUserService(UserAccountServices usrService) {
        this.usrServices = usrService;
    }

    private LocationServices locationServices;
    @Autowired
    @Qualifier(value = "LocationServices")
    public void setLocationsServices(LocationServices locService) {
        this.locationServices = locService;
    }
    // End of Database services

    /**
     * Load a group without invite accepting/declining
     * @param groupId id to load
     * @param model interface that defines a holder for model attributes
     * @param session current session
     * @return group page for groupId
     */
    @RequestMapping("/Group")
    public String groupLoad(@RequestParam String groupId, Model model, HttpSession session) {
        // Bypass invite accepted/decline string
        return groupInvite(groupId, "bypass", model, session);
    }

    /**
     * Loads a group with invite accepting/declining plus invite bypassing
     * @param groupId id to load
     * @param accepted string to determine if user is added to group
     *                 Can be 'accepted', 'declined' or 'bypassed'
     *                 'bypassed' will ignore invite checking
     * @param model interface that defines a holder for model attributes
     * @param session current session
     * @return group page for groupId
     */
    @RequestMapping("/groupsInvite")
    public String groupInvite(@RequestParam String groupId, @RequestParam String accepted, Model model, HttpSession session){
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

                if (accepted.equals("accept")) {
                    groupServices.addUser(userId);
                    model.addAttribute("accepted", true);
                } else if (accepted.equals("decline")) {
                    groupServices.addDecline(userId);
                    return "redirect:account";
                }

            }
            else {
                model.addAttribute("usr", getSession(session));
                model.addAttribute("Error", "Not in group");
                return "Error/404";
            }
        }

        model.addAttribute("groupId", groupId);

        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("usr", getSession(session));
        return "User/Group";
    }
    /*To Do: Making separate branch for the time being. */

    /**
     * Checks if a user can be added to a group. Then returns a html fragment of all invited users
     * @param inviteUser User to be invited to group
     * @param groupId id to load
     * @param model interface that defines a holder for model attributes
     * @param session current session
     * @return html fragment of all invited users
     */
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
            model.addAttribute("Error", "User is already in group");
           // System.out.println("User is already in group");
            return "Fragments/Groups/InviteList";
        }
        // If valid user
        else if (usrServices.getById(inviteUser) != null) {
            groupServices.addInvite(inviteUser);
        }
        // If incoming user email is loading just update users
        else if (inviteUser.equals("loading")){
            System.out.println("Loading invite list for group: " + groupId);
        }
        // If these checks fail not a valid user
        else {
            // TODO send this to the front end instead
            model.addAttribute("Error", "Not a valid user");
            return "Fragments/Groups/InviteList";
            //System.out.println("Not a valid user");
        }

        // Get list of all invited pending and declined users
        model.addAttribute("inviteUsers", groupServices.listAllInvitedUsers());
        model.addAttribute("declinedUsers", groupServices.listAllDeclinedUsers());

        return "Fragments/Groups/InviteList :: invite_list_fragment";
    }

    /**
     * Load a list of users currently in group
     * @param groupId id to load
     * @param model interface that defines a holder for model attributes
     * @param session current session
     * @return list of user currently in group
     */
    @PostMapping("/added_users")
    public String addedUsersList(@RequestParam("groupId") String groupId, Model model, HttpSession session) {
        // Ensure that correct group is selected when loading page
        groupServices.loadUserGroup(groupId);

        // Ensure that the user sending post request is actually a member of the group
        if (!groupServices.isUserInGroup(getSession(session).getEmail())) {
            model.addAttribute("Error", "Not in group");
            return "redirect:/Error/404";
        }

        return loadAddedUsers(groupId, model, session);
    }

    /**
     * Remove a user from a group
     * @param userId user id to remove
     * @param groupId id to load
     * @param model interface that defines a holder for model attributes
     * @param session current session
     * @return updated list of current group users
     */
    @PostMapping("/remove_group_user")
    public String removeGroupUser(@RequestParam("userId") String userId, @RequestParam("groupId") String groupId, Model model, HttpSession session) {
        System.out.printf("Attempting to remove user: %s from group: %s %n", userId, groupId);

        // Ensure that correct group is selected when loading page
        groupServices.loadUserGroup(groupId);

        // Ensure that the user sending post request is actually a member of the group
        if (!groupServices.isUserInGroup(getSession(session).getEmail())) {
            model.addAttribute("Error", "Not in group");
            return "redirect:/Error/404";
        }

        // Remove user from group
        groupServices.removeUser(userId);

        return loadAddedUsers(groupId, model, session);
    }


    /**
     * returns a html fragment list of current group users
     * @param groupId id to load
     * @param model interface that defines a holder for model attributes
     * @param session current session
     * @return html fragment list of current group users
     */
    private String loadAddedUsers(String groupId, Model model, HttpSession session) {
        String userId = getSession(session).getEmail();
        // Check if user is admin
        if(groupServices.isAdmin(userId)) {
            model.addAttribute("isAdmin", true);
            model.addAttribute("admin", groupServices.getAdmin());
            model.addAttribute("groupId", groupId);
        }

        // Add all group users
        model.addAttribute("groupUsers", groupServices.listAllUsers());

        return "Fragments/Groups/GroupsAddedUsers :: added_users_fragment";
    }

    private UserSession getSession(HttpSession session) {
        UserSession sessionUser = null;
        try {
            sessionUser = (UserSession) session.getAttribute("User");
        } catch (Exception ignored) {
        }

        if (sessionUser == null) {
            sessionUser = new UserSession(null);
            session.setAttribute("User", sessionUser);
        }

        return sessionUser;
    }
}
