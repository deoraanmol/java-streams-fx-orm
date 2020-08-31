package sources;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

enum EnvironmentType {PROD, TEST}

public class DatafileIOclass {
    private EnvironmentType environmentType = EnvironmentType.PROD;
    public DatafileIOclass() {}
    public DatafileIOclass(EnvironmentType environmentType) {
        this.environmentType = environmentType;
        this.path = this.environmentType == EnvironmentType.PROD ? "src/main/resources/" : "src/test/resources/";
    }

    String path = this.environmentType == EnvironmentType.PROD ? "src/main/resources/" : "src/test/resources/";

    public void writeDataFile(String filename, String datafilew) {
        String pfile = this.path + filename;
        File file = new File(pfile);
        FileWriter fr = null;

        try {
            fr = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fr);
            bw.write(datafilew);
            bw.newLine();
            bw.close();
            System.out.println("Data has been successfully written in the " + filename.toUpperCase());
        } catch (IOException var15) {
            var15.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException var14) {
                var14.printStackTrace();
            }

        }

    }

    public void updateDataFile(String filename, String existingObjectId, String datafilew){
        List<String> fileContent = null;
        try {
            fileContent = new ArrayList<>(Files.readAllLines(Paths.get(this.path + filename), StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Read Exception while updating file: "+e.getMessage());
        }
        Integer rowToUpdate = null;
        for (int i = 0; i < fileContent.size(); i++) {
            String objectId = fileContent.get(i).split(" ")[0]; // handle invalid cases by adding if here
            if (objectId.equalsIgnoreCase(existingObjectId)) {
                rowToUpdate = i;
                break;
            }
        }
        if (rowToUpdate != null) {
            fileContent.set(rowToUpdate, datafilew);
            try {
                Files.write(Paths.get(this.path + filename), fileContent, StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.out.println("Write Exception while updating file: "+e.getMessage());
            }
        }
    }

    public void clearFileData(String filename) {
        String pfile = this.path + filename;
        File file = new File(pfile);
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
            printWriter.println("");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            printWriter.close();
        }
    }

    public ArrayList<String> readDataFile(String filename) {
        String pfile = this.path + filename;
        ArrayList<String> textListFile = new ArrayList();
        File myrfile = new File(pfile);
        FileReader reader = null;

        try {
            if (myrfile.exists() && myrfile.length() != 0L) {
                reader = new FileReader(myrfile);
                String textf = "";

                int i;
                while((i = reader.read()) != -1) {
                    if ((char)i != '\n') {
                        textf = textf + (char)i;
                    } else {
                        textListFile.add(textf);
                        textf = "";
                    }
                }
            } else if (myrfile.exists() && myrfile.length() == 0L) {
                System.out.println("File is Empty");
                textListFile = null;
            } else {
                System.out.println("File Doesn't exists");
                textListFile = null;
            }
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception var15) {
                var15.printStackTrace();
            }

        }

        return textListFile;
    }

    public String fetchObjectById(String filename, String objectId) {
        List<String> rows = this.readDataFile(filename);
        String object = null;
        Iterator var5 = rows.iterator();

        while(var5.hasNext()) {
            String row = (String)var5.next();
            String[] columns = row.split(" ");
            String id = columns[0];
            if (id.equalsIgnoreCase(objectId)) {
                object = row;
                break;
            }
        }

        if (object != null) {
            return object;
        } else {
            System.out.println("Object doesnt exist in file: " + filename);
            return null;
        }
    }

    public void deleteDataFile(String filename) {
        String pfile = this.path + filename;
        File myrfile = new File(pfile);

        try {
            if (myrfile.exists()) {
                myrfile.delete();
            } else {
                System.out.println("There isn't any file..");
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public void printDataFile(ArrayList<String> filetext) {
        for(int i = 0; i < filetext.size(); ++i) {
            System.out.println((String)filetext.get(i));
        }

    }

    public HashSet<String> allInputID(ArrayList<String> filetext) {
        HashSet<String> inputIDfile = new HashSet();

        for(int i = 0; i < filetext.size(); ++i) {
            String strID1 = (String)filetext.get(i);
            String[] strID = strID1.split(" ");
            inputIDfile.add(strID[0].toLowerCase());
        }

        return inputIDfile;
    }
}
