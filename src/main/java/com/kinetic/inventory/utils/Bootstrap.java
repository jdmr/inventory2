/*
 * The MIT License
 *
 * Copyright 2013 martinezl.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.kinetic.inventory.utils;

import com.kinetic.inventory.dao.RoleRepository;
import com.kinetic.inventory.dao.UserRepository;
import com.kinetic.inventory.model.Role;
import com.kinetic.inventory.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Configuration file can be scanned by the classpath scanning
@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {
    
    // declaration of 'log' for debugging purposes
    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);
    
    // Declarations of the userRepository class and the passwordEncoder
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Method to validate roles and users, if there's no user it'll create admin
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Validating Roles");
        Role adminRole = roleRepository.findOne("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ROLE_ADMIN");
            adminRole = roleRepository.save(adminRole);
        }
        
        Role userRole = roleRepository.findOne("ROLE_USER");
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            userRole = roleRepository.save(userRole);
        }
        
        log.info("Validating Users");
        User admin = userRepository.findOne("admin@kinetic.com");
        if (admin == null) {
            String password = passwordEncoder.encode("admin");
            admin = new User("admin@kinetic.com", password, "Admin", "User");
            admin.addRole(adminRole);
            admin.addRole(userRole);
            userRepository.save(admin);
        }
        
        log.info("Application is running!");
    }
    
    
}
