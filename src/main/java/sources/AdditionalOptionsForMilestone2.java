package sources;

import java.util.ArrayList;
import java.util.List;

class AdditionalOptionsForMilestone2 {
    public void inputTwoOptions() {
        System.out.println("You have opted for Additional options, Please select between a and b");
        ValidateDataInput vDInput = new ValidateDataInput();
        String optInEx = "([a-b])";
        String optIn = vDInput.inpRegText(optInEx);
        List<Teams> newTeams = new ArrayList();
        DatafileIOclass datafileIOclass = new DatafileIOclass();
        switch(optIn.charAt(0)) {
            case 'a':
                System.out.println("\nYou have Selected `Form .Teams`");
                DataClass dataClass = new DataClass();

                for(int i = 1; i < 6; ++i) {
                    System.out.println("Please enter data for Team: " + i);
                    Teams team = dataClass.formTeams();
                    newTeams.add(team);
                    datafileIOclass.writeDataFile("teams.txt", Teams.teamToFilerow(team));
                    System.out.println("Team: " + i + " successfully added!");
                }

                return;
            case 'b':
                System.out.println("\nYou have Selected `Display Team Fitness Metrics`");
                List<String> teamRows = datafileIOclass.readDataFile("teams.txt");
                if (teamRows != null && teamRows.size() >= 5) {
                    List<Teams> teams = new ArrayList();
                    for(String row: teamRows) {
                        teams.add(Teams.fileRowToTeams(row));
                    }
                    Teams.displayTeamMetrics(teams);
                    Teams.displayTeamPreference(teams);
                    Teams.displayTeamSkillShortage(teams);
                } else {
                    System.out.println("ERROR: You have to form at least 5 teams before selecting this option");
                    this.inputTwoOptions();
                }
                break;
            default:
                throw new IllegalArgumentException("Unexpected value under Additional Options: " + optIn);
        }

    }
}
