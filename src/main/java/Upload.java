import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Bonheur on 05/01/2015.
 */
public class Upload {

    public String upload(String url, File uploadFile, String authtoken) throws IOException {
        WebResource resource = Client.create().resource(url);
        FormDataMultiPart form = new FormDataMultiPart();
        form.field("fileName", uploadFile.getName());
        if(StringUtils.isNotBlank(authtoken)){
            resource.header("X-Auth-Token",authtoken);
        }

        FormDataBodyPart fdp = new FormDataBodyPart("file",
                new FileInputStream(uploadFile),
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
        form.bodyPart(fdp);
        String response = resource.type(MediaType.MULTIPART_FORM_DATA).post(String.class, form);
        return response;
    }
    public String authentication(String url, String user, String pwd) throws IOException {
        WebResource resource = Client.create().resource(url);
//        FormDataMultiPart form = new FormDataMultiPart();
//        resource.setProperty("username", user);
  //      resource.setProperty("password", pwd);
//        form.field("username", user);
//        form.field("password", pwd);


        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("username", user); //set parametes for request
        queryParams.add("password", pwd); //set parametes for request

        ClientResponse response = null;
        response = resource.queryParams(queryParams)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(ClientResponse.class);

        String jsonStr = response.getEntity(String.class);
        //Get response from RESTful Server
        System.out.println("Testing:");
        System.out.println(jsonStr);
//        String response = resource.type(MediaType.APPLICATION_JSON).post(String.class, form);
        return jsonStr;
    }

}
