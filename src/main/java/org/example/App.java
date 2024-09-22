package org.example;

import java.io.IOException;
import java.util.*;


class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String userOption;

        boolean valid = false;
        do {

            // Meny
            System.out.println("    \n" +
                    "                Elpriser\n" +
                    "                ========\n" +
                    "                1. Inmatning\n" +
                    "                2. Min, Max och Medel\n" +
                    "                3. Sortera\n" +
                    "                4. Bästa Laddningstid (4h)\n" +
                    "                5. Visualize\n" +
                    "                e. Avsluta\n");


            userOption = scanner.nextLine();
            userOption = userOption.toLowerCase();

            loop:
            // Menu case options
            switch (userOption) {

                case "1" -> {
                    valid = true;
                    System.out.print("\nInmatning");
                    inputPrice(scanner);

                    break loop;
                }

                case "2" -> {
                    System.out.print("\nMin Max och Medel");
                    minMax(scanner);
                    break loop;
                }

                case "3" -> {
                    System.out.print("\nSortera");
                    comparison(scanner);
                    break loop;

                }


                case "4" -> {
                    System.out.print("\nBästa Laddningstid");
                    chargetime(scanner);

                }

                case "5" -> {

                    System.out.print("\nVisualize\n");
                    visualization(scanner);


                }


                case "e" -> {


                    System.out.print("\nAvslutar programmet");

                }

                default -> {
                    System.out.print("\nfel input, försök igen");
                    break;

                }
            }
        } while (!userOption.equals("e"));


    }

    // Ange pris per timma
    public static void inputPrice(Scanner scanner) {


        for (int index = 0; index < 24; index++) {
            boolean avsluta = false;
            System.out.print("\n\n Snälla ange pris i öre per kWh \n" +
                    "Du anger nu pris för timma: " + index + "-" + (index + 1) + "\n\n\n"
                    + "Om du råkat skriva fel skriv: 't' för tillbaka\n"
                    + "Eller skriv;                  'a' för avsluta");

            // lägger till priset i index och två positioner för jämnförelse senare + feedback
            if (scanner.hasNextInt()) {
                prices[index] = scanner.nextInt();
                comparisonnumbers[index][0] = prices[index];
                comparisonnumbers[index][1] = index;
                System.out.print("\nYou added " + prices[index] + "!\n");
                scanner.nextLine();
                // returns instead of restarting
            } else if (scanner.nextLine().equals("t")) {

                System.out.print("\nGår tillbaka två steg");
                index = index - 2;
            } else if (index <= -1) {
                index = 0;
                // to prevent indexing back too far
                System.out.print("\nKan inte gå tillbaka längre");
            } else if (scanner.nextLine().equals("a")) {
                avsluta = true;
            } else {  // prevents missinputs
                System.out.print("\nFel inmatning! Ange ett nummer i kWh eller 't' " +
                        "för tillbaka eller " +
                        "a för avsluta\n" + "försök igen");
                index = index - 1;

            }


            if (avsluta) {
                System.out.print("\nAvslutar programmet");
                break;

            }


        }
    }

    public static int[] prices = new int[24];
    public static int[][] comparisonnumbers = new int[24][2];

    public static void minMax(Scanner scanner) {

        System.out.print("\nVisar minimum, maximum och medelvärdet\n");
        Locale.setDefault(new Locale("sv", "SE"));
        double average = 0;
        int sum = 0;

        for (int price : prices) {
            sum = sum + price;
        }

        // minmax values
        int minValue = Arrays.stream(prices).min().orElse(Integer.MAX_VALUE);
        int maxValue = Arrays.stream(prices).max().orElse(Integer.MIN_VALUE);

        // Array to Arraylist
        ArrayList<Integer> convertedarraylist = new ArrayList<>();
        for (int index : prices) {
            convertedarraylist.add(index);

        }


        ArrayList<Integer> timelist = new ArrayList<>();
        ArrayList<Integer> timelist2 = new ArrayList<>();

        // count to find max index position(hour)

        int count = 0;
        int count2 = 0;
        while (count < convertedarraylist.size()) {

            if (convertedarraylist.get(count) == maxValue) {

                timelist.add(count);
            }
            count++;
        }
        // count to find min index position(hour)
        while (count2 < convertedarraylist.size()) {

            if (convertedarraylist.get(count2) == minValue) {

                timelist2.add(count2);
            }
            count2++;
        }


        // temp Sorterar // insertionsorting method

        for (int i = 1; i < prices.length; i++) {

            int currentValue = prices[i];


            int j = i - 1;

            while (j >= 0 && prices[j] > currentValue) {
                prices[j + 1] = prices[j];
                j--;
            }
            prices[j + 1] = currentValue;

        }

        average = sum / 24;

        // prints tempsorted min value
        System.out.print("==================================================================\n"
                + "\nMinsta priset är " + prices[0] + " öre mellan klockan: \n ");

        for (int i = 0; i < timelist2.size(); i++) {

            System.out.print(timelist2.get(i) + " - ");
            int totime = timelist2.get(i);
            totime = totime + 1;
            System.out.print(totime + " ");

            while (i != timelist2.size() - 1) { // om det finns samma värden så printas det ut
                System.out.print(" och klockan ");

                break;
            }

        }

        System.out.print("\n\n-----------------------------------------------\n");
        // prints tempsorted max value
        System.out.print("Största priset är " + prices[23] + " öre mellan klockan:\n ");
        for (int i = 0; i < timelist.size(); i++) {

            System.out.print(timelist.get(i) + " - ");
            int totime = timelist.get(i);
            totime = totime + 1;
            System.out.print(totime);

            while (i != timelist.size() - 1) {
                System.out.print(" och klockan "); // om det finns samma värden så printas det ut

                break;
            }

        }
        System.out.printf("\n\n\n==||== Medelvärdet av priset är %.2f öre " +
                "\n==================================================================", average);

        promptEnterKey();
    }


    public static void comparison(Scanner scanner) {

        Arrays.sort(comparisonnumbers, Comparator.comparingInt(a -> -a[0]));

        // prints original index position with value sorted

        System.out.print("\nPriser sorterade med tidstämpel \n");
        for (int index = 0; index < 24; index++) {
            int tempnumberextender = comparisonnumbers[index][1] + 1;
            System.out.print("\n\n" + comparisonnumbers[index][0] + " öre klockan " + comparisonnumbers[index][1]
                    + " - " + tempnumberextender);


        }
        promptEnterKey();
    }


    // reads input from user in bytesize like hitting enter

    public static void promptEnterKey() {
        System.out.print("\n\nTryck \"ENTER\" för att fortsätta...");
        try {
            int read = System.in.read(new byte[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void chargetime(Scanner scanner) {


        int mincost = Integer.MAX_VALUE;     // tracks minimum sum
        int startH = 0;            // keeps track of start hour

        Locale.setDefault(new Locale("sv", "SE"));

        for (int index = 0; index <= 20; index++) {      // itterates through 21 possible sets of 4

            int currentprice = prices[index] + prices[index + 1]   // sum the price
                    + prices[index + 2] + prices[index + 3];

            if (currentprice < mincost) {            // check if its lowest sum found so far
                mincost = currentprice;
                startH = index;

            }

        }

        double average = mincost / 4.0;
        System.out.printf("\nBästa tid att börja ladda batteriet en 4 timmar frammåt är klockan: " + startH + " - " + (startH = startH + 1)
                + "\nDå behöver du betala " + mincost + " öre" +
                "\nMedelpriset är:  %.2f  öre", average);

        promptEnterKey();
    }


    public static void visualization(Scanner scanner) {
                    // minmax added
        Integer maxPrice = Arrays.stream(prices).max().getAsInt();
        Integer minPrice = Arrays.stream(prices).min().getAsInt();

        int height = 6;  // scale roof
        int length = 24; // hours a day

        for (int index = height; index >= 0; index--) {

            int scaler = minPrice + (maxPrice - minPrice) * index / height;  // scales a threshhold for each row of the graph

            System.out.printf("|%4d | ", scaler);


            for (int j = 0; j < length; j++) {

                if (prices[j] >= scaler) {  // if the price exceeds threshhold for the current row it will print x else empty

                    System.out.print(" x ");

                } else {
                    System.out.print("   ");
                }

            }
            System.out.println();   // a new line added for the next row
        }


        System.out.print("|_____|________________________________________________________________________|");
        System.out.print("\n| Tim |- 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23|");
        promptEnterKey();

    }

}




