package fr.mipih.rh.testcandidats.config;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import fr.mipih.rh.testcandidats.dtos.PersonneDto;
import fr.mipih.rh.testcandidats.services.PersonneService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class UserAuthProvider {

	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;
	
	private final PersonneService personneService;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	public String createToken(PersonneDto user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1 hour
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        
        return JWT.create()
                .withIssuer(user.getRole())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("nom", user.getNom())
                .withClaim("prenom", user.getPrenom())
                .sign(algorithm);
    }
	
	public Authentication validateToken(String token) {
		 Algorithm algorithm = Algorithm.HMAC256(secretKey);
		 
		 JWTVerifier verifier = JWT.require(algorithm).build();
		 
		 DecodedJWT decoded = verifier.verify(token);
		 
		 PersonneDto user = PersonneDto.builder()
				 .role(decoded.getIssuer())
				 .nom(decoded.getClaim("nom").asString())
				 .prenom(decoded.getClaim("prenom").asString())
				 .build();
		 
		 return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
	}
	
	 public Authentication validateTokenStrongly(String token) {
	        Algorithm algorithm = Algorithm.HMAC256(secretKey);

	        JWTVerifier verifier = JWT.require(algorithm)
	                .build();

	        DecodedJWT decoded = verifier.verify(token);

	        PersonneDto user = personneService.findByNom(decoded.getClaim("nom").asString());

	        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
	    }
	
}
