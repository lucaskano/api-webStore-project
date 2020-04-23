package br.com.webstore.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @project: api-webstore
 * @author: Lucas Kanô de Oliveira (lucaskano)
 * @since: 23/04/2020
 */
public class PasswordEncoder {

    public static void main (String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("lucaskano"));
    }

}