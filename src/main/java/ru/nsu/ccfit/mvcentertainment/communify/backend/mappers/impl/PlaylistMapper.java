package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.UserBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;

import javax.annotation.PostConstruct;

@Component
public class PlaylistMapper extends AbstractMapper<Playlist, PlaylistDto, Long> {

    private final Mapper<User, UserBriefDto, Long> userMapper;
    private final JpaRepository<User, Long> userRepository;

    @Autowired
    public PlaylistMapper(ModelMapper mapper,
                          Mapper<User, UserBriefDto, Long> userMapper,
                          JpaRepository<User, Long> userRepository) {
        super(mapper, Playlist.class, PlaylistDto.class);
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void setupMapper() {
        skipDtoField(PlaylistDto::setOwner);

        skipEntityField(Playlist::setOwner);
        skipEntityField(Playlist::setTracks);
    }

    @Override
    protected void mapSpecificFields(Playlist sourceEntity, PlaylistDto destinationDto) {
        destinationDto.setOwner(userMapper.toDto(sourceEntity.getOwner()));
    }

    @Override
    protected void mapSpecificFields(PlaylistDto sourceDto, Playlist destinationEntity) {
        destinationEntity.setOwner(userRepository.getOne(sourceDto.getOwner().getId()));
    }

}
