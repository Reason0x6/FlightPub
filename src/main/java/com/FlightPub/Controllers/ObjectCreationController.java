package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.*;
import com.FlightPub.Services.*;
import com.FlightPub.model.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ObjectCreationController {
    private UserAccountServices usrServices;
    private FlightServices flightServices;
    private LocationServices locationServices;
    private BookingServices bookingServices;
    private UserGroupServices groupServices;
    private TicketServices ticketServices;
    private AirlineServices airlineServices;
    private UserAccount SessionUser;

    private AdminAccountServices adminAccountServices;

    @Autowired
    @Qualifier(value = "UserAccountServices")
    public void setUserService(UserAccountServices usrService) {
        this.usrServices = usrService;
    }

    @Autowired
    @Qualifier(value = "TicketServices")
    public void setTicketServices(TicketServices ticketServices) { this.ticketServices = ticketServices; }

    @Autowired
    @Qualifier(value = "FlightServices")
    public void setFlightServices(FlightServices flightService) {
        this.flightServices = flightService;
    }

    @Autowired
    @Qualifier(value = "LocationServices")
    public void setLocationsServices(LocationServices locService) {
        this.locationServices = locService;
    }

    @Autowired
    @Qualifier(value = "BookingServices")
    public void setBookingServices(BookingServices bookingService) {
        this.bookingServices = bookingService;
    }

    @Autowired
    @Qualifier(value = "AirlineServices")
    public  void setAirlineServices(AirlineServices airlineServices) { this.airlineServices = airlineServices; }

    @Autowired
    @Qualifier(value = "UserGroupServices")
    public void setUserGroupServices(UserGroupServices userGroupServices) {
        this.groupServices = userGroupServices;
    }

    @Autowired
    @Qualifier(value = "AdminAccountServices")
    public void setAdminServices(AdminAccountServices adminAccountServices) {
        this.adminAccountServices = adminAccountServices;
    }

    @PostMapping("/airline/add")
    public String addAirline(@ModelAttribute Airlines airline, Model model, HttpSession session) {
        model.addAttribute("usr", getSession(session));
        model.addAttribute("admin", getAdminSession(session));
        model.addAttribute("Airlines", airline);

        boolean invalid = false;

        // Ensures that the fields are filled and that the input is valid
        if(airline.getAirlineID() == null || airline.getAirlineName() == null || airline.getCountryCode() == null)
            invalid = true;
        else if(airline.getAirlineID() == "" || airline.getAirlineName() == "" || airline.getCountryCode() == "")
            invalid = true;

        if(invalid) {
            return "redirect:/admin/airline/management?error=form";
        }


        // Save to the database
        if(airlineServices.saveOrUpdate(airline) != null) {
            model.addAttribute("Airline", airline);
            return "Confirmations/NewAirline";
        }
        else {
            return "redirect:/admin/airline/management?error=form";
        }
    }

    @PostMapping("/price/add")
    public String addPrice(@ModelAttribute Price price, Model model, HttpSession session) {
        model.addAttribute("usr", getSession(session));
        model.addAttribute("admin", getAdminSession(session));
        model.addAttribute("Price", price);
        List<TicketClass> classes = ticketServices.getAllTicketClass();
        List<TicketType> types = ticketServices.getAllTicketType();

        boolean invalid = false;

        // Changes the full Class name to its code
        for(TicketClass ticketClass : classes) {
            if(price.getClassCode() != null && ticketClass.getDetails().equals(price.getClassCode())) {
                price.setClassCode(ticketClass.getTicketClass());
                break;
            }
        }

        // Changes the full Ticket type name to its code
        for(TicketType ticketType : types) {
            if(price.getTicketCode() != null && ticketType.getName().equals(price.getTicketCode())) {
                price.setTicketCode(ticketType.getTicketCode());
                break;
            }
        }

        // Ensures that the fields are filled and that the input is valid
        if(price.getFlightNumber() == null || price.getAirlineCode() == null || price.getClassCode() == null
           || price.getTicketCode() == null || price.getStartDate() == null || price.getEndDate() == null)
            invalid = true;
        else if(price.getFlightNumber() == "" || price.getAirlineCode() == "" || price.getClassCode() == ""
                || price.getTicketCode() == "")
            invalid = true;
        else if(price.getStartDate() < 0 || price.getEndDate() < 0 || price.getPriceLeg1() < 0 || price.getPriceLeg2() < 0)
            invalid = true;
        else if(price.getEndDate() < price.getStartDate())
            invalid = true;

        // Ensures that the airline exists
        if(!invalid && !airlineServices.airlineExists(price.getAirlineCode()))
            invalid = true;

        if(invalid) {
            return "redirect:/admin/price/management?error=form";
        }

        // Trys to match the Pricing with a current price object
        Price currentPrice = flightServices.getSpecificPriceTimeframe(price.getFlightNumber(), price.getStartDate(), price.getEndDate(), price.getClassCode(), price.getTicketCode());
        if(currentPrice != null) {
            price.setID(currentPrice.getID());
        }

        // Checks that the specified date doest overlap with existing
        if(currentPrice == null && flightServices.existingPriceTimeframe(price)) {
            return "redirect:/admin/price/management?error=form";
        }

        // Fill missing field
        double leg1 = price.getPriceLeg1() == null ? 0 : price.getPriceLeg1();
        double leg2 = price.getPriceLeg2() == null ? 0 : price.getPriceLeg2();
        price.setPrice(leg1 + leg2);

        // Save to the database
        if(flightServices.saveOrUpdatePrice(price) != null) {
            model.addAttribute("Price", price);
            return "Confirmations/NewPrice";
        }
        else {
            return "redirect:/admin/price/management?error=form";
        }
    }

    @PostMapping("/location/add")
    public String addLoc(@ModelAttribute Location location, Model model, HttpSession session) {
        model.addAttribute("usr", getSession(session));
        model.addAttribute("admin", getAdminSession(session));
        boolean invalid = false;

        // Ensures that all fields are filled in and valid
        if (location.getLocationID() == null || location.getLocation() == null || location.getCountry() == null || location.getDescription() == null)
            invalid = true;
        else if (location.getLocationID() == "" || location.getLocation() == "" || location.getCountry() == "" || location.getDescription() == "")
            invalid = true;
        else if (location.getLatitude() > 90 || location.getLatitude() < -90 || location.getLongitude() > 180 || location.getLongitude() < -180)
            invalid = true;

        // Gets the popularity of existing locations
        Location current = locationServices.getById(location.getLocationID());
        if (current != null)
            location.setPopularity(current.getPopularity());
        else
            location.setPopularity(locationServices.getLocationCount() + 1);


        // Checks whether the supplied input is valid then tries to performs database interaction
        if (invalid || locationServices.saveOrUpdate(location) == null) {
            return "redirect:/admin/location/management?error=form";
        }

        model.addAttribute("addedLoc", location);
        return "Confirmations/NewLocation";
    }

    @PostMapping("/RegisterUser")
    public String registerUSR(@ModelAttribute UserRegister newUser, Model model, HttpSession session) {
        model.addAttribute("usr", getSession(session));
        if (usrServices.getById(newUser.getEmail()) != null) {
            model.addAttribute("Error", "User already exists");
            return "Error/404";
        } else if (newUser.isValid()) {
            UserAccount nUser = new UserAccount(newUser.getFirstname(), newUser.getEmail(), newUser.getPassword());
            usrServices.saveOrUpdate(nUser);

            // TODO: Notification of new user account to be sent to newUser.getEmail()

            model.addAttribute("Registered", true);

            return "User/Login";
        }

        return "redirect:/RegisterUser?error=form";
    }

    @PostMapping("/UpdateUser")
    public String updateSR(@ModelAttribute UserRegister newUser, Model model, HttpSession session) {
        model.addAttribute("usr", getSession(session));
        if (newUser.getPassword().equals(newUser.getConfirmpassword())) {
            UserAccount nUser = getSession(session).getUsr();
            nUser.setPassword(newUser.getPassword());
            nUser.setFirstname(newUser.getFirstname());
            nUser.setPreferredAirport(newUser.getPrefAirport());
            usrServices.saveOrUpdate(nUser);

            return "Confirmations/UpdatedUser";
        }

        return "redirect:/account?error=form";
    }

    @PostMapping("/RegisterAdmin")
    public String registerAdminUSR(@ModelAttribute AdminRegister newAdmin, Model model, HttpSession session) {
        model.addAttribute("Admin", getAdminSession(session));
        if (adminAccountServices.getById(newAdmin.getEmail()) != null) {
            model.addAttribute("Error", "Admin already exists");
            return "Error/404";
        } else if (newAdmin.isValid()) {
            AdminAccount admin = new AdminAccount(newAdmin.getEmail(), newAdmin.getFirstName(), newAdmin.getLastName(), newAdmin.getCompany(), newAdmin.getPassword());
            adminAccountServices.saveOrUpdate(admin);
            model.addAttribute("addedAdmin", admin);
            return "Confirmations/RegisterAdmin";
        }

        return "redirect:/AdminRegister?error=form";
    }

    @PostMapping("/flight/add")
    public String addFlight(@ModelAttribute EditedFlightContainer container, Model model, HttpSession session) {
        model.addAttribute("usr", getSession(session));
        model.addAttribute("admin", getAdminSession(session));
        List<String> availabilityID = getSession(session).getAvailabilityID();

        // Interpret the data from the form, generating availability objects
        Flight flight = container.getFlight();
        List<Availability> availabilityList = container.getAvailabilities();
        for(int count = 0; count < availabilityList.size(); count++) {
            String[] id = availabilityID.get(count).split("-");
            availabilityList.get(count).setAirlineCode(flight.getAirlineCode());
            availabilityList.get(count).setClassCode(id[0]);
            availabilityList.get(count).setTicketCode(id[1]);
            availabilityList.get(count).setDepartureTime(flight.getDepartureTime());
            availabilityList.get(count).setFlightNumber(flight.getFlightNumber());
        }


        if (flight.getFlightID() == null) {
            flight.setFlightID(new ObjectId());
        }
        model.addAttribute("flight", flight);


        // Validates the input
        boolean invalid = false;
        if(flight.getFlightNumber()==null || flight.getAirlineCode()==null || flight.getDestinationCode()==null
                || flight.getDepartureCode()==null
                || flight.getDepartureTime() == null
                || flight.getArrivalTime() == null)
            invalid = true;
        else if(flight.getFlightNumber()=="" || flight.getAirlineCode()=="" || flight.getDestinationCode()==""
                || flight.getDepartureCode()=="")
            invalid = true;
        else if (!((flight.getDepartureTimeStopOver() == null) == (flight.getArrivalTimeStopOver() == null)) ||
                !((flight.getDepartureTimeStopOver() == null) == (flight.getStopoverCode() == null)))
            invalid = true;
        else if (flight.getRating() < 0 || flight.getDurationSecondLeg()<0 || flight.getDuration()<0
                || flight.getDepartureTime()<0 || flight.getArrivalTime()<0)
            invalid = true;
            // Checks whether the supplied origin and destination are ID or the location name and that they exist
        else {
            // Convert the ID to align with the Database standard)
            flight.setDestinationCode(flight.getDestinationCode().toUpperCase());
            flight.setDepartureCode(flight.getDepartureCode().toUpperCase());

            // Tests if the supplied string is the name of the locations
            // Test for Destination Location
            if (locationServices.getById(flight.getDestinationCode()) == null) {
                Location destination = locationServices.findByLocation(flight.getDestinationCode());
                if (destination != null)
                    flight.setDestinationCode(destination.getLocationID());
                else
                    invalid = true;
            }
            // Test for Departure Location
            if (!invalid && locationServices.getById(flight.getDepartureCode()) == null) {
                Location origin = locationServices.findByLocation(flight.getDepartureCode());
                if (origin != null)
                    flight.setDepartureCode(origin.getLocationID());
                else
                    invalid = true;
            }
            // Test for Stopover location
            if (!invalid && flight.getStopoverCode() != null) {
                if (flight.getArrivalTimeStopOver() < 0 && flight.getDepartureTimeStopOver() < 0)
                    invalid = true;
                else {
                    // Convert the ID to align with the Database standard)
                    flight.setStopoverCode(flight.getStopoverCode().toUpperCase());

                    // Tests if the supplied string is the name of the locations
                    if (locationServices.getById(flight.getStopoverCode()) == null) {
                        Location stop = locationServices.findByLocation(flight.getStopoverCode());
                        if (stop != null)
                            flight.setDestinationCode(stop.getLocationID());
                        else
                            invalid = true;
                    }
                }
            }
        }

        // Tests that there does exist pricing for a flightCode
        if(!invalid && !flightServices.priceExists(flight.getFlightNumber()))
            invalid = true;

        // Ensures that the airline exists
        if(!invalid && !airlineServices.airlineExists(flight.getAirlineCode()))
            invalid = true;

        // Returns a invalid flight to the edit and creation page
        if (invalid) {
            return "redirect:/admin/flight/management?error=form";
        }

        // Adds the ID to existing availabilities to ensure they are overridden
        List<Availability> flightAvailabilities = flightServices.getAvailability(flight.getFlightNumber(), flight.getDepartureTime());
        for(Availability currentAvailability: flightAvailabilities) {
            for(int count = 0; count < availabilityList.size(); count++) {
                if(currentAvailability.getTicketCode().equals(availabilityList.get(count).getTicketCode())&&currentAvailability.getClassCode().equals(availabilityList.get(count).getClassCode())) {
                    availabilityList.get(count).setID(currentAvailability.getID());
                    break;
                }
            }
        }

        // Ensures that unavailable availabilities are not added to the database
        for(int count = 0; count < availabilityList.size();) {
            if(availabilityList.get(count).getID() == null && availabilityList.get(count).getNumberAvailableSeatsLeg1() <= 0 && availabilityList.get(count).getNumberAvailableSeatsLeg2() <= 0)
                availabilityList.remove(count);
            else
                count++;
        }

        // Update the database with a update or new entry
        flight = flightServices.saveOrUpdate(flight);

        // Saves all availabilities to the database
        for(Availability availability : availabilityList) {
            flightServices.saveOrUpdateAvailability(availability);
        }

        if (flight == null) {
            return "redirect:/admin/flight/management?error=form";
        }
        else {
            model.addAttribute("flight", flight);
            return "Confirmations/NewFlight";
        }
    }

    /**
     * Add a new group to the database
     *
     * @param group   contains new group name and optionally flight id
     * @param session current session
     * @return redirect to new group
     */
    @PostMapping("/group/add") //e.g localhost:8080/group/add?groupName=group1
    public String addGroup(@ModelAttribute NewGroup group, HttpSession session) {
        if (!getSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        // Create a new group
        UserGroup newGroup = new UserGroup(getSession(session).getEmail(), group.getGroupName());
        newGroup.addFlight(group.getFlightId());
        groupServices.saveUsers(newGroup);

        return "redirect:/Group?groupId=" + newGroup.getId();
    }

    private UserSession getSession(HttpSession session) {
        UserSession sessionUser = null;
        try {
            sessionUser = (UserSession) session.getAttribute("User");
        } catch (Exception e) {
            sessionUser = null;
        }

        if (sessionUser == null) {
            sessionUser = new UserSession(null);
            session.setAttribute("User", sessionUser);
        }

        return sessionUser;
    }

    private AdminSession getAdminSession(HttpSession session) {
        AdminSession sessionAdmin = null;
        try {
            sessionAdmin = (AdminSession) session.getAttribute("Admin");
        } catch (Exception e) {
            sessionAdmin = null;
        }

        if (sessionAdmin == null) {
            sessionAdmin = new AdminSession(null);
            session.setAttribute("Admin", sessionAdmin);
        }

        return sessionAdmin;
    }


}
