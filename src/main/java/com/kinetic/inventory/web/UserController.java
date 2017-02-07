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
package com.kinetic.inventory.web;

import com.kinetic.inventory.dao.UserRepository;
import com.kinetic.inventory.model.User;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Defines class as a controller in the MVC architecture
@Controller
/* The directory used by the controller will be /admin/user and any mapping values hereinafter
 * will be assumed to be under the /admin/user directory, everything under /admin
 * will only be accessed by users with the admin role
 */
@RequestMapping("/admin/user")
public class UserController {

    // declaration of 'log' for debugging purposes
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    
    // Declare userRepository and connect to the UserDao class
    @Autowired
    private UserRepository userRepository;

    // Returns us to the page where the list of users is
    @RequestMapping(value = {"", "/list"})
    private String list(Model model) {
        model.addAttribute("list", userRepository.findAll());
        return "admin/user/list";
    }

    // Takes us to the new user form
    @RequestMapping("/newUser")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "admin/user/newUser";
    }

    // Creates user unless there's an error, then we go back to the form
    @RequestMapping("/create")
    public String createUser(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/user/newUser";
        }
        log.debug("{}", user);
        user = userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "The User " + user.getFullName() + " has been created");

        return "redirect:/admin/user/see/" + user.getUsername()+"/";
    }

    // After a new user is created we see the values we input
    @RequestMapping("/see/{username}")
    public String see(@PathVariable String username, Model model) {
        User user = userRepository.findOne(username);
        log.debug("{}", user);
        model.addAttribute("user", user);
        return "admin/user/see";
    }

    // Deletes the user, and we go back to the list of users
    @RequestMapping("/delete/{username}/")
    public String delete(@PathVariable String username, Model model, RedirectAttributes redirectAttributes) {
         log.debug("deleting" );
        User user = userRepository.findOne(username);
        userRepository.delete(user);
        redirectAttributes.addFlashAttribute("message", "The user " + username + " has been deleted");
        return "redirect:/admin/user";
    }
}
