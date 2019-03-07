/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.persistence.impl;

import edu.eci.arsw.cinema.filter.Filter;
import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cristian
 */

@Service
public class InMemoryCinemaPersistence implements CinemaPersitence{
	
    @Autowired
	private Filter filter = null;
	
    private final ConcurrentHashMap<String,Cinema> cinemas=new ConcurrentHashMap<>();
    
    

    public InMemoryCinemaPersistence() {
    	
        //load stub data
    	
        String functionDate = "2018-12-18 15:30";
        List<CinemaFunction> functions1= new ArrayList<>();
        List<CinemaFunction> functions2= new ArrayList<>();
        List<CinemaFunction> functions3= new ArrayList<>();
        CinemaFunction funct1 = new CinemaFunction(new Movie("SuperHeroes Movie","Action"),functionDate);
        CinemaFunction funct2 = new CinemaFunction(new Movie("The Night","Horror"),functionDate);
        CinemaFunction funct3 = new CinemaFunction(new Movie("Green Book","Mystery"),functionDate);
        CinemaFunction funct4 = new CinemaFunction(new Movie("Harry Potter","Fantasy"),functionDate);
        CinemaFunction funct5 = new CinemaFunction(new Movie("A star is born","Romance"),functionDate);
        CinemaFunction funct6 = new CinemaFunction(new Movie("The Favorite","Drama"),functionDate);
        functions1.add(funct1);
        functions1.add(funct2);
        functions2.add(funct3);
        functions2.add(funct4);
        functions3.add(funct5);
        functions3.add(funct6);
        Cinema c1=new Cinema("cinemaX",functions1);
        Cinema c2=new Cinema("cinePolis",functions2);
        Cinema c3=new Cinema("cineMania",functions3);
        cinemas.put("cinemaX", c1);
        cinemas.put("cinePolis", c2);
        cinemas.put("cineMania", c3);
    }    
    
    
    
    
    @Override
    public void buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException {
        try {
            Cinema cine = cinemas.get(cinema);
            for (CinemaFunction funcion : cine.getFunctions()) {
                if(funcion.getDate().equals(date) && funcion.getMovie().getName().equals(movieName)){
                    funcion.buyTicket(row, col);
                }
            }
        } catch (Exception e) {
        	throw new CinemaException("Seat booked"); 
        }
        
    }

    
    
    @Override
    public List<CinemaFunction> getFunctionsbyCinemaAndDate(String cinema, String date) throws CinemaException {
        try {
            Cinema cine=cinemas.get(cinema);
            List<CinemaFunction> funciones = new ArrayList<CinemaFunction>();
            for (CinemaFunction funcion : cine.getFunctions()) {
                if(funcion.getDate().equals(date)){
                    funciones.add(funcion);
                }
            }
            
            return funciones; 
        } catch (Exception e) {
        	throw new CinemaException("Cinema not found"); 
        }
        
    }
    
    @Override
    public CinemaFunction getFunctionsbyCinemaDateName(String cinema, String date,String name) throws CinemaException {
    	CinemaFunction cinemaFunction = null;
    	try {
    		Cinema cine = cinemas.get(cinema);

    		for (CinemaFunction funcion : cine.getFunctions()) {
    			if (funcion.getMovie().getName().equals(name)) {
    				cinemaFunction = funcion;
    			}
    		}
    	}catch (Exception e) {
			throw new CinemaException("Cinema not found");
		}
		
		

		return cinemaFunction;
        
    }

    @Override
    public void saveCinema(Cinema c) throws CinemaPersistenceException {
    	
        if (cinemas.containsKey(c.getName())){
            
            throw new CinemaPersistenceException("The given cinema already exists: "+c.getName());
        }
        else{
            cinemas.put(c.getName(),c);
        }   
    }

    @Override
    public Cinema getCinemaByName(String name) throws CinemaPersistenceException {
    	if(cinemas.get(name) == null) throw new CinemaPersistenceException("cine no encontrado");
        return cinemas.get(name);
    }
    
    @Override
    public Map<String,Cinema> getCinemas (){
    	return this.cinemas;
    }




	@Override
	public List<Movie> filterMovies(String cinema, String fecha, String filter) throws CinemaException {
		Cinema c = cinemas.get(cinema);
		List<Movie> movies= this.filter.filter(c, fecha, filter);
		return movies;
	}
	
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
    
 
    
    
    
    
    
}
