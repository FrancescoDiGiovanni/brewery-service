package nl.sudsandbuds.brewery_service.controllers;

import feign.FeignException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.sudsandbuds.brewery_service.DTOs.BreweryAutocompleteDTO;
import nl.sudsandbuds.brewery_service.DTOs.BreweryDTO;
import nl.sudsandbuds.brewery_service.clients.OpenBreweryApiClient;
import nl.sudsandbuds.brewery_service.models.BreweryMetaData;
import nl.sudsandbuds.exceptions.ServiceHttpStatusException;
import nl.sudsandbuds.utilities.EncodeUtilities;
import nl.sudsandbuds.utilities.Response;
import nl.sudsandbuds.utilities.ResponseUtility;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("brewery")
@AllArgsConstructor
public class BreweryController {
    @Autowired
    OpenBreweryApiClient openBreweryApiClient;

    /**
     * API that return a list of Breweries
     * @Param name: String - Name of the brewery
     * @Param city: String - City of the brewery
     * @Param state: String - State of the brewery
     * @Param type: String - Type of the brewery
     * @Param page: Integer - Offset of Brewery list for pagination
     * @Param perPage: Integer - Limit of results per page on Brewery List for pagination
     * @Return List<BreweryDTO> - Return a list of brewery
     */
    @CrossOrigin
    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<Response<List<BreweryDTO>>> getBreweries(
            @Size(max = 40, message = "Name filter must be long from 0 to 40 characters") @RequestParam(value = "name", required = false)
            String name,
            @Size(max = 15, message = "City filter must be long from 0 to 15 characters") @RequestParam(value = "city", required = false)
            String city,
            @Size(max = 15, message = "State filter must be long from 0 to 15 characters") @RequestParam(value = "state", required = false)
            String state,
            @Size(max = 10, message = "Type filter must be long from 0 to 10 characters") @RequestParam(value = "type", required = false)
            String type,
            @RequestParam(value = "ids", required = false)
            String ids,
            @Range(min=1, message = "Offset minimum is 1") @RequestParam(value = "offset", required = false)
            Integer offset,
            @Range(min=1, max=50, message = "Limit must be from 1 to 50") @RequestParam(value = "limit", required = false)
            Integer limit
    ) throws ServiceHttpStatusException {
        try {
            ResponseEntity<List<BreweryDTO>> responseEntityFromOpenBreweryApiClient = openBreweryApiClient.getBreweriesWithParameters(name, city, state, type, ids, offset, limit);

            List<BreweryDTO> breweryDTOList = responseEntityFromOpenBreweryApiClient.getBody();

            String okMessage = "Breweries list successfully loaded";
            return ResponseUtility.buildSuccessResponseEntity(okMessage, breweryDTOList, log);
        } catch ( FeignException e ) {
            return ResponseUtility.buildResponseEntityFromFeignClientException(e, "BREWERY-SERVICE@");
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * https://api.openbrewerydb.org/v1/breweries/{id}
     * API that return details of a single brewery
     * @Param id: String - id of the brewery
     * @Return BreweryDTO - return a specific brewery
     */
    @CrossOrigin
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Response<BreweryDTO>> getBrewery(
            @NotEmpty(message = "Id cannot be empty")
            @NotNull(message = "Id cannot be null")
            @Size(max = 40, message = "Id maximum length is 40 characters")
            @PathVariable("id")
            String id
    ) throws ServiceHttpStatusException {
        try {
            ResponseEntity<BreweryDTO> responseEntityFromOpenBreweryClient = openBreweryApiClient.getBrewery(id);

            BreweryDTO breweryDTO = responseEntityFromOpenBreweryClient.getBody();

            String okMessage = "Brewery successfully loaded";
            return ResponseUtility.buildSuccessResponseEntity(okMessage, breweryDTO, log);
        } catch ( FeignException e ) {
            return ResponseUtility.buildResponseEntityFromFeignClientException(e, "BREWERY-SERVICE@");
        }
    }

    /**
     * API that return a list of breweries names for autocomplete inputs
     * @Param query: String - a search query
     * @Return BroweryAutocompleteDTO a list of brewery name for autocomplete inputs
     */
    @CrossOrigin
    @GetMapping(path="/autocomplete", produces = "application/json")
    public ResponseEntity<Response<List<BreweryAutocompleteDTO>>> autocompleteBreweries(
            @RequestParam("query")
            String query
    ) throws ServiceHttpStatusException {
        String encodedQuery = EncodeUtilities.encodeURI(query);
        ResponseEntity<List<BreweryAutocompleteDTO>> responseEntityFromOpenBreweryApiClient = openBreweryApiClient.autocompleteBreweries(encodedQuery);

        List<BreweryAutocompleteDTO> breweryAutocompleteDTOList = responseEntityFromOpenBreweryApiClient.getBody();

        String okMessage = "Autocomplete names list successfully loaded";
        return ResponseUtility.buildSuccessResponseEntity(okMessage, breweryAutocompleteDTOList, log);
    }

    /**
     * API that return brewery list metadata for pagination
     * @Param name: String - Name of the brewery
     * @Param city: String - City of the brewery
     * @Param state: String - State of the brewery
     * @Param type: String - Type of the brewery
     * @Param perPage: Integer - Limit of results per page on Brewery List for pagination
     * @Return BreweryMetaData - A object of metadata for pagination
     */
    @CrossOrigin
    @GetMapping(path = "/meta", produces = "application/json")
    public ResponseEntity<Response<BreweryMetaData>> getBreweriesMeta(
            @Size(max = 40, message = "Name filter must be long from 0 to 40 characters") @RequestParam(value = "name", required = false)
            String name,
            @Size(max = 15, message = "City filter must be long from 0 to 15 characters") @RequestParam(value = "city", required = false)
            String city,
            @Size(max = 15, message = "State filter must be long from 0 to 15 characters") @RequestParam(value = "state", required = false)
            String state,
            @Size(max = 10, message = "Type filter must be long from 0 to 10 characters") @RequestParam(value = "type", required = false)
            String type,
            @Range(min=1, max=50, message = "Limit must be from 1 to 50") @RequestParam(value = "limit", required = false)
            Integer limit
    ) throws ServiceHttpStatusException {
        try {
            ResponseEntity<BreweryMetaData> responseEntityFromOpenBreweryApiClient = openBreweryApiClient.getBreweriesMeta(name, city, state, type, limit);

            BreweryMetaData breweryMetaData = responseEntityFromOpenBreweryApiClient.getBody();

            String okMessage = "Breweries meta data successfully loaded";
            return ResponseUtility.buildSuccessResponseEntity(okMessage, breweryMetaData, log);
        } catch ( FeignException e ) {
            return ResponseUtility.buildResponseEntityFromFeignClientException(e, "BREWERY-SERVICE@");
        }
    }

}
