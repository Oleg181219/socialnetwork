package JavaPRO.api.response;

import JavaPRO.model.DTO.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostUpdateResponse extends Response{

    private String error;

    private Long timestamp;

    private PostDTO post;
}
