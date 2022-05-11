package com.FlightPub.model;

import com.FlightPub.Services.SecurityService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("UserAccount")
public class UserAccount {


		@Getter
		@Setter
		private String firstname;

		@Id
		@Getter
		@Setter
		private String email;

		@Getter
		@Setter
		private String password;
		
		public UserAccount(String firstname, String email, String password, int api) {

			try {
				SecurityService sec = new SecurityService();

				if(api == 1){this.password = sec.hash(password);}
				else{ this.password = password;}
			}catch(Exception r){

			}

				this.firstname = firstname;
				this.email = email;

		}

	public UserAccount(String firstname, String email, String password) {
		super();
		this.password = password;
		this.firstname = firstname;
		this.email = email;

	}


	UserAccount(){}


}
