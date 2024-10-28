package com.lihao.courseware.enums;


public enum IdentityEnum {
    TEACHER(0,"教师"),
    STUDENT(1,"学生");
    private Integer status;
    private String type;

    IdentityEnum(Integer status, String type) {
        this.status = status;
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public static IdentityEnum getIdentityEnumByStatus(Integer status){
        for(IdentityEnum identityEnum:IdentityEnum.values()){
            if(identityEnum.getStatus().equals(status)){
                return identityEnum;
            }
        }
        return null;
    }

}
