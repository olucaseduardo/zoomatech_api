package com.olucaseduardo.zoomatech_api.dto.home;

import com.olucaseduardo.zoomatech_api.entity.CategoryMember;
import com.olucaseduardo.zoomatech_api.entity.Member;

public record MemberHomePageResponseDTO(
        String name,
        String photo,
        String description,
        CategoryMember category,
        Boolean active,
        String lattes,
        String role
) {
    public MemberHomePageResponseDTO(Member members) {
        this(members.getName(), members.getPhoto(), members.getDescription(), members.getCategory(), members.getActive(), members.getLattes(), members.getRole().getName());
    }
}
