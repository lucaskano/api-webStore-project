package br.com.webstore.config;

/**
 * @project: api-webstore
 * @author: Lucas Kanô de Oliveira (lucaskano)
 * @since: 23/04/2020
 */
public class SecurityConstants {
    //Authorization Bearer MyToken*******

    public static final String SECRET = "LucasKano";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
    static final long EXPIRATION_TIME = 864000001;
}
