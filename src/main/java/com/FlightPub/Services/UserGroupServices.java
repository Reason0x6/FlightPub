package com.FlightPub.Services;

import com.FlightPub.model.UserAccount;
import com.FlightPub.model.UserGroup;
import com.FlightPub.repository.UserGroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service("UserGroupServices")
public class UserGroupServices {
        private final UserGroupRepo userGroupRepo;

        @Autowired
        private UserAccountServices accData;
        private UserGroup usrGroup;

        @Autowired
        public UserGroupServices(UserGroupRepo userGroupRepository) {
                this.userGroupRepo = userGroupRepository;
        }

        public LinkedList<UserAccount> listAllUsers() {
                LinkedList<UserAccount> accounts = new LinkedList<>();
                LinkedList<String> usrs = usrGroup.getUserIDs();
                for(String usr : usrs){
                        accounts.add(accData.getById(usr));
                }
                return accounts;
        }

        public LinkedList<UserAccount> listAllInvitedUsers() {
                LinkedList<UserAccount> accounts = new LinkedList<>();
                LinkedList<String> usrs = usrGroup.getInvitedIds();
                for(String usr : usrs){
                        accounts.add(accData.getById(usr));
                }
                return accounts;
        }

        public LinkedList<UserAccount> listAllDeclinedUsers() {
                LinkedList<UserAccount> accounts = new LinkedList<>();
                LinkedList<String> usrs = usrGroup.getDeclinedIds();
                for(String usr : usrs){
                        accounts.add(accData.getById(usr));
                }
                return accounts;
        }

        public UserAccount getAdmin() {
               String adminID = usrGroup.getAdminID();
               return accData.getById(adminID);
        }

        public void saveUsers(UserGroup usrGroupObj) {
                userGroupRepo.save(usrGroupObj);
        }

        public void addUser(String id) {
                usrGroup.addUser(id);
                saveUsers(usrGroup);
        }

        public void removeUser(String id) {
                usrGroup.removeUser(id);
                saveUsers(usrGroup);
        }

        public void addInvite(String id) {
                usrGroup.addInvite(id);
                saveUsers(usrGroup);
        }

        public void addDecline(String id) {
                usrGroup.addDecline(id);
                saveUsers(usrGroup);
        }

        public void removeInvite(String id) {
                usrGroup.removeInvite(id);
                saveUsers(usrGroup);
        }

        public void loadUserGroup(String id){

             usrGroup =  userGroupRepo.findById(id).orElse(null);

        }

        public List<UserGroup> findGroupsContaining(String userIDs) {
                return userGroupRepo.findAllByUserIDs(userIDs);
        }

        public List<UserGroup> findInvitedGroupsContaining(String userIDs) {
                return userGroupRepo.findAllByInvitedIds(userIDs);
        }

        public boolean isUserInGroup(String userId) {
                return usrGroup.getUserIDs().contains(userId);
        }

        public boolean isUserInvited(String userId) {
                return usrGroup.getInvitedIds().contains(userId);
        }

        public boolean isAdmin(String userId) {
                return usrGroup.getAdminID().equals(userId);
        }
}
