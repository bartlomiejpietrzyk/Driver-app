package pl.bartlomiejpietrzyk.service;

import pl.bartlomiejpietrzyk.model.User;

public interface UserService {
    User findByUserName(String name);

    void saveUser(User user);
}
