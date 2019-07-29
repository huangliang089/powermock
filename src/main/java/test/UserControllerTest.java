package test;


import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;


@RunWith(PowerMockRunner.class)
@PrepareForTest({UserController.class, FileHelper.class})
@PowerMockIgnore("javax.management.*")
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController uc;
    
    //mock普通方法
    @Test
    public void testAddUser() throws Exception {
        UserDto ud = new UserDto();
        System.out.println("userService="+userService);
        PowerMockito.when(userService.addUser(ud)).thenReturn(2);
        
        System.out.println("返回值："+PowerMockito.when(userService.addUser(ud)).thenReturn(1));
        // can not stub like this
         //PowerMockito.doReturn(1).when(userService.addUser(ud));
        boolean result = uc.addUser(ud);
        Assert.assertEquals(result, true);
    }
    
    @Test
    public void testAddUser02() throws Exception {
        UserDto ud = new UserDto();
        System.out.println("userService="+userService);
        PowerMockito.when(userService.addUser(ud)).thenReturn(3);
        
        //System.out.println("返回值："+PowerMockito.when(userService.addUser(ud)).thenReturn(1));
        // can not stub like this
         //PowerMockito.doReturn(1).when(userService.addUser(ud));
        boolean result = uc.addUser(ud);
        Assert.assertEquals(result, true);
    }
    
//    @Test
//    public void testDelUser() throws Exception {
//        int toDelete = 1;
//        PowerMockito.when(userService.delUser(toDelete)).thenThrow(new Exception("mock exception"));
//        boolean result = uc.delUser(toDelete);
//        Assert.assertEquals(result, false);
//    }
    
    //mock抛异常
    @Test
    public void testDelUser() throws Exception {
    	int toDelete = 1;
    	PowerMockito.when(userService.delUser(toDelete)).thenThrow(new Exception("mock exception"));
    	System.out.println("del前");
    	boolean result = uc.delUser(toDelete);
    	System.out.println("del后 result = "+result);
    	Assert.assertEquals(result,false);
    	
    }
    
    //mock静态方法
    @Test
    public void mockFileHelper() {
        PowerMockito.mockStatic(FileHelper.class);
        PowerMockito.when(FileHelper.getName("lucy")).thenReturn("god");
        System.out.println("FileHelper.getName('lucy') = "+FileHelper.getName("lucy"));
        Assert.assertEquals(FileHelper.getName("lucy"), "A_god");
    }
    
    //mock 返回值为 void 的方法
    @Test
    public void testSaveUser() throws Exception {
        UserDto userDto = new UserDto();

        // way one:
//        PowerMockito.doNothing().when(userService, "saveUser", userDto);

        // way two:
        PowerMockito.doNothing().when(userService).saveUser(userDto);

        uc.saveUser(userDto);
    }
    
    //mock私有方法   方式一
    @Test
    public void testModUser() throws Exception {
        UserDto ud = new UserDto();
        int moded = 1;

        PowerMockito.when(userService.modUser(ud)).thenReturn(moded);

        //此处mock了一个UserController uc2，需要重新赋值
        //需要注意的是：此处的uc2是mock出来的，不是 UserControllerTest 类中的成员变量 uc
        UserController uc2 = PowerMockito.mock(UserController.class);

        // 给没有 setter 方法的 私有字段赋值(userService为private的，并且没有set方法)。
        Whitebox.setInternalState(uc2, "userService", userService);

        // 因为要测试的是 modUser() 方法，
        // 所以，当调用这个方法时，应该让它调用真实的方法，而非被mock掉的方法
        PowerMockito.when(uc2.modUser(ud)).thenCallRealMethod();

        // 在modUser()方法中会调用verifyMod()这个私有方法，所以，需要将mock掉
        PowerMockito.when(uc2, "verifyMod", moded).thenReturn(true);

        boolean result = uc2.modUser(ud);

        Assert.assertEquals(result, true);
    }
    
    //mock私有方法   方式二
    //使用spy方法可以避免执行被测类中的成员函数，即mock掉不想被执行的私有方法。
    @Test
    public void testModUser2() throws Exception {
        UserDto ud = new UserDto();
        int moded = 1;

        PowerMockito.when(userService.modUser(ud)).thenReturn(moded);

        // 对uc进行监视
        uc = PowerMockito.spy(uc);
        // 当uc的verifyMod被执行时，将被mock掉
        PowerMockito.when(uc, "verifyMod", moded).thenReturn(true);
        boolean result = uc.modUser(ud);

        Assert.assertEquals(result, true);
    }
    
    //测试私有方法(注意： 是测试，不是mock)  方法一
    //报错(boolean)method.invoke(uc, 1)为obj类型不能转换为boolean类型
//    @Test
//    public void testVerifyMod() throws Exception {
//        // 获取Method对象，
//        Method method = PowerMockito.method(UserController.class, "verifyMod", int.class);
//        // 调用Method的invoke方法来执行
//        boolean result = (boolean)method.invoke(uc, 1);
//        Assert.assertEquals(result, true);
//    }
    
   //测试私有方法(注意： 是测试，不是mock)  方法二
    @Test
    public void testVerifyMod2() throws Exception {
        // 通过 Whitebox 来执行
        boolean result = Whitebox.invokeMethod(uc, "verifyMod", 1);
        Assert.assertEquals(result, true);
    }
    
    
    //mock新建对象
    @Test
    public void testCountUser() throws Exception {
        UserDto ud = new UserDto();
        ud.setId(1);

        PowerMockito.whenNew(UserDto.class).withNoArguments().thenReturn(ud);

        int count = uc.countUser();

        Assert.assertEquals(count, 1);
    }
    
    //mock返回值为 void 的 static 方法 (此为后期补充，所以没有提供相关完整代码)
}
