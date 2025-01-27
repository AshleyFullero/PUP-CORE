package org.htech.universityproject.modal;

import org.htech.universityproject.dao.AchievementsDao;

public class Achievement {

    private String title;
    private String description;
    private String date;
    private AchievementType type;

    public Achievement(String title, String description, AchievementType type) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.date = null;
    }

    public Achievement(String title, String description, String date, AchievementType type) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    /*
    Overriding:
        The addAchievement() method adapts based on the type of accomplishment:
            o Students log awards like achievements or club recognitions.
            o Professors record achievements like published research or keynote speeches.
     */

    public void addAchievement(Achievement achievement){
        AchievementsDao dao = new AchievementsDao();
        dao.addAchievementToDB(achievement);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AchievementType getType() {
        return type;
    }

    public void setType(AchievementType type) {
        this.type = type;
    }
}
