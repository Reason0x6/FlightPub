package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.*;
import com.FlightPub.Services.*;
import com.FlightPub.model.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.FlightPub.model.Email;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Strings;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController {

    private UserAccountServices usrServices;
    private LocationServices locationServices;
    private FlightServices flightServices;
    private BookingServices bookingServices;
    private UserGroupServices groupServices;
    private WishListServices wishListServices;
    private AdminAccountServices adminAccountServices;
    private HolidayPackageServices holidayPackageServices;
    private AirlineServices airlineServices;
    private TicketServices ticketServices;
    private EmailServices emailServices;

    @Autowired
    @Qualifier(value = "AirlineServices")
    public void setAirlineServices(AirlineServices airlineServices) {
        this.airlineServices = airlineServices;
    }

    @Autowired
    @Qualifier(value = "WishListServices")
    public void setWishListServices(WishListServices wishListServices) {
        this.wishListServices = wishListServices;
    }

    @Autowired
    @Qualifier(value = "FlightServices")
    public void setFlightServices(FlightServices flightService) {
        this.flightServices = flightService;
    }

    @Autowired
    @Qualifier(value = "UserAccountServices")
    public void setUserService(UserAccountServices usrService) {
        this.usrServices = usrService;
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
    @Qualifier(value = "UserGroupServices")
    public void setUserGroupServices(UserGroupServices userGroupServices) {
        this.groupServices = userGroupServices;
    }

    @Autowired
    @Qualifier(value = "AdminAccountServices")
    public void setAdminAccountServices(AdminAccountServices adminAccountServices) {
        this.adminAccountServices = adminAccountServices;
    }

    @Autowired
    @Qualifier(value = "HolidayPackageServices")
    public void setHolidayPackageServices(HolidayPackageServices holidayPackageServices) {
        this.holidayPackageServices = holidayPackageServices;
    }

    @Autowired
    @Qualifier(value = "TicketServices")
    public void setTicketServices(TicketServices ticketServices){ this.ticketServices = ticketServices; }

    @Autowired
    @Qualifier(value = "EmailServices")
    public void setEmailServices(EmailServices emailServices) {
        this.emailServices = emailServices;
    }

    @RequestMapping("/invalidatecache")
    public String cache() {
        System.out.println("Cache Cleared");
        flightServices.invalidate();
        return "index";
    }

    @RequestMapping("/")
    public String loadIndex(Model model, HttpSession session) {

        model = addDateAndTimeToModel(model);

        model.addAttribute("usr", getSession(session));
        model.addAttribute("admin", getAdminSession(session));

        model.addAttribute("searchLocation", locationServices.listAll());
        model.addAttribute("LoadingRecommendation", true);
        model.addAttribute("MostPop", locationServices.topTen());
        model.addAttribute("travelPackages", getTravelPackages());

        return "index";
    }

    @RequestMapping("/login")
    public String loadLogin(Model model, HttpSession session) {

        model.addAttribute("usr", getSession(session));
        model.addAttribute("admin", getAdminSession(session));
        return "User/login";
    }

    // This can be used to redirect the login back to a page after logging in
    // Usage is /login?redirect=a_page_url?params=can_be_included
    @GetMapping(value = "/login", params = "redirect")
    public String loadLoginRedirect(@RequestParam String redirect, Model model, HttpSession session) {
        model.addAttribute("redirect", redirect);
        model.addAttribute("usr", getSession(session));
        model.addAttribute("Admin", getAdminSession(session));
        return "User/login";
    }

    @RequestMapping("/newuser")
    public String user(Model model) {
        return "Notifications/newuser";
    }

    @RequestMapping("/Register")
    public String loadRegister(Model model, HttpSession session) {

        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("usr", getSession(session));
        return "User/Register";
    }

    @RequestMapping("/AdminRegister")
    public String loadAdminRegister(Model model, HttpSession session) {

        model.addAttribute("usr", getSession(session));
        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("admin", getAdminSession(session));
        return "User/AdminRegister";
    }

    @RequestMapping("/logout")
    public String loadLogout(Model model, HttpSession session) {
        session.setAttribute("Admin", new AdminSession(null));
        session.setAttribute("User", new UserSession(null));
        model.addAttribute("usr", getSession(session));
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String runLogin(@ModelAttribute LoginRequest req, Model model, HttpSession session) {
        model.addAttribute("usr", getSession(session));
        model.addAttribute("admin", getAdminSession(session));

        String redirect = req.getRedirect();
        model.addAttribute("redirect", redirect);

        try {

            UserAccount newUser = usrServices.getById(req.getEmail());
            AdminAccount newAdmin = adminAccountServices.getById(req.getEmail());

            if (newUser != null) {
                if (req.getPassword().equals(newUser.getPassword())) {
                    // Set post flag
                    model.addAttribute("method", "post");

                    // Set user session
                    UserSession usr = new UserSession(newUser);
                    session.setAttribute("User", usr);
                    model.addAttribute("usr", usr);

                    // If a redirect has been set, redirect upon login
                    if (!redirect.equals("")) {
                        return "redirect:" + redirect;
                    } else {
                        return "redirect:account";
                    }
                } else {
                    model.addAttribute("valid", false);
                }
            }
            if (newAdmin != null) {
                if (req.getPassword().equals(newAdmin.getPassword())) {
                    // Set post flag
                    model.addAttribute("method", "post");
                    // Set admin session
                    AdminSession admin = new AdminSession(newAdmin);
                    session.setAttribute("Admin", admin);
                    model.addAttribute("admin", admin);

                    return "redirect:adminAccount";
                } else {
                    model.addAttribute("valid", false);
                }
            }

        } catch (Exception e) {
            model.addAttribute("valid", false);
        }
        return "User/login";
    }

    @RequestMapping("/account")
    public String account(Model model, HttpSession session) {
        if (!getSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        List<Booking> bookings = bookingServices.getUserBookings(getSession(session).getEmail());
        if (bookings.size() > 0) {
            for(Booking b: bookings){
                b.setFlight(flightServices.getById(b.getFlightID()));
            }
            List<Booking> previous = new ArrayList<>();
            List<Booking> upcoming = new ArrayList<>();
            for(Booking b: bookings){
                System.out.println( b.getFlight().getDepartureTime() + " | " + (new Date().getTime () / 1000L));
               if(b.getFlight().getDepartureTime() > (new Date().getTime () / 1000L)){
                   upcoming.add(b);
               }else{
                   previous.add(b);
               }
            }
            if(upcoming.size() > 0){
                model.addAttribute("bookings", upcoming);
            }else{

                model.addAttribute("bookings", null);
            }

            if(previous.size() > 0){
                model.addAttribute("history", previous);
            }else{

                model.addAttribute("history", null);
            }
        } else {
            model.addAttribute("bookings", null);
            model.addAttribute("history", null);
        }


        List<UserGroup> invitedGroups = groupServices.findInvitedGroupsContaining(getSession(session).getEmail());
        model.addAttribute("invitedGroups", invitedGroups);

        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("usr", getSession(session));
        model.addAttribute("userHolidayPackages", getUserHolidayPackages(session));
        model.addAttribute("travelPackages", getTravelPackages());


        return "User/Personalised";
    }


    @RequestMapping("/adminAccount")
    public String adminAccount(Model model, HttpSession session) {
        if (!getAdminSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        model.addAttribute("wish", wishListServices.findAllByPopularitySortDesc());
        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("admin", getAdminSession(session));
        return "User/AdminControl";
    }

    @RequestMapping("/stopoverFlight")
    public String viewStopoverFlight(@RequestParam String id, Model model, HttpSession session) {
        String[] flightID = id.split("-");  // Breaks up the flight ID's if there are multiple

        // Collection variables for the flight details of each leg
        List<Location> dest = new ArrayList<>();
        List<Location> dep = new ArrayList<>();
        List<Flight> flight = new ArrayList<>();
        List<List<String[]>> businessClass = new ArrayList<>();
        List<List<String[]>> economyClass = new ArrayList<>();
        List<List<String[]>> firstClass = new ArrayList<>();
        List<List<String[]>> premiumEconomy = new ArrayList<>();

        // Collects all stopover flight details
        for (int count = 0; count < flightID.length; count++) {
            Flight f = flightServices.getById(flightID[count]);
            System.out.println(flightID[count]);
            List<Availability> availableSeats = flightServices.getAvailability(f.getFlightNumber(), f.getDepartureTime());

            dest.add(locationServices.getById(f.getDestinationCode()));
            dep.add(locationServices.getById(f.getDepartureCode()));
            flight.add(f);
            businessClass.add(flightServices.getSeatList("BUS", availableSeats));
            economyClass.add(flightServices.getSeatList("ECO", availableSeats));
            firstClass.add(flightServices.getSeatList("FIR", availableSeats));
            premiumEconomy.add(flightServices.getSeatList("PME", availableSeats));
        }

        // Updates the model
        model.addAttribute("Dest", dest);
        model.addAttribute("Dep", dep);
        model.addAttribute("Flight", flight);
        model.addAttribute("businessClass", businessClass);
        model.addAttribute("economyClass", economyClass);
        model.addAttribute("firstClass", firstClass);
        model.addAttribute("premiumEconomy", premiumEconomy);
        model.addAttribute("usr", getSession(session));
        model.addAttribute("ID", id);

        // Updates the session
        getSession(session).setLastViewedFlight(flight);
        getSession(session).setBusClassSeatList(businessClass);
        getSession(session).setEcoClassSeatList(economyClass);
        getSession(session).setFirClassSeatList(firstClass);
        getSession(session).setPmeClassSeatList(premiumEconomy);

        return "StopoverFlight";
    }

    @RequestMapping("/flight")
    public String viewFlight(@RequestParam String id, Model model, HttpSession session) {
        Flight f = flightServices.getById(id);

        System.out.println(id);
        List<Availability> availableSeats = flightServices.getAvailability(f.getFlightNumber(), f.getDepartureTime());
        for(Availability a: availableSeats){
            System.out.println(a.getClassCode());
        }
        model.addAttribute("Dest", locationServices.getById(f.getDestinationCode()));
        model.addAttribute("Dep", locationServices.getById(f.getDepartureCode()));

        model.addAttribute("Flight", f);
        model.addAttribute("usr", getSession(session));

        model.addAttribute("businessClass", flightServices.getSeatList("BUS", availableSeats));
        model.addAttribute("economyClass", flightServices.getSeatList("ECO", availableSeats));
        model.addAttribute("firstClass", flightServices.getSeatList("FIR", availableSeats));
        model.addAttribute("premiumEconomy", flightServices.getSeatList("PME", availableSeats));

        getSession(session).setLastViewedFlightDirect(f);
        getSession(session).setBusClassSeatListDirect((List<String[]>) model.getAttribute("businessClass"));
        getSession(session).setEcoClassSeatListDirect((List<String[]>) model.getAttribute("economyClass"));
        getSession(session).setFirClassSeatListDirect((List<String[]>) model.getAttribute("firstClass"));
        getSession(session).setPmeClassSeatListDirect((List<String[]>) model.getAttribute("premiumEconomy"));

        return "Flight";
    }

    @RequestMapping("/admin/flight/management")
    @PostMapping("/admin/flight/management")
    public String modifyFlights(@ModelAttribute Flight flight, Model model, HttpSession session) {
        if (!getAdminSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        model.addAttribute("admin", getAdminSession(session));

        if (flight != null)
            flight = flightServices.getByFlightNumberAndDeparture(flight.getFlightNumber(), flight.getDepartureTime());

        // Generates all of the ID values for availability and price
        List<TicketClass> ticketClasses = ticketServices.getAllTicketClass();
        List<TicketType> ticketTypes = ticketServices.getAllTicketType();
        List<Availability> availabilities = new ArrayList<>();
        List<String> types = new ArrayList<>();
        List<String> classes = new ArrayList<>();
        List<String> id = new ArrayList<>();

        // Generates a List of PriceContainers as the default display
        for(TicketClass ticketClass : ticketClasses) {
            for(TicketType ticketType : ticketTypes) {
                availabilities.add(new Availability(ticketClass.getTicketClass(), ticketType.getTicketCode()));
                types.add(ticketType.getName());
                classes.add(ticketClass.getDetails());
                id.add(ticketClass.getTicketClass()+"-"+ticketType.getTicketCode());
            }
        }

        if (flight == null) flight = new Flight();
        else {
            List<Availability> flightAvailabilities = flightServices.getAvailability(flight.getFlightNumber(), flight.getDepartureTime());
            for(Availability availability : flightAvailabilities) {
                for (int count = 0; count < availabilities.size(); count++) {
                    if (availability.getClassCode().equals(availabilities.get(count).getClassCode()) && availability.getTicketCode().equals(availabilities.get(count).getTicketCode())) {
                        availabilities.set(count, availability);
                        break;
                    }
                }
            }
        }

        getSession(session).setAvailabilityID(id);
        model.addAttribute("container", new EditedFlightContainer(flight, availabilities));
        model.addAttribute("types", types);
        model.addAttribute("classes", classes);
        model.addAttribute("flight", flight);
        model.addAttribute("usr", getSession(session));

        return "Admin/FlightManagement";
    }

    @RequestMapping("/admin/location/management")
    @PostMapping("/admin/location/management")
    public String modifyLocation(@ModelAttribute Location location, Model model, HttpSession session) {
        if (!getAdminSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        model.addAttribute("admin", getAdminSession(session));

        if (location != null) {
            if (location.getLocationID() != null) {
                String id = location.getLocationID();
                Location queryResult = locationServices.getById(id);
                if (queryResult == null) {
                    queryResult = locationServices.findByLocation(id);
                }

                location = queryResult;
            }

            if (location == null) location = new Location();
        }

        model.addAttribute("location", location);
        model.addAttribute("usr", getSession(session));

        return "Admin/LocationManagement";
    }

    @RequestMapping("/admin/price/management")
    @PostMapping("/admin/price/management")
    public String modifyPrice(@ModelAttribute Price price, Model model, HttpSession session) {
        if (!getAdminSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        model.addAttribute("admin", getAdminSession(session));
        List<TicketClass> classes = ticketServices.getAllTicketClass();
        List<TicketType> types = ticketServices.getAllTicketType();


        // Populate with the specified Price object
        if (price != null) {
            if (price.getFlightNumber() != null && price.getStartDate() != null
                && price.getTicketCode() != null && price.getClassCode() != null) {

                // Changes the full Class name to its code
                for(TicketClass ticketClass : classes) {
                    if(ticketClass.getDetails().equals(price.getClassCode())) {
                        price.setClassCode(ticketClass.getTicketClass());
                        break;
                    }
                }

                // Changes the full Ticket type name to its code
                for(TicketType ticketType : types) {
                    if(ticketType.getName().equals(price.getTicketCode())) {
                        price.setTicketCode(ticketType.getTicketCode());
                        break;
                    }
                }
                price = flightServices.getSpecificPrice(price.getFlightNumber(), price.getStartDate(), price.getClassCode(), price.getTicketCode());
            }
        }

        // Populate with a default value if the price doesnt exist or one has not been provided
        if (price == null)
            price = new Price();

        model.addAttribute("Classes", classes);
        model.addAttribute("Types", types);
        model.addAttribute("Price", price);
        model.addAttribute("usr", getSession(session));

        return "Admin/PriceManagement";
    }

    @RequestMapping("/admin/airline/management")
    @PostMapping("/admin/airline/management")
    public String modifyPrice(@ModelAttribute Airlines airline, Model model, HttpSession session) {
        if (!getAdminSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        model.addAttribute("admin", getAdminSession(session));

        if(airline != null) {
            if(airline.getAirlineID() != null) {
                Airlines currentAirline;
                currentAirline = airlineServices.getAirlineByID(airline.getAirlineID());
                if(currentAirline == null)
                    currentAirline = airlineServices.getAirlineByAirlineName(airline.getAirlineID());
                airline = currentAirline;
            }
        }

        // Populate with a default value if the price doesnt exist or one has not been provided
        if (airline == null)
            airline = new Airlines();

        model.addAttribute("Airlines", airline);
        model.addAttribute("usr", getSession(session));

        return "Admin/AirlineManagement";
    }

    @RequestMapping("/wishlist") //e.g localhost:8080/flight/book?id=1001&seats=2
    public String bookFlight(@RequestParam(required = false) String id, @RequestParam(required = false) String remove, Model model, HttpSession session) {


        getSession(session).setFlightServices(flightServices);
        getSession(session).setUserServices(usrServices);
        if (remove != null) {
            try {
                getSession(session).removeFromWishList(remove);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                model.addAttribute("WishL", getSession(session).getWishList());
                model.addAttribute("usr", getSession(session));
                return "WishList";
            }

            model.addAttribute("WishL", getSession(session).getWishList());
            model.addAttribute("usr", getSession(session));
            return "WishList";
        }
        if (id != null) {

            Boolean accepted = getSession(session).addToWishList(id);
            Flight f = flightServices.getById(id);
            model.addAttribute("Flight", f);
            if (accepted) {
                model.addAttribute("WishL", getSession(session).getWishList());
                model.addAttribute("usr", getSession(session));
                return "WishList";
            }
        }


        model.addAttribute("WishL", getSession(session).getWishList());
        model.addAttribute("usr", getSession(session));

        return "WishList";
    }

    @PostMapping("/search")
    public String runSearch(@ModelAttribute BasicSearch search, Model model, HttpSession session) {
        model = addDateAndTimeToModel(model);
        List<Flight>[] flights = new ArrayList[2];
        List<StopOver>[] stopOver = new ArrayList[3];
        search.setFlightServices(flightServices);
        search.setLocationServices(locationServices);
        search.setAirlineServices(airlineServices);

        model.addAttribute("searchLocation", locationServices.listAll());

        // Increase the popularity of destination location
        locationServices.incrementPopularity(search.getDestinationIn());
        if (locationServices.findByLocation(search.getDestinationIn()) != null) {
            getSession(session).setLastSearchedDestination(search.getDestinationIn());
        }


        // Gathers Flights and Stopovers
        flights[0] = search.runBasicSearch(search.getStart(), search.getEnd(), false);
        search.setCheapestPriceForSearchResults(flights[0]);

        flights[1] = search.getPromotedFlights(flights[0]);
        stopOver[0] = search.basicStopOverSearch(1);
        stopOver[1] = search.basicStopOverSearch(2);
        stopOver[2] = search.basicStopOverSearch(3);
        search.setCheapestPriceForStopOverResults(stopOver[0]);
        search.setCheapestPriceForStopOverResults(stopOver[1]);
        search.setCheapestPriceForStopOverResults(stopOver[2]);


        model.addAttribute("count", flights[0].size() + flights[1].size() + stopOver[0].size() + stopOver[1].size() + stopOver[2].size());

        // Stops unnecessary objects from being added to the response
        if (flights[0] != null || flights[1] != null) model.addAttribute("flights", flights);
        if (stopOver[0] != null || stopOver[1] != null || stopOver[2] != null) model.addAttribute("stopOver", stopOver);

        model.addAttribute("search", search);
        model.addAttribute("usr", getSession(session));
        return "search";
    }

    @PostMapping("/advancedSearch")
    public String runAdvancedSearch(@ModelAttribute BasicSearch search, Model model, HttpSession session) {
        model = addDateAndTimeToModel(model);
        List<Flight>[] flights = new ArrayList[2];
        List<StopOver>[] stopOver = new ArrayList[3];
        search.setFlightServices(flightServices);
        search.setLocationServices(locationServices);
        search.setAirlineServices(airlineServices);

        model.addAttribute("searchLocation", locationServices.listAll());

        // Increase the popularity of destination location
        locationServices.incrementPopularity(search.getDestinationIn());

        // Gathers Flights and Stopovers
        flights[0] = search.runAdvancedSearch();
        search.setCheapestPriceForSearchResults(flights[0]);
        flights[1] = search.getPromotedFlights(flights[0]);
        if (!search.isDirectFlight()) {
            stopOver[0] = search.advancedStopOverSearch(1);
            stopOver[1] = search.advancedStopOverSearch(2);
            stopOver[2] = search.advancedStopOverSearch(3);
        }

        // Stops unnecessary objects from being added to the response
        if (flights[0] != null || flights[1] != null) {
            if (flights[0] != null && !flights[0].isEmpty() && flights[0].get(0).getCheapestPrice() == null)
                search.setCheapestPriceForSearchResults(flights[0]);

            model.addAttribute("flights", flights);
        }

        if (stopOver[0] != null || stopOver[1] != null || stopOver[2] != null) {
            search.setCheapestPriceForStopOverResults(stopOver[0]);
            search.setCheapestPriceForStopOverResults(stopOver[1]);
            search.setCheapestPriceForStopOverResults(stopOver[2]);
            model.addAttribute("stopOver", stopOver);

        }

        model.addAttribute("search", search);
        model.addAttribute("usr", getSession(session));
        return "search";
    }

    @RequestMapping("/cart")
    public String cart(Model model, HttpSession session) {
        if (!getSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        model.addAttribute("usr", getSession(session));

        List<BookingRequest> cart = getSession(session).getCart();

        if (cart != null) {
            for (int count = 0; count < cart.size(); ) {
                if (cart.get(count).getTotalSeats() <= 0) getSession(session).removeFromCart(cart.get(count).getId());
                else {
                    cart.get(count).setPrice(getBookingPrice(cart.get(count)));
                    count++;
                }
            }
        }

        getSession(session).setFlightServices(flightServices);
        model.addAttribute("usr", getSession(session));
        model.addAttribute("cart", getSession(session).getCart());


        return "Booking/Cart";
    }

    @PostMapping("/cart/direct")
    public String updateCart(@ModelAttribute BookingRequest bookingRequest, Model model, HttpSession session) {
        if (!getSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        model.addAttribute("Flight", getSession(session).getLastViewedFlight());

        bookingRequest.setFlight(getSession(session).getLastViewedFlight().get(0));
        bookingRequest.setBusSeats(getSession(session).getBusClassSeatList().get(0));
        bookingRequest.setEcoSeats(getSession(session).getEcoClassSeatList().get(0));
        bookingRequest.setFirSeats(getSession(session).getFirClassSeatList().get(0));
        bookingRequest.setPmeSeats(getSession(session).getPmeClassSeatList().get(0));
        getSession(session).addToCart(bookingRequest);

        model.addAttribute("usr", getSession(session));

        return "redirect:/cart";
    }

    @PostMapping("/cart/indirect")
    public String updateCartWithIndirect(@ModelAttribute BookingRequestContainer bookingRequest, Model model, HttpSession session) {
        if (!getSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        List<Flight> flights = getSession(session).getLastViewedFlight();
        BookingRequest[] requests = bookingRequest.getBookingRequest();
        model.addAttribute("Flight", flights);

        for (int count = 0; count < bookingRequest.size(); count++) {
            requests[count].setFlight(flights.get(count));
            requests[count].setBusSeats(getSession(session).getBusClassSeatList().get(count));
            requests[count].setEcoSeats(getSession(session).getEcoClassSeatList().get(count));
            requests[count].setFirSeats(getSession(session).getFirClassSeatList().get(count));
            requests[count].setPmeSeats(getSession(session).getPmeClassSeatList().get(count));
            getSession(session).addToCart(requests[count]);
        }

        model.addAttribute("usr", getSession(session));

        return "redirect:/cart";
    }

    @RequestMapping("/cart/remove")
    //e.g localhost:8080/location/add?id=Hob&country=Australia&location=Hobart&lat=-42.3&lng=147.3&pop=1
    public String removeFlightFromCart(@RequestParam String id, Model model, HttpSession session) {
        if (!getSession(session).isLoggedIn()) {
            return "redirect:/login";
        }
        getSession(session).removeFromCart(id);
        return "redirect:/cart";
    }

    @RequestMapping("/checkout")
    public String checkout(Model model, HttpSession session) {

        if (!getSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        List<BookingRequest> check = getSession(session).getCart();
        int totalSeats = 0;
        for(BookingRequest b: check){
            totalSeats += b.getTotalSeats();
        }
        if(totalSeats > 100){
            return "redirect:/cart?error=maxseats";
        }

        //getSession(session).setCheckedOutCart(getSession(session).getCart());
        model.addAttribute("checkout", getSession(session).getCart());
        model.addAttribute("usr", getSession(session));
        model.addAttribute("traveller", new Traveller());

        return "Booking/Checkout";
    }

    @PostMapping("/checkout")
    public String updateCheckout(@ModelAttribute TravellerContainer travellerContainer, Model model, HttpSession session) {
        if (!getSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        model.addAttribute("usr", getSession(session));

        String confirmationID = generateConfirmationID();
        String accountEmail = getSession(session).getEmail();

        Traveller[] travellers = travellerContainer.getTravellers();

        for (BookingRequest br : getSession(session).getCart()) {
            Booking booking;
            for (int i = 0; i < travellers.length; i++) {
                if(travellers[i] == null){
                    continue;
                }
                if (travellers[i].getAccountEmail() == null || travellers[i].getAccountEmail().equals("")) {
                    travellers[i].setAccountEmail(accountEmail);
                }

                if (travellers[i].getId() == null) {
                    travellers[i].setTravellerID(new ObjectId());
                }

                booking = new Booking(accountEmail, br.getFlight().getFlightID(), travellers[i].getId(), travellers[i].getSeat(), confirmationID);
                if (booking.getId() == null) {
                    booking.setBookingID(new ObjectId());
                }

                bookingServices.addTraveller(travellers[i]);
                bookingServices.getTravellers().add(travellers[i]);
                bookingServices.addBooking(booking);
                bookingServices.getBookings().add(booking);
            }
        }

        getSession(session).setConfirmationID(confirmationID);

        return "redirect:/bookingConfirmation";
    }

    @RequestMapping("/bookingConfirmation")
    public String bookingConfirmation(Model model, HttpSession session) {
        if (!getSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        model.addAttribute("usr", getSession(session));

        //getSession(session).setBookedCart(getSession(session).getCheckedOutCart());
        model.addAttribute("booking", getSession(session).getCart());

        String accountEmail = getSession(session).getEmail();
        String confirmationID = getSession(session).getConfirmationID();

        List<Booking> bookingDetails = bookingServices.getBookings();
        List<Traveller> travellerDetails = new ArrayList<>();

        for (int i = 0; i < bookingDetails.size(); i++) {
            for (int j = 0; j < bookingServices.getTravellers().size(); j++) {
                travellerDetails.add(bookingServices.getTravellers().get(j));
            }
        }

        model.addAttribute("bookingDetails", bookingDetails);
        model.addAttribute("travellers", travellerDetails);
        model.addAttribute("confirmationID", confirmationID);

        Email email = new Email();

        email.setEmailRecipient(accountEmail);
        email.setEmailSubject("FlightPub Booking Confirmation : " + confirmationID);
        email.setEmailBody("Thank you for booking with FlightPub. Your booking reference is "
                + confirmationID + ".\n\n" + "This email has been sent to " + accountEmail + ".\n\n"
                + "Please keep this email for your records.\n\n" + "We hope you enjoy your flight.\n\n"
                + "Kind regards,\n" + "FlightPub");

        try {
            emailServices.sendSimpleMail(email);
        } catch (HttpMessageNotReadableException e) {
            e.printStackTrace();
        }


        return "Confirmations/BookingConfirmation";
    }

    @PostMapping("/bookingConfirmation")
    public String bookingConfirmation(@ModelAttribute Booking booking, Model model, HttpSession session) {
        if (!getSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        getSession(session).setCart(null);
        getSession(session).setCheckedOutCart(null);
        getSession(session).setBookedCart(null);
        getSession(session).setConfirmationID(null);

        return "redirect:/bookingConfirmation";
    }

    protected String generateConfirmationID() {
        return RandomStringUtils.randomAlphanumeric(8).toUpperCase();
    }

    @RequestMapping("/bookingalert")
    public String booking(Model model, HttpSession session) {
        if (!getSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        model.addAttribute("usr", getSession(session));
        return "Notifications/booking";
    }

    @RequestMapping("/registeredalert")
    public String user(Model model, HttpSession session) {
        if (!getSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        model.addAttribute("usr", getSession(session));
        return "Notifications/newuser";
    }

    @RequestMapping("/AdminControl")
    public String adminControl(Model model, HttpSession session) {
        if (!getAdminSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        return "User/AdminControl";
    }

    @RequestMapping("/covidRestrict")
    public String covidRestrict(@RequestParam String covidRestrictedLocation, @RequestParam String covidRestriction, Model model, HttpSession session) {
        if (!getAdminSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        Location loc = locationServices.getById(covidRestrictedLocation);
        loc.setCovid_restricted(covidRestriction.equals("restrict"));
        locationServices.saveOrUpdate(loc);

        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("admin", getAdminSession(session));

        return "User/AdminControl";
    }

    @RequestMapping("HolidayPackage")
    public String holidayPackage(@ModelAttribute HolidayPackage hp, Model model, HttpSession session) {
        if (!getAdminSession(session).isLoggedIn()) {
            return "redirect:/login";
        }
        holidayPackageServices.saveOrUpdate(hp);
        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("admin", getAdminSession(session));

        return "User/AdminControl";
    }

    @RequestMapping("/userModification")
    public String userModification(@RequestParam String user, @RequestParam String userField, @RequestParam String modification, Model model, HttpSession session) {
        if (!getAdminSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        UserAccount userAccount = usrServices.getById(user);
        if (userAccount != null) {
            if (userField.equals("email")) {
                userAccount.setEmail(modification);
            } else if (userField.equals("firstName")) {
                userAccount.setFirstname(modification);
            } else if (userField.equals("password")) {
                userAccount.setPassword(modification);
            } else if (userField.equals("preferredAirport")) {
                userAccount.setPreferredAirport(modification);
            }
            usrServices.saveOrUpdate(userAccount);
        }
        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("admin", getAdminSession(session));

        return "User/AdminControl";
    }

    @RequestMapping("/userDelete")
    public String userModification(@RequestParam String userDelete, Model model, HttpSession session) {
        if (!getAdminSession(session).isLoggedIn()) {
            return "redirect:/login";
        }

        UserAccount userAccount = usrServices.getById(userDelete);
        if (userAccount != null) {
            usrServices.delete(userDelete);
        }
        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("admin", getAdminSession(session));

        return "User/AdminControl";
    }

    @RequestMapping("/addUser")
    public String addUser(@ModelAttribute UserAccount ua, Model model, HttpSession session) {
        if (!getAdminSession(session).isLoggedIn()) {
            return "redirect:/login";
        }
        usrServices.saveOrUpdate(ua);
        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("admin", getAdminSession(session));

        return "User/AdminControl";
    }


    private UserSession getSession(HttpSession session) {
        UserSession sessionUser = null;
        try {
            sessionUser = (UserSession) session.getAttribute("User");
        } catch (Exception e) {
        }

        if (sessionUser == null) {
            sessionUser = new UserSession(null);
            session.setAttribute("User", sessionUser);
        }

        return sessionUser;
    }

    private Model addDateAndTimeToModel(Model model) {
        // Get server time for flight date pickers
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String today = dateFormat.format(date);

        // get server time + 1 year for current max future booking date
        model.addAttribute("today", today); // Temp/placeholder
        cal.add(Calendar.YEAR, 1);
        date = cal.getTime();
        String max = dateFormat.format(date);
        model.addAttribute("max", max);
        return model;
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

    private double getBookingPrice(BookingRequest br) {
        double price = 0;
        for (String[] seat : br.getFirSeats()) {
            switch (seat[0]) {
                case "Standby":
                    if (br.getFirStandby().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getFirStandby());
                    }
                    break;
                case "Premium Discounted":
                    if (br.getFirPremDisc().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getFirPremDisc());
                    }
                    break;
                case "Discounted":
                    if (br.getFirDiscounted().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getFirDiscounted());
                    }
                    break;
                case "Standard":
                    if (br.getFirStandard().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getFirStandard());
                    }
                    break;
                case "Premium":
                    if (br.getFirPremium().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getFirPremium());
                    }
                    break;
                case "Long Distance":
                    if (br.getFirLongDistance().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getFirLongDistance());
                    }
                    break;
                case "Platinum":
                    if (br.getFirPlatinum().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getFirPlatinum());
                    }
                    break;
            }
            //System.out.println("First Class Tickets Price " + price);
        }
        for (String[] seat : br.getBusSeats()) {
            switch (seat[0]) {
                case "Standby":
                    if (br.getBusStandby().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getBusStandby());
                    }
                    break;
                case "Premium Discounted":
                    if (br.getBusPremDisc().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getBusPremDisc());
                    }
                    break;
                case "Discounted":
                    if (br.getBusDiscounted().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getBusDiscounted());
                    }
                    break;
                case "Standard":
                    if (br.getBusStandard().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getBusStandard());
                    }
                    break;
                case "Premium":
                    if (br.getBusPremium().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getBusPremium());
                    }
                    break;
                case "Long Distance":
                    if (br.getBusLongDistance().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getBusLongDistance());
                    }
                    break;
                case "Platinum":
                    if (br.getBusPlatinum().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getBusPlatinum());
                    }
                    break;
            }
            //System.out.println("Business Class Tickets Price " + price);
        }
        for (String[] seat : br.getPmeSeats()) {
            switch (seat[0]) {
                case "Standby":
                    if (br.getPmeStandby().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getPmeStandby());
                    }
                    break;
                case "Premium Discounted":
                    if (br.getPmePremDisc().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getPmePremDisc());
                    }
                    break;
                case "Discounted":
                    if (br.getPmeDiscounted().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getPmeDiscounted());
                    }
                    break;
                case "Standard":
                    if (br.getPmeStandard().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getPmeStandard());
                    }
                    break;
                case "Premium":
                    if (br.getPmePremium().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getPmePremium());
                    }
                    break;
                case "Long Distance":
                    if (br.getPmeLongDistance().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getPmeLongDistance());
                    }
                    break;
                case "Platinum":
                    if (br.getPmePlatinum().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getPmePlatinum());
                    }
                    break;
            }
            //System.out.println("Premium Economy Class Tickets Price " + price);
        }
        for (String[] seat : br.getEcoSeats()) {
            switch (seat[0]) {
                case "Standby":
                    if (br.getEcoStandby().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getEcoStandby());
                    }
                    break;
                case "Premium Discounted":
                    if (br.getEcoPremDisc().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getEcoPremDisc());
                    }
                    break;
                case "Discounted":
                    if (br.getEcoDiscounted().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getEcoDiscounted());
                    }
                    break;
                case "Standard":
                    if (br.getEcoStandard().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getEcoStandard());
                    }
                    break;
                case "Premium":
                    if (br.getEcoPremium().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getEcoPremium());
                    }
                    break;
                case "Long Distance":
                    if (br.getEcoLongDistance().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getEcoLongDistance());
                    }
                    break;
                case "Platinum":
                    if (br.getEcoPlatinum().equals("")) {
                        price += Double.parseDouble(seat[3]) * 0;
                    } else {
                        price += Double.parseDouble(seat[3]) * Integer.parseInt(br.getEcoPlatinum());
                    }
                    break;
            }
            //System.out.println("Eco Class Tickets Price " + price);
        }
        return price;
    }

    public List<HolidayPackage> getTravelPackages(){
        List<Airlines> sponsoredAirlines = airlineServices.getSponsoredAirlines();
        List<HolidayPackage> holidayPackages = holidayPackageServices.listAll();
        List<HolidayPackage> travelPackages = new LinkedList<>();
        for (Airlines airline : sponsoredAirlines) {
            for (HolidayPackage hp : holidayPackages) {
                if (airline.getAirlineName().equals(hp.getAirlineName())) {
                    travelPackages.add(hp);
                }
            }
        }
        return travelPackages;
    }

    public List<HolidayPackage> getUserHolidayPackages(HttpSession session){
        List<WishListItem> wishListItems = wishListServices.findAllByUserIDs(getSession(session).getUsr().getEmail());
        List<HolidayPackage> holidayPackages = holidayPackageServices.listAll();
        List<HolidayPackage> userHolidayPackages = new LinkedList<>();
        for (WishListItem wli : wishListItems) {
            for (HolidayPackage hp : holidayPackages) {
                if (wli.getDestinationID().equals(hp.getDestinationCode())) {
                    userHolidayPackages.add(hp);
                }
            }
        }
        return userHolidayPackages;
    }
}
