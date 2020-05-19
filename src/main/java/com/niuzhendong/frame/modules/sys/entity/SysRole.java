package com.niuzhendong.frame.modules.sys.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "sys_role")
@Data
public class SysRole implements Serializable {
    private static final long serialVersionUID = -8687790154329829056L;

    @Id
    @Column(name = "tsr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tsrId;

    private String rname;

    @ManyToMany
    @JoinTable(name = "relations_role_permission",joinColumns = {@JoinColumn(name = "tsr_id")},inverseJoinColumns = {@JoinColumn(name = "tsp_id")})
    private List<SysPermission> permissions;

    @ManyToMany
    @JoinTable(name = "relations_user_role",joinColumns = {@JoinColumn(name = "tsr_id")},inverseJoinColumns = {@JoinColumn(name = "tsu_id")})
    private List<SysUser> users;
}
