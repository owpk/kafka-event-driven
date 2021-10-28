package owpk;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Request {
    private Integer reqId;
    private String name;
    private String value;

    public Request(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
