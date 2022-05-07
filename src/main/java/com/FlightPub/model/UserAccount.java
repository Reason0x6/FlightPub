package com.FlightPub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("UserAccount")
public class UserAccount {
		@Id
		private String id;
		private String username;
		private String email;
		private String pwdHash;
		
		public UserAccount(String id, String username, String email) {
			super();
			this.id = id;
			this.username = username;
			this.email = email;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUserName() {
			return username;
		}

		public void setUserName(String name) {
			this.username = name;
		}

		public String getUserEmail() {
		return this.email;
	}

		public void setUserEmail(String email) {
			this.email = email;
		}

		public void setPassword(String hash){ this.pwdHash = hash; }

		public boolean checkPassword(String hash){ return pwdHash.equals(hash); }

}
