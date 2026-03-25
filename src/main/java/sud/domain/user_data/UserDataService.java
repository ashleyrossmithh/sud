package sud.domain.user_data;


import java.util.List;

public interface UserDataService {
    List<UserData> findAllUsers();

    void createUserData(String login, String password, Long personId);
    void createUserData(String login, Long personId);

    void clearPassword(String login);
}
