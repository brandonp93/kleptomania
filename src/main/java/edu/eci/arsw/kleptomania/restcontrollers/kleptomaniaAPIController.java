/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.kleptomania.restcontrollers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.eci.arsw.kleptomania.model.Player;
import edu.eci.arsw.kleptomania.model.Room;
import edu.eci.arsw.kleptomania.services.kleptomaniaServices;
import edu.eci.arsw.kleptomania.services.kleptomaniaServicesException;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author xbran
 */

@RestController
@RequestMapping(value = "/lobby")
public class kleptomaniaAPIController {
    
    @Autowired
    kleptomaniaServices services;
    
    @Autowired
    SimpMessagingTemplate msgt;
    
     @RequestMapping(method = RequestMethod.GET)
        public ResponseEntity<?> controllerGetRooms() throws kleptomaniaServicesException{
            try {
                    //obtener datos que se enviarán a través del API
                    return new ResponseEntity<>(services.getCurrentRooms(),HttpStatus.ACCEPTED);
            } catch (kleptomaniaServicesException ex) {
                    Logger.getLogger(kleptomaniaServicesException.class.getName()).log(Level.SEVERE, null, ex);
                    return new ResponseEntity<>("There are no rooms: " + ex,HttpStatus.NOT_FOUND);
            }  
        } 
    
    @RequestMapping(path = "/{roomNumber}/thief", method = RequestMethod.GET)
        public ResponseEntity<?> controllerGetThievesRoom(@PathVariable String roomNumber) throws kleptomaniaServicesException{
            try {
                    //obtener datos que se enviarán a través del API
                    return new ResponseEntity<>(services.getThieves(Integer.parseInt(roomNumber)),HttpStatus.ACCEPTED);
            } catch (kleptomaniaServicesException ex) {
                    Logger.getLogger(kleptomaniaServicesException.class.getName()).log(Level.SEVERE, null, ex);
                    return new ResponseEntity<>("There are no thieves in this room: " + ex,HttpStatus.NOT_FOUND);
            }  
        } 

    @RequestMapping(path = "/{room}/thief",method = RequestMethod.PUT)
    public ResponseEntity<?> addThief(@PathVariable("room") String roomNumber,@RequestBody Player player) throws kleptomaniaServicesException {
           
            try {       
                System.out.println("PUT Player: "+ player.getNickname());
                services.addThief(Integer.parseInt(roomNumber), player);
                msgt.convertAndSend("/topic/currentPlayers." + roomNumber,services.getThieves(Integer.parseInt(roomNumber)));     
            } catch (kleptomaniaServicesException ex) {
                Logger.getLogger(kleptomaniaAPIController.class.getName()).log(Level.SEVERE, null, ex);
                return new ResponseEntity<>("We've a problem joining a new thief: ",HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> controllerPostRoom(@RequestBody Room r) throws kleptomaniaServicesException {
        synchronized(services){
            try{

                if(r.getRoomNumber() == 0){
                     throw new kleptomaniaServicesException("Insert a valid number room");
                }
                else{
                    services.addRoom(r.getRoomNumber(),r);
                    msgt.convertAndSend("/topic/currentRooms.",services.getCurrentRooms());
                }
                
            }
            catch(kleptomaniaServicesException ex){
                Logger.getLogger(kleptomaniaAPIController.class.getName()).log(Level.SEVERE, null, ex);
                return new ResponseEntity<>("Somenthing went wrong creating a new Room: POST METHOD ",HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
    
}
