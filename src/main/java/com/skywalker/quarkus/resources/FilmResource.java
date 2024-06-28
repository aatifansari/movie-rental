package com.skywalker.quarkus.resources;

import com.skywalker.quarkus.model.Film;
import com.skywalker.quarkus.repository.FilmRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.stream.Collectors;

@Path("/")
public class FilmResource {

    @Inject
    FilmRepository filmRepository;

    @GET
    @Path("helloWorld")
    @Produces(MediaType.TEXT_PLAIN)
    public String getText(){
        return "Hello World";
    }

    @GET
    @Path("/film/{filmId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Film getText(Short filmId){

//        try{
            return filmRepository.getFilm(filmId).orElse(null);
//        }catch(Exception ex){
//            throw new RuntimeException("Internal Server Error");
//        }

    }

    @GET
    @Path("/film/{pageNumber}/{minPageLength}")
    @Produces(MediaType.TEXT_PLAIN)
    public String paged(long pageNumber, short minPageLength){
        return filmRepository.paged(pageNumber, minPageLength)
                .map(film -> String.format("%s (%d min)", film.getTitle(), film.getLength()))
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("/actors/{startWith}/{minLength}")
    @Produces(MediaType.TEXT_PLAIN)
    public String actors(String startWith, short minLength){
        return filmRepository.actors(startWith, minLength)
                .map(film -> String.format(" %s, (%d min): %s",
                        film.getTitle(),
                        film.getLength(),
                        film.getActors().stream()
                                .map(actor -> String.format("%s %s", actor.getFirstName(), actor.getLastName()))
                                .collect(Collectors.joining(","))))
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("/film/update/{minLength}/{rentalRate}")
    @Produces(MediaType.TEXT_PLAIN)
    public String update(short minLength, short rentalRate){
        filmRepository.updateRentalRate(minLength, rentalRate);
        return filmRepository.getFilms(minLength)
                .map(film -> String.format("%s ( %d min) - $%f", film.getTitle(), film.getLength(), film.getRentalRate()))
                .collect(Collectors.joining("\n"));
    }

}
