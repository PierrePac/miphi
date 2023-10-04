package fr.mipih.rh.testcandidats.config;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import fr.mipih.rh.testcandidats.dtos.AdminDto;
import fr.mipih.rh.testcandidats.dtos.CandidatDto;
import fr.mipih.rh.testcandidats.services.AdminService;
import fr.mipih.rh.testcandidats.services.CandidatService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class UserAuthProvider {

	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;

	private final AdminService adminService;
	private final CandidatService candidatService;

	@PostConstruct
	public void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public Map<String, String> createTokens(Object userDto) {
		String accessToken = createAccessToken(userDto);
		String refreshToken = createRefreshToken(userDto);


		if(userDto instanceof AdminDto) {
			AdminDto admin = (AdminDto) userDto;
			admin.setRefreshToken(refreshToken);
			adminService.save(admin);
		} else if (userDto instanceof CandidatDto) {
			CandidatDto candidat = (CandidatDto) userDto;
			candidat.setRefreshToken(refreshToken);

			candidatService.save(candidat);
		}

		Map<String, String> tokens = new HashMap<>();
		tokens.put("accessToken", accessToken);
		tokens.put("refreshToken", refreshToken);

		return tokens;
	}

	String createAccessToken(Object userDto) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + 3600 * 1000); //1 heure
		Algorithm algorithm = Algorithm.HMAC256(secretKey);

		String userType;
		String nom;
		String prenom;

		if (userDto instanceof AdminDto) {
			userType = "ADMIN";
			nom = ((AdminDto) userDto).getNom();
			prenom = ((AdminDto) userDto).getPrenom();
		} else if (userDto instanceof CandidatDto) {
			userType = "CANDIDAT";
			nom = ((CandidatDto) userDto).getNom();
			prenom = ((CandidatDto) userDto).getPrenom();
		} else {
			throw new IllegalArgumentException("Donné utilisateur incorrect");
		}

		return JWT.create()
				.withIssuer(userType)
				.withIssuedAt(now)
				.withExpiresAt(validity)
				.withClaim("nom", nom)
				.withClaim("prenom", prenom)
				.sign(algorithm);
	}

	private String createRefreshToken(Object userDto) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + 86400000); //1 jour
		Algorithm algorithm = Algorithm.HMAC256(secretKey);

		String userType = (userDto instanceof AdminDto) ? "ADMIN" : "CANDIDAT";

		return JWT.create()
				.withIssuer(userType)
				.withIssuedAt(now)
				.withExpiresAt(validity)
				.sign(algorithm);
	}

	public String refreshAccessToken(String refreshToken) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decoded = verifier.verify(refreshToken);
		String userType = decoded.getIssuer();

		Object userDto;

		if("ADMIN".equals(userType)) {
			AdminDto admin = adminService.findByRefreshToken(refreshToken);
			if(admin == null) {
				throw new IllegalArgumentException("Donnée admin incorrect");
			}
			userDto = admin;
		} else if ("CANDIDAT".equals(userType)) {
			CandidatDto candidat = candidatService.findByRefreshToken(refreshToken);
			if(candidat == null) {
				throw new IllegalArgumentException("Donnée candidat incorrect");
			}
			userDto = candidat;
		} else {
			throw new IllegalArgumentException("Donnée utilisateur incorrect");
		}

		return createAccessToken(userDto);
	}




	public Authentication validateToken(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey);

		JWTVerifier verifier = JWT.require(algorithm).build();

		DecodedJWT decoded = verifier.verify(token);
		String userType = decoded.getIssuer();
		String nom = decoded.getClaim("nom").asString();

		Object userDto;
		if ("ADMIN".equals(userType)) {
			userDto = adminService.findByNom(nom);
		} else if ("CANDIDAT".equals(userType)) {
			userDto = candidatService.findByNom(nom);
		} else {
			throw new IllegalArgumentException("Donné utilisateur incorrect");
		}


		return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());
	}

	public Authentication validateTokenStrongly(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey);

		JWTVerifier verifier = JWT.require(algorithm).build();

		DecodedJWT decoded = verifier.verify(token);
		String userType = decoded.getIssuer();
		String nom = decoded.getClaim("nom").asString();

		Object userDto;

		if ("ADMIN".equals(userType)) {
			AdminDto admin = adminService.findByNom(nom);
			if (admin == null) {
				throw new IllegalArgumentException("Donnée admin incorrect");
			}
			userDto = admin;
		} else if ("CANDIDAT".equals(userType)) {
			CandidatDto candidat = candidatService.findByNom(nom);
			if(candidat == null) {
				throw new IllegalArgumentException("Donnée candidat incorrect");
			}
			userDto = candidat;
		} else {
			throw new IllegalArgumentException("Donnée utilisateur incorrect");
		}

		return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());
	}

}
