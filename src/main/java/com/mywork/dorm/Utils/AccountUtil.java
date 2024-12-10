package com.mywork.dorm.Utils;

import java.util.Random;


public class AccountUtil {
    public static final String STUDENT = "Student";
    public static final String SUPERVISOR = "Supervisor";
    public static final String ADMIN = "Admin";

    /**
     * 生成一个基于角色的账户标识符。
     *
     * @param roles 角色类型，接受以下值：
     *              <ul>
     *              <li>Student - 学生角色</li>
     *              <li>Supervisor - 监督者角色</li>
     *              <li>Admin - 管理员角色</li>
     *              </ul>
     * @return 生成的accountID
     * @throws IllegalArgumentException 如果传入的角色无效
     */
    public static String Account(String roles){
        Random random = new Random();
        StringBuilder randomNumber = new StringBuilder(10);

        // 生成10位随机数字
        for (int i = 0; i < 9; i++) {
            randomNumber.append(random.nextInt(10)); // 生成0-9之间的随机数
        }

        switch (roles){
            case "Student":
                randomNumber.insert(0, "STU");
                break;
            case "Supervisor":
                randomNumber.insert(0, "SRA");
                break;
            case "Admin":
                randomNumber.insert(0, "ADM");
                break;
            default:
                throw new IllegalArgumentException("Invalid role parameter: " + roles);
        }

        return randomNumber.toString();
    }

    /**
     * 根据传入的账号检测身份。
     * @param Account 账号
     *                <p>1 学生</p>
     *                <p>2 宿管</p>
     *                <p>3 管理员</p>
     * @return 身份代指的数字
     * @throws IllegalArgumentException 传入的账号有误
     */
    public static Integer selectByRole(String Account) {
        String role = Account.substring(0, 3);
        Integer roleNum = 0;
        switch (role) {
            case "STU":
                roleNum = 1;
                break;
            case "SRA":
                roleNum = 2;
                break;
            case "ADM":
                roleNum = 3;
                break;
            default:
                roleNum = -1;
        }

        return roleNum;
    }
}
