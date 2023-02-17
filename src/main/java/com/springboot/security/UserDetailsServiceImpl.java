package com.springboot.security;

import com.springboot.dao.UserDao;
import com.springboot.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        List<User> byEmail = userDao.findByEmail(s);
        User user = userDao.getUserByUserName(s);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Available");
        }
        UserDetailImpl userDetail = new UserDetailImpl(user);

        return userDetail;
    }
}
