package sud.core.dto.support;


import sud.core.action.checkutils.CheckInfo;

import java.util.Optional;

public abstract class BaseEntityDTO<T> {
    private CheckInfo checkInfo;

    public BaseEntityDTO() {
    }

    public abstract boolean isIdSet();

    public abstract boolean isIdOldSet();

    public abstract T getId();

    public Optional<CheckInfo> getCheckInfo() {
        return Optional.ofNullable(this.checkInfo);
    }

    public void setCheckInfo(CheckInfo checkInfo) {
        this.checkInfo = checkInfo;
    }

    public void clearCheckList() {
        this.checkInfo = null;
    }

    public boolean containsCriticalErrors() {
        return this.getCheckInfo().map(CheckInfo::containsCritical).orElse(false);
    }
}
