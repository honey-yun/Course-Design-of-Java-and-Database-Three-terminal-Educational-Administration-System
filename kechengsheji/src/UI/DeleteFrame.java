package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteFrame extends JFrame {

    public DeleteFrame() {
        super("删除信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 删除学生信息按钮
        JButton deleteStudentButton = new JButton("删除学生信息");
        deleteStudentButton.setFont(new Font("楷体", Font.PLAIN, 20));
        deleteStudentButton.setContentAreaFilled(false);
        deleteStudentButton.setBounds(100, 100, 200, 30);

        // 删除课程信息按钮
        JButton deleteCourseButton = new JButton("删除课程信息");
        deleteCourseButton.setFont(new Font("楷体", Font.PLAIN, 20));
        deleteCourseButton.setContentAreaFilled(false);
        deleteCourseButton.setBounds(100, 200, 200, 30);

        // 删除教师信息按钮
        JButton deleteTeacherButton = new JButton("删除教师信息");
        deleteTeacherButton.setFont(new Font("楷体", Font.PLAIN, 20));
        deleteTeacherButton.setContentAreaFilled(false);
        deleteTeacherButton.setBounds(100, 300, 200, 30);

        frame.setLayout(null);
        frame.add(deleteStudentButton);
        frame.add(deleteCourseButton);
        frame.add(deleteTeacherButton);

        // 删除学生信息按钮点击事件
        deleteStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 调出删除学生信息界面
                new DeleteStudentFrame().setVisible(true);
            }
        });

        // 删除课程信息按钮点击事件
        deleteCourseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 调出删除课程信息界面
                new DeleteCourseFrame().setVisible(true);
            }
        });

        // 删除教师信息按钮点击事件
        deleteTeacherButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 调出删除教师信息界面
                new DeleteTeacherFrame().setVisible(true);
            }
        });
    }
}