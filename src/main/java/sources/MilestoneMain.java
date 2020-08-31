package sources;

import utils.FileNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MilestoneMain {

    public static void main(String[] args) {
        new Scanner(System.in);
        DataClass dc = new DataClass();
        DatafileIOclass dfile = new DatafileIOclass();
        ValidateDataInput vDInput = new ValidateDataInput();
        ArrayList<String> textListFile = dfile.readDataFile("students.txt");
        HashMap personType = new HashMap();
        personType.put("a", 0);
        personType.put("b", 0);
        personType.put("c", 0);
        personType.put("d", 0);

        for(char conds = 'y'; conds == 'y'; conds = vDInput.inpRegText("([yn])").charAt(0)) {
            String optInEx = "([a-g])";

            try {
                System.out.println("\nPlease select any one options\n");
                System.out.println("Select A for Add Company");
                System.out.println("Select B for Add Project Owner");
                System.out.println("Select C for Add Project");
                System.out.println("Select D for Capture Personlaity");
                System.out.println("Select E for Student Preference");
                System.out.println("Select F for ShortList Project");
                System.out.println("Select G for Additional Steps (Milestone 2)");
                System.out.print("\nEnter your option a-g:   ");
                String optIn = vDInput.inpRegText(optInEx);
                String datafilew;
                switch(optIn.charAt(0)) {
                    case 'a':
                        System.out.println("\nYou have Selected to Add Company");
                        datafilew = dc.addCompany();
                        dfile.writeDataFile("companies.txt", datafilew);
                        break;
                    case 'b':
                        System.out.println("\nYou have Selected to Project Owner");
                        datafilew = dc.addOwner();
                        dfile.writeDataFile("owners.txt", datafilew);
                        break;
                    case 'c':
                        System.out.println("\nYou have Selected to Add Project");
                        datafilew = dc.addProject();
                        dfile.writeDataFile("projects.txt", datafilew);
                        break;
                    case 'd':
                        System.out.println("\nYou have Selected to Capture Personlaity");
                        textListFile = dfile.readDataFile("students.txt");
                        personType = dc.studPreson(textListFile, personType);
                        break;
                    case 'e':
                        System.out.println("\nYou have Selected to Add Student Preference");
                        datafilew = dc.studPreference();
                        dfile.writeDataFile(FileNames.PREFERENCES, datafilew);
                        break;
                    case 'f':
                        System.out.println("\nYou have Selected Short Listed Project");
                        datafilew = dc.shortlistProject();
                        dfile.writeDataFile("projects.txt", datafilew);
                        break;
                    case 'g':
                        System.out.println("Select A for Forming Teams");
                        System.out.println("Select B for Displaying Team Fitness Metrics (First you need to create at least 5 teams)");
                        AdditionalOptionsForMilestone2 additionalOptionsForMilestone2 = new AdditionalOptionsForMilestone2();
                        additionalOptionsForMilestone2.inputTwoOptions();
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected value: " + optIn);
                }
            } catch (Exception var12) {
                System.out.println("Exception due to" + var12.getMessage());
                var12.printStackTrace();
            }

            System.out.print("If you want to continue then please enter y or n exits....");
        }

        System.out.println("Thank You....!");
    }
}
