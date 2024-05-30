package com.dxc.solution_intelligente.DTO.Permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionDTO {
    private int id;
    private String authority;
}
