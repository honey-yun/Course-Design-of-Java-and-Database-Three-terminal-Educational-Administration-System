package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.DBUtil;

public class UpdateTeacherFrame extends JFrame {

    public UpdateTeacherFrame() {
        super("更新教师信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 教师信息标签和文本框
        JLabel j9 = new JLabel("教师工号:");
        JLabel j10 = new JLabel("教师姓名:");
        JLabel j11 = new JLabel("教师性别:");
        JLabel j12 = new JLabel("教师年龄:");
        JLabel j13 = new JLabel("教师职称:");
        JLabel j14 = new JLabel("手机号:");

        j9.setFont(new Font("楷体", Font.PLAIN, 20));
        j10.setFont(new Font("楷体", Font.PLAIN, 20));
        j11.setFont(new Font("楷体", Font.PLAIN, 20));
        j12.setFont(new Font("楷体", Font.PLAIN, 20));
        j13.setFont(new Font("楷体", Font.PLAIN, 20));
        j14.setFont(new Font("楷体", Font.PLAIN, 20));

        JTextField c9 = new JTextField(15);
        JTextField c10 = new JTextField(15);
        JTextField c11 = new JTextField(15);
        JTextField c12 = new JTextField(15);
        JTextField c13 = new JTextField(15);
        JTextField c14 = new JTextField(15);

        JButton aa = new JButton("更新");
        JButton bb = new JButton("重置");

        aa.setFont(new Font("楷体", Font.PLAIN, 20));
        bb.setFont(new Font("楷体", Font.PLAIN, 20));

        // 设置按钮背景透明
        aa.setContentAreaFilled(false);
        bb.setContentAreaFilled(false);

        // 教师信息组件布局
        j9.setBounds(20, 30, 120, 20);
        c9.setBounds(80, 30, 120, 25);
        j10.setBounds(20, 70, 120, 20);
        c10.setBounds(80, 70, 100, 25);
        j11.setBounds(20, 110, 120, 30);
        c11.setBounds(80, 110, 100, 25);
        j12.setBounds(20, 150, 120, 30);
        c12.setBounds(80, 150, 100, 25);
        j13.setBounds(20, 190, 120, 30);
        c13.setBounds(80, 190, 100, 25);
        j14.setBounds(20, 230, 120, 30);
        c14.setBounds(80, 230, 100, 25);

        aa.setBounds(100, 400, 100, 30);
        bb.setBounds(300, 400, 100, 30);

        frame.setLayout(null);
        frame.add(j9);
        frame.add(c9);
        frame.add(j10);
        frame.add(c10);
        frame.add(j11);
        frame.add(c11);
        frame.add(j12);
        frame.add(c12);
        frame.add(j13);
        frame.add(c13);
        frame.add(j14);
        frame.add(c14);
        frame.add(aa);
        frame.add(bb);

        aa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tno = c9.getText().trim();
                String tname = c10.getText().trim();
                String tsex = c11.getText().trim();
                String tage = c12.getText().trim();
                String ttitle = c13.getText().trim();
                String tphone = c14.getText().trim();

                try {
                    if (updateTeacher(tno, tname, tsex, tage, ttitle, tphone)) {
                        JOptionPane.showMessageDialog(null, "更新教师信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "更新教师信息失败，可能教师工号不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        bb.addActionListener(e -> {
            c9.setText("");
            c10.setText("");
            c11.setText("");
            c12.setText("");
            c13.setText("");
            c14.setText("");
        });
    }

    // 更新教师信息
    private boolean updateTeacher(String tno, String tname, String tsex, String tage, String ttitle, String tphone) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE dbo.Teacher SET Tname =?, Tsex =?, Tage =?, Ttitle =?, phone =? WHERE Tno =?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, tname);
        pstmt.setString(2, tsex);
        pstmt.setString(3, tage);
        pstmt.setString(4, ttitle);
        pstmt.setString(5, tphone);
        pstmt.setString(6, tno);

        try {
            int result = pstmt.executeUpdate();
            DBUtil.closeResources(conn, pstmt, null);
            return result == 1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "更新教师信息失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}