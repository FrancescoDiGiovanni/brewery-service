package nl.sudsandbuds.brewery_service.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BreweryDTO {
    private String id;
    private String name;
    private String breweryType;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String longitude;
    private String latitude;
    private String phone;
    private String websiteUrl;
    private String state;
    private String street;
}
