package com.example.onlinestoreproject.services.impl;
import com.example.onlinestoreproject.entity.Roles;
import com.example.onlinestoreproject.entity.Users;
import com.example.onlinestoreproject.repostories.RolesRepository;
import com.example.onlinestoreproject.repostories.UserRepository;
import com.example.onlinestoreproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RolesRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;





//    @Autowired
//    public void setBCryptEncoder(BCryptPasswordEncoder passwordEncoder){
//        this.passwordEncoder = passwordEncoder;
//    }

//    public BCryptPasswordEncoder getPasswordEncoder(){
//        return passwordEncoder;
//    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Users myUser = userRepository.findByEmail(s);
        if(myUser!=null){
            return new User(myUser.getEmail(),myUser.getPassword(), myUser.getRoles());
        }
        throw new UsernameNotFoundException("User not Found");
    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users createUser(Users users) {
        Users checkUser = userRepository.findByEmail(users.getEmail());
        if(checkUser==null){
            Roles role = roleRepository.findByRole("ROLE_USER");
            if(role!=null){
                ArrayList<Roles> roles = new ArrayList<>();
                roles.add(role);
                users.setRoles(roles);
                users.setPassword(passwordEncoder.encode(users.getPassword()));
                return userRepository.save(users);
            }
        }
        return  null;
    }

    @Override
    public void changePassword(Users users,String password, String rePassword) {
       if(password.equals(rePassword)){
           users.setPassword(passwordEncoder.encode(password));
           userRepository.save(users);
       }
    }




    @Override
    public List<Users> getAllUsers(Long id) {
        return userRepository.findAllByIdIsNot(id);
    }

    @Override
    public Users save(Users user) {
        return userRepository.save(user);
    }




}
