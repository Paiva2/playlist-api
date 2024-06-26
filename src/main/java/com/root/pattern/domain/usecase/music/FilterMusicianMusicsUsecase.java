package com.root.pattern.domain.usecase.music;

import com.root.pattern.adapter.dto.album.AlbumOutputDTO;
import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.music.FilterMusicOutputDTO;
import com.root.pattern.adapter.dto.music.ListFilterMusicOutputDTO;
import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
public class FilterMusicianMusicsUsecase {
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;

    public ListFilterMusicOutputDTO exec(Long musicianId, Integer page, Integer perPage) {
        this.validateInputs(musicianId);
        Musician musician = this.checkIfMusicianExists(musicianId);
        this.checkIfMusicianIsNotDisabled(musician);

        if (page < 1) {
            page = 1;
        }

        if (perPage < 5) {
            perPage = 5;
        } else if (perPage > 50) {
            perPage = 50;
        }

        Page<Music> musics = this.getAllMusicsFromMusician(musician.getId(), page, perPage);

        return this.mountOutput(musics, perPage);
    }

    public void validateInputs(Long musicianId) {
        if (Objects.isNull(musicianId)) {
            throw new BadRequestException("Musician id can't be empty");
        }
    }

    public Musician checkIfMusicianExists(Long musicianId) {
        return this.musicianDataProvider.findById(musicianId).orElseThrow(() -> new NotFoundException("Musician"));
    }

    public void checkIfMusicianIsNotDisabled(Musician musician) {
        if (musician.getDisabled()) {
            throw new ForbiddenException("Musician is disabled");
        }
    }

    public Page<Music> getAllMusicsFromMusician(Long musicianId, Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.Direction.DESC, "createdAt");

        return this.musicDataProvider.findAllByMusician(pageable, musicianId);
    }

    public ListFilterMusicOutputDTO mountOutput(Page<Music> musics, Integer perPage) {
        return ListFilterMusicOutputDTO.builder()
            .page(musics.getNumber() + 1)
            .perPage(perPage)
            .totalItems(musics.getTotalElements())
            .musics(musics.stream().map(music ->
                FilterMusicOutputDTO.builder()
                    .id(music.getId())
                    .name(music.getName())
                    .isSingle(music.getIsSingle())
                    .duration(music.getDuration())
                    .createdAt(music.getCreatedAt())
                    .category(CategoryOutputDTO.builder()
                        .id(music.getCategory().getId())
                        .name(music.getCategory().getName().name())
                        .build()
                    )
                    .musician(MusicianOutputDTO.builder()
                        .id(music.getMusician().getId())
                        .name(music.getMusician().getName())
                        .email(music.getMusician().getEmail())
                        .role(music.getMusician().getRole())
                        .createdAt(music.getMusician().getCreatedAt())
                        .build()
                    )
                    .album(Objects.nonNull(music.getAlbum()) ?
                        AlbumOutputDTO.builder()
                            .id(music.getAlbum().getId())
                            .name(music.getAlbum().getName())
                            .createdAt(music.getAlbum().getCreatedAt())
                            .totalMusics(music.getAlbum().getMusic().size())
                            .build()
                        : null
                    )
                    .build()
            ).collect(Collectors.toList()))
            .build();
    }
}
