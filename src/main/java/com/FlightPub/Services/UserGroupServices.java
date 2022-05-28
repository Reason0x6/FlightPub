package com.FlightPub.Services;

import com.FlightPub.model.UserAccount;
import com.FlightPub.model.UserGroup;
import com.FlightPub.repository.UserAccountRepo;
import com.FlightPub.repository.UserGroupRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

public class UserGroupServices {
        private UserGroupRepo userGroupRepo;

        @Autowired
        private UserAccountServices accData;
        private UserGroup usrGroup;

        @Autowired
        public UserGroupServices(UserGroupRepo userGroupRepository) {
                this.userGroupRepo = userGroupRepository;
        }

        public LinkedList<UserAccount> listAllUsers() {
                LinkedList<UserAccount> accounts = new LinkedList<UserAccount>();
                LinkedList<String> usrs = usrGroup.getUserIDs();
                for(String usr : usrs){
                        accounts.add(accData.getById(usr));
                }
                return accounts;
        }

        public UserAccount getAdmin(String id) {
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

        public void loadUserGroup(String id){

             usrGroup =  userGroupRepo.findById(id).orElse(null);

        }


}
