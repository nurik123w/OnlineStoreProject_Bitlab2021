package com.example.onlinestoreproject.config;
import com.example.onlinestoreproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,proxyTargetClass = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserService userService;

//    @Autowired
//    public void setUserService(UserService userService){
//        this.userService = userService;
//    }
//
//    public UserService getUserService(){
//        return userService;
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().accessDeniedPage("/403");

        http.authorizeRequests().antMatchers("/","/css/**","/js/**").permitAll();

        http.formLogin()
                .loginPage("/login").permitAll()
                .usernameParameter("user_email")
                .passwordParameter("user_password")
                .loginProcessingUrl("/auth").permitAll()
                .failureUrl("/login?error")
                .defaultSuccessUrl("/");

        http.logout()
                .logoutUrl("/logout").permitAll()
                .logoutSuccessUrl("/");

      //   http.csrf().disable();
    }
}
