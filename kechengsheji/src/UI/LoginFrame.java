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

    // 新增方法用于设置用户名
    public static void setUsername(String username) {
        LoginFrame.username = username;
    }

    // 新增方法用于获取用户名
    public static String getUsername() {
        return username;
    }
}