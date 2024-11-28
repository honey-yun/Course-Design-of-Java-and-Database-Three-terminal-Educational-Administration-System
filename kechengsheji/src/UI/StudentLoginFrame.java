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

public class StudentLoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public StudentLoginFrame() {
        super("学生登录");
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

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "账号和密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (authenticateStudent(username, password)) {
                    // 登录成功，设置用户名并跳转到学生主界面
                    LoginFrame.setUsername(username);
                    new StudentFrame().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        backButton.addActionListener(e -> {
            // 返回登录主界面
            new LoginFrame().setVisible(true);
            dispose();
        });

        registerButton.addActionListener(e -> {
            // 跳转到学生注册界面
            new StudentRegisterFrame().setVisible(true);
        });
    }

    // 验证学生账号密码
    private boolean authenticateStudent(String username, String password) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT * FROM dbo.Users WHERE ID =? AND PAWD =? AND role = '学生'";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        boolean authenticated = rs.next();
        DBUtil.closeResources(conn, pstmt, rs);
        return authenticated;
    }
}