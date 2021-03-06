package com.rippletec.medicine.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rippletec.medicine.bean.PageBean;
import com.rippletec.medicine.bean.Result;
import com.rippletec.medicine.dao.FindAndSearchDao;
import com.rippletec.medicine.dao.MeetingDao;
import com.rippletec.medicine.exception.DaoException;
import com.rippletec.medicine.model.CheckData;
import com.rippletec.medicine.model.Enterprise;
import com.rippletec.medicine.model.Meeting;
import com.rippletec.medicine.model.Subject;
import com.rippletec.medicine.service.CheckDataManager;
import com.rippletec.medicine.service.MeetingManager;
import com.rippletec.medicine.utils.FileUtil;
import com.rippletec.medicine.utils.PPTUtil;
import com.rippletec.medicine.utils.ParamMap;
import com.rippletec.medicine.utils.StringUtil;
import com.rippletec.medicine.vo.web.MeetingVo;

@Service(MeetingManager.NAME)
public class MeetingManagerImpl extends BaseManager<Meeting> implements MeetingManager{

    @Resource(name = MeetingDao.NAME)
    private MeetingDao meetingDao;
    
    @Resource(name = CheckDataManager.NAME)
    private CheckDataManager checkDataManager;
    
    @Override
    protected FindAndSearchDao<Meeting> getDao() {
	return this.meetingDao;
    }

    @Override
    public void addMeeting(Enterprise enterprise, MeetingVo meeting, Subject subject) throws DaoException {
	Meeting meetingModel = new Meeting(enterprise, meeting, subject);
	meetingModel.setStatus(Meeting.ON_PUBLISTH);
	meetingModel.setCommitDate(new Date());
	int dataId = meetingDao.save(meetingModel);
	CheckData checkData = new CheckData(enterprise, meeting.getName(),dataId, CheckData.TYPE_MEETING, null, new Date(), CheckData.CHECKING);
	checkDataManager.save(checkData);
    }

    @Override
    public List<Meeting> findRecentMeeting(PageBean pageBean ,String param, Object value) throws DaoException {
	return meetingDao.findByTime(pageBean, param ,value);
    }

    @Override
    public Result active(int id) throws DaoException {
	Meeting meeting = meetingDao.find(id);
	meeting.setStatus(Meeting.ON_PUBLISTH);
	String pptPath = meeting.getPPT();
	if(!StringUtil.hasText(pptPath)){
	    meetingDao.update(meeting);
	    return new Result(true);
	}
	meeting.setPPT("App"+PPTUtil.separator+"ppt"+PPTUtil.separator+meeting.getId());
	meetingDao.update(meeting);
	File pptFile = new File(pptPath);
	if(!pptFile.exists()){
	    
	    return new Result(false, "操作失败");
	}
	String fileName = pptFile.getName();
	try {
	    PPTUtil.saveToImg(fileName.substring(0, fileName.indexOf(FileUtil.getSuffixByFilename(fileName))), pptFile, 2, "png");
	} catch (IOException e) {
	    logger.debug("ppt转图片失败");
	    return new Result(false, "操作失败");
	}
	return new Result(true);
    }

    @Override
    public void deleteMeeting(int id, Integer enterpriseId) throws DaoException {
	ParamMap paramMap = new ParamMap().put(Meeting.ID, id)
					  .put(Meeting.ENTERPRISE_ID, enterpriseId);
	List<Meeting> meetings = meetingDao.findBySql(Meeting.TABLE_NAME, paramMap);
	meetingDao.delete(meetings.get(0).getId());
    }

    @Override
    public void updateMeeting(int meetingId, int enterpriseId,
	    MeetingVo meeting, Subject subject) throws DaoException {
	ParamMap paramMap = new ParamMap().put(Meeting.ID, meetingId)
					  .put(Meeting.ENTERPRISE_ID, enterpriseId);
	List<Meeting> meetings = meetingDao.findBySql(Meeting.TABLE_NAME, paramMap);
	Meeting updateMeeting = meetings.get(0);
	updateMeeting.setUpdate(meeting, subject);
	updateMeeting.setModifyTime(new Date());
	meetingDao.update(updateMeeting);
    }

    @Override
    public List<Meeting> findBySubject(Integer id, Integer enterpriseId) throws DaoException {
	ParamMap paramMap = new ParamMap().put(Meeting.SUBJECT_ID, id)
					  .put(Meeting.ENTERPRISE_ID, enterpriseId);
	return meetingDao.findBySql(Meeting.TABLE_NAME, paramMap);
    }

    @Override
    public Result block(int id) throws DaoException {
	Meeting meeting = meetingDao.find(id);
	meeting.setStatus(Meeting.ON_CLOSE);
	meeting.setModifyTime(new Date());
	meetingDao.update(meeting);
	return new Result(true);
    }

    @Override
    public void unblock(int id) throws DaoException {
	Meeting meeting = meetingDao.find(id);
	meeting.setStatus(Meeting.ON_PUBLISTH);
	meeting.setModifyTime(new Date());
	meetingDao.update(meeting);
    }

    @Override
    public Meeting getMeeting(int id, int enterpriseId) throws DaoException {
	ParamMap paramMap = new ParamMap().put(Meeting.ID, id).put(Meeting.ENTERPRISE_ID, enterpriseId);
	List<Meeting> meetings = meetingDao.findBySql(Meeting.TABLE_NAME, paramMap);
	return meetings.get(0);
    }


}
