package com.sanhai.entity;

import java.io.Serializable;

/**
 * 作者：boys
 * 创建时间：2017-02-21 12:58
 * 类描述：
 * 修改人：
 * 修改时间：
 */
public class ESTopic implements Serializable {

    private String topicId;

    private String gradeId;

    private String grade;

    private String subjectId;

    private String subject;

    private String knowledgePoint;

    private String contentHtml;

    private String contentText;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getKnowledgePoint() {
        return knowledgePoint;
    }

    public void setKnowledgePoint(String knowledgePoint) {
        this.knowledgePoint = knowledgePoint;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public ESTopic() {
        this.topicId = "";
        this.gradeId = "";
        this.grade = "";
        this.subjectId = "";
        this.subject = "";
        this.knowledgePoint = "";
        this.contentHtml = "";
        this.contentText = "";
    }
}
