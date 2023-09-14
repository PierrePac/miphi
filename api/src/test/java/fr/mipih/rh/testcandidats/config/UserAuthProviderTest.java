package fr.mipih.rh.testcandidats.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Map;

import com.auth0.jwt.interfaces.Verification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import fr.mipih.rh.testcandidats.dtos.AdminDto;
import fr.mipih.rh.testcandidats.dtos.CandidatDto;
import fr.mipih.rh.testcandidats.services.AdminService;
import fr.mipih.rh.testcandidats.services.CandidatService;

@SpringBootTest
public class UserAuthProviderTest {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Mock
    private AdminService adminService;

    @Mock
    private CandidatService candidatService;

    @InjectMocks
    private UserAuthProvider userAuthProvider;

    JWTVerifier mockVerifier = mock(JWTVerifier.class);
    DecodedJWT mockDecodedJWT = mock(DecodedJWT.class);

    @BeforeEach
    public void setUp() {
        userAuthProvider = new UserAuthProvider(adminService, candidatService);
        ReflectionTestUtils.setField(userAuthProvider, "secretKey", "MaCleSecrete123");
        userAuthProvider.init();

    }

    @Test
    public void testCreateTokensForAdmin() {
        AdminDto adminDto = new AdminDto();
        adminDto.setNom("AdminNom");
        adminDto.setPrenom("AdminPrenom");

        when(adminService.save(adminDto)).thenReturn(adminDto);

        Map<String, String> tokens = userAuthProvider.createTokens(adminDto);

        assertNotNull(tokens.get("accessToken"));
        assertNotNull(tokens.get("refreshToken"));
        assertEquals(adminDto.getRefreshToken(), tokens.get("refreshToken"));
    }

    @Test
    public void testCreateTokensForCandidat() {
        CandidatDto candidatDto = new CandidatDto();
        candidatDto.setNom("CandidatNom");
        candidatDto.setPrenom("CandidatPrenom");

        when(candidatService.save(candidatDto)).thenReturn(candidatDto);

        Map<String, String> tokens = userAuthProvider.createTokens(candidatDto);

        assertNotNull(tokens.get("accessToken"));
        assertNotNull(tokens.get("refreshToken"));
        assertEquals(candidatDto.getRefreshToken(), tokens.get("refreshToken"));
    }

    @Test
    public void shouldCreateTokenForAdmin() {
        AdminDto admin = new AdminDto();
        admin.setNom("AdminNom");
        admin.setPrenom("AdminPrenom");

        String token = userAuthProvider.createAccessToken(admin);
        DecodedJWT jwt = JWT.decode(token);

        assertEquals("AdminNom", jwt.getClaim("nom").asString());
        assertEquals("AdminPrenom", jwt.getClaim("prenom").asString());
        assertEquals("ADMIN", jwt.getIssuer());
    }

    @Test
    public void shouldThrowExceptionForUnknownUser() {
        String unknown = "unknown";

        assertThrows(IllegalArgumentException.class, () -> {
            userAuthProvider.createAccessToken(unknown);
        });
    }

    @Test
    public void shouldCreateRefreshTokenForAdmin(){
        AdminDto adminDto = new AdminDto();
        adminDto.setNom("AdminNom");
        adminDto.setPrenom("adminPrenom");

        Map<String, String> tokens = userAuthProvider.createTokens(adminDto);
        String refreshToken = tokens.get("refreshToken");

        DecodedJWT jwt = JWT.decode(refreshToken);

        assertEquals("ADMIN", jwt.getIssuer());
        assertNotNull(jwt.getExpiresAt());
        assertNotNull(jwt.getIssuedAt());
    }

    @Test
    public void shouldCreateRefreshTokenForCandidat() {
        CandidatDto candidatDto = new CandidatDto();
        candidatDto.setNom("candidatNom");
        candidatDto.setPrenom("cnadidatprenom");

        Map<String, String> tokens = userAuthProvider.createTokens(candidatDto);
        String refreshToken = tokens.get("refreshToken");

        DecodedJWT jwt = JWT.decode(refreshToken);

        assertEquals("CANDIDAT", jwt.getIssuer());
        assertNotNull(jwt.getExpiresAt());
        assertNotNull(jwt.getIssuedAt());
    }

}




