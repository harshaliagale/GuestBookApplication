package com.owneroftime.guestbook.api.service;

import com.owneroftime.guestbook.api.entity.GuestBookEntryEntity;
import com.owneroftime.guestbook.api.model.GuestBookEntryModel;
import com.owneroftime.guestbook.api.repository.GuestBookEntryRepository;
import com.owneroftime.guestbook.common.utility.GuestBookSecurityContextHolder;
import com.owneroftime.guestbook.common.utility.converter.EntityToModelUtil;
import com.owneroftime.guestbook.common.utility.converter.ModelToEntityUtil;
import com.owneroftime.guestbook.security.entity.UserEntity;
import com.owneroftime.guestbook.security.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = {Exception.class})
public class GuestBookEntryServiceImpl implements GuestBookEntryService{

    @Autowired
    private GuestBookEntryRepository guestBookEntryRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Override
    public void createGuestBookEntry(GuestBookEntryModel guestBookEntryModel) {
        GuestBookEntryEntity guestBookEntryEntity = ModelToEntityUtil.convertGuestBookEntryModelToGuestBookEntryEntity(guestBookEntryModel);

        if (null != guestBookEntryModel.getGuestBookEntryImage()) {
            guestBookEntryEntity.setGuestBookEntryImage(guestBookEntryModel.getGuestBookEntryImage());
        } else {
            guestBookEntryEntity.setGuestBookEntryText(guestBookEntryModel.getGuestBookEntryText());
        }

        if (null == guestBookEntryModel.getCapturedBy()) {
            UserEntity userEntity = userDetailsRepository.loadUserByUsername(GuestBookSecurityContextHolder.getContext().getUserEmail());
            guestBookEntryEntity.setUserEntity(userEntity);
        } else {
            UserEntity userEntity = userDetailsRepository.loadUserByUsername(guestBookEntryModel.getCapturedBy());
            guestBookEntryEntity.setUserEntity(userEntity);
        }
        guestBookEntryEntity.setGuestBookEntryStatus(1L);
        guestBookEntryRepository.save(guestBookEntryEntity);
    }

    @Override
    public List<GuestBookEntryModel> getAllGuestBookEntries() {
        List<GuestBookEntryEntity> guestBookEntryEntities = guestBookEntryRepository.findAll(Sort.by(Sort.Order.desc("createdOn")));
        List<GuestBookEntryModel> guestBookEntryModels = new ArrayList<>();
        guestBookEntryEntities.forEach(guestBookEntryEntity ->
                guestBookEntryModels.add(EntityToModelUtil.convertGuestBookEntryEntityToGuestBookEntryModel(guestBookEntryEntity)));
        return guestBookEntryModels;
    }

    @Override
    public void deleteGuestBookEntries(String guestBookEntryIds) {
        List<String> guestBookEntryIdList = Arrays.asList(guestBookEntryIds.split(","));
        guestBookEntryIdList.forEach(guestBookEntryId -> {
            if (null != guestBookEntryId) {
                guestBookEntryRepository.deleteById(Long.parseLong(guestBookEntryId));
            }
        });
    }

    @Override
    public void approveGuestBookEntries(String guestBookEntryIds) {
        List<String> guestBookEntryIdList = Arrays.asList(guestBookEntryIds.split(","));
        guestBookEntryIdList.forEach(guestBookEntryId -> {
            if (null != guestBookEntryId) {
                Optional<GuestBookEntryEntity> guestBookEntryEntity = guestBookEntryRepository.findById(Long.parseLong(guestBookEntryId));
                guestBookEntryEntity.ifPresent(entity -> {
                    entity.setGuestBookEntryStatus(2L);
                    guestBookEntryRepository.save(entity);
                });
            }
        });
    }

    @Override
    public List<GuestBookEntryModel> getAllGuestBookEntriesForUser(Long userId) {
        List<GuestBookEntryEntity> guestBookEntryEntities = guestBookEntryRepository.getAllGuestBookEntriesForUser(userId);
        List<GuestBookEntryModel> guestBookEntryModels = new ArrayList<>();;
        if (null != guestBookEntryEntities) {
            guestBookEntryEntities.forEach(guestBookEntryEntity -> {
                guestBookEntryModels.add(EntityToModelUtil.convertGuestBookEntryEntityToGuestBookEntryModel(guestBookEntryEntity));
            });
        }
        return guestBookEntryModels;
    }

    @Override
    public void updateGuestEntry(GuestBookEntryModel guestBookEntryModel) {
        Optional<GuestBookEntryEntity> guestBookEntryEntity = guestBookEntryRepository.findById(guestBookEntryModel.getGuestBookEntryId());
        guestBookEntryEntity.ifPresent(guestBookEntryEntity1 -> {
            if (null != guestBookEntryModel.getGuestBookEntryImage()) {
                guestBookEntryEntity1.setGuestBookEntryImage(guestBookEntryModel.getGuestBookEntryImage());
            } else {
                guestBookEntryEntity1.setGuestBookEntryText(guestBookEntryModel.getGuestBookEntryText());
            }

            if (null == guestBookEntryModel.getCapturedBy()) {
                UserEntity userEntity = userDetailsRepository.loadUserByUsername(GuestBookSecurityContextHolder.getContext().getUserEmail());
                guestBookEntryEntity1.setUserEntity(userEntity);
            } else {
                UserEntity userEntity = userDetailsRepository.loadUserByUsername(guestBookEntryModel.getCapturedBy());
                guestBookEntryEntity1.setUserEntity(userEntity);
            }
//            guestBookEntryEntity1.setGuestBookEntryText(guestBookEntryModel.getGuestBookEntryText());
            guestBookEntryRepository.save(guestBookEntryEntity1);
        });
    }
}
