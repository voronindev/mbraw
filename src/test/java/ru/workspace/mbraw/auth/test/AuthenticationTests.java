package ru.workspace.mbraw.auth.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import ru.workspace.mbraw.webapp.ApplicationTestConfiguration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTestConfiguration.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class AuthenticationTests extends Assert {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mvc;

    @Before
    public void setUp() {
        if (mvc == null) {
            mvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(springSecurityFilterChain).build();
        }
    }

    @Test
    public void formAuthenticationSuccess() throws Exception {
        mvc.perform(formLogin("/login").user("user").password("password"))
                .andExpect(authenticated().withRoles("USER"));

    }

    @Test
    public void httpBasicAuthenticationSuccess() throws Exception {
        mvc.perform(get("/api/").with(httpBasic("user", "password")))
                .andExpect(authenticated().withRoles("USER"));
    }

    @Test
    public void formAuthenticationFailed() throws Exception {
        mvc.perform(formLogin("/login").user("wrongUser").password("wrongPassword"))
                .andExpect(unauthenticated());

    }

    @Test
    public void httpBasicAuthenticationFailed() throws Exception {
        mvc.perform(get("/api/").with(httpBasic("wrongUser", "wrongPassword")))
                .andExpect(unauthenticated());
    }
}
