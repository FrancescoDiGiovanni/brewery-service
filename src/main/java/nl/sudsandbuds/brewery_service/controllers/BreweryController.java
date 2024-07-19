package nl.sudsandbuds.brewery_service.controllers;

import feign.FeignException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
     */
    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<Response<List<BreweryDTO>>> getBreweries(
            @RequestParam("name") Optional<String> name,
            @RequestParam("city") Optional<String> city,
            @RequestParam("state") Optional<String> state,
            @RequestParam("type") Optional<String> type,
            @RequestParam("offset") Optional<Integer> offset,
            @RequestParam("limit") Optional<Integer> limit
    ) throws ServiceHttpStatusException {
        try {
            ResponseEntity<List<BreweryDTO>> responseEntityFromOpenBreweryApiClient = openBreweryApiClient.getBreweriesWithParameters(name, city, state, type, offset, limit);

            List<BreweryDTO> breweryDTOList = responseEntityFromOpenBreweryApiClient.getBody();

            String okMessage = "breweries list successfully loaded";
            return ResponseUtility.buildSuccessResponseEntity(okMessage, breweryDTOList, log);
        } catch ( FeignException e ) {
            return ResponseUtility.buildResponseEntityFromFeignClientException(e, "BREWERY-SERVICE@");
        }
    }

    /**
     * https://api.openbrewerydb.org/v1/breweries/{id}
     * API that return details of a single brewery
     * @Param id: String - id of the brewery
     */
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Response<BreweryDTO>> getBrewery(@PathVariable("id") String id) throws ServiceHttpStatusException {
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
     */
    @GetMapping(path="/autocomplete", produces = "application/json")
    public ResponseEntity<Response<List<BreweryAutocompleteDTO>>> autocompleteBreweries(
            @RequestParam("query") String query
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
     */
    @GetMapping(path = "/meta", produces = "application/json")
    public ResponseEntity<Response<BreweryMetaData>> getBreweriesMeta(
            @RequestParam("name") Optional<String> name,
            @RequestParam("city") Optional<String> city,
            @RequestParam("state") Optional<String> state,
            @RequestParam("type") Optional<String> type,
            @RequestParam("limit") Optional<Integer> limit
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
