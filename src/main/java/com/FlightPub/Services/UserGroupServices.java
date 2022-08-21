package com.FlightPub.Services;

import com.FlightPub.model.UserAccount;
import com.FlightPub.model.UserGroup;
import com.FlightPub.repository.UserGroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Implements database interaction for user groups
 */
@Service("UserGroupServices")
public class UserGroupServices {
        private final UserGroupRepo userGroupRepo;
        @Autowired
        public UserGroupServices(UserGroupRepo userGroupRepository) {
                this.userGroupRepo = userGroupRepository;
        }

        private UserAccountServices accData;
        @Autowired
        private void UserAccountServices(UserAccountServices userAccountServices) {
                this.accData = userAccountServices;
        }

        private UserGroup usrGroup;


        /**
         * @return List of all users in group
         */
        public LinkedList<UserAccount> listAllUsers() {
                LinkedList<UserAccount> accounts = new LinkedList<>();
                LinkedList<String> userIDs = usrGroup.getUserIDs();
                for(String usr : userIDs){
                        accounts.add(accData.getById(usr));
                }
                return accounts;
        }

        /**
         * @return List of all invited users in group
         */
        public LinkedList<UserAccount> listAllInvitedUsers() {
                LinkedList<UserAccount> accounts = new LinkedList<>();
                LinkedList<String> userIDs = usrGroup.getInvitedIds();
                for(String usr : userIDs){
                        accounts.add(accData.getById(usr));
                }
                return accounts;
        }

        /**
         * @return List of all users who declined the invite to group
         */
        public LinkedList<UserAccount> listAllDeclinedUsers() {
                LinkedList<UserAccount> accounts = new LinkedList<>();
                LinkedList<String> userIDs = usrGroup.getDeclinedIds();
                for(String usr : userIDs){
                        accounts.add(accData.getById(usr));
                }
                return accounts;
        }

        /**
         * @return UserAccount of admin
         */
        public UserAccount getAdmin() {
               String adminID = usrGroup.getAdminID();
               return accData.getById(adminID);
        }

        /**
         * @return Current flight ID in group
         */
        public String getFlight() {
                return usrGroup.getFlight();
        }

        /**
         * @return Group name
         */
        public String getGroupName() {
                return usrGroup.getGroupName();
        }

        /**
         * Save user group to database
         * @param usrGroupObj user group to newBooking
         */
        public void saveUsers(UserGroup usrGroupObj) {
                userGroupRepo.save(usrGroupObj);
        }

        /**
         * Add a user to group
         * @param id user id to add
         */
        public void addUser(String id) {
                usrGroup.addUser(id);
                saveUsers(usrGroup);
        }

        /**
         * Remove a user from group
         * @param id user id to remove
         */
        public void removeUser(String id) {
                usrGroup.removeUser(id);
                saveUsers(usrGroup);
        }

        /**
         * Add a users invite to group
         * @param id user id to invite
         */
        public void addInvite(String id) {
                usrGroup.addInvite(id);
                saveUsers(usrGroup);
        }

        /**
         * Add a user id for declined group invite
         * @param id user id for declined group invite
         */
        public void addDecline(String id) {
                usrGroup.addDecline(id);
                saveUsers(usrGroup);
        }

        /**
         * Remove user invite from group
         * @param id user id to remove invite for
         */
        public void removeInvite(String id) {
                usrGroup.removeInvite(id);
                saveUsers(usrGroup);
        }

        /**
         * Adds a flight to group
         * @param flightId flight id to add to group
         */
        public void addFlight(String flightId) {
                usrGroup.addFlight(flightId);
                saveUsers(usrGroup);
        }

        /**
         * Load user group
         * @param id group id to load
         */
        public void loadUserGroup(String id){

             usrGroup =  userGroupRepo.findById(id).orElse(null);

        }

        /**
         * Find list of groups containing id
         * @param userId find group containing this id
         * @return List of groups
         */
        public List<UserGroup> findGroupsContaining(String userId) {
                return userGroupRepo.findAllByUserIDs(userId);
        }

        /**
         * Find list of groups containing invited id
         * @param userId find group containing this invited id
         * @return List of groups
         */
        public List<UserGroup> findInvitedGroupsContaining(String userId) {
                return userGroupRepo.findAllByInvitedIds(userId);
        }

        /**
         * Find list of groups containing admin id
         * @param adminId find group containing this invited id
         * @return List of groups
         */
        public List<UserGroup> findGroupsByAdmin(String adminId) {
                return userGroupRepo.findAllByAdminID(adminId);
        }

        /**
         * Check if user is in group
         * @param userId id to check for
         * @return true if in group, false if not
         */
        public boolean isUserInGroup(String userId) {
                return usrGroup.getUserIDs().contains(userId);
        }

        /**
         * Check if user has been invited to group
         * @param userId id to check for
         * @return true if invited, false if not
         */
        public boolean isUserInvited(String userId) {
                return usrGroup.getInvitedIds().contains(userId);
        }

        /**
         * Check if user is group admin
         * @param userId id to check for
         * @return true if user is group admin, false if not
         */
        public boolean isAdmin(String userId) {
                return usrGroup.getAdminID().equals(userId);
        }
}
