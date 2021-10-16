package main;

//Imports

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import service.Logic;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

//Class that host the main method.
//This class gets all the logic to manage all the business rules.
public class MainRun {

    //Gson library to parse JSON to Java Objects and vice versa
    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
            .create();

    AtomicReference<Account> singularAccount = new AtomicReference<>();
    LinkedList<String> violations = new LinkedList<>();
    ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        new MainRun();
    }

    //Constructor for normal use via stdin
    //Waits for every line in stdin and parse it to JSON
    //If is a valid JSON start the logic of the business rules.
    public MainRun() {

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String jsonString = sc.nextLine();
            if (!jsonString.isEmpty()) {
                try {
                    ConsoleInput inputObject = gson.fromJson(jsonString, ConsoleInput.class);
                    System.out.println(new Logic(true).checkInputObject(inputObject,
                            singularAccount));
                    System.out.println("");
                    if (singularAccount.get() != null)
                        synchronized (lock) {
                            violations = new LinkedList<>();
                        }
                } catch (JsonParseException e) {
                    System.out.println("The input JSON was unparseable");
                }

            }

        }
    }
}
