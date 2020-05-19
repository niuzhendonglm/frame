package com.niuzhendong.frame.modules.test.entity;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pub_user")
@Data
@Proxy(lazy = false)
public class User implements Serializable {
    private static final long serialVersionUID = -3320971805590503455L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;

}
