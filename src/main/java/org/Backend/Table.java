package org.Backend;

public class Table {
    public static final int CAPACITY = 6;
    private int occupiedSeats;
    private boolean isOccupied;
    public String reservationName;

    public Table() { // constructor
        this.isOccupied = false; // false for not occupied
        this.occupiedSeats = 0;  // set zero for occupied seats in this table
        this.reservationName = null; // set the reservation to null
    }

    public void reserve(int numberOfPeople, String reservationName) { // taking number of people and reservation name as input
        if (numberOfPeople > CAPACITY || numberOfPeople <= 0) {  // check if the number of people exceed the  capacity or equal or lower than zero
            throw new IllegalArgumentException("Invalid number of people"); // if one of those are true then throw this mesg
        } // if everything goes well do this
        this.occupiedSeats = numberOfPeople; // set number of people to occupiedSeats
        this.isOccupied = true; // change the table to occupied
        this.reservationName = reservationName; // set the reservation name
    }

    public boolean isOccupied() {  // helper function to check if the table is occupied
        return isOccupied;
    }

    @Override
    public String toString() {
        return isOccupied ?
                "Occupied " + occupiedSeats+ " by " + reservationName:
                "Available";
    }
}