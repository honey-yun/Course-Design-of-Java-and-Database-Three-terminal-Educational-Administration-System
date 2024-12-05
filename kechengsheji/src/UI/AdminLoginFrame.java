/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： AdminLoginFrame.java
 * 文件标识：无
 * 内容摘要：管理员登录界面UI
 * 其它说明：
 * 当前版本： V1.0
 * 作   者：李世赟
 * 完成日期： 20241130
 **********************************************************/
package UI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import server.DBUtil;

public class AdminLoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    /*********************************************************
     * 功能描述：构造函数，用于初始化管理员登录界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作模式、大小与位置并显示框架。创建面板，添加账号密码标签及文本框、登录返回注册按钮，设置字体样式与按钮背景透明效果，为按钮绑定对应动作监听器，构建完整登录界面布局与交互逻辑。
     ************************************************************/
    public AdminLoginFrame() {
        super("管理员登录");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 350);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel panel = new JPanel();
        add(panel);

        JLabel usernameLabel = new JLabel("账号:");
        JLabel passwordLabel = new JLabel("密码:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("登录");
        JButton backButton = new JButton("返回");
        JButton registerButton = new JButton("注册");

        usernameLabel.setFont(new Font("楷体", Font.BOLD, 17));
        passwordLabel.setFont(new Font("楷体", Font.BOLD, 17));
        loginButton.setFont(new Font("楷体", Font.PLAIN, 17));
        backButton.setFont(new Font("楷体", Font.PLAIN, 17));
        registerButton.setFont(new Font("楷体", Font.PLAIN, 17));

        // 设置按钮背景透明
        loginButton.setContentAreaFilled(false);
        backButton.setContentAreaFilled(false);
        registerButton.setContentAreaFilled(false);

        usernameLabel.setBounds(50, 50, 60, 30);
        usernameField.setBounds(120, 50, 150, 30);
        passwordLabel.setBounds(50, 100, 60, 30);
        passwordField.setBounds(120, 100, 150, 30);
        loginButton.setBounds(30, 150, 80, 30);
        backButton.setBounds(130, 150, 80, 30);
        registerButton.setBounds(230, 150, 80, 30);

        panel.setLayout(null);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(backButton);
        panel.add(registerButton);
        /*********************************************************
         * 功能描述：“登录”按钮点击事件处理方法
         * 输入参数：e - 动作事件对象，封装按钮点击相关事件信息
         * 返回值：无
         * 其它说明：获取账号密码文本框输入值，校验非空后调用 authenticateAdmin 方法验证。若验证成功，跳转管理员主界面并关闭当前登录框；否则显示错误提示。遇 SQL 或类加载异常则打印堆栈跟踪辅助调试。
         ************************************************************/
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "账号和密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (authenticateAdmin(username, password)) {
                    // 登录成功，跳转到管理员主界面
                    new AdminFrame().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        /*********************************************************
         * 功能描述：“返回”按钮点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击触发的事件详情
         * 返回值：无
         * 其它说明：点击按钮时，创建并显示登录主界面，关闭当前管理员登录界面，实现界面导航回退功能。
         ************************************************************/
        backButton.addActionListener(e -> {
            // 返回登录主界面
            new LoginFrame().setVisible(true);
            dispose();
        });
        /*********************************************************
         * 功能描述：“注册”按钮点击事件处理方法
         * 输入参数：e - 动作事件对象，传递按钮点击事件相关数据
         * 返回值：无
         * 其它说明：点击触发时，创建并显示管理员注册界面，实现从登录到注册界面跳转，满足新管理员账号创建需求。
         ************************************************************/
        registerButton.addActionListener(e -> {
            // 跳转到管理员注册界面
            new AdminRegisterFrame().setVisible(true);
        });
    }
    /*********************************************************
     * 功能描述：验证管理员账号密码方法
     * 输入参数：
     * - username：待验证管理员账号字符串
     * - password：对应密码字符串
     * 返回值：true - 账号密码匹配且角色为管理员；false - 验证失败
     * 其它说明：获取数据库连接，构建查询语句，将账号密码参数设置后查询。若结果集有下一条记录，表示匹配成功，关闭资源返回 true；否则返回 false。过程中遇异常则打印堆栈跟踪排查问题。
     ************************************************************/
    private boolean authenticateAdmin(String username, String password) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT * FROM dbo.Users WHERE ID =? AND PAWD =? AND role = '管理员'";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        boolean authenticated = rs.next();
        DBUtil.closeResources(conn, pstmt, rs);
        return authenticated;
    }
}