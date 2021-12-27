package com.danikvitek.IdentityService.config.security.jwt;

import com.danikvitek.IdentityService.config.security.UserDetailsImpl;
import com.danikvitek.IdentityService.data.model.entity.Role;
import com.danikvitek.IdentityService.data.model.entity.User;
import com.danikvitek.IdentityService.data.repository.BannedUserRepository;
import com.danikvitek.IdentityService.util.exception.NoTokenHeaderException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

public class JwtProcessor {
    private final JwtProperties properties;
    private final String secret;
    private final UserDetailsService userDetailsService;
    private final BannedUserRepository bannedUserRepository;

    @Autowired
    public JwtProcessor(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                        @NotNull JwtProperties properties,
                        BannedUserRepository bannedUserRepository) {
        this.userDetailsService = userDetailsService;
        this.bannedUserRepository = bannedUserRepository;
        this.properties = properties;
        this.secret = getBase64EncodedSecretKey(properties.getSecret());
    }

    private String getBase64EncodedSecretKey(@NotNull String secret) {
        return Base64.getEncoder().encodeToString(secret.getBytes());
    }
    
    public String createJwt(long userId, String username, GrantedAuthority authority) {
        Claims claims = createClaims(userId, username, authority);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + properties.getExpirationMillis());

        return buildJwt(claims, now, expirationDate);
    }

    private @NotNull Claims createClaims(long userId, String username, @NotNull GrantedAuthority authority) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("user_id", userId);
        claims.put("roles", authority.getAuthority());
        return claims;
    }

    private @NotNull @Unmodifiable Collection<? extends GrantedAuthority> getAuthorities(String jwt) {
        String role = (String) getClaims(jwt).getBody().get("roles");
        return Set.of(new SimpleGrantedAuthority(role));
    }

    private String buildJwt(Claims claims, Date issuedAt, Date expirationDate) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, this.secret)
                .compact();
    }

    @NotNull Authentication getAuthentication(String jwt) {
        UserDetails userDetails = new UserDetailsImpl(
                User.builder().username(getUsername(jwt)).build(),
                bannedUserRepository
        );
        return new UsernamePasswordAuthenticationToken(userDetails, "", getAuthorities(jwt));
    }

    public String getUsername(String jwt) {
        return getClaims(jwt).getBody().getSubject();
    }

    public long getUserId(String jwt) {
        return getClaims(jwt).getBody().get("user_id", Long.class);
    }

    public Role getRole(String jwt) {
        return Role.valueOf(getAuthorities(jwt).stream().findFirst().get().getAuthority());
    }

    private Jws<Claims> getClaims(String jwt) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt);
    }

    @NotNull String getJwt(@NotNull HttpServletRequest request) throws NoTokenHeaderException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return "no jwt";
    }

    boolean isValid(String jwt) {
        return jwt != null && jwt.contains(".") && hasNoWhitespaces(jwt) && isNotExpired(jwt);
    }

    @Contract(pure = true)
    private boolean hasNoWhitespaces(@NotNull String token) {
        return !token.matches("[\\s]");
    }

    private boolean isNotExpired(String jwt) {
        return isExpirationNotBeforeNow(getClaims(jwt));
    }

    private boolean isExpirationNotBeforeNow(@NotNull Jws<Claims> claims) {
        return !claims.getBody().getExpiration().before(new Date());
    }
}
