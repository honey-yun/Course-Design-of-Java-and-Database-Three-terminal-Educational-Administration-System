package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.DBUtil;

public class UpdateStudentFrame extends JFrame {

    public UpdateStudentFrame() {
        super("更新学生信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 学生信息标签和文本框
        JLabel j = new JLabel("学号:");
        JLabel j1 = new JLabel("姓名:");
        JLabel j2 = new JLabel("性别:");
        JLabel j3 = new JLabel("年龄:");
        JLabel j4 = new JLabel("专业:");

        j.setFont(new Font("楷体", Font.PLAIN, 20));
        j1.setFont(new Font("楷体", Font.PLAIN, 20));
        j2.setFont(new Font("楷体", Font.PLAIN, 20));
        j3.setFont(new Font("楷体", Font.PLAIN, 20));
        j4.setFont(new Font("楷体", Font.PLAIN, 20));

        JTextField c = new JTextField(15);
        JTextField c1 = new JTextField(15);
        JTextField c2 = new JTextField(15);
        JTextField c3 = new JTextField(15);
        JTextField c4 = new JTextField(15);

        JButton aa = new JButton("更新");
        JButton bb = new JButton("重置");

        aa.setFont(new Font("楷体", Font.PLAIN, 20));
        bb.setFont(new Font("楷体", Font.PLAIN, 20));

        // 设置按钮背景透明
        aa.setContentAreaFilled(false);
        bb.setContentAreaFilled(false);

        // 学生信息组件布局
        j.setBounds(20, 30, 120, 20);
        c.setBounds(80, 30, 120, 25);
        j1.setBounds(20, 70, 120, 20);
        c1.setBounds(80, 70, 100, 25);
        j2.setBounds(20, 110, 120, 30);
        c2.setBounds(80, 110, 100, 25);
        j3.setBounds(20, 150, 120, 30);
        c3.setBounds(80, 150, 100, 25);
        j4.setBounds(20, 190, 120, 30);
        c4.setBounds(80, 190, 100, 25);

        aa.setBounds(100, 400, 100, 30);
        bb.setBounds(300, 400, 100, 30);

        frame.setLayout(null);
        frame.add(j);
        frame.add(c);
        frame.add(j1);
        frame.add(c1);
        frame.add(j2);
        frame.add(c2);
        frame.add(j3);
        frame.add(c3);
        frame.add(j4);
        frame.add(c4);
        frame.add(aa);
        frame.add(bb);

        aa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sno = c.getText().trim();
                String sname = c1.getText().trim();
                String ssex = c2.getText().trim();
                String sage = c3.getText().trim();
                String sdept = c4.getText().trim();

                try {
                    if (updateStudent(sno, sname, ssex, sage, sdept)) {
                        JOptionPane.showMessageDialog(null, "更新学生信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "更新学生信息失败，可能学号不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        bb.addActionListener(e -> {
            c.setText("");
            c1.setText("");
            c2.setText("");
            c3.setText("");
            c4.setText("");
        });
    }

    // 更新学生信息
    private boolean updateStudent(String sno, String sname, String ssex, String sage, String sdept) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE dbo.Student SET Sname =?, Ssex =?, Sage =?, Sdept =? WHERE Sno =?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, sname);
        pstmt.setString(2, ssex);
        pstmt.setString(3, sage);
        pstmt.setString(4, sdept);
        pstmt.setString(5, sno);
        int result = pstmt.executeUpdate();
        DBUtil.closeResources(conn, pstmt, null);
        return result == 1;
    }
}