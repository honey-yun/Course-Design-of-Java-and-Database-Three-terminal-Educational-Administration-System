package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.DBUtil;

public class UpdateCourseFrame extends JFrame {

    public UpdateCourseFrame() {
        super("更新课程信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 课程信息标签和文本框
        JLabel j5 = new JLabel("课程号:");
        JLabel j6 = new JLabel("课程名:");
        JLabel j7 = new JLabel("学分:");
        JLabel j8 = new JLabel("教师工号:");

        j5.setFont(new Font("楷体", Font.PLAIN, 20));
        j6.setFont(new Font("楷体", Font.PLAIN, 20));
        j7.setFont(new Font("楷体", Font.PLAIN, 20));
        j8.setFont(new Font("楷体", Font.PLAIN, 20));

        JTextField c5 = new JTextField(15);
        JTextField c6 = new JTextField(15);
        JTextField c7 = new JTextField(15);
        JTextField c8 = new JTextField(15);

        JButton aa = new JButton("更新");
        JButton bb = new JButton("重置");

        aa.setFont(new Font("楷体", Font.PLAIN, 20));
        bb.setFont(new Font("楷体", Font.PLAIN, 20));

        // 设置按钮背景透明
        aa.setContentAreaFilled(false);
        bb.setContentAreaFilled(false);

        // 课程信息组件布局
        j5.setBounds(20, 30, 120, 20);
        c5.setBounds(80, 30, 120, 25);
        j6.setBounds(20, 70, 120, 20);
        c6.setBounds(80, 70, 100, 25);
        j7.setBounds(20, 110, 120, 30);
        c7.setBounds(80, 110, 100, 25);
        j8.setBounds(20, 150, 120, 30);
        c8.setBounds(80, 150, 100, 25);

        aa.setBounds(100, 400, 100, 30);
        bb.setBounds(300, 400, 100, 30);

        frame.setLayout(null);
        frame.add(j5);
        frame.add(c5);
        frame.add(j6);
        frame.add(c6);
        frame.add(j7);
        frame.add(c7);
        frame.add(j8);
        frame.add(c8);
        frame.add(aa);
        frame.add(bb);

        aa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cno = c5.getText().trim();
                String cname = c6.getText().trim();
                String ccredit = c7.getText().trim();
                String tno = c8.getText().trim();

                try {
                    if (updateCourse(cno, cname, ccredit, tno)) {
                        JOptionPane.showMessageDialog(null, "更新课程信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "更新课程信息失败，可能课程号不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        bb.addActionListener(e -> {
            c5.setText("");
            c6.setText("");
            c7.setText("");
            c8.setText("");
        });
    }

    // 更新课程信息
    private boolean updateCourse(String cno, String cname, String ccredit, String tno) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE dbo.Course SET Cname =?, Ccredit =?, Tno =? WHERE Cno =?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cname);
        pstmt.setString(2, ccredit);
        pstmt.setString(3, tno);
        pstmt.setString(4, cno);
        int result = pstmt.executeUpdate();
        DBUtil.closeResources(conn, pstmt, null);
        return result == 1;
    }
}