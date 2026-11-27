package com.mjpascual.gestbanda;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.mjpascual.gestbanda.dto.img.ImgResponse;
import com.mjpascual.gestbanda.services.ImgService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end checks of who can do what in a band, against a real PostgreSQL.
 * Imgur is mocked so no network call is made.
 */
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(properties = "jwt.secret=test-secret-key-that-is-at-least-32-bytes-long")
class BandAuthorizationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    private static final AtomicInteger PHONE = new AtomicInteger(600000000);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper json;

    @MockitoBean
    private ImgService imgService;

    private User director;
    private User member;
    private User leader;
    private User outsider;
    private String bandId;
    private String drumVoiceId;
    private String snareVoiceId;

    private record User(String id, String token) {
    }

    @BeforeEach
    void setUpBand() throws Exception {
        ImgResponse img = new ImgResponse();
        img.setData(new ImgResponse.DataImg());
        img.getData().setLink("https://i.imgur.com/logo.png");
        when(imgService.uploadImage(any())).thenReturn(img);

        director = register("Director");
        member = register("Componente");
        leader = register("Jefe");
        outsider = register("Ajeno");

        MockMultipartFile request = new MockMultipartFile("createBandRequest", "", MediaType.APPLICATION_JSON_VALUE,
                "{\"name\":\"Banda Prueba\"}".getBytes());
        MockMultipartFile logo = new MockMultipartFile("img", "logo.png", MediaType.IMAGE_PNG_VALUE, new byte[]{1});
        bandId = body(mvc.perform(multipart("/api/band/registerBand").file(request).file(logo).header(HttpHeaders.AUTHORIZATION, director.token()))
                .andExpect(status().isCreated()).andReturn()).get("id").asText();

        drumVoiceId = addVoice("Tambor");
        snareVoiceId = addVoice("Caja");

        join(member, drumVoiceId);
        join(leader, drumVoiceId);
        modify(director, leader, roleId("JEFE DE VOZ"), drumVoiceId).andExpect(status().isOk());
    }

    @Test
    void requestsWithoutTokenGet401WithJsonBody() throws Exception {
        mvc.perform(get("/api/roles/getAllRoles"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(14));
    }

    @Test
    void failedLoginReturnsTheUsualErrorBodyWithAnIsoDate() throws Exception {
        String body = json.writeValueAsString(Map.of("email", "nobody@example.com", "password", "Password1!"));
        mvc.perform(post("/api/users/login").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(3))
                .andExpect(jsonPath("$.timeMark").value(matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+00:00")));
    }

    @Test
    void tokenIsAcceptedWithAndWithoutTheBearerPrefix() throws Exception {
        String rawToken = director.token().substring("Bearer ".length());

        mvc.perform(get("/api/roles/getAllRoles").header(HttpHeaders.AUTHORIZATION, "Bearer " + rawToken))
                .andExpect(status().isOk());
        mvc.perform(get("/api/roles/getAllRoles").header(HttpHeaders.AUTHORIZATION, rawToken))
                .andExpect(status().isOk());
        mvc.perform(get("/api/roles/getAllRoles").header(HttpHeaders.AUTHORIZATION, "bearer " + rawToken))
                .andExpect(status().isOk());
        mvc.perform(get("/api/roles/getAllRoles").header(HttpHeaders.AUTHORIZATION, "not-a-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void joiningWithACodeGivesTheMemberRole() throws Exception {
        JsonNode members = body(mvc.perform(as(director, get("/api/userBands/getBandUsers").param("id", bandId)))
                .andExpect(status().isOk()).andReturn());

        assertThat(roleOf(members, director)).isEqualTo("DIRECTOR");
        assertThat(roleOf(members, member)).isEqualTo("COMPONENTE");
    }

    @Test
    void userInTheBodyIsIgnored() throws Exception {
        String code = createCode(drumVoiceId);
        String body = json.writeValueAsString(Map.of("code", code, "user", Map.of("id", director.id())));

        mvc.perform(as(outsider, post("/api/codes/joinBandByCode")).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());

        mvc.perform(as(outsider, get("/api/userBands/getBandUsers").param("id", bandId)))
                .andExpect(status().isOk());
    }

    @Test
    void nonMembersCannotReadTheBand() throws Exception {
        mvc.perform(as(outsider, get("/api/userBands/getBandUsers").param("id", bandId)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(12));
        mvc.perform(as(outsider, get("/api/bandVoices/getVoicesBand").param("id", bandId)))
                .andExpect(status().isForbidden());
        mvc.perform(as(outsider, get("/api/users/getUserByID").param("id", director.id())))
                .andExpect(status().isForbidden());
    }

    @Test
    void membersCanSeeEachOtherButOnlyTheirOwnBandList() throws Exception {
        mvc.perform(as(member, get("/api/users/getUserByID").param("id", director.id())))
                .andExpect(status().isOk());
        mvc.perform(as(member, get("/api/userBands/getUserBands").param("id", member.id())))
                .andExpect(status().isOk());
        mvc.perform(as(member, get("/api/userBands/getUserBands").param("id", director.id())))
                .andExpect(status().isForbidden());
    }

    @Test
    void onlyTheDirectorManagesCodesAndVoices() throws Exception {
        String body = json.writeValueAsString(Map.of("band_id", bandId, "voice_id", drumVoiceId, "end_date", "2099-01-01T00:00:00Z"));
        mvc.perform(as(member, post("/api/codes/createBandCode")).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isForbidden());
        mvc.perform(as(leader, get("/api/codes/getBandCodes").param("id", bandId)))
                .andExpect(status().isForbidden());
        mvc.perform(as(member, delete("/api/bandVoices/deleteVoiceBand").param("idVoice", drumVoiceId).param("idBand", bandId)))
                .andExpect(status().isForbidden());
    }

    @Test
    void voiceLeaderManagesOnlyMembersOfTheirVoiceAndCannotChangeRoles() throws Exception {
        modify(leader, member, roleId("DIRECTOR"), drumVoiceId).andExpect(status().isForbidden());

        modify(leader, member, roleId("COMPONENTE"), snareVoiceId).andExpect(status().isOk());

        // The member now plays another voice, so the leader no longer manages them
        mvc.perform(as(leader, delete("/api/userBands/deleteUserBand").param("idUser", member.id()).param("idBand", bandId)))
                .andExpect(status().isForbidden());
    }

    @Test
    void voiceLeaderCanNeverAssignTheDirectorRole() throws Exception {
        modify(leader, leader, roleId("DIRECTOR"), drumVoiceId).andExpect(status().isForbidden());
        modify(leader, member, roleId("JEFE DE VOZ"), drumVoiceId).andExpect(status().isForbidden());
        modify(leader, director, roleId("COMPONENTE"), drumVoiceId).andExpect(status().isForbidden());

        JsonNode members = body(mvc.perform(as(director, get("/api/userBands/getBandUsers").param("id", bandId))).andReturn());
        assertThat(roleOf(members, leader)).isEqualTo("JEFE DE VOZ");
        assertThat(roleOf(members, member)).isEqualTo("COMPONENTE");
        assertThat(roleOf(members, director)).isEqualTo("DIRECTOR");
    }

    @Test
    void voiceLeaderCannotManageMembersOfAnotherVoice() throws Exception {
        modify(director, member, roleId("COMPONENTE"), snareVoiceId).andExpect(status().isOk());

        modify(leader, member, roleId("COMPONENTE"), drumVoiceId).andExpect(status().isForbidden());
    }

    @Test
    void voiceLeaderCanRemoveMembersOfTheirVoice() throws Exception {
        mvc.perform(as(leader, delete("/api/userBands/deleteUserBand").param("idUser", member.id()).param("idBand", bandId)))
                .andExpect(status().isOk());
    }

    @Test
    void membersCanLeaveButNotRemoveOthers() throws Exception {
        mvc.perform(as(member, delete("/api/userBands/deleteUserBand").param("idUser", leader.id()).param("idBand", bandId)))
                .andExpect(status().isForbidden());
        mvc.perform(as(member, delete("/api/userBands/deleteUserBand").param("idUser", member.id()).param("idBand", bandId)))
                .andExpect(status().isOk());
    }

    @Test
    void clientErrorsAreNot500() throws Exception {
        mvc.perform(as(director, delete("/api/userBands/deleteUserBand").param("idUser", outsider.id()).param("idBand", bandId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(3));
        String unknownVoice = json.writeValueAsString(Map.of("voice_name", "Arpa", "band_uuid", bandId));
        mvc.perform(as(director, post("/api/bandVoices/addVoiceToBand")).contentType(MediaType.APPLICATION_JSON).content(unknownVoice))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(13));
        mvc.perform(as(director, get("/api/userBands/getBandUsers").param("id", "not-a-uuid")))
                .andExpect(status().isBadRequest());
    }

    private User register(String name) throws Exception {
        int phone = PHONE.incrementAndGet();
        String body = json.writeValueAsString(Map.of(
                "name", name, "email", name.toLowerCase() + phone + "@example.com", "phone", String.valueOf(phone), "password", "Password1!"));
        MvcResult result = mvc.perform(post("/api/users/registerUser").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andReturn();
        return new User(body(result).get("id").asText(), result.getResponse().getHeader(HttpHeaders.AUTHORIZATION));
    }

    private String addVoice(String name) throws Exception {
        String body = json.writeValueAsString(Map.of("voice_name", name, "band_uuid", bandId));
        return body(mvc.perform(as(director, post("/api/bandVoices/addVoiceToBand")).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andReturn()).get("id").asText();
    }

    private String createCode(String voiceId) throws Exception {
        String body = json.writeValueAsString(Map.of("band_id", bandId, "voice_id", voiceId, "end_date", "2099-01-01T00:00:00Z"));
        return body(mvc.perform(as(director, post("/api/codes/createBandCode")).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andReturn()).get("code").asText();
    }

    private void join(User user, String voiceId) throws Exception {
        String body = json.writeValueAsString(Map.of("code", createCode(voiceId)));
        mvc.perform(as(user, post("/api/codes/joinBandByCode")).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
    }

    private ResultActions modify(User actor, User target, String roleId, String voiceId) throws Exception {
        String body = json.writeValueAsString(Map.of("band_id", bandId, "user_id", target.id(), "role_id", roleId, "voice_id", voiceId));
        return mvc.perform(as(actor, put("/api/userBands/modifyUserBand")).contentType(MediaType.APPLICATION_JSON).content(body));
    }

    private String roleId(String name) throws Exception {
        for (JsonNode role : body(mvc.perform(as(director, get("/api/roles/getAllRoles"))).andReturn())) {
            if (role.get("name").asText().equals(name)) {
                return role.get("id").asText();
            }
        }
        throw new IllegalStateException("Role not found: " + name);
    }

    private static String roleOf(JsonNode members, User user) {
        for (JsonNode m : members) {
            if (m.get("user").get("id").asText().equals(user.id())) {
                return m.get("role").get("name").asText();
            }
        }
        throw new IllegalStateException("User is not a member: " + user.id());
    }

    private static MockHttpServletRequestBuilder as(User user, MockHttpServletRequestBuilder request) {
        return request.header(HttpHeaders.AUTHORIZATION, user.token());
    }

    private JsonNode body(MvcResult result) throws Exception {
        return json.readTree(result.getResponse().getContentAsString());
    }
}
