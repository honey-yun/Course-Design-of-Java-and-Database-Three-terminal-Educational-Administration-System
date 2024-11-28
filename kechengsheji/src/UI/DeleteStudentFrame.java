package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.DBUtil;

public class DeleteStudentFrame extends JFrame {

    public DeleteStudentFrame() {
        super("删除学生信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 学生信息标签和文本框（这里只需要学号用于确定要删除的学生记录）
        JLabel j = new JLabel("学号:");
        j.setFont(new Font("楷体", Font.PLAIN, 20));
        JTextField c = new JTextField(15);

        JButton aa = new JButton("删除");
        JButton bb = new JButton("重置");

        aa.setFont(new Font("楷体", Font.PLAIN, 20));
        bb.setFont(new Font("楷体", Font.PLAIN, 20));

        // 设置按钮背景透明
        aa.setContentAreaFilled(false);
        bb.setContentAreaFilled(false);

        // 组件布局
        j.setBounds(20, 30, 120, 20);
        c.setBounds(80, 30, 120, 25);
        aa.setBounds(100, 400, 100, 30);
        bb.setBounds(300, 400, 100, 30);

        frame.setLayout(null);
        frame.add(j);
        frame.add(c);
        frame.add(aa);
        frame.add(bb);

        aa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sno = c.getText().trim();

                try {
                    if (deleteStudent(sno)) {
                        JOptionPane.showMessageDialog(null, "删除学生信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "删除学生信息失败，可能学号不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        bb.addActionListener(e -> {
            c.setText("");
        });
    }

    // 删除学生信息
    private boolean deleteStudent(String sno) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "DELETE FROM dbo.Student WHERE Sno =?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, sno);
        try {
            int result = pstmt.executeUpdate();
            DBUtil.closeResources(conn, pstmt, null);
            return result == 1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "删除学生信息失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}