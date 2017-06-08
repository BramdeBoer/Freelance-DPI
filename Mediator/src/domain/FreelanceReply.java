package domain;

/**
 * Created by bramd on 31-5-2017.
 */
public class FreelanceReply {
    private String name;
    private String[] skillset;
    private double rate;
    private boolean accepted;
    private Project project;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getSkillset() {
        return skillset;
    }

    public void setSkillset(String[] skillset) {
        this.skillset = skillset;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public FreelanceReply(String name, String[] skillset, double rate, boolean accepted, Project project) {
        this.name = name;
        this.skillset = skillset;
        this.rate = rate;
        this.accepted = accepted;
        this.project = project;
    }
}
