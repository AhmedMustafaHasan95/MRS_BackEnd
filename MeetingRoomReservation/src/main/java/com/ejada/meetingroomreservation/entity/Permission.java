package com.ejada.meetingroomreservation.entity;

import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PERMISSION")
@Data
public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "permission_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "url_action", nullable = false, unique = true)
    private String urlAction;
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();


}
