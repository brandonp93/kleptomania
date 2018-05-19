/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.kleptomania.services;

import edu.eci.arsw.kleptomania.model.Player;
import edu.eci.arsw.kleptomania.model.Room;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;

/**
 *
 * @author xbran
 */
@Service
public class kleptomaniaServicesStub implements kleptomaniaServices{
    
    private final ConcurrentHashMap<Integer, Room> rooms = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, CopyOnWriteArrayList<Player>> players = new ConcurrentHashMap<>();


    
    @Override
    public void addThief(int roomNumber, Player player) throws kleptomaniaServicesException {
        /*if (!players.containsKey(player.getIdentification())) {
            
        } else {
           throw new kleptomaniaServicesException("This player have been joined already " + player);
        }*/
        players.get(roomNumber).add(player);  
        int a = 64+players.get(roomNumber).size();
        System.out.println("Prueba print");
        String nickname = player.getNickname();
        System.out.println("identification: " + a + " p: " + nickname );   
        player.setIdentification(Character.toString ((char) a));
        
    }


    @Override
    public void addCops(int roomNumber, Player p) throws kleptomaniaServicesException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Player> getThieves(int roomNumber) throws kleptomaniaServicesException {
        if(rooms.containsKey(roomNumber)){
            return players.get(roomNumber);
        }
        else{
            throw new kleptomaniaServicesException("This room doesn't exist " + roomNumber);
        }
        
    };

    @Override
    public List<Player> getCops(int roomNumber) throws kleptomaniaServicesException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addRoom(int roomNumber, Room r) throws kleptomaniaServicesException {
        if (rooms.containsKey(roomNumber)) {
            throw new kleptomaniaServicesException("This room have been created already " + roomNumber);
        } else {
            rooms.put(roomNumber,r);
            CopyOnWriteArrayList<Player> temp = new CopyOnWriteArrayList();
            Player firstPlayer = r.getHost();
            
            int a = 65;
            String nickname = firstPlayer.getNickname();
            System.out.println("identification: " + a + " p: " + nickname );   
            firstPlayer.setIdentification(Character.toString ((char) a));
            
            temp.add(firstPlayer);
            players.put(roomNumber,temp);
            for(Integer p: players.keySet()){
                String key =p.toString();
                CopyOnWriteArrayList<Player> value = players.get(p);  
                for(Player u: value){
                    System.out.println(key + " " + u.getNickname() + " aca");  
                }
                
            }
        }
    }
    
    @Override
    public CopyOnWriteArrayList<Room> getCurrentRooms() throws kleptomaniaServicesException{
        System.out.println("rooms api: " + rooms.keys());
        if(!rooms.isEmpty()){
            CopyOnWriteArrayList<Room> currentRooms = new CopyOnWriteArrayList();
            for(Room r: rooms.values()){
                currentRooms.add(r);
            }
            return currentRooms;
        }
        else{
            throw new kleptomaniaServicesException("There are no rooms");
        }
    }

    @Override
    public String getId(int roomNumber, String nickname) throws kleptomaniaServicesException{
        CopyOnWriteArrayList<Player> currentRoomPlayers = players.get(roomNumber);
        String id = null;
        for (Player p : currentRoomPlayers) {
            if(p.getNickname().contentEquals(nickname)){
                id = p.getIdentification();
            }
        }
        return id;

    }

}
