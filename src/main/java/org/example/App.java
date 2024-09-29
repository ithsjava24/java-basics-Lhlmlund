package org.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;


class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String userOption;
        boolean isTestMode = false;


        do {

            System.out.println("""
                Elpriser
                ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                5. Visualisering
                e. Avsluta
                """);

            userOption = scanner.nextLine();
            userOption = userOption.toLowerCase();

            loop:
            // Menu case options
            switch (userOption) {

                case "1" -> {

                    inputPrice(scanner);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(outputStream);
                    PrintStream originalOut = System.out;
                    try {
                        System.setOut(printStream);
                        visualization(scanner);
                        System.out.flush();
                        consoleOutput = outputStream.toString();
                        System.out.println(consoleOutput);

                    } finally { System.setOut(originalOut); }

                }

                case "2" -> {

                    minMax(scanner);

                }

                case "3" -> {

                    comparison(scanner);


                }


                case "4" -> {
                    chargetime(scanner);

                }

                case "5" -> {

                   visualization(scanner);
                  // System.out.print(consoleOutput);

                }


                case "e" -> {


                    System.out.print("\nAvslutar programmet");

                }

                default -> {
                    System.out.print("\nfel input, försök igen");

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
    public static Integer[][] comparisonnumbers = new Integer[24][2];
    public static int[] nonsortedprices = new int[24];
    public static String consoleOutput = "";

    public static void minMax(Scanner scanner) {

        Locale.setDefault(new Locale("sv", "SE"));
        double average = 0;
        int sum = 0;

        for (int price : prices) {
            sum = sum + price;
        }

        // minmax values
        String minhour = "";
        String maxhour = "";
        int minValue = Arrays.stream(prices).min().orElse(Integer.MAX_VALUE);
        int maxValue = Arrays.stream(prices).max().orElse(Integer.MIN_VALUE);

        // Array to Arraylist


        for (int i = 0; i < prices.length; i++) {

            if (prices[i] == minValue) {
                minhour = time(i);
            }
            if (prices[i] == maxValue) {
                maxhour = time(i);
            }
            if  (!minhour.isEmpty() && !maxhour.isEmpty()) {
                break;
            }
        }


        average = (double) sum / 24;




        System.out.printf("Lägsta pris: %s, %d öre/kWh\n", minhour, minValue);
        System.out.printf("Högsta pris: %s, %d öre/kWh\n", maxhour, maxValue);
        System.out.printf("Medelpris: %.2f öre/kWh\n", average);



    }


    public static void comparison(Scanner scanner) {

        Arrays.sort(comparisonnumbers, Comparator.comparingInt(a -> -a[0]));

        // prints original index position with value sorted


        for (Integer[] timedprices : comparisonnumbers) {

            System.out.printf("%02d-%02d %d öre\n", timedprices[1], timedprices[1] + 1, timedprices[0]);


        }

    }


    // reads input from user in bytesize like hitting enter

    public static void promptEnterKey() {
        try {
            int read = System.in.read(new byte[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void chargetime(Scanner scanner) {

        nonsortedprices = prices.clone();
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
        System.out.printf("  Påbörja laddning klockan %02d\n", startH);
        System.out.printf("Medelpris 4h: %.1f öre/kWh\n", average);



    }


    private static String time(int hour) {
        return String.format("%02d-%02d", hour, hour + 1);

    }




    public static void visualization(Scanner scanner) {
        // minmax added
        Integer maxPrice = Arrays.stream(prices).max().getAsInt();
        Integer minPrice = Arrays.stream(prices).min().getAsInt();


        int height = 6;  // scale roof
        int length = 24; // hours a day

        int maxlength = Math.max(String.format("%d", maxPrice).length(), String.format("%d", minPrice).length());

        for (int index = 0; index < height; index++) {

            int scalerthreshhold = minPrice + (maxPrice - minPrice) * (height - 1 - index) / (height - 1);  // scales a threshhold for each row of the graph


            if (index == 0) {
                System.out.printf("%"+ maxlength + "d| ", maxPrice);

            } else if (index == height- 1) {
                System.out.printf("%"+ maxlength + "d| ", minPrice);
            } else System.out.printf("%" + maxlength + "s| ", "");


            for (int j = 0; j < length; j++) {

                if (prices[j] >= scalerthreshhold) {  // if the price exceeds threshhold for the current row it will print x else empty

                    System.out.print(" x ");

                } else {
                    System.out.print("   ");
                }



            }
            System.out.println();   // a new line added for the next row
        }

        String spacer = " ".repeat(maxlength);
        System.out.print(spacer + "|------------------------------------------------------------------------\n");
        System.out.print(spacer + "| 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23\n\n\n");

    }

}




