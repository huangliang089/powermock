package test;

public interface UserService {
    int addUser(UserDto userDto);

    int delUser(int id) throws Exception;

    int modUser(UserDto userDto);

    void saveUser(UserDto userDto);
}
