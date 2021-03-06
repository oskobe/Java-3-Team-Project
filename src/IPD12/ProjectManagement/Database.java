/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IPD12.ProjectManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author 1796111
 */
public class Database {

    private final static String HOSTNAME = "den1.mysql3.gear.host:3306";
    private final static String DBNAME = "pjms";
    private final static String USERNAME = "pjms";
    private final static String PASSWORD = "Bh1x~QbvBu!x";

    private Connection conn;

    public Database() throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + HOSTNAME + "/" + DBNAME + "?useSSL=false",
                USERNAME, PASSWORD);
    }

    public ArrayList<Team> getAllTeamMembers(long projectId) throws SQLException {

        String sql = "SELECT u.id, u.name, u.ability FROM teams AS t join users AS u on t.userId = u.id WHERE t.projectId = ? AND t.isLeft = 0";
        ArrayList<Team> list = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, projectId);

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                String ability = result.getString("ability");

                Team teamMember = new Team(id, name, ability);
                list.add(teamMember);
            }
        }

        return list;
    }

    public ArrayList<Team> getAllTeamAvailabeResouces() throws SQLException {

        String sql = "SELECT id, name, ability FROM users WHERE isAvailable = 1";
        ArrayList<Team> list = new ArrayList<>();

        try (Statement stmt = conn.createStatement()) {
            ResultSet result = stmt.executeQuery(sql);

            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                String ability = result.getString("ability");

                Team availableResource = new Team(id, name, ability);
                list.add(availableResource);
            }
        }

        return list;
    }

    public Team getTeamMemberById(long id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return new Team(result.getLong("id"), result.getString("name"), result.getString("ability"));
            }
            else {
                return null;
            }
        }
    }

    public ArrayList<Task> getAllTasksByProjectId(long projectId) throws SQLException {

        String sql = "SELECT * FROM tasks WHERE projectId = ? AND isDeleted = false";
        ArrayList<Task> list = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, projectId);

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                String description = result.getString("description");
                Date startDatePlanned = result.getDate("startDatePlanned");
                Date endDatePlanned = result.getDate("endDatePlanned");
                Date startDateActual = result.getDate("startDateActual");
                Date endDateActual = result.getDate("endDateActual");
                long inChargePersonId = result.getLong("inChargePerson");
                boolean isCompleted = result.getBoolean("isCompleted");

                Task task = new Task(id, name, description, startDatePlanned, endDatePlanned,
                        startDateActual, endDateActual, inChargePersonId, isCompleted);
                list.add(task);
            }
        }

        return list;
    }
    
    public ArrayList<Task> getAllTasksByProjectIdOrderByItem(long projectId) throws SQLException {

        String sql = "SELECT item, name, description, startDatePlanned, endDatePlanned, startDateActual, endDateActual, inChargePerson, isCompleted "
                + "FROM tasks WHERE projectId = ? AND isDeleted = false ORDER BY item";
        ArrayList<Task> list = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, projectId);

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                int item = result.getInt("item");
                String name = result.getString("name");
                String description = result.getString("description");
                Date startDatePlanned = result.getDate("startDatePlanned");
                Date endDatePlanned = result.getDate("endDatePlanned");
                Date startDateActual = result.getDate("startDateActual");
                Date endDateActual = result.getDate("endDateActual");
                long inChargePersonId = result.getLong("inChargePerson");
                boolean isCompleted = result.getBoolean("isCompleted");

                Task task = new Task(projectId, item, name, description, startDatePlanned, endDatePlanned,
                        startDateActual, endDateActual, inChargePersonId, isCompleted);
                list.add(task);
            }
        }

        return list;
    }
    
    public int getMaxItem(long projectId) throws SQLException {
        int maxItem;
        String sql = "SELECT MAX(item) FROM tasks WHERE projectId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, projectId);
            
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                maxItem = result.getInt(1);
            } else {
                maxItem = 0;
            }
        }

        return maxItem;
    }
    
    public boolean checkItemIsExisting(long projectId, int itemNo) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE projectId = ? AND item = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, projectId);
            stmt.setInt(2, itemNo);
            
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean checkIfHasUncompletedTaskByProjectId(long projectId) throws SQLException {

        String sql = "SELECT * FROM tasks WHERE projectId = ? AND isDeleted = false AND isCompleted = false";
     
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, projectId);

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return true;
            } else 
                return false;
        }
    }
    
    public boolean checkProjectIsCompleted(long projectId) throws SQLException {

        String sql = "SELECT isCompleted FROM projects WHERE id = ?";
        boolean isCompleted = true;
     
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, projectId);

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                isCompleted = result.getBoolean("isCompleted");
            }            
            return isCompleted;
        }
    }

    public Project getProjectById(long projectId) throws SQLException {
        String sql = "SELECT * FROM projects WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, projectId);

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                String description = result.getString("description");
                Date startDatePlanned = result.getDate("startDatePlanned");
                Date endDatePlanned = result.getDate("endDatePlanned");
                Date startDateActual = result.getDate("startDateActual");
                Date endDateActual = result.getDate("endDateActual");
                long projectManager = result.getLong("projectManager");
                boolean isCompleted = result.getBoolean("isCompleted");
                
                Project project = new Project(id, name, description, startDatePlanned, endDatePlanned, startDateActual, endDateActual, projectManager, isCompleted);
                return project;
            }
            else {
                return null;
            }
        }
    }
    
    public long addProject(Project project) throws SQLException {
        long id = 0;
        
        String sql = "INSERT INTO projects (name, description, startDatePlanned, endDatePlanned, startDateActual, endDateActual, projectManager, isCompleted) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());

            stmt.setDate(3, GlobalProcess.formatSqlDate(project.getStartDatePlanned()));
            stmt.setDate(4, GlobalProcess.formatSqlDate(project.getEndDatePlanned()));
            stmt.setDate(5, GlobalProcess.formatSqlDate(project.getStartDateActual()));
            stmt.setDate(6, GlobalProcess.formatSqlDate(project.getEndDateActual()));

            if (project.getProjectManager() == 0) {
                stmt.setString(7, null);
            }
            else {
                stmt.setLong(7, project.getProjectManager());
            }

            stmt.setBoolean(8, project.getIsCompleted());

            stmt.executeUpdate();

            // get primary key
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            }
            
            return id;
        }
    }

    public void updateProject(Project project) throws SQLException {
        String sql = "UPDATE projects SET name = ?, "
                + "description = ?, startDatePlanned = ?, endDatePlanned = ?, "
                + "startDateActual = ?, endDateActual = ?, projectManager = ?, "
                + "isCompleted = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());

            stmt.setDate(3, GlobalProcess.formatSqlDate(project.getStartDatePlanned()));
            stmt.setDate(4, GlobalProcess.formatSqlDate(project.getEndDatePlanned()));
            stmt.setDate(5, GlobalProcess.formatSqlDate(project.getStartDateActual()));
            stmt.setDate(6, GlobalProcess.formatSqlDate(project.getEndDateActual()));
 
            if (project.getProjectManager() == 0) {
                stmt.setString(7, null);
            }
            else {
                stmt.setLong(7, project.getProjectManager());
            }

            stmt.setBoolean(8, project.getIsCompleted());
            stmt.setLong(9, project.getId());

            stmt.executeUpdate();
        }
    }
    
    public Team checkIfMemberInTeam(Team member) throws SQLException {
        String sql = "SELECT * FROM teams WHERE projectId = ? and userId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, member.getProjectId());
            stmt.setLong(2, member.getId());

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                long projectId = result.getLong("projectId");
                long userId = result.getLong("userId");
                boolean isLeft = result.getBoolean("isLeft");
                Team memberWithStatus = new Team(projectId, userId, isLeft);
                return memberWithStatus;
            }
            else {
                return null;
            }
        }
    }
    
    public void addTeamMember(Team member) throws SQLException {
        String sql = "INSERT INTO teams (projectId, userId, isLeft) VALUES(?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, member.getProjectId());
            stmt.setLong(2, member.getId());
            stmt.setBoolean(3, member.getIsLeft());
            
            stmt.executeUpdate();
        } 
    }
    
    public void updateTeamMemberStatus(Team member) throws SQLException {
        String sql = "UPDATE teams set isLeft =? WHERE projectId = ? and userId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, member.getIsLeft());
            stmt.setLong(2, member.getProjectId());
            stmt.setLong(3, member.getId());

            stmt.executeUpdate();
        } 
    }
    
    public void updateUserStatus(User user) throws SQLException {
        String sql = "UPDATE users set isAvailable = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, user.getIsAvailable());
            stmt.setLong(2, user.getId());

            stmt.executeUpdate();
        } 
    }
  
    public Task getTaskByProjectIdPlusItem(long projectId, int item) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE projectId = ? AND item = ?  AND isDeleted = false";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, projectId);
            stmt.setInt(2, item);
            
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                String name = result.getString("name");
                String despcription = result.getString("description");
                Date startDatePlanned = result.getDate("startDatePlanned");
                Date endDatePlanned = result.getDate("endDatePlanned");
                Date startDateActual = result.getDate("startDateActual");
                Date endDateActual = result.getDate("endDateActual");
                long personInCharge = result.getLong("inChargePerson");
                boolean isCompleted = result.getBoolean("isCompleted");
                
                Task task = new Task(projectId, item, name, despcription, startDatePlanned, endDatePlanned, startDateActual, endDateActual, personInCharge, isCompleted);
                return task;
            }
            else {
                return null;
            }
        }
    }
    
    public void addTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (projectId, item, name, description, startDatePlanned, endDatePlanned, startDateActual, endDateActual, inChargePerson, isCompleted) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, task.getProjectId());
            stmt.setInt(2, task.getItem());
            stmt.setString(3, task.getName());
            stmt.setString(4, task.getDescription());

            stmt.setDate(5, GlobalProcess.formatSqlDate(task.getStartDatePlanned()));
            stmt.setDate(6, GlobalProcess.formatSqlDate(task.getEndDatePlanned()));
            stmt.setDate(7, GlobalProcess.formatSqlDate(task.getStartDateActual()));
            stmt.setDate(8, GlobalProcess.formatSqlDate(task.getEndDateActual()));

            if (task.getPersonInCharge()== 0) {
                stmt.setString(9, null);
            }
            else {
                stmt.setLong(9, task.getPersonInCharge());
            }

            stmt.setBoolean(10, task.getIsCompleted());

            stmt.executeUpdate();

        }
    }

    public void updateTask(Task task) throws SQLException {
        String sql = "UPDATE tasks SET name = ?, "
                + "description = ?, startDatePlanned = ?, endDatePlanned = ?, "
                + "startDateActual = ?, endDateActual = ?, inChargePerson = ?, "
                + "isCompleted = ? WHERE projectId = ? AND item = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getName());
            stmt.setString(2, task.getDescription());

            stmt.setDate(3, GlobalProcess.formatSqlDate(task.getStartDatePlanned()));
            stmt.setDate(4, GlobalProcess.formatSqlDate(task.getEndDatePlanned()));
            stmt.setDate(5, GlobalProcess.formatSqlDate(task.getStartDateActual()));
            stmt.setDate(6, GlobalProcess.formatSqlDate(task.getEndDateActual()));

            if (task.getPersonInCharge()== 0) {
                stmt.setString(7, null);
            }
            else {
                stmt.setLong(7, task.getPersonInCharge());
            }

            stmt.setBoolean(8, task.getIsCompleted());
            stmt.setLong(9, task.getProjectId());
            stmt.setInt(10, task.getItem());

            stmt.executeUpdate();
        }
    }
    
    public void changeDeleteFlagStatus(long projectId, int item, boolean flag) throws SQLException {
        String sql = "UPDATE tasks SET isDeleted = ? WHERE projectId = ? AND item = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, flag);
            stmt.setLong(2, projectId);
            stmt.setInt(3, item);
            
            stmt.executeUpdate();
        }
    }
    
    public ArrayList<Project> getAllProjectIdName() throws SQLException {        
        String sql = "SELECT * FROM projects";
        ArrayList<Project> list = new ArrayList<>();     

        try (Statement stmt = conn.createStatement()) {
            ResultSet result = stmt.executeQuery(sql);            
            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");              
                Project p = new Project(id, name);
                list.add(p);
            }
        }         
        return list;
    }
   
    
   // For Jerry
   ////////////////////////////////////////////////////////////////////
    public ArrayList<Task> getAllTasks() throws SQLException{
        String sql = "SELECT * FROM tasks where isDeleted= '0'";
        ArrayList<Task> list = new ArrayList<>();     
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();            
            while (result.next()) {
                long taskid = result.getLong("id");
                String name = result.getString("name");
                String description = result.getString("description");
                Date sdp = result.getDate("startDatePlanned");
                Date edp = result.getDate("endDatePlanned");
                Date sda = result.getDate("startDateActual");
                Date eda = result.getDate("endDateActual");
                int ICPID = result.getInt("inChargePerson");
                String ICPName = getUserNameById(ICPID);
                boolean status = result.getBoolean("isCompleted");                                
                Task t = new Task(taskid, name,description,sdp,edp,sda,eda,ICPID,status,ICPName);
                list.add(t);
            }
        }         
        return list;
    }
    
    public ArrayList<Task> getTasksById(long id) throws SQLException{
        String sql = "SELECT * FROM tasks where isDeleted= '0' and projectId=" + id;
        ArrayList<Task> list = new ArrayList<>();     
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();            
            while (result.next()) {
                long taskid = result.getLong("id");
                String name = result.getString("name");
                String description = result.getString("description");
                Date sdp = result.getDate("startDatePlanned");
                Date edp = result.getDate("endDatePlanned");
                Date sda = result.getDate("startDateActual");
                Date eda = result.getDate("endDateActual");
                int ICPID = result.getInt("inChargePerson");
                String ICPName = getUserNameById(ICPID);
                boolean status = result.getBoolean("isCompleted");                                
                Task t = new Task(taskid,name,description,sdp,edp,sda,eda,ICPID,status,ICPName);
                list.add(t);
            }
        }         
        return list;
    }
    public long getUserIdByEmail(String email) throws SQLException{
        String sql = "SELECT id FROM users WHERE email = ?" ;                
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return result.getLong("id");
            }
        }
        return -1;
    }
    public String getUserNameById(long id) throws SQLException{
        String sql = "SELECT name FROM users WHERE id =" + id;                
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return result.getString("name");
            }
        }
        return "";
    }
    public ArrayList<Project> getAllProjects() throws SQLException {        
        String sql = "SELECT p.id as id,p.name as name,p.description as description,p.startDatePlanned as startDatePlanned,p.endDatePlanned as endDatePlanned,p.startDateActual as startDateActual, p.endDateActual as endDateActual, p.projectManager as PM,p.isCompleted as status,count(t.id) as tasknums FROM projects p left join tasks t  on p.id=t.projectId group by p.id";
        ArrayList<Project> list = new ArrayList<>();     
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();            
            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                String description = result.getString("description");
                Date sdp = result.getDate("startDatePlanned");
                Date edp = result.getDate("endDatePlanned");
                Date sda = result.getDate("startDateActual");
                Date eda = result.getDate("endDateActual");
                int PMID = result.getInt("PM");
                String PMName = getUserNameById(PMID);
                boolean status = result.getBoolean("status");
                int tasknums = result.getInt("tasknums");                
                Project p = new Project(id, name,description,sdp,edp,sda,eda,status,PMID,PMName,tasknums);
                list.add(p);
            }
        }         
        return list;
    }
    public final static int GETALLPROJECTS_ORDERBYID_ASC=1;
    public final static int GETALLPROJECTS_ORDERBYSTARTDATE_ASC=2;
    public final static int GETALLPROJECTS_ORDERBYSTARTDATE_DESC=3;
    public ArrayList<Project> getAllProjects(int controlCode) throws SQLException {  
        String sql = "";
        if(controlCode==GETALLPROJECTS_ORDERBYID_ASC){
            sql = "SELECT p.id as id,p.name as name,p.description as description,p.startDatePlanned as startDatePlanned,p.endDatePlanned as endDatePlanned,p.startDateActual as startDateActual, p.endDateActual as endDateActual, p.projectManager as PM,p.isCompleted as status,(select count(id) from tasks where projectid = p.id and isdeleted = false) as tasknums FROM projects p left join tasks t  on p.id=t.projectId group by p.id";
        }
        if(controlCode==GETALLPROJECTS_ORDERBYSTARTDATE_ASC){
            sql = "SELECT p.id as id,p.name as name,p.description as description,p.startDatePlanned as startDatePlanned,p.endDatePlanned as endDatePlanned,p.startDateActual as startDateActual, p.endDateActual as endDateActual, p.projectManager as PM,p.isCompleted as status,(select count(id) from tasks where projectid = p.id and isdeleted = false) as tasknums FROM projects p left join tasks t  on p.id=t.projectId group by p.id order by p.startDatePlanned asc";
        }
        if(controlCode==GETALLPROJECTS_ORDERBYSTARTDATE_DESC){
            sql = "SELECT p.id as id,p.name as name,p.description as description,p.startDatePlanned as startDatePlanned,p.endDatePlanned as endDatePlanned,p.startDateActual as startDateActual, p.endDateActual as endDateActual, p.projectManager as PM,p.isCompleted as status,(select count(id) from tasks where projectid = p.id and isdeleted = false) as tasknums FROM projects p left join tasks t  on p.id=t.projectId group by p.id order by p.startDatePlanned desc";
        }
        ArrayList<Project> list = new ArrayList<>();     
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();            
            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                String description = result.getString("description");
                Date sdp = result.getDate("startDatePlanned");
                Date edp = result.getDate("endDatePlanned");
                Date sda = result.getDate("startDateActual");
                Date eda = result.getDate("endDateActual");
                int PMID = result.getInt("PM");
                String PMName = getUserNameById(PMID);
                boolean status = result.getBoolean("status");
                int tasknums = result.getInt("tasknums");                
                Project p = new Project(id, name,description,sdp,edp,sda,eda,status,PMID,PMName,tasknums);
                list.add(p);
            }
        }         
        return list;
    }
    
    public User getUserById(long id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {                
                return new User(result.getLong("id"), result.getString("name"), result.getString("email"),result.getString("ability"),result.getString("password"));
            }
            else {
                return null;
            }
        }
    }
    
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET email=?, ability=? ,password = ? WHERE id= ?";
        //System.out.println(user.getPassword());
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getAbility());
            stmt.setString(3, user.getPassword());
            stmt.setLong(4, user.getId());
            stmt.executeUpdate();
        }
    }
    public long AddUser(User user) throws SQLException {        
        long id = 0;    
        String sql = "INSERT INTO users (name, email, ability, password) VALUES (?, ?, ?, ?)";       
        try (PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getAbility());
            stmt.setString(4, user.getPassword());            
            stmt.executeUpdate();
            // get primary key
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            }
        }  
        return id;
    }
                

    // For Jerry
    ////////////////////////////////////////////////////////////////////
    public String getPasswordByEmail(String email) throws SQLException {
        String sql = "SELECT password FROM users WHERE email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return result.getString("password");
            }
        }
        return "";
    }

    public String getPasswordByEmployeeID(String ID) throws SQLException {
        String sql = "SELECT password FROM users WHERE id =" + ID;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return result.getString("password");
            }
        }
        return "";
    }

    ////////////////////////////////////////////////////////////////////
    /**
     * @param conn the conn to set
     */
    public void setAutoCommit(boolean flag) throws SQLException {
        conn.setAutoCommit(flag);
    }

    public void commitUpdate() throws SQLException {
        conn.commit();
    }

    public void rollbackUpdate() throws SQLException {
        conn.rollback();
    }

}
