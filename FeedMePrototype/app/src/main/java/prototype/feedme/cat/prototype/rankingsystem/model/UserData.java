package prototype.feedme.cat.prototype.rankingsystem.model;

import netdb.course.softwarestudio.service.rest.model.Resource;

/**
 * Created by AT on 2015/1/18.
 */
public class UserData extends Resource {
    private String user;
    private long days;
    private int ranking;

    public UserData() {
        super();
    }

    public UserData(String user, long days) {
        this.user = user;
        this.days = days;
    }
    public static String getCollectionName() {
        return "userdatas";
    }
    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getDays() {
        return this.days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public int getRanking() {
        return this.ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}
