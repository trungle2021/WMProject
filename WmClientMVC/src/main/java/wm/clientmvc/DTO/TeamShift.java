package wm.clientmvc.DTO;

public class TeamShift {

   private String teamName;
    private String teamLeader;
    private  Integer teamsize;
    private  Integer numberOfShift;


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    public Integer getTeamsize() {
        return teamsize;
    }

    public void setTeamsize(Integer teamsize) {
        this.teamsize = teamsize;
    }

    public Integer getNumberOfShift() {
        return numberOfShift;
    }

    public void setNumberOfShift(Integer numberOfShift) {
        this.numberOfShift = numberOfShift;
    }
}
