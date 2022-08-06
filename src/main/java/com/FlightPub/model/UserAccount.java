package com.FlightPub.model;

import com.FlightPub.Services.SecurityService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("UserAccount")
public class UserAccount {


		@Id
		@Getter
		@Setter
		private String email;

		@Getter
		@Setter
		@Field("firstname")
		private String firstname;


		@Getter
		@Setter
		@Field("password")
		private String password;

		@Getter
		@Setter
		private String preferredAirport;
		
		public UserAccount(String firstname, String email, String password, String prefairport, int api) {

			try {
				SecurityService sec = new SecurityService();

				if(api == 1){this.password = sec.hash(password);}
				else{ this.password = password;}
			}catch(Exception r){

			}

				this.firstname = firstname;
				this.email = email;
				this.preferredAirport = prefairport;

		}

	public UserAccount(String firstname, String email, String password) {
		super();
		this.password = password;
		this.firstname = firstname;
		this.email = email;

	}


	UserAccount(){}


}
