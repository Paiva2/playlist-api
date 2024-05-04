package com.root.pattern.adapter.dto.album;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AlbumOutputDTO {
    private UUID id;
    private String name;
    private Date createdAt;
    private long totalMusics;
}
