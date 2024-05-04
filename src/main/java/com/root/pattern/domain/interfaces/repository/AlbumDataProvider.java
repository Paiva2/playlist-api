package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface AlbumDataProvider {
    Optional<Album> findById(UUID id);

    Album register(Album album);

    Optional<Album> findByAlbumNameAndMusicianId(String albumName, Long musicianId);

    Page<Album> findAllByMusicianId(Pageable pageable, Long musicianId);

    Page<Album> findAllByMusicianIdAndAlbumName(Pageable pageable, Long musicianId, String albumName);
}
