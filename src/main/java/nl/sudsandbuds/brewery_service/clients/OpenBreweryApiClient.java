package nl.sudsandbuds.brewery_service.clients;

import nl.sudsandbuds.brewery_service.DTOs.BreweryAutocompleteDTO;
import nl.sudsandbuds.brewery_service.DTOs.BreweryDTO;
import nl.sudsandbuds.brewery_service.models.BreweryMetaData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Client interface that contains mapping of external open brewery api
 */
@FeignClient(name = "open-brewery-client", url = "${open-brewery-api.uri}")
public interface OpenBreweryApiClient {

    /**
     * Mapping of OpenBreweryApi that returns a list of breweries depending on filters
     * @param name filters by name
     * @param city filters by city
     * @param state filters by state
     * @param type filters by type
     * @param ids filters by ids separated by comma
     * @param page pagination offset
     * @param perPage pagination limit
     */
    @GetMapping(path = "/breweries", produces = "application/json")
    ResponseEntity<List<BreweryDTO>> getBreweriesWithParameters(
            @RequestParam("by_name") String name,
            @RequestParam("by_city") String city,
            @RequestParam("by_state") String state,
            @RequestParam("by_type") String type,
            @RequestParam("by_ids") String ids,
            @RequestParam("page") Integer page,
            @RequestParam("per_page") Integer perPage
    );

    /**
     * Mapping of OpenBreweryApi that returns a single brewery depending on its id
     * @param id id of the brewery
     */
    @GetMapping(path = "/breweries/{id}", produces = "application/json")
    ResponseEntity<BreweryDTO> getBrewery(
            @PathVariable("id") String id
    );

    /**
     * Mapping of OpenBreweryApi that returns a list of brewery titles and ids
     * @param query filtering query
     * @return
     */
    @GetMapping(path = "/breweries/autocomplete", produces = "application/json")
    ResponseEntity<List<BreweryAutocompleteDTO>> autocompleteBreweries(
            @RequestParam("query") String query
    );

    /**
     * Mapping of OpenBreweryApi that returns pagination metadata depending on filters
     * @param name filters by name
     * @param city filters by city
     * @param state filters by state
     * @param type filters by brewery type
     * @param perPage pagination limit
     */
    @GetMapping(path = "/breweries/meta", produces = "application/json")
    ResponseEntity<BreweryMetaData> getBreweriesMeta(
            @RequestParam("by_name") String name,
            @RequestParam("by_city") String city,
            @RequestParam("by_state") String state,
            @RequestParam("by_type") String type,
            @RequestParam("per_page") Integer perPage
    );
}
