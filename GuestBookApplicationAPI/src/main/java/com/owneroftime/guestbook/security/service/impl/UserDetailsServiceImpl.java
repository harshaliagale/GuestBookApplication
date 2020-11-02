package com.owneroftime.guestbook.security.service.impl;

import com.owneroftime.guestbook.common.utility.converter.EntityToModelUtil;
import com.owneroftime.guestbook.common.utility.converter.ModelToEntityUtil;
import com.owneroftime.guestbook.security.constant.SecurityConstants;
import com.owneroftime.guestbook.security.entity.UserEntity;
import com.owneroftime.guestbook.security.repository.UserDetailsRepository;
import com.owneroftime.guestbook.security.model.UserModel;
import com.owneroftime.guestbook.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service(value = SecurityConstants.BEAN_USER_DETAILS_SERVICE_IMPL)
@Transactional(rollbackOn = {Exception.class})
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    /**
     * Get User details 
     * @param username
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity userEntity = userDetailsRepository.loadUserByUsername(username);
        if (null == userEntity) {
            throw new UsernameNotFoundException(username);
        }
        return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
    }

    /**
     * Service method to create user
     */
    @Override
    public Void createUser(UserModel userModel) throws Exception {
        UserEntity userEntity = ModelToEntityUtil.convertUserModelToUserEntity(userModel);
        if (null != userDetailsRepository.loadUserByUsername(userEntity.getEmail())) {
            throw new Exception("User email already exists. Please login to continue");
        }
        encryptPassword(userEntity);
        userDetailsRepository.save(userEntity);
        return null;
    }

    /**
     * Get User details
     * @param emailID
     * @return UserModel
     */
    @Override
    public UserModel getUserDetailsByEmail(String emailIO) throws Exception {
        UserEntity userEntity = userDetailsRepository.loadUserByUsername(emailIO);
        if (null == userEntity) {
        	throw new Exception("No User details found");
        }
        return EntityToModelUtil.converUserEntityToUserModel(userEntity);
    }

    /**
     * Encrypt password
     * @param userEntity
     */
    private void encryptPassword(UserEntity userEntity) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(userEntity.getPassword());
        userEntity.setPassword(encryptedPassword);
    }
}
