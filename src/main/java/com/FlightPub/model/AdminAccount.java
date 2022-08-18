package com.FlightPub.model;

import com.FlightPub.Services.SecurityService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Java Object Representation of Database Object
 */
@Document("AdminAccount")
public class AdminAccount {
    @Id
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String company;

    @Getter
    @Setter
    private String password;

    public AdminAccount(String email, String firstName, String lastName, String company, String password, int api) {

        try {
            SecurityService sec = new SecurityService();

            if(api == 1){this.password = sec.hash(password);}
            else{ this.password = password;}
        }catch(Exception r){
            r.printStackTrace();
        }

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
    }

    public AdminAccount(String email, String firstName, String lastName, String company, String password){
        super();
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.password = password;
    }

    public AdminAccount(){}

}
