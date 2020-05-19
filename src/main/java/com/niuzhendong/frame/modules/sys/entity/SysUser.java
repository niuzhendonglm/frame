package com.niuzhendong.frame.modules.sys.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "sys_user")
@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = -3320971805590503443L;

    @Id
    @Column(name = "tsu_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tsuId;

    private String username;
    private String password;
    private String salt;

    @ManyToMany
    @JoinTable(name = "relations_user_role",joinColumns = {@JoinColumn(name = "tsu_id")},inverseJoinColumns = {@JoinColumn(name = "tsr_id")})
    private List<SysRole> roles;

    public String getCredentialsSalt() {
        return username+salt+salt;
    }

//    @ApiModelProperty(name = "createTime",value = "记录创建时间",hidden = true)
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @Column(name = "create_time")
//    private Date createTime;
//
//    @ApiModelProperty(name = "updateTime",value = "记录修改时间",hidden = true)
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @Column(name = "update_time")
//    private Date updateTime;
}
