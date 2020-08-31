package sources;

import exceptions.InvalidMemberException;
import exceptions.PersonalityImbalanceException;
import utils.FileNames;
import utils.RegexPatterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class DataClass {
    Scanner scanner;
    ValidateDataInput valdC;
    DatafileIOclass textfileN1;
    ArrayList<String> addINarrData;
    HashMap<String, Integer> proPrefernce;
    String inusrData;

    public DataClass() {
        this.scanner = new Scanner(System.in);
        this.valdC = new ValidateDataInput();
        this.textfileN1 = new DatafileIOclass();
        this.addINarrData = new ArrayList();
        this.proPrefernce = new HashMap();
    }

    public String addCompany() {
        try {
            System.out.print("\nPlease Enter Company ID (comp1/COMP1)   :  ");

            for(this.inusrData = this.valdC.inpRegText("(^comp[0-9]*$)"); this.valdC.validateID("companies.txt", this.inusrData); this.inusrData = this.valdC.inpRegText("(^comp[0-9]*$)")) {
                System.out.print("\nPlease Enter Once Company ID (comp1/COMP1)   :  ");
            }

            this.addINarrData.add(this.inusrData);
            System.out.print("\nPlease Enter  Company Name: ");
            this.inusrData = this.scanner.next();
            this.addINarrData.add(this.inusrData);
            System.out.print("\nPlease Enter ABN Numer:  ");
            this.inusrData = this.valdC.inpRegText("(^[0-9]*$)");
            this.addINarrData.add(this.inusrData);
            System.out.print("\nPlease Enter Company URL :  ");
            this.inusrData = this.scanner.next();
            this.inusrData = this.valdC.urlValidate(this.inusrData);
            this.addINarrData.add(this.inusrData);
            System.out.print("\nPlease Enter Company Address :  ");
            this.inusrData = this.scanner.next();
            this.addINarrData.add(this.inusrData);
            this.inusrData = this.arraylistTostring(this.addINarrData);
            System.out.print(this.addINarrData.toString());
        } catch (Exception var2) {
            System.out.println("Error Occured due to " + var2.getMessage());
        }

        this.addINarrData.clear();
        return this.inusrData.toUpperCase();
    }

    public String addOwner() {
        try {
            System.out.print("Please Enter .Project Owner ID (own1/Own1):  ");

            for(this.inusrData = this.valdC.inpRegText("(^own[0-9]*$)"); this.valdC.validateID("owners.txt", this.inusrData); this.inusrData = this.valdC.inpRegText("(^own[0-9]*$)")) {
                System.out.print("Please Enter Once again .Project Owner ID (own1/Own1):  ");
            }

            this.addINarrData.add(this.inusrData);
            System.out.print("Please Enter .Project Owner First Name :  ");
            this.inusrData = this.valdC.inpRegText("(^[a-zA-Z]*$)");
            this.addINarrData.add(this.inusrData);
            System.out.print("Please Enter .Project Owner Surname   :  ");
            this.inusrData = this.valdC.inpRegText("(^[a-zA-Z]*$)");
            this.addINarrData.add(this.inusrData);
            System.out.print("Please Enter .Project Owner Role / Job Title  :  ");
            this.inusrData = this.valdC.inpRegText("((?:\\s*[a-zA-Z]{2,}\\s*)*)");
            this.addINarrData.add(this.inusrData);
            System.out.print("Please Enter .Project Owner Email Address  :   ");
            this.inusrData = this.valdC.emailInput(this.scanner.next());
            this.addINarrData.add(this.inusrData);
            System.out.print("Please Enter  .Project Owner Company ID (c1/C2):  ");
            this.inusrData = this.valdC.inpRegText("(^c[0-9]*$)");
            this.addINarrData.add(this.inusrData);
            this.inusrData = this.arraylistTostring(this.addINarrData);
            System.out.println(this.addINarrData.toString());
        } catch (Exception var2) {
            System.out.println("Error Occured due to " + var2.getMessage());
        }

        this.addINarrData.clear();
        return this.inusrData.toUpperCase();
    }

    public String addProject() {
        HashSet rankSkill = new HashSet();

        try {
            System.out.print("Please Enter .Project ID  (pr1/Pr1)  :  ");

            for(this.inusrData = this.valdC.inpRegText("(^pr[0-9]*$)"); this.valdC.validateID("projects.txt", this.inusrData); this.inusrData = this.valdC.inpRegText("(^pr[0-9]*$)")) {
                System.out.print("Please Enter Once again .Project ID  (pr1/Pr1)  :  ");
            }

            this.addINarrData.add(this.inusrData);
            System.out.print("Please Enter .Project Title   :  ");
            this.inusrData = this.scanner.nextLine();
            this.addINarrData.add(this.inusrData);
            System.out.print("Please Enter .Project Descriptions  :  ");
            this.inusrData = this.scanner.nextLine();
            this.addINarrData.add(this.inusrData);
            System.out.print("Please Enter .Project Owner ID (own1/Own1)  :  ");
            this.inusrData = this.valdC.inpRegText("(^own[0-9]*$)");
            this.addINarrData.add(this.inusrData);
            System.out.println("****Please Enter Rank Technical Skills (1-4)**** ");
            System.out.print("Please Enter Programming & Software Engineering rank  : ");
            this.inusrData = this.addSkilledRank(rankSkill);
            rankSkill.add(this.inusrData);
            String strSkill = "P " + this.inusrData + " ";
            System.out.print("Please Enter Networking and Security ranking    :  ");
            this.inusrData = this.addSkilledRank(rankSkill);
            rankSkill.add(this.inusrData);
            strSkill = strSkill + "N " + this.inusrData + " ";
            System.out.print("Please Enter Analytics and Big Data  ranking    :  ");
            this.inusrData = this.addSkilledRank(rankSkill);
            rankSkill.add(this.inusrData);
            strSkill = strSkill + "A " + this.inusrData + " ";
            System.out.print("Please Enter Web & Mobile applications ranking  :  ");
            this.inusrData = this.addSkilledRank(rankSkill);
            rankSkill.add(this.inusrData);
            strSkill = strSkill + "W " + this.inusrData;
            this.addINarrData.add(strSkill);
            this.inusrData = this.arraylistTostring(this.addINarrData);
            System.out.println(this.addINarrData.toString());
        } catch (Exception var3) {
            System.out.println("Error Occured due to " + var3.getMessage());
        }

        this.addINarrData.clear();
        return this.inusrData.toUpperCase();
    }

    public HashMap<String, Integer> studPreson(ArrayList<String> textStudList, HashMap<String, Integer> personType1) {
        HashSet confStudT = new HashSet();

        try {
            System.out.print("\nPlease Select Anyone Student ID S1/s1...");

            for(this.inusrData = this.valdC.inpRegText("(^s[0-9]*$)"); !this.valdC.validateID("students.txt", this.inusrData); this.inusrData = this.valdC.inpRegText("(^s[0-9]*$)")) {
                System.out.print("\nPlease Enter Once again ID S1/s1...");
            }

            String[] strSkills = null;

            for(int i = 0; i < textStudList.size(); ++i) {
                if (((String)textStudList.get(i)).toLowerCase().startsWith(this.inusrData + " ")) {
                    System.out.println("Student ID and Skills: " + (String)textStudList.get(i));
                    strSkills = ((String)textStudList.get(i)).split(" ");
                    confStudT.add(this.inusrData);
                    this.inusrData = (String)textStudList.get(i);
                    break;
                }
            }

            this.addINarrData.add(this.inusrData.replaceAll("(\\r|\\n|\\t)", ""));
            System.out.println("Student Programmming :  " + strSkills[1] + " " + strSkills[2]);
            System.out.println("Student Networking :  " + strSkills[3] + " " + strSkills[4]);
            System.out.println("Student Analytics :  " + strSkills[5] + " " + strSkills[6]);
            System.out.println("Student Web     :  " + strSkills[7] + " " + strSkills[8]);
            System.out.println("\nAssign student Role in the .Project \n Direcotr -- A \n Socializer -- B \n Thinker -- C \n Supporter -- D ");
            System.out.print("Please Enter Student Personality A-D: ");
            this.inusrData = this.valdC.inpRegText("([a-d])");
            if ((Integer)personType1.get(this.inusrData) > 5) {
                throw new Exception("the personality types of the 4 students assigned\r\nby the project manager is not balanced");
            }

            personType1.put(this.inusrData, (Integer)personType1.get(this.inusrData) + 1);
            this.addINarrData.add(this.inusrData);
            System.out.print("Please Enter Student Conflict 1:   ");

            for(this.inusrData = this.valdC.inpRegText("(^s[0-9]*$)"); confStudT.contains(this.inusrData); this.inusrData = this.valdC.inpRegText("(^s[0-9]*$)")) {
                System.out.print("Please Enter Once again ID S1/s1...");
            }

            while(!this.valdC.validateID("students.txt", this.inusrData)) {
                System.out.print("Please Enter Once again ID S1/s1...");
                this.inusrData = this.valdC.inpRegText("(^s[0-9]*$)");
            }

            confStudT.add(this.inusrData);
            this.addINarrData.add(this.inusrData.toUpperCase());
            System.out.print("Please Enter Student Confilt 2:   ");

            for(this.inusrData = this.valdC.inpRegText("(^s[0-9]*$)"); confStudT.contains(this.inusrData); this.inusrData = this.valdC.inpRegText("(^s[0-9]*$)")) {
                System.out.print("Please Enter Once again ID S1/s1...");
            }

            while(!this.valdC.validateID("students.txt", this.inusrData)) {
                System.out.print("Please Enter Once again ID S1/s1...");
                this.inusrData = this.valdC.inpRegText("(^s[0-9]*$)");
            }

            this.addINarrData.add(this.inusrData.toUpperCase());
            this.inusrData = this.arraylistTostring(this.addINarrData);
            System.out.println(this.inusrData);
        } catch (Exception var6) {
            System.out.println("Error Occured due to " + var6.getMessage());
        }

        this.addINarrData.clear();
        confStudT.clear();
        this.textfileN1.writeDataFile(FileNames.STUDENT_INFO, this.inusrData);
        return personType1;
    }

    public String studPreference() {
        HashSet confPrefH = new HashSet();

        try {
            System.out.print("\nPlease Enter Student ID:  ");

            for(this.inusrData = this.valdC.inpRegText("(^s[0-9]*$)"); !this.valdC.validateID("students.txt", this.inusrData); this.inusrData = this.valdC.inpRegText("(^s[0-9]*$)")) {
                System.out.print("\nPlease Enter Once again ID S1/s1...");
            }

            this.addINarrData.add(this.inusrData);

            for(char sPc = 'y'; sPc == 'y'; sPc = this.valdC.inpRegText("([yn])").charAt(0)) {
                confPrefH.clear();
                String dataSpc = (String)this.addINarrData.get(0);
                System.out.print("\nPlease Enter your preferred project most - least ( 1-4) ");
                System.out.print("\nPlease Enter project ID Pr(1-10) for preference 4 :\t");
                this.inusrData = this.valdC.inpRegText("(^pr[1-9]|10$)");
                this.inusrData = this.inusrData + " " + 4;
                confPrefH.add(this.inusrData);
                dataSpc = dataSpc + " " + this.inusrData;
                System.out.print("\nPlease Enter project ID Pr(1-10) for preference 3 :\t");

                for(this.inusrData = this.valdC.inpRegText("(^pr[1-9]|10$)"); confPrefH.contains(this.inusrData); this.inusrData = this.valdC.inpRegText("(^pr[1-9]|10$)")) {
                    System.out.print("\nPlease Enter Once again project ID Pr(1-10)...");
                }

                this.inusrData = this.inusrData + " " + 3;
                confPrefH.add(this.inusrData);
                System.out.print("\nPlease Enter project ID Pr(1-10) for preference 2 :\t");

                for(this.inusrData = this.valdC.inpRegText("(^pr[1-9]|10$)"); confPrefH.contains(this.inusrData); this.inusrData = this.valdC.inpRegText("(^pr[1-9]|10$)")) {
                    System.out.print("\nPlease Enter Once again project ID Pr(1-10)...");
                }

                this.inusrData = this.inusrData + " " + 2;
                confPrefH.add(this.inusrData);
                dataSpc = dataSpc + " " + this.inusrData;
                System.out.print("\nPlease Enter project ID Pr(1-10) for preference 1 :\t");

                for(this.inusrData = this.valdC.inpRegText("(^pr[1-9]|10$)"); confPrefH.contains(this.inusrData); this.inusrData = this.valdC.inpRegText("(^pr[1-9]|10$)")) {
                    System.out.print("\nPlease Enter Once again project ID Pr(1-10)...");
                }

                this.inusrData = this.inusrData + " " + 1;
                confPrefH.add(this.inusrData);
                dataSpc = dataSpc + " " + this.inusrData;
                System.out.println("\nYour Projects Preferences:" + dataSpc.toUpperCase());
                System.out.println("\n If you want to try once again, please enter y or n to exits...");
            }

            this.addINarrData.addAll(confPrefH);
            System.out.println("\nYour Projects Preferences:");
            this.inusrData = this.arraylistTostring(this.addINarrData);
            System.out.println(this.inusrData);
        } catch (Exception var4) {
            System.out.println("Error Occured due to " + var4.getMessage());
        }

        this.addINarrData.clear();
        return this.inusrData.toUpperCase();
    }

    public String shortlistProject() {
        System.out.println("\nThe List of Short Listed .Project \n ");
        this.addINarrData = this.textfileN1.readDataFile(FileNames.PREFERENCES);

        for(int i = 0; i < this.addINarrData.size(); ++i) {
            System.out.println(i + 1 + "-" + (String)this.addINarrData.get(i));
        }

        this.proPrefernce = this.valdC.validateData(FileNames.PREFERENCES);
        System.out.println("Sum of Each projects : \n" + this.proPrefernce);
        System.out.println("\n.Project Rankings \n");
        Map<String, Integer> hmproPrefernce = sortByValue(this.proPrefernce);
        ArrayList<String> textListFile = this.textfileN1.readDataFile("projects.txt");
        this.textfileN1.deleteDataFile("projects.txt");
        Iterator var3 = hmproPrefernce.entrySet().iterator();

        while(true) {
            Entry en;
            do {
                if (!var3.hasNext()) {
                    this.inusrData = this.arraylistTostring(textListFile);
                    System.out.println("\nData to write***\n" + this.inusrData);
                    return this.inusrData.toLowerCase();
                }

                en = (Entry)var3.next();
                System.out.println(".Project = " + (String)en.getKey() + ", Ranking = " + en.getValue());
            } while(textListFile.size() <= 0);

            for(int i = 0; i < 5; ++i) {
                if (((String)textListFile.get(i)).startsWith((String)en.getKey())) {
                    textListFile.remove(i);
                }
            }
        }
    }

    public String arraylistTostring(ArrayList<String> userinputdata) {
        String retstr = "";

        for(int i = 0; i < userinputdata.size(); ++i) {
            if (i == 0) {
                retstr = (String)userinputdata.get(i);
            } else {
                retstr = retstr + " " + (String)userinputdata.get(i);
            }
        }

        return retstr;
    }

    public String addSkilledRank(HashSet<String> rankSkilled) {
        String inputUData;
        for(inputUData = this.valdC.inpRegText("([1-4])"); rankSkilled.contains(inputUData); inputUData = this.valdC.inpRegText("([1-4])")) {
            System.out.print("Your enter value already assigned, please enter except " + inputUData + "  :  ");
        }

        return inputUData;
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        List<Entry<String, Integer>> list = new LinkedList(hm.entrySet());
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return ((Integer)o1.getValue()).compareTo((Integer)o2.getValue());
            }
        });
        HashMap<String, Integer> temp = new LinkedHashMap();
        Iterator var3 = list.iterator();

        while(var3.hasNext()) {
            Entry<String, Integer> aa = (Entry)var3.next();
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;
    }

    public Teams formTeams() {
        try {
            System.out.print("\nPlease Enter .Project ID (please start with `pr`, e.g: pr1)   :  ");
            this.inusrData = this.valdC.inpRegText("(^pr[1-9]|10$)");
            this.inusrData = this.inputIdUntilCorrect(this.inusrData, "projects.txt", "(^pr[1-9]|10$)");
            Teams newTeam = new Teams();
            newTeam.setProjectId(this.inusrData);
            System.out.println("Now you have to enter 4 students");
            List<StudentInfo> studentInfosAdded = new ArrayList();

            for(int i = 1; i < 5; ++i) {
                System.out.println("Please enter id of student: " + i);
                this.inusrData = this.valdC.inpRegText(RegexPatterns.STUDENT);
                this.inusrData = this.inputIdUntilCorrect(this.inusrData, "students.txt", RegexPatterns.STUDENT);
                StudentInfo.addStudentToTeam(this.inusrData, studentInfosAdded);
            }

            newTeam.setStudentInfos(studentInfosAdded);
            Teams.hasPersonalityBalance(newTeam);
            return newTeam;
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        }
    }

    private String inputIdUntilCorrect(String inUsrData, String fileName, String pattern) {
        while(!this.valdC.validateID(fileName, this.inusrData)) {
            System.out.println("Entered ID: " + this.inusrData + " doesn't exist in " + fileName);
            System.out.print("Please Enter ID: ");
            this.inusrData = this.valdC.inpRegText(pattern);
        }

        return inUsrData;
    }
}
