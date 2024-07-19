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
import java.util.Optional;

@FeignClient(name = "open-brewery-client", url = "${open-brewery-api.uri}")
public interface OpenBreweryApiClient {

    @GetMapping(path = "/breweries", produces = "application/json")
    ResponseEntity<List<BreweryDTO>> getBreweriesWithParameters(
            @RequestParam("by_name") Optional<String> name,
            @RequestParam("by_city") Optional<String> city,
            @RequestParam("by_state") Optional<String> state,
            @RequestParam("by_type") Optional<String> type,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("per_page") Optional<Integer> perPage
    );

    @GetMapping(path = "/breweries/{id}", produces = "application/json")
    ResponseEntity<BreweryDTO> getBrewery(
            @PathVariable("id") String id
    );

    @GetMapping(path = "/breweries/autocomplete", produces = "application/json")
    ResponseEntity<List<BreweryAutocompleteDTO>> autocompleteBreweries(
            @RequestParam("query") String query
    );

    @GetMapping(path = "/breweries/meta", produces = "application/json")
    ResponseEntity<BreweryMetaData> getBreweriesMeta(
            @RequestParam("by_name") Optional<String> name,
            @RequestParam("by_city") Optional<String> city,
            @RequestParam("by_state") Optional<String> state,
            @RequestParam("by_type") Optional<String> type,
            @RequestParam("per_page") Optional<Integer> perPage
    );
}
