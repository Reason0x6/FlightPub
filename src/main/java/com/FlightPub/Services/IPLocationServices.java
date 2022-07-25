package com.FlightPub.Services;

import com.FlightPub.model.IPLocation;

/*import com.FlightPub.model.Location;
import com.FlightPub.repository.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class IPLocationServices {
    /*Need To work*/

    public IPLocation getIp(String ip) {
        String key = "9d6jinufcfdfacc213c7ddf4ef911dfe97b55e4696be3732bf8302876c09ebad06b";
        ip = ip.trim();
        IPLocation IpReal = new IPLocation();
        //Check System.out.println(ip);
        try {
            if (ip.contains(",")) {
                String temp_ip[] = ip.split(",");
                ip = temp_ip[1].trim();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println("The ip adress is after " + ip + " split");
        URL url;
        try {
            url = new URL("https://api.ipinfodb.com/v3/ip-city/?key=" + key + "&ip=" + ip);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String TempString = "";
            String Temp = "";
            String TempArr[] = null;

            while (null != (TempString = br.readLine())) {
                Temp = TempString;
            }
            TempArr = Temp.split(";");

            int length = TempArr.length;

            if (length == 11) {
                IpReal.setIPAddress(ip);
                if (TempArr[3] != null) {
                    IpReal.setCountryCode(TempArr[3]);
                }
                if (TempArr[4] != null) {
                    IpReal.setCountry(TempArr[4]);
                }
                if (TempArr[5] != null) {
                    IpReal.setState(TempArr[5]);
                }
                if (TempArr[6] != null) {
                    IpReal.setCity(TempArr[6]);
                }
                if (TempArr[7] != null) {           /*Need to remove - Zip is not needed*/
                    IpReal.setZip(TempArr[7]);
                }
                if (TempArr[8] != null) {
                    IpReal.setLatitude(TempArr[8]);
                }
                if (TempArr[9] != null) {
                    IpReal.setLongitude(TempArr[9]);
                }
                if (TempArr[10] != null) {
                    IpReal.setUtcOffset(TempArr[10]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IpReal;
    }

}
