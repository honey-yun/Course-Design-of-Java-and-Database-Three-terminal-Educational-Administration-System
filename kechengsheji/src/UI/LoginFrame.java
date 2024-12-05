/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： LoginFrame.java
 * 文件标识：无
 * 内容摘要：身份选择主UI，跳转各个身份的登录界面UI
 * 其它说明：
 * 当前版本： V1.0
 * 作   者：李世赟
 * 完成日期： 20241130
 **********************************************************/
package UI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;

public class LoginFrame extends JFrame {
    public static String username;
    /*********************************************************
     * 功能描述：构造函数，用于初始化登录主界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置。创建顶部面板展示提示信息，底部面板放置管理员、学生、教师登录按钮，设置按钮字体与透明背景，布局面板。为按钮添加监听器，实现点击跳转对应登录界面及关闭当前界面功能，构建登录入口导航逻辑。
     ************************************************************/
    public LoginFrame() {
        setTitle("登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 250);
        setLocation(300, 120);

        // 顶部面板，用于显示提示信息
        JPanel topPanel = new JPanel();
        JLabel label = new JLabel("请选择登录角色");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("楷体", Font.PLAIN, 25));
        topPanel.add(label);

        // 底部面板，放置按钮
        JPanel bottomPanel = new JPanel();
        JButton adminButton = new JButton("管理员");
        JButton studentButton = new JButton("学生");
        JButton teacherButton = new JButton("教师");

        // 设置按钮背景透明
        adminButton.setContentAreaFilled(false);
        studentButton.setContentAreaFilled(false);
        teacherButton.setContentAreaFilled(false);

        bottomPanel.add(adminButton);
        bottomPanel.add(studentButton);
        bottomPanel.add(teacherButton);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);

        adminButton.addActionListener(e -> {
            // 跳转到管理员登录界面
            new AdminLoginFrame().setVisible(true);
            dispose();
        });

        studentButton.addActionListener(e -> {
            // 跳转到学生登录界面
            new StudentLoginFrame().setVisible(true);
            dispose();
        });

        teacherButton.addActionListener(e -> {
            // 跳转到教师登录界面
            new TeacherLoginFrame().setVisible(true);
            dispose();
        });
    }

    /*********************************************************
     * 功能描述：设置用户名的静态方法
     * 输入参数：username - 要设置的用户名字符串
     * 返回值：无
     * 其它说明：将传入用户名赋值给静态变量 username，用于在多界面间共享登录用户名信息，为后续基于用户身份的操作提供数据基础，如权限判定与个性化功能实现。
     ************************************************************/
    public static void setUsername(String username) {
        LoginFrame.username = username;
    }
    /*********************************************************
     * 功能描述：获取用户名的静态方法
     * 输入参数：无
     * 返回值：返回当前设置的用户名；若未设置则返回 null
     * 其它说明：提供外部获取已设置用户名的途径，辅助其他界面或功能模块依据用户名实施业务逻辑，如界面个性化展示、权限适配与数据查询筛选等操作。
     ************************************************************/
    public static String getUsername() {
        return username;
    }
}