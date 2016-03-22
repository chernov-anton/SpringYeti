package com.antonchernov.yetilibrary.controller;

import com.antonchernov.yetilibrary.model.Response;
import com.antonchernov.yetilibrary.repository.AccountRepository;
import com.antonchernov.yetilibrary.model.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AccountRepository accountRepository;
    @Value("${secretKey}")
    private String secretKey;


    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Response login(@RequestBody Account account, HttpServletResponse response)
            throws ServletException {
        if (account.getUsername() == null || account.getPassword() == null) {
            return new Response();
        }
        Account dbAccount = accountRepository.findByUsername(account.getUsername());
        if (Objects.isNull(dbAccount) || !StringUtils.equals(dbAccount.getPassword(), account.getPassword())) {
            throw new ServletException("Invalid username");
        }

        response.setHeader("Authorization", "Bearer " + Jwts.builder().setSubject(account.getUsername())
                .claim("roles", dbAccount.getRoles()).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secretKey).compact());
        return new Response();
    }






}
