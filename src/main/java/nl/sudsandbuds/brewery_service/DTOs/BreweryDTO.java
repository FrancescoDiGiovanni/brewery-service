package nl.sudsandbuds.brewery_service.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BreweryDTO {
    private String id;
    private String name;
    private String brewery_type;
    private String address_1;
    private String address_2;
    private String address_3;
    private String city;
    private String state_province;
    private String postal_code;
    private String country;
    private String longitude;
    private String latitude;
    private String phone;
    private String website_url;
    private String state;
    private String street;
}
