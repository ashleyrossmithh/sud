package sud.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sud.domain.user_data.UserData;

import java.util.Arrays;

public enum Roles {
    ADMIN,
    BOSS,
    CLIENT,
    ADVOKAT,
    OTHER;

    public SimpleGrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + this.name());
    }

    public static Roles parse(String authority) {
        return Arrays.stream(values()).filter(cur -> cur.toAuthority().getAuthority().equals(authority)).findFirst().orElse(null);
    }

    public static Roles calcRole(Integer directionTypeCode) {
        DirectionType directionType = DirectionType.parse(directionTypeCode);
        return switch (directionType) {
            case ADVOCATE, HELPER -> Roles.ADVOKAT;
            case BOSS -> Roles.BOSS;
            case ZAYAV, OTV -> Roles.CLIENT;
            default -> Roles.OTHER;
        };
    }
}
