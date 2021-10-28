package owpk;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Response {
    private Integer responseId;
    private String responseBody;
    private List<InnerResponseObject1> innerResponseObject1List;

    public Response(String responseBody, List<InnerResponseObject1> innerResponseObject1List) {
        this.responseBody = responseBody;
        this.innerResponseObject1List = innerResponseObject1List;
    }
}
