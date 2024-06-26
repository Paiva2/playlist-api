package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class MusicianDataProviderImpl implements MusicianDataProvider {
    private final MusicianRepository musicianRepository;

    @Override
    public Optional<Musician> findById(Long id) {
        return this.musicianRepository.findById(id);
    }

    @Override
    public Optional<Musician> findByEmail(String email) {
        return this.musicianRepository.findByEmail(email);
    }

    @Override
    public Musician register(Musician newMusician) {
        return this.musicianRepository.save(newMusician);
    }

    @Override
    public Musician update(Musician musician) {
        return this.musicianRepository.save(musician);
    }

    @Override
    public Optional<Musician> findByEmailOrName(String email, String name) {
        return this.musicianRepository.findByEmailOrName(email, name);
    }

    @Override
    public Optional<Musician> findByName(String name) {
        return this.musicianRepository.findByName(name);
    }

}
