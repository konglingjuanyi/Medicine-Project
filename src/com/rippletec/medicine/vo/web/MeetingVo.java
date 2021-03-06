package com.rippletec.medicine.vo.web;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.rippletec.medicine.model.Meeting;

public class MeetingVo {
    
    public static final String CLASS_NAME = "MeetingVo";
    
    @NotNull(message="会议名不能为空")
    private String name;
    
    private String speaker;
    
    public Integer medicineID;
    
    @NotNull(message="日期不能为空")
    private Date date;

    private String PPT;

    private String video;
    
    @NotNull(message="内容不能为空")
    private String content;
    
    @NotNull(message="所属科目不能为空")
    private Integer subject_id;
    
    @NotNull(message="会议地址不能为空")
    private String pageUrl;
    
    public String medicineName;
    
    public MeetingVo() {
    }
    
    

    public MeetingVo(Meeting meeting) {
	super();
	this.name = meeting.getName();
	this.speaker = meeting.getSpeaker();
	this.medicineID = meeting.getMedicine().getId();
	this.date = meeting.getCommitDate();
	PPT = meeting.getPPT();
	this.video = meeting.getVideo();
	this.content = meeting.getContent();
	this.subject_id = meeting.getSubject().getId();
	this.pageUrl = meeting.getPageUrl();
    }



    @Override
    public String toString() {
	return "MeetingVo [name=" + name + ", speaker=" + speaker + ", date=" + date + ", PPT=" + PPT + ", video=" + video
		+ ", content=" + content + ", subject=" + subject_id + "]";
    }


    

    public String getPageUrl() {
        return pageUrl;
    }



    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }



    public String getName() {
        return name;
    }

    public String getSpeaker() {
        return speaker;
    }

    public Date getDate() {
        return date;
    }

    public String getPPT() {
        return PPT;
    }

    public String getVideo() {
        return video;
    }

    public String getContent() {
        return content;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public void setPPT(String pPT) {
        PPT = pPT;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public Integer getMedicineID() {
        return medicineID;
    }



    public void setMedicineID(Integer medicineID) {
        this.medicineID = medicineID;
    }



    public String getMedicineName() {
        return medicineName;
    }



    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }



    public Integer getSubject_id() {
        return subject_id;
    }



    public void setSubject_id(Integer subject_id) {
        this.subject_id = subject_id;
    }
    
    
    
    
    

}
