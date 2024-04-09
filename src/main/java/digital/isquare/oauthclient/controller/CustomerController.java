package digital.isquare.oauthclient.controller;

import digital.isquare.oauthclient.entity.Customer;
import digital.isquare.oauthclient.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/")
    public String showHomePage(Model model, Principal principal) {
        model.addAttribute("isAuthenticated", principal != null);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
        request.logout();
        return "redirect:/";
    }

    @GetMapping("/customers")
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers";
    }

    @GetMapping("/customers/new")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "add_customer";
    }

    @PostMapping("/customers")
    public String addCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.saveOrUpdateCustomer(customer);
        return "redirect:/customers";
    }

    @GetMapping("/customers/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Customer customer = customerService.getCustomerId(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customer);
        return "update_customer";
    }

    @PutMapping("/customers/update/{id}")
    public String updateCustomer(@PathVariable("id") long id, @ModelAttribute("customer") Customer customer) {
        customerService.saveOrUpdateCustomer(customer);
        return "redirect:/customers";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable("id") long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
