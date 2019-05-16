import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EncodingManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class demo {
    final static String testOsm = "../vietnam-latest.pbf";
    private static final String ghLoc = "../map/tmp";

    public static void main(String[] args) throws SQLException {
        ConnectJdbc connectJdbc = new ConnectJdbc();
        List<User> teacherList = new ArrayList<User>();
        List<User> parentList = new ArrayList<User>();
        try {
            Connection connection = connectJdbc.getConnection();
            teacherList = getListTeacher(connection);
            parentList = getListParent(connection);

            GraphHopper graphHopper = new GraphHopper().setInMemory()
                .setEncodingManager(new EncodingManager("car"))
                .setGraphHopperLocation(ghLoc)
                .setOSMFile(testOsm)
                .forServer();
            graphHopper.importOrLoad();

            for (int i = 0; i < teacherList.size(); i++) {
                for (int j = 0; j < parentList.size(); j++) {
                    GHRequest request = new GHRequest(teacherList.get(i).getLatitude(), teacherList.get(i).getLongtitude(), parentList.get(j).getLatitude(), parentList.get(j).getLongtitude());
                    request.setWeighting("fastest");
                    request.setVehicle("car");
                    GHResponse ph = graphHopper.route(request);
                    if(checkDistance(teacherList.get(i).getId(),parentList.get(j).getId(),connection)!=1){
                        addDistance(teacherList.get(i).getId(),parentList.get(j).getId(),ph.getBest().getDistance(),connection);
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }




    }

    public static List<User> getListTeacher(Connection connection) {
        String query = "SELECT id,latitude,longitude from user where role='ROLE_TEACHER' and active=1";
        List<User> userList = new ArrayList<User>();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                User user = new User();
                Long id = rs.getLong("id");
                user.setId((id));
                user.setLatitude(rs.getDouble("latitude"));
                user.setLongtitude(rs.getDouble("longitude"));
                userList.add(user);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static List<User> getListParent(Connection connection) {
        String query = "SELECT id,latitude,longitude from user where role='ROLE_PARENT' and active=1";
        List<User> userList = new ArrayList<User>();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                User user = new User();
                Long id = rs.getLong("id");
                user.setId((id));
                user.setLatitude(rs.getDouble("latitude"));
                user.setLongtitude(rs.getDouble("longitude"));
                userList.add(user);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static void addDistance(Long teacherId, Long parentId, Double distance, Connection connection) {
        String query = "insert into distance VALUES (?,?,?)";

        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setLong(1, teacherId);
            st.setLong(2, parentId);
            st.setDouble(3, distance);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int checkDistance(Long teacherId,Long parentID,Connection connection) {
        String query = "SELECT 1 from distance where teacher_id="+teacherId+"  and parent_id= "+parentID;

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
