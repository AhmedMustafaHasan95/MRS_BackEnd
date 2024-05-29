package com.ejada.meetingroomreservation.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class RolePermissionId implements Serializable {

    private Long roleId;
    private Long permissionId;

}