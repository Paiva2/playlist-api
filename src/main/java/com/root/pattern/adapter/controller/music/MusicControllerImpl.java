package com.root.pattern.adapter.controller.music;

import com.root.pattern.adapter.dto.music.*;
import com.root.pattern.domain.usecase.album.InsertMusicOnAlbumUsecase;
import com.root.pattern.domain.usecase.album.RemoveMusicFromAlbumUsecase;
import com.root.pattern.domain.usecase.music.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class MusicControllerImpl implements MusicController {
    private final RegisterMusicUsecase registerMusicUsecase;
    private final FilterMusicsNameUsecase filterMusicsNameUsecase;
    private final FilterOneMusicUsecase filterOneMusicUsecase;
    private final FilterMusicianMusicsUsecase filterMusicianMusicsUsecase;
    private final GetAllOwnMusicsUsecase getAllOwnMusicsUsecase;
    private final DisableMusicUsecase disableMusicUsecase;
    private final InsertMusicOnAlbumUsecase insertMusicOnAlbumUsecase;
    private final RemoveMusicFromAlbumUsecase removeMusicFromAlbumUsecase;
    private final UpdateMusicUsecase updateMusicUsecase;

    @Override
    public ResponseEntity<NewMusicOutputDTO> register(
        @PathVariable(name = "categoryId") UUID categoryId,
        Authentication authentication,
        @RequestBody @Valid NewMusicDTO newMusicDTO
    ) {
        Long musicianId = Long.valueOf(authentication.getName());
        NewMusicOutputDTO output = this.registerMusicUsecase.exec(musicianId, newMusicDTO.toEntity(categoryId));

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @Override
    public ResponseEntity<ListFilterMusicOutputDTO> getMusicByName(
        @RequestParam("name") String name,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "5") Integer size
    ) {
        ListFilterMusicOutputDTO output = this.filterMusicsNameUsecase.exec(name, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<FilterMusicOutputDTO> getMusic(@PathVariable("musicId") UUID id) {
        FilterMusicOutputDTO output = this.filterOneMusicUsecase.exec(id);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<ListFilterMusicOutputDTO> getMusicianMusics(
        @PathVariable("musicianId") Long id,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "5") Integer perPage
    ) {
        ListFilterMusicOutputDTO output = this.filterMusicianMusicsUsecase.exec(id, page, perPage);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<ListFilterMusicOutputDTO> getOwnMusicianMusics(
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "5") Integer perPage,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "album", required = false) String album,
        Authentication authentication
    ) {
        Long musicianId = Long.valueOf(authentication.getName());
        ListFilterMusicOutputDTO output = this.getAllOwnMusicsUsecase.exec(musicianId, page, perPage, name, album);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<MusicOutputDTO> disable(
        Authentication authentication,
        @PathVariable("musicId") UUID musicId
    ) {
        Long musicianId = Long.valueOf(authentication.getName());
        MusicOutputDTO output = this.disableMusicUsecase.exec(musicianId, musicId);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<MusicOutputDTO> insertOnAlbum(
        Authentication authentication,
        @PathVariable("musicId") UUID musicId,
        @PathVariable("albumId") UUID albumId
    ) {
        Long musicianId = Long.valueOf(authentication.getName());
        MusicOutputDTO output = this.insertMusicOnAlbumUsecase.exec(musicianId, albumId, musicId);

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @Override
    public ResponseEntity<MusicOutputDTO> removeFromAlbum(
        Authentication authentication,
        @PathVariable("musicId") UUID musicId
    ) {
        Long musicianId = Long.valueOf(authentication.getName());
        MusicOutputDTO output = this.removeMusicFromAlbumUsecase.exec(musicianId, musicId);

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @Override
    public ResponseEntity<MusicOutputDTO> update(
        Authentication authentication,
        @PathVariable("musicId") UUID musicId,
        @RequestBody @Valid UpdateMusicInputDTO dto
    ) {
        Long musicianId = Long.valueOf(authentication.getName());
        MusicOutputDTO output = this.updateMusicUsecase.exec(musicianId, dto.toEntity(musicId));

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }
}
