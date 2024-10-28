package com.lihao.courseware.enums;

public enum DiscussEnum {
    PUBLISH(0,"time desc"),
    COMMENT(1,"comment_time desc");
    private Integer sort;
    private String type;

    public Integer getSort() {
        return sort;
    }

    public String getType() {
        return type;
    }

    DiscussEnum(Integer sort, String type) {
        this.sort = sort;
        this.type = type;
    }

    public static DiscussEnum getDiscussEnumBySort(Integer sort){
        for(DiscussEnum discussEnum:DiscussEnum.values()){
            if (discussEnum.getSort().equals(sort)){
                return discussEnum;
            }
        }
        return null;
    }
}
