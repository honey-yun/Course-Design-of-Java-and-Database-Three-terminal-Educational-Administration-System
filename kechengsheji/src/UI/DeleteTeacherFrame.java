package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.DBUtil;

public class DeleteTeacherFrame extends JFrame {

    public DeleteTeacherFrame() {
        super("删除教师信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 教师信息标签和文本框（这里只需要教师工号用于确定要删除的教师记录）
        JLabel j9 = new JLabel("教师工号:");
        j9.setFont(new Font("楷体", Font.PLAIN, 20));
        JTextField c9 = new JTextField(15);

        JButton aa = new JButton("删除");
        JButton bb = new JButton("重置");

        aa.setFont(new Font("楷体", Font.PLAIN, 20));
        bb.setFont(new Font("楷体", Font.PLAIN, 20));

        // 设置按钮背景透明
        aa.setContentAreaFilled(false);
        bb.setContentAreaFilled(false);

        // 组件布局
        j9.setBounds(20, 30, 120, 20);
        c9.setBounds(80, 30, 120, 25);
        aa.setBounds(100, 400, 100, 30);
        bb.setBounds(300, 400, 100, 30);

        frame.setLayout(null);
        frame.add(j9);
        frame.add(c9);
        frame.add(aa);
        frame.add(bb);

        aa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tno = c9.getText().trim();

                try {
                    if (deleteTeacher(tno)) {
                        JOptionPane.showMessageDialog(null, "删除教师信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "删除教师信息失败，可能教师工号不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        bb.addActionListener(e -> {
            c9.setText("");
        });
    }

    // 删除教师信息
    private boolean deleteTeacher(String tno) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "DELETE FROM dbo.Teacher WHERE Tno =?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, tno);
        try {
            int result = pstmt.executeUpdate();
            DBUtil.closeResources(conn, pstmt, null);
            return result == 1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "删除教师信息失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}