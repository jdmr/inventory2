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

import com.kinetic.inventory.dao.ClientRepository;
import com.kinetic.inventory.model.Client;
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
/* The directory used by the controller will be /client and any mapping values hereinafter
 * will be assumed to be under the /client directory
 */
@RequestMapping("/client")
public class ClientController {

    // declaration of 'log' for debugging purposes
    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
    
    // Connect to the clientRepository class to use the object 'client'
    @Autowired
    private ClientRepository clientRepository;

    // the directory '/client/list' will receive the list array
    @RequestMapping(value = {"", "/list"})
    public String list(Model model) {
        model.addAttribute("list", clientRepository.findAll());
        return "/client/list";
    }

    // the directory '/client/newClient' will be the directory to the form
    @RequestMapping("/newClient")
    public String newClient(Model model) {
        model.addAttribute("client", new Client());
        return "/client/newClient";
    }

    // if theres errors return to the form, if its all good show the values saved
    @RequestMapping("/create")
    public String createClient(@Valid Client client, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/client/newClient";
        }
        client = clientRepository.save(client);
        redirectAttributes.addFlashAttribute("message", "The Client " + client.getCompany() + " has been created");
        return "redirect:/client/see/" + client.getId() + "/";
    }

    // the '/client/see/{id}' directory takes in the client id and shows its attributes
    @RequestMapping("/see/{id}")
    public String see(@PathVariable Long id, Model model) {
        Client client = clientRepository.findOne(id);
        model.addAttribute("client", client);
        return "/client/see";
    }

    // the '/client/delete/{id}' directory will delete the client with the id
    @RequestMapping("/delete/{id}/")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Client client = clientRepository.findOne(id);
        clientRepository.delete(client);
        redirectAttributes.addFlashAttribute("message", "The Client " + id + " has been deleted");
        return "redirect:/client/";
    }
}
