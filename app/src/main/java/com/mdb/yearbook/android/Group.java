package com.mdb.yearbook.android;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by datarsd1 on 4/4/17.
 */

public class Group
{

    private long firstDate;
    private String title;
    private String creatorId;
    private ArrayList<String> adminIds;
    private int missedCount;
    private ArrayList<String> memberIds;
    private ArrayList<String> photoIds;
    private int allowedMisses;
    private String coverPhotoUrl;
    private String description;
    private HashMap<String, Long> memberJoinDates; // string int
    private int duration;
    private int streak;
    public Group () {
        this.firstDate = -1;
        this.title = "";
        this.creatorId = "";
        this.adminIds = new ArrayList<>();
        this.missedCount = -1;
        this.memberIds = new ArrayList<>();
        this.photoIds = new ArrayList<>();
        this.allowedMisses = -1;
        this.coverPhotoUrl = "";
        this.description = "";
        this.memberJoinDates = new HashMap<>();
        this.duration = -1; // NOTE: UGH IOS WHY
        this.streak = -1;
    }
    public Group(long firstDate, String title, String creatorId, ArrayList<String> adminIds, int missedCount,
                 ArrayList<String> memberIds, ArrayList<String> photoIds, int allowedMisses, String coverPhotoUrl,
                 String description, HashMap<String, Long> memberJoinDates, int duration, int streak)
    {
        this.firstDate = firstDate;
        this.title = title;
        this.creatorId = creatorId;
        this.adminIds = adminIds;
        this.missedCount = missedCount;
        this.memberIds = memberIds;
        this.photoIds = photoIds;
        this.allowedMisses = allowedMisses;
        this.coverPhotoUrl = coverPhotoUrl;
        this.description = description;
        this.memberJoinDates = memberJoinDates;
        this.duration = -1; // NOTE: UGH IOS WHY
        this.streak = streak;
    }

    public long getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(long firstDate) {
        this.firstDate = firstDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public ArrayList<String> getAdminIds() {
        return adminIds;
    }

    public void setAdminIds(ArrayList<String> adminIds) {
        this.adminIds = adminIds;
    }

    public int getMissedCount() {
        return missedCount;
    }

    public void setMissedCount(int missedCount) {
        this.missedCount = missedCount;
    }

    public ArrayList<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(ArrayList<String> memberIds) {
        this.memberIds = memberIds;
    }

    public ArrayList<String> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(ArrayList<String> photoIds) {
        this.photoIds = photoIds;
    }

    public int getAllowedMisses() {
        return allowedMisses;
    }

    public void setAllowedMisses(int allowedMisses) {
        this.allowedMisses = allowedMisses;
    }

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public void setCoverPhotoUrl(String coverPhotoUrl) {
        this.coverPhotoUrl = coverPhotoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, Long> getMemberJoinDates() {
        return memberJoinDates;
    }

    public void setMemberJoinDates(HashMap<String, Long> memberJoinDates) {
        this.memberJoinDates = memberJoinDates;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }
}
