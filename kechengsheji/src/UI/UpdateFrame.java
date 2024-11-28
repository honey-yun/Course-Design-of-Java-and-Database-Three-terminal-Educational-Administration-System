package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateFrame extends JFrame {

    public UpdateFrame() {
        super("更新信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 更新学生信息按钮
        JButton updateStudentButton = new JButton("更新学生信息");
        updateStudentButton.setFont(new Font("楷体", Font.PLAIN, 20));
        updateStudentButton.setContentAreaFilled(false);
        updateStudentButton.setBounds(100, 100, 200, 30);

        // 更新课程信息按钮
        JButton updateCourseButton = new JButton("更新课程信息");
        updateCourseButton.setFont(new Font("楷体", Font.PLAIN, 20));
        updateCourseButton.setContentAreaFilled(false);
        updateCourseButton.setBounds(100, 200, 200, 30);

        // 更新教师信息按钮
        JButton updateTeacherButton = new JButton("更新教师信息");
        updateTeacherButton.setFont(new Font("楷体", Font.PLAIN, 20));
        updateTeacherButton.setContentAreaFilled(false);
        updateTeacherButton.setBounds(100, 300, 200, 30);

        frame.setLayout(null);
        frame.add(updateStudentButton);
        frame.add(updateCourseButton);
        frame.add(updateTeacherButton);

        // 更新学生信息按钮点击事件
        updateStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 调出更新学生信息界面
                new UpdateStudentFrame().setVisible(true);
            }
        });

        // 更新课程信息按钮点击事件
        updateCourseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 调出更新课程信息界面
                new UpdateCourseFrame().setVisible(true);
            }
        });

        // 更新教师信息按钮点击事件
        updateTeacherButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 调出更新教师信息界面
                new UpdateTeacherFrame().setVisible(true);
            }
        });
    }
}