package sources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class ValidateDataInput {
    Scanner scanner;

    public ValidateDataInput() {
        this.scanner = new Scanner(System.in);
    }

    public void textInput() {
    }

    public void numberInput(int nn) {
    }

    public String emailInput(String emailVer) {
        String valEmail = emailVer;

        for(String eformat = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"; !valEmail.matches(eformat); valEmail = this.scanner.next()) {
            System.out.print("Please Enter the correct email:  ");
        }

        return valEmail;
    }

    public String urlValidate(String urlvalre) {
        String urlre = urlvalre;

        for(String urlValidt = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"; !urlre.matches(urlValidt); urlre = this.scanner.next()) {
            System.out.print("\nPlease Enter the correct URL:  ");
        }

        return urlre;
    }

    public boolean validateID(String fileName, String dataID) {
        Boolean valdCond = false;
        DatafileIOclass textfileN = new DatafileIOclass();
        ArrayList<String> textListFile = textfileN.readDataFile(fileName);
        if (textListFile != null) {
            HashSet<String> inputID = textfileN.allInputID(textListFile);
            if (inputID.contains(dataID)) {
                System.out.println(dataID + " exits in data file.");
                valdCond = true;
            }
        }

        return valdCond;
    }

    public HashMap<String, Integer> validateData(String fileName) {
        DatafileIOclass textfileN = new DatafileIOclass();
        ArrayList<String> textListFile = textfileN.readDataFile(fileName);
        HashMap<String, Integer> projectRank = new HashMap();
        if (textListFile != null) {
            for(int i = 0; i < textListFile.size(); ++i) {
                String strDatatex = ((String)textListFile.get(i)).trim();
                strDatatex.replaceAll("  ", " ");
                strDatatex.replaceAll("(\\r|\\n|\\t)", "");
                String[] strID1 = strDatatex.split(" ");

                for(int j = 1; j < strID1.length; j += 2) {
                    if (projectRank.containsKey(strID1[j])) {
                        projectRank.put(strID1[j], (Integer)projectRank.get(strID1[j]) + Integer.valueOf(strID1[j + 1]));
                    } else {
                        projectRank.put(strID1[j], Integer.valueOf(strID1[j + 1]));
                    }
                }
            }
        }

        return projectRank;
    }

    public String inpRegText(String textReg) {
        String retInpReg;
        for(retInpReg = this.scanner.next().toLowerCase(); !retInpReg.matches(textReg); retInpReg = this.scanner.next().toLowerCase()) {
            System.out.print("\nPlease Enter your once again  :   ");
        }

        return retInpReg;
    }
}
