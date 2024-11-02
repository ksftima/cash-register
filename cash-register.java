import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * This program manages inventory and sales for a retail store.
 Steps:
 * 1. Initialize the items and sales arrays along with other necessary variables and constants.
 * 2. Display the menu options to the user.
 *    - Receive user input for menu selection.
 * 3. Based on the user's menu selection:
 *    3a. If the user selects to insert items:
 *       - Prompt the user to enter the number of items to add.
 *       - Insert the specified number of items into the items array.
 *       - Update the last item number.
 *    3b. If the user selects to remove an item:
 *       - Prompt the user to enter the item ID of the item to remove.
 *       - Remove the item from the items array if found.
 *    3c. If the user selects to display a list of items:
 *       - Print the item details (ID, quantity, price) for all items in the items array.
 *    3d. If the user selects to register a sale:
 *       - Prompt the user to enter the item ID and quantity to sell.
 *       - Update the sales array with the sale details if the item is found and there's enough quantity.
 *    3e. If the user selects to display sales history:
 *       - Print the sales history including item number, quantity sold, total amount, and sale date.
 *    3f. If the user selects to sort and display the sales history table:
 *       - Sort the sales table by item number in ascending order.
 *       - Print the sorted sales table.
 *    3g. If the user chooses to quit:
 *       - Terminate the program.
 * 4. Write methods for each menu action such as inserting items, removing items, selling items, printing items, printing sales, and sorting sales and ensure error handling for invalid input.
 *
 */

public class Main {

    // Constants for the item array
    public static final int ITEM_ID = 0;
    public static final int ITEM_COUNT = 1;
    public static final int ITEM_PRICE = 2;
    public static final int ITEM_COLUMN_SIZE = 3;
    public static final int INITIAL_ITEM_SIZE = 10;

    // Constants for the sales array
    public static final int SALE_ITEM_ID = 0;
    public static final int SALE_ITEM_COUNT = 1;
    public static final int SALE_ITEM_PRICE = 2;
    public static final int SALE_COLUMN_SIZE = 3;
    public static final int MAX_SALES = 1000;

    // Other constants
    public static final int MENU_ITEM_1 = 1;
    public static final int MENU_ITEM_2 = 2;
    public static final int MENU_ITEM_3 = 3;
    public static final int MENU_ITEM_4 = 4;
    public static final int MENU_ITEM_5 = 5;
    public static final int MENU_ITEM_6 = 6;
    public static final int MENU_ITEM_Q = -1;

    public static final int INITIAL_ITEM_NUMBER = 999;
    public static final int RANDOM_COUNT = 10;
    public static final int RANDOM_PRICE = 900;
    public static final int MIN_PRICE = 100;

    private static Scanner userInputScanner = new Scanner(System.in);

    /**
     * This method should be used only for unit testing on CodeGrade. Do not change this method!
     * Swaps userInputScanner with a custom scanner object bound to a test input stream
     *
     * @param inputScanner test scanner object
     */
    public static void injectInput(final Scanner inputScanner) {
        userInputScanner = inputScanner;
    }

    public static void main(final String[] args) {
        int[][] items = new int[INITIAL_ITEM_SIZE][ITEM_COLUMN_SIZE];
        int[][] sales = new int[MAX_SALES][SALE_COLUMN_SIZE];
        Date[] saleDates = new Date[MAX_SALES];
        int lastItemNumber = INITIAL_ITEM_NUMBER;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while (true) {
            int userSelection = menu();
            switch (userSelection) {
                case MENU_ITEM_1:
                    System.out.println("How many items do you want to add? ");
                    int noOfItems = input();
                    items = insertItems(items, lastItemNumber, noOfItems);
                    lastItemNumber = lastItemNumber + noOfItems;
                    System.out.println("Items added!");
                    break;

                case MENU_ITEM_2:
                    System.out.println("Please enter the item ID of the item you want to remove: ");
                    int itemId = input();
                    int result = removeItem(items, itemId);
                    break;

                case MENU_ITEM_3:
                    printItems(items);
                    break;

                case MENU_ITEM_4:
                    System.out.println("Please enter the item ID and quantity to sell: ");
                    int itemToSell = input();
                    int quantityToSell = input();
                    sellItem(sales, saleDates, items, itemToSell, quantityToSell);
                    break;

                case MENU_ITEM_5:
                    printSales(sales, saleDates, dateFormat);
                    break;

                case MENU_ITEM_6:
                    sortedTable(sales, saleDates, dateFormat);
                    break;

                case MENU_ITEM_Q:
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid input");
                    return;
            }
        }
    }

    /**
     * Displays the menu for the user.
     *
     * @return input by the user
     */
    public static int menu() {
        //Show the Menu
        System.out.println("1. Insert Items");
        System.out.println("2. Remove an Item");
        System.out.println("3. Display a list of items");
        System.out.println("4. Register a sale");
        System.out.println("5. Display sales history");
        System.out.println("6. Sort and display sales history table");
        System.out.println("q. Quit");
        return input();
    }

    /**
     * Receives user input and handles the input for menu.
     *
     * @return integer as input by the user or -1 for 'q'.
     */
    public static int input() {
        while (true) {
            if (userInputScanner.hasNextInt()) {
                return userInputScanner.nextInt();
            } else {
                String userInput = userInputScanner.next();
                if (userInput.equalsIgnoreCase("q")) {
                    return -1;
                } else {
                    System.out.println("Invalid input");
                }
            }
        }
    }

    /**
     * Gets the actual size of the items array.
     *
     * @param items the array of items
     * @return the actual size of the items array
     */
    public static int getActualSize(final int[][] items) {
        int count = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i][0] != 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Inserts the specified number of items in the array after first checking that the number of free spaces is sufficient.
     * @param items the array of items
     * @param itemNumber the last used item number
     * @param noOfItems the number of items to insert
     * @return the updated array of items
     */
    public static int[][] insertItems(final int[][] items, final int itemNumber, final int noOfItems) {
        int newItemNumber = itemNumber + 1;
        int[][] workingArray = items;

        if (checkFull(items, noOfItems)) {
            workingArray = extendArray(items, noOfItems);
        }

        int lastItem = getActualSize(workingArray);

        for (int i = lastItem; i < lastItem + noOfItems; i++) {
            workingArray[i][ITEM_ID] = newItemNumber++;
            workingArray[i][ITEM_COUNT] = (int) (Math.random() * RANDOM_COUNT) + 1;
            workingArray[i][ITEM_PRICE] = (int) (Math.random() * RANDOM_PRICE) + MIN_PRICE;
        }
        return workingArray;
    }

    /**
     * Counts the number of free slots in the items array.
     *
     * @param items     the array of items
     * @return the number of free slots
     */
    public static int getFreeSlots(final int[][] items) {
        int count = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i][ITEM_ID] == 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if the array cannot hold the specified number of new items.
     *
     * @param items     the array of items
     * @param noOfItems the number of items to insert
     * @return true if the array is full or cannot hold the requested number of items, false otherwise
     */
    public static boolean checkFull(final int[][] items, final int noOfItems) {
        int freeSlots = getFreeSlots(items);
        if (noOfItems > freeSlots) {
            return true;
        }
        return false;
    }

    /**
     * Extends the size of the items array by the specified number of items.
     *
     * @param items     the array of items
     * @param noOfItems the number of items to add to the array
     * @return the extended array of items
     */
    public static int[][] extendArray(final int[][] items, final int noOfItems) {
        int[][] newItemsArray = new int[items.length + noOfItems][ITEM_COLUMN_SIZE];

        for (int i = 0; i < items.length; i++) {
            newItemsArray[i] = items[i];
        }
        return newItemsArray;
    }

    /**
     * Removes the item based on the item ID provided by the user
     *
     * @param items     the array of items
     * @param itemId    the ID of the items
     * @return 0 if item is removed and -1 if the item is not found
     */
    public static int removeItem(final int[][] items, final int itemId) {
        for (int i = 0; i < items.length; i++) {
            if (items[i][ITEM_ID] == itemId) {
                // Shift all subsequent items one position to the left
                for (int j = i; j < items.length - 1; j++) {
                    items[j][ITEM_ID] = items[j + 1][ITEM_ID];
                    items[j][ITEM_COUNT] = items[j + 1][ITEM_COUNT];
                    items[j][ITEM_PRICE] = items[j + 1][ITEM_PRICE];
                }
                // Clear the last item
                items[items.length - 1][ITEM_ID] = 0;
                items[items.length - 1][ITEM_COUNT] = 0;
                items[items.length - 1][ITEM_PRICE] = 0;
                System.out.println("Item removed!");
                return 0;
            }
        }
        System.out.println("Could not find item!");
        return -1;
    }


    /**
     * Prints item number, number, and price for all items that have an item number.
     *
     * @param items     the array of items
     */
    public static void printItems(final int[][] items) {
        System.out.println("ITEM ID   |QUANTITY   |PRICE      |");
        for (int i = 0; i < items.length; i++) {
            if (items[i][ITEM_ID] != 0) {
                System.out.printf("%10d| %10d| %10d|%n", items[i][ITEM_ID], items[i][ITEM_COUNT], items[i][ITEM_PRICE]);
            }
        }
    }

    /**
     * Maintains inventory of items for sale.
     * @param items     the array of items
     * @param sales     array representing sales data
     * @param itemIdToSell  the item ID to be sold
     * @param amountToSell  the number of items to be sold
     * @param salesDate     array representing the date of the sale
     * @return 0 if the number of items available are less than the user requested, -1 if the item is not found
     */
    public static int sellItem(final int[][] sales, final Date[] salesDate, final int[][] items, final int itemIdToSell, final int amountToSell) {
        while (true) {
            boolean itemFound = false;
            for (int i = 0; i < items.length; i++) {
                if (items[i][ITEM_ID] == itemIdToSell) {
                    itemFound = true;

                    if (items[i][ITEM_COUNT] < amountToSell) {
                        System.out.println("Failed to sell specified amount!");
                        return items[i][ITEM_COUNT];
                    } else {
                        items[i][ITEM_COUNT] -= amountToSell;

                        for (int j = 0; j < sales.length; j++) {
                            if (sales[j][SALE_ITEM_ID] == 0 && salesDate[j] == null) {
                                sales[j][SALE_ITEM_ID] = itemIdToSell;
                                sales[j][SALE_ITEM_COUNT] = amountToSell;
                                sales[j][SALE_ITEM_PRICE] = amountToSell * items[i][ITEM_PRICE];
                                salesDate[j] = new Date();
                                System.out.println("Item sold!");
                                return 0;
                            }
                        }
                    }
                    break; // Exit the loop once the item is found
                }
            }

            if (!itemFound) {
                System.out.println("Could not find item! Try again: ");
                return -1;
            }

            return 0;
        }
    }


    /**
     * Displays sales data.
     * @param sales         array representing sales data
     * @param salesDate     array representing the date of the sale.
     * @param dateFormat    displays date in the desired format
     */
    public static void printSales(final int[][] sales, final Date[] salesDate, final SimpleDateFormat dateFormat) {
        System.out.println("Sales History:");
        System.out.println("Item Number | Quantity Sold | Total Amount|Date                |");
        for (int i = 0; i < sales.length; i++) {
            if (sales[i][SALE_ITEM_ID] != 0) {
                System.out.printf("%-11d | %14d | %13d|%-20s%n|", sales[i][SALE_ITEM_ID], sales[i][SALE_ITEM_COUNT], sales[i][SALE_ITEM_PRICE], dateFormat.format(salesDate[i]));
            }
        }
    }

    /**
     * Sorts the selling table by item number in ascending order.
     * @param sales         array representing sales data
     * @param salesDate     array representing the date of the sale.
     * @param dateFormat    displays date in the desired format
     */
    public static void sortedTable(final int[][] sales, final Date[] salesDate, final SimpleDateFormat dateFormat) {
        //bubble sort
        for (int i = 0; i < sales.length - 1; i++) {
            for (int j = 0; j < sales.length - i - 1; j++) {
                if (sales[j][SALE_ITEM_ID] > sales[j + 1][SALE_ITEM_ID]) {
                    int[] tempSales = sales[j];
                    sales[j] = sales[j + 1];
                    sales[j + 1] = tempSales;

                    //swap dates
                    Date tempDate = salesDate[j];
                    salesDate[j] = salesDate[j + 1];
                    salesDate[j + 1] = tempDate;
                }
            }
        }
        //printing the sorted table
        System.out.println("Sorted Sales Table:");
        System.out.println("Item Number | Quantity Sold | Total Amount| Date                |");
        for (int i = 0; i < sales.length; i++) {
            if (sales[i][SALE_ITEM_ID] != 0) {
                System.out.printf("%-11d | %14d | %13d|%20s%n|", sales[i][SALE_ITEM_ID], sales[i][SALE_ITEM_COUNT], sales[i][SALE_ITEM_PRICE], dateFormat.format(salesDate[i]));
            }
        }
    }
}