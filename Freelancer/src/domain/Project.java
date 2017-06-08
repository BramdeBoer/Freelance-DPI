package domain;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by bramd on 17-5-2017.
 */
public class Project {

    private Sector sector;
    private String description;
    private long startDate;
    private long endDate;
    private String[] neededSkillsets;

    public Project(Sector sector, String description, long startDate, long endDate, String[] neededSkillsets) {
        this.sector = sector;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.neededSkillsets = neededSkillsets;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String[] getNeededSkillsets() {
        return neededSkillsets;
    }

    public void setNeededSkillsets(String[] neededSkillsets) {
        this.neededSkillsets = neededSkillsets;
    }

    private String millisToDate(long millis){

        return DateFormat.getDateInstance(DateFormat.SHORT).format(millis);
        //You can use DateFormat.LONG instead of SHORT

    }

    @Override
    public String toString() {
        return "\nSector= " + sector +
                "\nDescription= " + description +
                "\nStart Date= " + millisToDate(startDate) +
                "\nEnd Date= " + millisToDate(endDate) +
                "\nNeeded Skillsets= " + Arrays.toString(neededSkillsets);
    }
}
