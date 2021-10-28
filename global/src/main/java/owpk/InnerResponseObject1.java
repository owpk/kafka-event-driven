package owpk;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InnerResponseObject1 {
    private String body;
    private List<InnerResponseObject2> innerResponseObject2List;
}
