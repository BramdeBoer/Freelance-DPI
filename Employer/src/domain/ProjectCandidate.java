package domain;

import java.util.Arrays;

/**
 * Created by bramd on 8-6-2017.
 */
public class ProjectCandidate implements Comparable<ProjectCandidate> {
    private FreelanceReply freelanceReply;
    private int requiredSkillAmount;

    public FreelanceReply getFreelanceReply() {
        return freelanceReply;
    }

    public void setFreelanceReply(FreelanceReply freelanceReply) {
        this.freelanceReply = freelanceReply;
    }

    public int getRequiredSkillAmount() {
        return requiredSkillAmount;
    }

    public void setRequiredSkillAmount(int requiredSkillAmount) {
        this.requiredSkillAmount = requiredSkillAmount;
    }

    public ProjectCandidate(FreelanceReply freelanceReply, int requiredSkillAmount) {
        this.freelanceReply = freelanceReply;
        this.requiredSkillAmount = requiredSkillAmount;
    }

    @Override
    public int compareTo(ProjectCandidate o) {
        if (this.requiredSkillAmount == o.requiredSkillAmount) {
            return Double.compare(this.freelanceReply.getRate(), o.getFreelanceReply().getRate());
        }
        return Integer.compare(o.requiredSkillAmount, this.requiredSkillAmount);
    }

    public String toString() {
        return "Name: " + this.freelanceReply.getName() + " Amount of required skills: " + this.requiredSkillAmount + " Skillset: " + Arrays.toString(this.freelanceReply.getSkillset()) + " Rate: " + this.freelanceReply.getRate();
    }
}
