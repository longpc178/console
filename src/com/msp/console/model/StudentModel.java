package com.msp.console.model;

import com.msp.console.util.SubjectEnum;

import java.util.Date;

import static com.msp.console.util.DateUtils.convertDatetoString;
import static com.msp.console.util.FormatDate.FORMAT_6;

public class StudentModel {

    private String code;
    private String fullName;
    private Date dateOfBirth;
    private String classCode;
    private SubjectModel[][] subjects;
    private float avgMark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public SubjectModel[][] getSubjects() {
        return subjects;
    }

    public void setSubjects(SubjectModel[][] subjects) {
        this.subjects = subjects;
    }

    public float getAvgMark() {
        return avgMark;
    }

    public void setAvgMark(float avgMark) {
        this.avgMark = avgMark;
    }

    @Override
    public String toString() {
        return "Học sinh{" +
                "Mã='" + code + '\'' +
                ", Tên đầy đủ='" + fullName + '\'' +
                ", Ngày sinh=" + convertDatetoString(dateOfBirth, FORMAT_6) +
                ", Lớp='" + classCode + '\'' +
                ", Điểm trung bình='" + avgMark + '\'' +
                ", Bảng điểm môn học=[" + parseSubjects() +
                "]}";
    }

    private String parseSubjects() {
        StringBuilder val = new StringBuilder();
        if (subjects != null && subjects.length > 0) {
            for (SubjectModel[] subject : subjects) {
                for (SubjectModel sub : subject) {
                    if (sub != null) {
                        val.append(sub.toString());
                    }
                }
            }
        }
        return val.toString();
    }
}
