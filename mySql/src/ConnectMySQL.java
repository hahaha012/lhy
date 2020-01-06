
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
		s.setName("马六");
		s.setGender("f");
		s.setBirthdate(new Date(19930623));
		s.setAge(18);
		s.setMajor("网络工程");
		int row = insert(s);
		System.out.println(row);
	}

	static List<Student> getAllStudenst() {
		// JDBC的一些配置
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
			Class.forName(driverName);// jdbc4.0 后无需使用这句进行驱动注册操作
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
		} finally {/* 下面资源释放，可以使用try..with..resources语法简化 */
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
        SimpleDateFormat hmFromat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");// 格式化日期

        // String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/test";
        /*连接test数据库，userUnicode和characterEncoding是正常插入中文所需要的，不过mysql较新版本无需在url中指定相应参数*/
        //String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8";
        
        String userName = "root";
        String password = "java123";
        String driverName = "com.mysql.jdbc.Driver";
        int row=0;

        // 数据库包含很多表 test.students , test.teachers, test.courses
        try {
            
            Class.forName(driverName);//jdbc4.0后无需载入驱动，不过为了保证兼容性一般保留  
            con = DriverManager.getConnection(url, userName, password);

            // 根据参数的插入数据
            String strSql = "insert into students(stuno,name,gender,birthdate,major,age) values(?,?,?,?,?,?)";
            pStatement = con.prepareStatement(strSql);
            pStatement.setString(1, stu.getStuno());
            pStatement.setString(2, stu.getName());
            pStatement.setString(3, stu.getGender());
            pStatement.setString(4, stu.getBirthdate().toString());
            pStatement.setString(5, stu.getMajor());
            pStatement.setInt(6, stu.getAge());
            row = pStatement.executeUpdate();
            pStatement.close();// 立即释放资源
            System.out.println("id\t编号 \t\t姓名\t\t性别\t出生年月日\t\t专业");
            while (rs.next()) {

                // 通过列的下标(index)取数据
                System.out.print(rs.getInt(1) + "\t");
                System.out.print(rs.getString(2) + "\t");
                System.out.print(rs.getString(3) + "\t");
                System.out.print(rs.getString(4) + "\t ");
                System.out.print(rs.getDate("birthdate") + "\t");// 这列有错,why?
                System.out.println(rs.getString(7));
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("没有找到驱动类!");
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
                    pStatement.close();// 关闭语句
                    pStatement = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (con != null) {
                try {
                    con.close();// 关闭连接
                    con = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return row;
	}

}
