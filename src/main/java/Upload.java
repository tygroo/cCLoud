import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Bonheur on 05/01/2015.
 */
public class Upload {

    public String upload(String url, File uploadFile) throws IOException {
        WebResource resource = Client.create().resource(url);
        FormDataMultiPart form = new FormDataMultiPart();
        form.field("fileName", uploadFile.getName());
        FormDataBodyPart fdp = new FormDataBodyPart("file",
                new FileInputStream(uploadFile),
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
        form.bodyPart(fdp);
        String response = resource.type(MediaType.MULTIPART_FORM_DATA).post(String.class, form);
        return response;
    }
}
