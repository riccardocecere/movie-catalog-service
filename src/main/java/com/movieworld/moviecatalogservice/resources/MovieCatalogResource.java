package com.movieworld.moviecatalogservice.resources;

import com.movieworld.moviecatalogservice.models.CatalogItem;
import com.movieworld.moviecatalogservice.models.Movie;
import com.movieworld.moviecatalogservice.models.Rating;
import com.movieworld.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplete;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){


        UserRating ratings = restTemplete.getForObject("http://rating-data-service/ratingsdata/users/"+userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating ->{
                Movie movie = restTemplete.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);

                return new CatalogItem(movie.getName(),"Desc", rating.getRating());
        })
                .collect(Collectors.toList());

    }
}


/*
                Movie movie = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8082/movies/"+rating.getMovieId())
                        .retrieve()
                        .bodyToMono(Movie.class)
                        .block();
                */