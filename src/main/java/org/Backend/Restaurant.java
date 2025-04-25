package org.Backend;

public class Restaurant {
    private String name;     // The reservation name
    private Table[] tables;  // array of tables
    private int totalTables;
    private int availableTables; // sum of both totaltables + aditionalTables
    private final int additionalTables = 2;
    private int currentTableIndex;    // this is an index to start looking for a free table from the position
    // of the last booked table

    public Restaurant(String name, int totalTables) { // constructor
        this.name = name;
        this.totalTables = totalTables;
        this.availableTables = totalTables + additionalTables;
        this.tables = new Table[totalTables + additionalTables]; // Builds room for all your Table objects
        for (int i = 0; i < tables.length; i++) { // Actually creates each Table instance
            tables[i] = new Table();
        }
        this.currentTableIndex = 0; // Start from the first table for any upcoming reservation
    }



    //////////////////////EXAM PART/////////////////////////////////////////////////

    public String getAvailibilityReport(int numberOfPeople){
        int tablesNeeded = (int) Math.ceil((double) numberOfPeople / Table.CAPACITY);
        StringBuilder sb = new StringBuilder();
        int free=0;
        int occupied=0;
        String condition;
        for (int i = 0; i < tables.length ; i++) {
            if(tables[i].isOccupied()){
                occupied++;
            }
            else{
                free++;
            }
        }
        if(free>=tablesNeeded){
            condition = "Yes";
        }
        else{
            condition = "No";
        }
        sb.append("  Available tables : ").append(free).append(". Occupied tables ").append(occupied).append(". Enough capacity : ").append(condition);
        return sb.toString().trim();
    }


    public boolean reserveTables(int numberOfPeople, String reservationName) {
        // Divide the party size by 6, round up with Math.ceil.
        //
        //E.g. 7 people → 2 tables.
        int tablesNeeded = (int) Math.ceil((double) numberOfPeople / Table.CAPACITY);
        if (tablesNeeded <= 0 || tablesNeeded > availableTables) { // return false if the tablesNeeded are less than 0 or more than available tables
            return false;
        }

        int startIndex = currentTableIndex;
        for (int i = 0; i < tablesNeeded; i++) {
            int index = (startIndex + i) % tables.length;
            if (tables[index].isOccupied()) {
                return false;
            }
        }

        for (int i = 0; i < tablesNeeded; i++) { // Make reservation for all the tables we need
            // If we have one table then one table is reserved
            // if we need 2? then we have a loop to assign the name and the capacity of the table
            // then we get
            // Table 1: reserved by soulaimane
            // table 2: reserved by soulaimane
            // in case we need two !!!!!!!!!
            int index = (currentTableIndex + i) % tables.length;
            tables[index].reserve(Table.CAPACITY, reservationName);
        }
        currentTableIndex = (currentTableIndex + tablesNeeded) % tables.length; // here we save the index where we have the last reserved table
        // so we need
        availableTables -= tablesNeeded; // we decrement the number of available tables by how many tables we reserved
        return true;
    }

    public String getName() {
        return name;
    }

    public boolean hasAvailableTables(int numberOfPeople) {
        int tablesNeeded = (int) Math.ceil((double) numberOfPeople / Table.CAPACITY);
        return availableTables >= tablesNeeded;
    }

    public String availableTablesInfo(int numberOfPeople) {
        int tablesNeeded = (int) Math.ceil((double) numberOfPeople / Table.CAPACITY);
        StringBuilder info = new StringBuilder();
        int count = 0;
        int index = currentTableIndex;

        for (int i = 0; i < tables.length && count < tablesNeeded; i++) {
            Table table = tables[index];
            if (!table.isOccupied()) {
                info.append("Table ").append(index + 1).append(": ").append(table).append("\n");
                count++;
            }
            index = (index + 1) % tables.length;
        }
        return count >= tablesNeeded ? info.toString().trim() : "Not enough tables available.";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Restaurant: ").append(name).append("\n");
        for (int i = 0; i < tables.length; i++) {
            sb.append("Table ").append(i + 1).append(": ").append(tables[i]).append("\n");
        }
        return sb.toString().trim();
    }
}