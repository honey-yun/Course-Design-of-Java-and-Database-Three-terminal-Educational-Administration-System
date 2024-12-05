/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： StudentRegisterFrame.java
 * 文件标识：无
 * 内容摘要：学生端注册界面，用于向数据库存储用户注册信息
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import server.DBUtil;

public class StudentRegisterFrame extends JFrame {
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField snoField;
    private JTextField phoneField;
    /*********************************************************
     * 功能描述：构造函数，用于初始化学生注册界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建面板，添加账号、密码、学号、手机号标签及文本框与注册、返回按钮，设置字体与按钮样式，布局组件，为按钮绑定动作监听器以处理注册和返回逻辑，构建学生注册交互界面布局。
     ************************************************************/
    public StudentRegisterFrame() {
        super("学生注册");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel panel = new JPanel();
        add(panel);

        JLabel usernameLabel = new JLabel("账号:");
        JLabel passwordLabel = new JLabel("密码:");
        JLabel snoLabel = new JLabel("学号:");
        JLabel phoneLabel = new JLabel("手机号:");
        usernameField = new JTextField(20);
        passwordField = new JTextField(20);
        snoField = new JTextField(20);
        phoneField = new JTextField(20);
        JButton registerButton = new JButton("注册");
        JButton backButton = new JButton("返回");

        usernameLabel.setFont(new Font("楷体", Font.BOLD, 17));
        passwordLabel.setFont(new Font("楷体", Font.BOLD, 17));
        snoLabel.setFont(new Font("楷体", Font.BOLD, 17));
        phoneLabel.setFont(new Font("楷体", Font.BOLD, 17));
        registerButton.setFont(new Font("楷体", Font.PLAIN, 17));
        backButton.setFont(new Font("楷体", Font.PLAIN, 17));

        usernameLabel.setBounds(50, 50, 60, 30);
        usernameField.setBounds(120, 50, 150, 30);
        passwordLabel.setBounds(50, 100, 60, 30);
        passwordField.setBounds(120, 100, 150, 30);
        snoLabel.setBounds(50, 150, 60, 30);
        snoField.setBounds(120, 150, 150, 30);
        phoneLabel.setBounds(50, 200, 100, 30);
        phoneField.setBounds(120, 200, 150, 30);
        registerButton.setBounds(100, 250, 80, 30);
        backButton.setBounds(200, 250, 80, 30);

        panel.setLayout(null);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(snoLabel);
        panel.add(snoField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(registerButton);
        panel.add(backButton);

/*********************************************************
 * 功能描述：“注册”按钮点击事件处理方法
 * 输入参数：e - 动作事件对象，封装按钮点击事件信息
 * 返回值：无
 * 其它说明：获取各文本框输入，校验非空后调用 isUsernameExists 查用户名是否存在。若不存在，调用 registerStudent 注册并提示成功；否则提示已存在。遇 SQL 或类加载异常则打印堆栈跟踪排查，确保注册流程准确可靠。
 ************************************************************/
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String sno = snoField.getText().trim();
            String phone = phoneField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || sno.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(null, "所有字段不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (isUsernameExists(username)) {
                    JOptionPane.showMessageDialog(null, "用户名已存在", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    registerStudent(username, password, sno, phone);
                    JOptionPane.showMessageDialog(null, "注册成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
/*********************************************************
 * 功能描述：“返回”按钮点击事件处理方法
 * 输入参数：e - 动作事件对象，包含按钮点击触发事件详情
 * 返回值：无
 * 其它说明：点击时关闭当前注册界面，实现返回功能，优化用户操作流程与界面交互体验，提供操作灵活性与便捷性。
 ************************************************************/
        backButton.addActionListener(e -> dispose());
    }
    /*********************************************************
     * 功能描述：检查用户名是否已存在的方法
     * 输入参数：username - 待检查用户名
     * 返回值：true - 用户名已存在；false - 用户名不存在
     * 其它说明：获取数据库连接，构建查询语句，设置用户名参数查询。若结果集有下一条记录则存在，关闭资源返回 true；否则返回 false，为注册唯一性校验提供数据依据。
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
     * 功能描述：注册学生账号的方法
     * 输入参数：
     * - username：新学生账号
     * - password：账号密码
     * - sno：学号
     * - phone：手机号
     * 返回值：无
     * 其它说明：获取连接，先后向 Users 与 Student 表插入账号与学生信息。构建 SQL 设参执行更新，成功则提示注册成功；遇 SQL 异常提示错误打印堆栈跟踪，保障注册功能稳定运行及数据完整性。
     ************************************************************/
    private void registerStudent(String username, String password, String sno, String phone) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "数据库连接失败，请检查网络连接或联系管理员", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 向Users表插入学生账号信息
            String usersSql = "INSERT INTO dbo.Users (ID, PAWD, Sno, phone, role) VALUES (?,?,?,?, '学生')";
            try (PreparedStatement usersPstmt = conn.prepareStatement(usersSql)) {
                usersPstmt.setString(1, username);
                usersPstmt.setString(2, password);
                usersPstmt.setString(3, sno);
                usersPstmt.setString(4, phone);
                usersPstmt.executeUpdate();
            }

            // 向Student表插入学生详细信息（假设Student表有Sname, Ssex, Sage, Sdept等字段，此处可根据实际情况修改）
            String studentSql = "INSERT INTO dbo.Student (Sno, Sname, Ssex, Sage, Sdept) VALUES (?,?,?,?,?)";
            try (PreparedStatement studentPstmt = conn.prepareStatement(studentSql)) {
                studentPstmt.setString(1, sno);
                // 假设学生姓名、性别、年龄、专业先设置为默认值，后续可提供界面让学生自行修改
                studentPstmt.setString(2, "未设置姓名");
                studentPstmt.setString(3, "未设置性别");
                studentPstmt.setString(4, "0");
                studentPstmt.setString(5, "未设置专业");
                studentPstmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "注册成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "注册学生账号时出错: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBUtil.closeResources(conn, null, null);
        }
    }
}