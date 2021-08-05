package javapro.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDialogResponse {

    private String error;
    private Long timestamp;
    private CreateDialogData data;
}
