package com.pooja.spanvia.app;

import com.pooja.spanvia.model.HeritageSite;
import com.pooja.spanvia.service.SpanviaService;
import com.pooja.spanvia.util.SpanviaRules;

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        SpanviaService service = new SpanviaService();

        // Load dataset from CSV file
        service.loadInitialData("../data/heritage_sites.csv");

        int choice;

        do {
            System.out.println("\n===== SPANVIA Heritage Tourism Backend =====");
            System.out.println("1. View all sites");
            System.out.println("2. View festivals");
            System.out.println("3. Search by state");
            System.out.println("4. Search by keyword");
            System.out.println("5. Budget recommendation");
            System.out.println("6. Show heritage rules");
            System.out.println("7. Show site counter");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    service.displayAllSites();
                    break;

                case 2:
                    service.displayFestivals();
                    break;

                case 3:
                    System.out.print("Enter state: ");
                    service.searchByState(sc.nextLine());
                    break;

                case 4:
                    System.out.print("Enter keyword: ");
                    service.searchByKeyword(sc.nextLine());
                    break;

                case 5:
                    System.out.print("Enter maximum budget: ₹");
                    service.recommendByBudget(sc.nextDouble());
                    break;

                case 6:
                    SpanviaRules.showRules();
                    break;

                case 7:
                    service.showSiteCount();
                    System.out.println(
                        "Static Counter: " +
                        HeritageSite.getSiteCounter());
                    break;

                case 8:
                    System.out.println(
                        "Thank you for using SPANVIA!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 8);

        sc.close();
    }
}