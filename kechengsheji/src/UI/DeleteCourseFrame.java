package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.DBUtil;

public class DeleteCourseFrame extends JFrame {

    public DeleteCourseFrame() {
        super("删除课程信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 课程信息标签和文本框（这里只需要课程号用于确定要删除的课程记录）
        JLabel j5 = new JLabel("课程号:");
        j5.setFont(new Font("楷体", Font.PLAIN, 20));
        JTextField c5 = new JTextField(15);

        JButton aa = new JButton("删除");
        JButton bb = new JButton("重置");

        aa.setFont(new Font("楷体", Font.PLAIN, 20));
        bb.setFont(new Font("楷体", Font.PLAIN, 20));

        // 设置按钮背景透明
        aa.setContentAreaFilled(false);
        bb.setContentAreaFilled(false);

        // 组件布局
        j5.setBounds(20, 30, 120, 20);
        c5.setBounds(80, 30, 120, 25);
        aa.setBounds(100, 400, 100, 30);
        bb.setBounds(300, 400, 100, 30);

        frame.setLayout(null);
        frame.add(j5);
        frame.add(c5);
        frame.add(aa);
        frame.add(bb);

        aa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cno = c5.getText().trim();

                try {
                    if (deleteCourse(cno)) {
                        JOptionPane.showMessageDialog(null, "删除课程信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "删除课程信息失败，可能课程号不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        bb.addActionListener(e -> {
            c5.setText("");
        });
    }

    // 删除课程信息
    private boolean deleteCourse(String cno) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "DELETE FROM dbo.Course WHERE Cno =?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cno);
        try {
            int result = pstmt.executeUpdate();
            DBUtil.closeResources(conn, pstmt, null);
            return result == 1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "删除课程信息失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}