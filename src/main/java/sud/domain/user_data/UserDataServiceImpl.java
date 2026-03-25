package sud.domain.user_data;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sud.domain.person.PersonRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDataServiceImpl implements UserDataService {

    private final UserDataRepository userDataRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    @Override
    public List<UserData> findAllUsers() {
        return userDataRepository.findAll();
    }

    private void setPassword(String password, UserData userData) {
        userData.setPassword(passwordEncoder.encode(password));
    }


    @Override
    public void createUserData(String login, Long personId) {
        createUserData(login, login, personId);
    }

    @Override
    public void createUserData(String login, String password, Long personId) {
        UserData userData = new UserData();
        userData.setLogin(login);
        setPassword(password, userData);
        if (personId != null) {
            userData.setPerson(personRepository.getReferenceById(personId));
        }
        userDataRepository.save(userData);
    }

    @Override
    public void clearPassword(String login) {
        UserData userData = userDataRepository.findByLogin(login);
        setPassword(login, userData);
        userDataRepository.save(userData);
    }
}
