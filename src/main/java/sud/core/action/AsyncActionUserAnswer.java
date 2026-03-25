package sud.core.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class AsyncActionUserAnswer {
    String asyncActionId;
    String answer;
}
