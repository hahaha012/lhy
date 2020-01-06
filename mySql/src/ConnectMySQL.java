
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.sql.PreparedStatement;

public class ConnectMySQL {

	public static void main(String[] args) {

		List<Student> stu = getAllStudenst();
		System.out.println(stu.toString());
		Student s = new Student();
		s.setStuno("20182112");
		s.setName("����");
		s.setGender("f");
		s.setBirthdate(new Date(19930623));
		s.setAge(18);
		s.setMajor("���繤��");
		int row = insert(s);
		System.out.println(row);
	}

	static List<Student> getAllStudenst() {
		// JDBC��һЩ����
		String URL = "jdbc:mysql://localhost:3306/test";
		String driverName = "com.mysql.jdbc.Driver";
		String sql = "select * from students";
		String userName = "root";// root
		String password = "java123";// 123456
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Student> stu = new ArrayList<>();
		try {
			Class.forName(driverName);// jdbc4.0 ������ʹ������������ע�����
			conn = DriverManager.getConnection(URL, userName, password);
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			// id | stuno | name | gender | birthdate | major
			while (resultSet.next()) {
				Student s = new Student();
				s.setId(resultSet.getInt("id"));
				s.setStuno(resultSet.getString("stuno"));
				s.setName(resultSet.getString("name"));
				s.setGender(resultSet.getString("gender"));
				s.setAge(resultSet.getInt("age"));
				s.setBirthdate(resultSet.getDate("birthdate"));
				s.setMajor(resultSet.getString("major"));
				stu.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {/* ������Դ�ͷţ�����ʹ��try..with..resources�﷨�� */
			if (resultSet != null) {
				try {
					resultSet.close();
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
					statement = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
			if (conn != null)
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return stu;
	}

	public static int insert(Student stu) {
		Connection con = null;
        PreparedStatement pStatement = null;
        ResultSet rs = null;
        SimpleDateFormat hmFromat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");// ��ʽ������

        // String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/test";
        /*����test���ݿ⣬userUnicode��characterEncoding������������������Ҫ�ģ�����mysql���°汾������url��ָ����Ӧ����*/
        //String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8";
        
        String userName = "root";
        String password = "java123";
        String driverName = "com.mysql.jdbc.Driver";
        int row=0;

        // ���ݿ�����ܶ�� test.students , test.teachers, test.courses
        try {
            
            Class.forName(driverName);//jdbc4.0��������������������Ϊ�˱�֤������һ�㱣��  
            con = DriverManager.getConnection(url, userName, password);

            // ���ݲ����Ĳ�������
            String strSql = "insert into students(stuno,name,gender,birthdate,major,age) values(?,?,?,?,?,?)";
            pStatement = con.prepareStatement(strSql);
            pStatement.setString(1, stu.getStuno());
            pStatement.setString(2, stu.getName());
            pStatement.setString(3, stu.getGender());
            pStatement.setString(4, stu.getBirthdate().toString());
            pStatement.setString(5, stu.getMajor());
            pStatement.setInt(6, stu.getAge());
            row = pStatement.executeUpdate();
            pStatement.close();// �����ͷ���Դ
            System.out.println("id\t��� \t\t����\t\t�Ա�\t����������\t\tרҵ");
            while (rs.next()) {

                // ͨ���е��±�(index)ȡ����
                System.out.print(rs.getInt(1) + "\t");
                System.out.print(rs.getString(2) + "\t");
                System.out.print(rs.getString(3) + "\t");
                System.out.print(rs.getString(4) + "\t ");
                System.out.print(rs.getDate("birthdate") + "\t");// �����д�,why?
                System.out.println(rs.getString(7));
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("û���ҵ�������!");
            e.printStackTrace();
        }
        finally {
            if (rs != null)
                try {
                    rs.close();
                    rs = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (pStatement != null)
                try {
                    pStatement.close();// �ر����
                    pStatement = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (con != null) {
                try {
                    con.close();// �ر�����
                    con = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return row;
	}

}
