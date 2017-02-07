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

import com.kinetic.inventory.dao.ProductRepository;
import com.kinetic.inventory.model.Product;
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
/* The directory used by the controller will be /products and any mapping values hereinafter
 * will be assumed to be under the /products directory
 */
@RequestMapping("/products")
public class ProductsController {

    // declaration of 'log' for debugging purposes
    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
    
    // Declare and connect the ProductsDao class to use here
    @Autowired
    private ProductRepository productRepository;

    // Method that takes us to the directory of the products list
    @RequestMapping(value = {"", "/list"})
    public String list(Model model) {
        model.addAttribute("list", productRepository.findAll());
        return "/products/list";
    }

    // Method to the form of the new product
    @RequestMapping("/newProduct")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "/products/newProduct";
    }

    // Creates new product, if any errors goes back to the form
    @RequestMapping("/create")
    public String createProduct(@Valid Product products, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/products/newProduct";
        }
        products = productRepository.save(products);
        redirectAttributes.addFlashAttribute("message", "The Product " + products.getModel() + " has been created");
        return "redirect:/products/see/" + products.getId() + "/";
    }

    // Shows the values we input for the product attributes
    @RequestMapping("/see/{id}")
    public String see(@PathVariable Long id, Model model) {
        Product product = productRepository.findOne(id);
        model.addAttribute("product", product);
        return "/products/see";
    }

    // Deletes a product
    @RequestMapping("/delete/{id}/")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Product product = productRepository.findOne(id);
        productRepository.delete(product);
        redirectAttributes.addFlashAttribute("message", "The product " + id + " has been deleted");
        return "redirect:/products/";
    }
}
