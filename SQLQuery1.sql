-- ����ѧ����
CREATE TABLE Student (
    Sno VARCHAR(10) NOT NULL PRIMARY KEY, -- ѧ�ţ��ǿ���Ϊ����
    Sname VARCHAR(20) NOT NULL, -- �������ǿ�
    Ssex VARCHAR(2) NOT NULL CHECK (Ssex IN ('��', 'Ů')), -- �Ա𣬷ǿ���ֻ��Ϊ'��'��'Ů'
    Sage INT NOT NULL CHECK (Sage >= 0), -- ���䣬�ǿ��Ҵ��ڵ���0
    Sdept VARCHAR(20) NOT NULL -- רҵ���ǿ�
);

-- �����γ̱�
CREATE TABLE Course (
    Cno VARCHAR(10) NOT NULL PRIMARY KEY, -- �γ̺ţ��ǿ���Ϊ����
    Cname VARCHAR(20) NOT NULL UNIQUE, -- �γ������ǿ���Ψһ
    Ccredit INT NOT NULL CHECK (Ccredit > 0), -- ѧ�֣��ǿ��Ҵ���0
    Tno VARCHAR(10) NOT NULL, -- ��ʦ���ţ��ǿ�
    FOREIGN KEY (Tno) REFERENCES Teacher(Tno) -- �������Teacher��
);

-- ������ʦ��
CREATE TABLE Teacher (
    Tno VARCHAR(10) NOT NULL PRIMARY KEY, -- ��ʦ���ţ��ǿ���Ϊ����
    Tname VARCHAR(20) NOT NULL, -- ��ʦ�������ǿ�
    Tsex VARCHAR(2) NOT NULL CHECK (Tsex IN ('��', 'Ů')), -- ��ʦ�Ա𣬷ǿ���ֻ��Ϊ'��'��'Ů'
    Tage INT NOT NULL CHECK (Tage >= 0), -- ��ʦ���䣬�ǿ��Ҵ��ڵ���0
    Ttitle VARCHAR(20) NOT NULL, -- ��ʦְ�ƣ��ǿ�
    phone VARCHAR(15) NOT NULL UNIQUE -- ��ʦ�ֻ��ţ��ǿ���Ψһ
);

-- �����û���
CREATE TABLE Users (
    ID VARCHAR(20) NOT NULL PRIMARY KEY, -- �˺ţ��ǿ���Ϊ����
    PAWD VARCHAR(20) NOT NULL, -- ���룬�ǿ�
    Sno VARCHAR(10), -- ѧ�ţ���Ϊ��
    Tno VARCHAR(10), -- ��ʦ���ţ���Ϊ��
    phone VARCHAR(15) NOT NULL UNIQUE, -- �ֻ��ţ��ǿ���Ψһ
    role VARCHAR(10) NOT NULL CHECK (role IN ('ѧ��', '��ʦ', '����Ա')) -- ��ɫ���ǿ���ֻ��Ϊ'ѧ��'��'��ʦ'��'����Ա'
);

-- ������Լ����ȷ��Sno��Tno��һ����
ALTER TABLE Users ADD CONSTRAINT FK_Users_Student FOREIGN KEY (Sno) REFERENCES Student(Sno);
ALTER TABLE Users ADD CONSTRAINT FK_Users_Teacher FOREIGN KEY (Tno) REFERENCES Teacher(Tno);

-- ����ѡ�α�
CREATE TABLE SC (
    Sno VARCHAR(10) NOT NULL, -- ѧ�ţ��ǿ�
    Cno VARCHAR(10) NOT NULL, -- �γ̺ţ��ǿ�
    Grade INT CHECK (Grade >= 0 AND Grade <= 100), -- �ɼ���ȡֵ��Χ��0��100֮��
    LEVEL VARCHAR(10), -- �ȼ�����Ϊ��
    PRIMARY KEY (Sno, Cno), -- ����������ȷ��һ��ѧ����һ�ſγ�ֻ��һ��ѡ�μ�¼
    FOREIGN KEY (Sno) REFERENCES Student(Sno), -- �������Student��
    FOREIGN KEY (Cno) REFERENCES Course(Cno) -- �������Course��
);