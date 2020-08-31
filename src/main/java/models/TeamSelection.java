package models;

public class TeamSelection {
    private String teamNo;
    private String rowNo;

    public TeamSelection(String team, String row) {
        this.teamNo = team;
        this.rowNo = row;
    }

    public String getTeamNo() {
        return teamNo;
    }

    public void setTeamNo(String teamNo) {
        this.teamNo = teamNo;
    }

    public String getRowNo() {
        return rowNo;
    }

    public void setRowNo(String rowNo) {
        this.rowNo = rowNo;
    }

    public String getFxId() {
        return this.teamNo+"|"+this.rowNo;
    }
}
