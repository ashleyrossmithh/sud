package sud.core.action.checkutils;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CheckInfoAndBlockId extends CheckInfo {
    Long blockId;

    public CheckInfoAndBlockId() {
        super(new ArrayList<>());
    }
}

