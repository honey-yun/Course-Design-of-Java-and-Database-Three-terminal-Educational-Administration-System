package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddFrame extends JFrame {

    public AddFrame() {
        super("添加信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 添加学生信息按钮
        JButton addStudentButton = new JButton("添加学生信息");
        addStudentButton.setFont(new Font("楷体", Font.PLAIN, 20));
        addStudentButton.setContentAreaFilled(false);
        addStudentButton.setBounds(100, 100, 200, 30);

        // 添加课程信息按钮
        JButton addCourseButton = new JButton("添加课程信息");
        addCourseButton.setFont(new Font("楷体", Font.PLAIN, 20));
        addCourseButton.setContentAreaFilled(false);
        addCourseButton.setBounds(100, 200, 200, 30);

        // 添加教师信息按钮
        JButton addTeacherButton = new JButton("添加教师信息");
        addTeacherButton.setFont(new Font("楷体", Font.PLAIN, 20));
        addTeacherButton.setContentAreaFilled(false);
        addTeacherButton.setBounds(100, 300, 200, 30);

        frame.setLayout(null);
        frame.add(addStudentButton);
        frame.add(addCourseButton);
        frame.add(addTeacherButton);

        // 添加学生信息按钮点击事件
        addStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 调出添加学生信息界面
                new AddStudentFrame().setVisible(true);
            }
        });

        // 添加课程信息按钮点击事件
        addCourseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 调出添加课程信息界面
                new AddCourseFrame().setVisible(true);
            }
        });

        // 添加教师信息按钮点击事件
        addTeacherButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 调出添加教师信息界面
                new AddTeacherFrame().setVisible(true);
            }
        });
    }
}