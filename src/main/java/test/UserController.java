package test;

import org.springframework.beans.factory.annotation.Autowired;

public class UserController {
    @Autowired
    private UserService userService;

    /**
     * test upload 2
     */
    public boolean addUser(UserDto userDto) {
        int added = userService.addUser(userDto);
        System.out.println("调用后返回值added" + added);
        if (added <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean delUser(int id) {
        try {
            userService.delUser(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void saveUser(UserDto userDto) {
        userService.saveUser(userDto);
    }

    public int countUser() {
        UserDto ud = new UserDto();

        int count = 0;

        if (ud.getId() > 0) {
            count += 1;
        }
        return count;
    }

    public boolean modUser(UserDto userDto) {
        int moded = userService.modUser(userDto);
        return verifyMod(moded);
    }

    private boolean verifyMod(int moded) {
        if (moded <= 0) {
            return false;
        } else {
            return true;
        }
    }
}
