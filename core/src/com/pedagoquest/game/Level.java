package com.pedagoquest.game;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private List<Room> rooms;

    public Level() {
        this.rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }


    public List<Room> getRooms() {
        return rooms;
    }

    public boolean isCellOccupied(int x, int y) {
        for (Room room : rooms) {
            if (room.contains(x, y)) {
                return true;
            }
        }


        return false;
    } 
}
