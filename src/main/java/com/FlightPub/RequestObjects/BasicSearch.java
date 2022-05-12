package com.FlightPub.RequestObjects;


import com.FlightPub.Services.FlightServices;
import com.FlightPub.Services.LocationServices;
import com.FlightPub.model.Flight;
import com.FlightPub.model.Location;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thymeleaf.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class BasicSearch {
    @Getter
    @Setter
    private String destinationIn;
    @Getter
    @Setter
    private String originIn;

    @Getter
    @Setter
    private String start;

    @Getter
    @Setter
    private String end;

    @Getter
    @Setter
    private boolean nodest;

    @Getter
    @Setter
    private boolean exactdate;

    private FlightServices flightServices;

    private LocationServices locService;

    BasicSearch(String destination, String origin){
        this.originIn = origin;
        this.destinationIn = destination;

    }

    @Autowired
    @Qualifier(value = "FlightServices")
    public void setFlightServices(FlightServices flightService) {
        this.flightServices = flightService;
    }

    @Autowired
    @Qualifier(value = "LocationServices")
    public void setLocationServices(LocationServices locRepo) {
        this.locService = locRepo;
    }

    public BasicSearch(){}

    public List<Flight> runBasicSearch(String start, String end) throws ParseException {
        Date dstart = new SimpleDateFormat("yyyy-MM-dd").parse(start);
        Date dend = null;

        if(end == null || end.equals("")) {
            dend = endOfDay(dstart);
        }
        else{
            dend = new SimpleDateFormat("yyyy-MM-dd").parse(end);
            dend = endOfDay(dend);
        }


        Location originObj = locService.findByLocation(originIn);
        Location destinationObj = locService.findByLocation(destinationIn);

        if (originObj != null && destinationObj != null)
            return flightServices.getByOriginAndDestination(originObj.getLocationID(), destinationObj.getLocationID(), dstart, dend);

        else if(originObj != null && destinationObj == null)
            return flightServices.getByOrigin(originObj.getLocationID(), dstart, dend);

        else
            return flightServices.getByOriginAndDestination(originIn, destinationIn, dstart, dend);
    }

    private Date endOfDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        return calendar.getTime();
    }

    public List<Flight> runAdvancedSearch() {
        return null;
    }
}
