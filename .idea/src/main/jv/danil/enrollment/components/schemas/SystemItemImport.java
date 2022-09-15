package danil.enrollment.components.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
 public class SystemItemImport {
    private String id;
    private String type;
    private String parentId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String url;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer size;
}
