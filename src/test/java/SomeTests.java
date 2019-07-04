import Entities.RegisterUser;
import Entities.User;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;


public class SomeTests {

    @Test
    public void GetAndRegiesterUserTest(){
        ReqresClient reqresClient = new ReqresClient();
        User user = reqresClient.GetUser(2);
        assertThat(reqresClient.GetStatusCode(), is(200));
        assertThat(user.getLastName(), is("Weaver"));

        RegisterUser user1 = new RegisterUser();
        user1.setEmail("eve.holt@reqres.in");
        user1.setPassword("dsfsdfsdf");
        JsonNode json = reqresClient.Register(user1);
        assertThat(reqresClient.GetStatusCode(), is(200));
        assertThat(json.get("id").asText(), is("4"));
        assertThat(json.get("token").asText(), is("QpwL5tke4Pnpja7X4"));
    }
}
