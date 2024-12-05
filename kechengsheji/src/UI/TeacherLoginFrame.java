/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： TeacherLoginFrame.java
 * 文件标识：无
 * 内容摘要：教师端登录界面UI，用于跳转教师端主界面
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import server.DBUtil;

public class TeacherLoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    /*********************************************************
     * 功能描述：构造函数，用于初始化教师登录界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建包含账号、密码输入框及登录、返回、注册按钮的面板，设置组件字体、布局及按钮背景透明等样式，为按钮添加事件监听器，用于处理登录验证、返回主界面及跳转到注册界面操作，构建教师登录交互布局。
     ************************************************************/
    public TeacherLoginFrame() {
        super("教师登录");
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
 * 输入参数：e - 动作事件对象，封装按钮点击的相关信息
 * 返回值：无
 * 其它说明：获取账号密码文本框输入值，校验非空后调用 authenticateTeacher 方法验证。若成功，设置用户名并跳转教师主界面关闭当前登录框；否则提示错误。遇 SQL 或类加载异常则打印堆栈跟踪排查问题，保障登录验证功能稳定运行。
 ************************************************************/
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "账号和密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (authenticateTeacher(username, password)) {
                    // 登录成功，跳转到教师主界面
                    LoginFrame.setUsername(username);
                    new TeacherFrame().setVisible(true);
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
 * 其它说明：点击时创建并显示登录主界面，关闭当前教师登录界面，实现返回导航功能，优化用户操作流程与界面交互体验，提升系统易用性。
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
 * 其它说明：点击触发则创建并显示教师注册界面，实现从登录到注册的跳转，满足新教师注册需求，完善系统用户管理功能流程。
 ************************************************************/
        registerButton.addActionListener(e -> {
            // 跳转到教师注册界面
            new TeacherRegisterFrame().setVisible(true);
        });
    }
    /*********************************************************
     * 功能描述：验证教师账号密码的方法
     * 输入参数：
     * - username：待验证教师账号字符串
     * - password：对应密码字符串
     * 返回值：true - 账号密码匹配且角色为教师；false - 验证失败
     * 其它说明：获取数据库连接，构建查询语句，设置账号密码参数查询。若结果集有下一条记录且角色为教师则匹配成功，关闭资源返回 true；否则返回 false，为教师登录验证提供核心逻辑判断依据。
     ************************************************************/
    private boolean authenticateTeacher(String username, String password) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT * FROM dbo.Users WHERE ID =? AND PAWD =? AND role = '教师'";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        boolean authenticated = rs.next();
        DBUtil.closeResources(conn, pstmt, rs);
        return authenticated;
    }
}