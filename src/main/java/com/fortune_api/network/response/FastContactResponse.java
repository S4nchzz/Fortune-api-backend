package com.fortune_api.network.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FastContactResponse {
    private String pfp;
    private String name;
    private long to_id;
}
