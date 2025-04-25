package org.Backend;

public class Chain {
    private Restaurant[] restaurants; // an array of restaurants we have
    private int numberOfRestaurants; // Number of restaurants we have in our Chain
    private String chainName;  // The chain name

    public Chain(String chainName, int maxRestaurants) {  // the constructor for the chain
        // takes Chain name and number of restaurant we gonna have
        this.chainName = chainName;
        this.restaurants = new Restaurant[maxRestaurants]; //  instantiate the array with number of the restaurants
        this.numberOfRestaurants = 0; // Set the number of restaurant to 0 since yet we have no object inside it
    }

    public boolean addRestaurant(Restaurant restaurant) {   // a method to add a restaurant object to the array
        if (numberOfRestaurants >= restaurants.length) { // check first if there is place in the array
            return false; // if no place is there return false
        }
        for (int i = 0; i < restaurants.length; i++) { // Loop to go through all the array and find the first null place
            if (restaurants[i] == null) {
                restaurants[i] = restaurant; // assign that object to the Free place
                numberOfRestaurants++; // increment the number of restaurants
                return true; // return true
            }
        }
        return false; // otherwise return false
    }

    public boolean reserveRestaurant(int numberOfPeople, String restaurantName, String reservationName) {
        // Looks up the Restaurant object by its name (getRestaurant), then calls its reserveTables
        Restaurant restaurant = getRestaurant(restaurantName);
        return restaurant != null && restaurant.reserveTables(numberOfPeople, reservationName);
        // Returns false if either the name is wrong or the reservation fails

    }

    public Restaurant getRestaurant(String name) {  // Return the restaurant object by name
        for (Restaurant restaurant : restaurants) {
            if (restaurant != null && restaurant.getName().equals(name)) {
                return restaurant; // returning the object
            }
        }
        return null; // no object with the same name then return null
    }

    public Restaurant searchRestaurant(int numberOfPeople, String restaurantName) {
        int startPos = getRestaurantPosition(restaurantName);
        if (startPos == -1) return null; // if the restaurant doesn't exist, it will return null

        int currentPos = startPos; // The position of the book
        do {
            Restaurant current = restaurants[currentPos]; // creating a new object of restaurant and assigning the restaurant we have to it
            if (current != null && current.hasAvailableTables(numberOfPeople)) { // check the restaurant if it has any free tables
                return current; // if yes ---> return that restaurant
            }
            currentPos = (currentPos + 1) % restaurants.length; // incrementing the currentPos and checking the Resto of %6
        } while (currentPos != startPos); //

        return null;  // If no restaurant has the same name or no restaurant has an available table return false
    }




    public String getAvailabilityAcrossRestaurants(int numberOfPeople){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < restaurants.length; i++) {
            sb.append("Restaurant: ").append(" ").append(restaurants[i].getName()).append("  ").append(restaurants[i].getAvailibilityReport(numberOfPeople)).append("\n");
        }
        return sb.toString().trim();
    }



    private int getRestaurantPosition(String name) { // Used in searchRestaurant to find the position of the restaurant in the array
        for (int i = 0; i < restaurants.length; i++) {
            if (restaurants[i] != null && restaurants[i].getName().equals(name)) {
                return i;
            }
        }
        return -1; // No restaurant with such name, return -1
    }

}


