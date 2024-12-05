
/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： AdminRegisterFrame.java
 * 文件标识：无
 * 内容摘要：用于管理员端注册操作
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
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import server.DBUtil;

public class AdminRegisterFrame extends JFrame {
    private JTextField usernameField;
    private JTextField passwordField;
    /*********************************************************
     * 功能描述：构造函数，用于初始化管理员注册界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小与位置并使其可见。创建面板，添加账号、密码标签及文本框与注册、返回按钮，设置字体与按钮样式，布局组件，为按钮绑定动作监听器以处理注册和返回逻辑，构建注册交互界面布局。
     ************************************************************/
    public AdminRegisterFrame() {
        super("管理员注册");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 350);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel panel = new JPanel();
        add(panel);

        JLabel usernameLabel = new JLabel("账号:");
        JLabel passwordLabel = new JLabel("密码:");
        usernameField = new JTextField(20);
        passwordField = new JTextField(20);
        JButton registerButton = new JButton("注册");
        JButton backButton = new JButton("返回");

        usernameLabel.setFont(new Font("楷体", Font.BOLD, 17));
        passwordLabel.setFont(new Font("楷体", Font.BOLD, 17));
        registerButton.setFont(new Font("楷体", Font.PLAIN, 17));
        backButton.setFont(new Font("楷体", Font.PLAIN, 17));

        usernameLabel.setBounds(50, 50, 60, 30);
        usernameField.setBounds(120, 50, 150, 30);
        passwordLabel.setBounds(50, 100, 60, 30);
        passwordField.setBounds(120, 100, 150, 30);
        registerButton.setBounds(100, 150, 80, 30);
        backButton.setBounds(200, 150, 80, 30);

        panel.setLayout(null);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(registerButton);
        panel.add(backButton);
/*********************************************************
 * 功能描述：“返回”按钮点击事件处理方法
 * 输入参数：e - 动作事件对象，包含按钮点击触发事件详情
 * 返回值：无
 * 其它说明：点击时关闭当前注册界面，实现返回功能，优化用户操作流程与界面交互体验。
 ************************************************************/
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "账号和密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (isUsernameExists(username)) {
                    JOptionPane.showMessageDialog(null, "用户名已存在", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    registerAdmin(username, password);
                    JOptionPane.showMessageDialog(null, "注册成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    // 注册成功后可进行其他操作，如跳转到登录界面等
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
/*********************************************************
 * 功能描述：“返回”按钮点击事件处理方法
 * 输入参数：e - 动作事件对象，包含按钮点击触发事件详情
 * 返回值：无
 * 其它说明：点击时关闭当前注册界面，实现返回功能，优化用户操作流程与界面交互体验。
 ************************************************************/
        backButton.addActionListener(e -> dispose());
    }
    /*********************************************************
     * 功能描述：检查用户名是否已存在的方法
     * 输入参数：username - 待检查用户名
     * 返回值：true - 用户名已存在；false - 用户名不存在
     * 其它说明：获取数据库连接，构建查询语句，设置用户名参数查询。若结果集有下一条记录，表明存在，关闭资源返回 true；否则返回 false，为注册逻辑提供唯一性检查依据。
     ************************************************************/
    private boolean isUsernameExists(String username) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT * FROM dbo.Users WHERE ID =?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        boolean exists = rs.next();
        DBUtil.closeResources(conn, pstmt, rs);
        return exists;
    }
    /*********************************************************
     * 功能描述：注册管理员账号的方法
     * 输入参数：
     * - username：新管理员账号
     * - password：账号密码
     * 返回值：无
     * 其它说明：获取连接后向 Users 表插入管理员账号信息，构建 SQL 并设参执行更新。若成功则提示注册成功；遇 SQL 异常则提示错误并打印堆栈跟踪，确保注册功能稳定可靠。
     ************************************************************/
    private void registerAdmin(String username, String password) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "INSERT INTO dbo.Users (ID, PAWD, role) VALUES (?,?, '管理员')";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.executeUpdate();
        DBUtil.closeResources(conn, pstmt, null);
    }
}