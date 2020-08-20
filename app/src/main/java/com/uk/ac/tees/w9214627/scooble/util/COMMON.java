package com.uk.ac.tees.w9214627.scooble.util;

import android.Manifest;

public class COMMON {

    public static final String
            USERS_REF = "US_USERS",
            USERS_ACC_STATUS_REF = "US_ACC_STATUS",
            USER_FULL_NAME = "US_FULL_NAME",
            USER_EMAIL_ID = "US_EMAIL_ID",
            USER_PHONE = "US_PHONE",
            USER_DOB = "US_DOB",
            USER_PROPICURL = "US_PROPICURL",

            AGENCY_REF = "US_AGENCY",
            AGENCY_NAME = "US_AGENCY_NAME",
            AGENCY_EMAIL = "US_AGENCY_EMAIL",
            AGENCY_PHONE = "US_AGENCY_PHONE",

            CAR_ID = "CAR_ID",
                    USER_ID = "USER_ID",
                    AGENCY_ID = "AGENCY_ID",
                    BOOKING_ID = "BOOKING_ID",
            BOOKINGS = "CAR_BOOKINGS",

            CARS_REF = "US_CARS",
            CAR_COMPANY = "CAR_COMPANY",
            CAR_MODEL = "CAR_MODEL",
            CAR_DESC = "CAR_DESC",
            CAR_SPEED = "CAR_SPEED",
            CAR_SEATS = "CAR_SEATS",
            CAR_ENGINE = "CAR_ENGINE",
            CAR_CITY = "CAR_CITY",
            CAR_LAT = "CAR_LAT",
            CAR_LNG = "CAR_LNG",
            CAR_PRICE = "CAR_PRICE",
            CAR_PICURL= "CAR_PICURL",
            CAR_AGENCY_ID = "CAR_AGENCY_ID",

            SAVED_USERS = "CAR_SAVES",
                    BOOKING_DATE = "BOOKING_DATE";

    public static final int LOCATION_REQUEST = 1000;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    public static String getMonth(String s) {
        String mon;
        switch (s)
        {
            case "1": mon = "Jan";
                break;
            case "2": mon = "Feb";
                break;
            case "3": mon = "Mar";
                break;
            case "4": mon = "Apr";
                break;
            case "5": mon = "May";
                break;
            case "6": mon = "Jun";
                break;
            case "7": mon = "Jul";
                break;
            case "8": mon = "Aug";
                break;
            case "9": mon = "Sep";
                break;
            case "10": mon = "Oct";
                break;
            case "11": mon = "Nov";
                break;
            case "12": mon = "Dec";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + s);
        }

        return mon;
    }
}
