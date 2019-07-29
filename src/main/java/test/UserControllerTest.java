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
    
    //mock��ͨ����
    @Test
    public void testAddUser() throws Exception {
        UserDto ud = new UserDto();
        System.out.println("userService="+userService);
        PowerMockito.when(userService.addUser(ud)).thenReturn(2);
        
        System.out.println("����ֵ��"+PowerMockito.when(userService.addUser(ud)).thenReturn(1));
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
        
        //System.out.println("����ֵ��"+PowerMockito.when(userService.addUser(ud)).thenReturn(1));
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
    
    //mock���쳣
    @Test
    public void testDelUser() throws Exception {
    	int toDelete = 1;
    	PowerMockito.when(userService.delUser(toDelete)).thenThrow(new Exception("mock exception"));
    	System.out.println("delǰ");
    	boolean result = uc.delUser(toDelete);
    	System.out.println("del�� result = "+result);
    	Assert.assertEquals(result,false);
    	
    }
    
    //mock��̬����
    @Test
    public void mockFileHelper() {
        PowerMockito.mockStatic(FileHelper.class);
        PowerMockito.when(FileHelper.getName("lucy")).thenReturn("god");
        System.out.println("FileHelper.getName('lucy') = "+FileHelper.getName("lucy"));
        Assert.assertEquals(FileHelper.getName("lucy"), "A_god");
    }
    
    //mock ����ֵΪ void �ķ���
    @Test
    public void testSaveUser() throws Exception {
        UserDto userDto = new UserDto();

        // way one:
//        PowerMockito.doNothing().when(userService, "saveUser", userDto);

        // way two:
        PowerMockito.doNothing().when(userService).saveUser(userDto);

        uc.saveUser(userDto);
    }
    
    //mock˽�з���   ��ʽһ
    @Test
    public void testModUser() throws Exception {
        UserDto ud = new UserDto();
        int moded = 1;

        PowerMockito.when(userService.modUser(ud)).thenReturn(moded);

        //�˴�mock��һ��UserController uc2����Ҫ���¸�ֵ
        //��Ҫע����ǣ��˴���uc2��mock�����ģ����� UserControllerTest ���еĳ�Ա���� uc
        UserController uc2 = PowerMockito.mock(UserController.class);

        // ��û�� setter ������ ˽���ֶθ�ֵ(userServiceΪprivate�ģ�����û��set����)��
        Whitebox.setInternalState(uc2, "userService", userService);

        // ��ΪҪ���Ե��� modUser() ������
        // ���ԣ��������������ʱ��Ӧ������������ʵ�ķ��������Ǳ�mock���ķ���
        PowerMockito.when(uc2.modUser(ud)).thenCallRealMethod();

        // ��modUser()�����л����verifyMod()���˽�з��������ԣ���Ҫ��mock��
        PowerMockito.when(uc2, "verifyMod", moded).thenReturn(true);

        boolean result = uc2.modUser(ud);

        Assert.assertEquals(result, true);
    }
    
    //mock˽�з���   ��ʽ��
    //ʹ��spy�������Ա���ִ�б������еĳ�Ա��������mock�����뱻ִ�е�˽�з�����
    @Test
    public void testModUser2() throws Exception {
        UserDto ud = new UserDto();
        int moded = 1;

        PowerMockito.when(userService.modUser(ud)).thenReturn(moded);

        // ��uc���м���
        uc = PowerMockito.spy(uc);
        // ��uc��verifyMod��ִ��ʱ������mock��
        PowerMockito.when(uc, "verifyMod", moded).thenReturn(true);
        boolean result = uc.modUser(ud);

        Assert.assertEquals(result, true);
    }
    
    //����˽�з���(ע�⣺ �ǲ��ԣ�����mock)  ����һ
    //����(boolean)method.invoke(uc, 1)Ϊobj���Ͳ���ת��Ϊboolean����
//    @Test
//    public void testVerifyMod() throws Exception {
//        // ��ȡMethod����
//        Method method = PowerMockito.method(UserController.class, "verifyMod", int.class);
//        // ����Method��invoke������ִ��
//        boolean result = (boolean)method.invoke(uc, 1);
//        Assert.assertEquals(result, true);
//    }
    
   //����˽�з���(ע�⣺ �ǲ��ԣ�����mock)  ������
    @Test
    public void testVerifyMod2() throws Exception {
        // ͨ�� Whitebox ��ִ��
        boolean result = Whitebox.invokeMethod(uc, "verifyMod", 1);
        Assert.assertEquals(result, true);
    }
    
    
    //mock�½�����
    @Test
    public void testCountUser() throws Exception {
        UserDto ud = new UserDto();
        ud.setId(1);

        PowerMockito.whenNew(UserDto.class).withNoArguments().thenReturn(ud);

        int count = uc.countUser();

        Assert.assertEquals(count, 1);
    }
    
    //mock����ֵΪ void �� static ���� (��Ϊ���ڲ��䣬����û���ṩ�����������)
}
