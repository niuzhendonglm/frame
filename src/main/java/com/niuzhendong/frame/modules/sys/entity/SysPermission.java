package com.niuzhendong.frame.modules.sys.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "sys_permission")
@Data
public class SysPermission implements Serializable {
    private static final long serialVersionUID = 353629772108330570L;

    @Id
    @Column(name = "tsp_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tspId;

    private String pname;

    private String url;

    @ManyToMany
    @JoinTable(name = "relations_role_permission",joinColumns = {@JoinColumn(name = "tsp_id")},inverseJoinColumns = {@JoinColumn(name = "tsr_id")})
    private List<SysRole> roles;
}

