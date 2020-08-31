package sources;

import java.io.Serializable;

class ProjectPreference implements Serializable {
    private String projectId;
    private Integer rank;

    public String getProjectId() {
        return this.projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getRank() {
        return this.rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
