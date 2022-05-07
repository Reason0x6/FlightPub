package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("UserAccount")
public class UserAccount {
		@Id
		@Getter
		@Setter
		private String id;
		@Getter
		@Setter
		private String username;

		@Getter
		@Setter
		private String email;

		@Getter
		@Setter
		private String pwdHash;
		
		public UserAccount(String id, String username, String email) {
			super();
			this.id = id;
			this.username = username;
			this.email = email;
		}



}
