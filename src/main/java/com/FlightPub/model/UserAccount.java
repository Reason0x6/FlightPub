package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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

		@Getter
		@Setter
		private String phone;

		@Getter
		@Setter
		private String address;

		@Getter
		@Setter
		private List<String> memberships;

		@Getter
		@Setter
		private List<String> notifications;

		@Getter
		@Setter
		private List<String> travelHistory;

		@Getter
		@Setter
		private List<String> searchHistory;

		@Getter
		@Setter
		private List<String> Groups;
		
		public UserAccount(String id, String username, String email) {
			super();
			this.id = id;
			this.username = username;
			this.email = email;
		}

		public UserAccount() {}

}
