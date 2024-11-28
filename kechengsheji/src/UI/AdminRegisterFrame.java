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

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    // 检查用户名是否已存在
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

    // 注册管理员账号
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