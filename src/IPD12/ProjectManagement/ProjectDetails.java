/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IPD12.ProjectManagement;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 1796111
 */
public class ProjectDetails extends javax.swing.JFrame {

    Database db;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DefaultListModel<Team> modelResourceList = new DefaultListModel<>();
    DefaultListModel<Team> modelMemberList = new DefaultListModel<>();

    /**
     * Creates new form ProjectList
     */
    public ProjectDetails(Project project) {
        try {
            // connect to db
            db = new Database();

            initComponents();
            loadProjectInfo(project);
        }
        catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error connecting to database: " + e.getMessage(),
                    "Database error",
                    JOptionPane.ERROR_MESSAGE);
            dispose(); // can't continue if database connection failed
        }

    }

    public void loadProjectInfo(Project project) {
        // load project details
        loadProjectDetails(project);
        // load task list
        loadTaskList(project);
        // load team member
        loadTeamMember(project);
    }

    public void loadProjectDetails(Project project) {

        // initialization
        DefaultComboBoxModel modelPM = (DefaultComboBoxModel) pjd_cbProjectManager.getModel();
        modelPM.removeAllElements();

        // load project information 
        if (project != null) {
            pjd_lblProjectId.setText(project.getId() + "");
            pjd_tfName.setText(project.getName());
            pjd_taDescription.setText(project.getDescription());
            if (project.getStartDatePlanned() != null) {
                pjd_tfStartDatePlanned.setText(sdf.format(project.getStartDatePlanned()));
            }
            else {
                pjd_tfStartDatePlanned.setText("");
            }
            if (project.getEndDatePlanned() != null) {
                pjd_tfEndDatePlanned.setText(sdf.format(project.getEndDatePlanned()));
            }
            else {
                pjd_tfEndDatePlanned.setText("");
            }
            if (project.getStartDateActual() != null) {
                pjd_tfStartDateActual.setText(sdf.format(project.getStartDateActual()));
            }
            else {
                pjd_tfStartDateActual.setText("");
            }
            if (project.getEndDateActual() != null) {
                pjd_tfEndDateActual.setText(sdf.format(project.getEndDateActual()));
            }
            else {
                pjd_tfEndDateActual.setText("");
            }

            pjd_chkbIsCompleted.setSelected(project.getIsCompleted());

            try {
                // initial value list for project manager combo box
                // get all team members
                ArrayList<Team> teamList = db.getAllTeamMembers(project.getId());

                for (Team tm : teamList) {
                    modelPM.addElement(tm.getIdName());
                    if (tm.getId() == project.getProjectManager()) {
                        modelPM.setSelectedItem(tm.getIdName());
                    }
                }

            }
            catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error fetching data: " + ex.getMessage(),
                        "Database error",
                        JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }
        }
        else {
            pjd_lblProjectId.setText("");
            pjd_tfName.setText("");
            pjd_taDescription.setText("");
            pjd_tfStartDatePlanned.setText("");
            pjd_tfEndDatePlanned.setText("");
            pjd_tfStartDateActual.setText("");
            pjd_tfEndDateActual.setText("");
            pjd_chkbIsCompleted.setSelected(false);

            try {
                // initial value list for project manager combo box
                // get all availabe resourses
                ArrayList<Team> resourceList = db.getAllTeamAvailabeResouces();

                for (Team rsc : resourceList) {
                    modelPM.addElement(rsc.getIdName());
                }

            }
            catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error fetching data: " + ex.getMessage(),
                        "Database error",
                        JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }

        }
    }

    public void loadTaskList(Project project) {

        DefaultTableModel tbModel = (DefaultTableModel) pjd_tbTaskList.getModel();
        // FIXED: initialization
        tbModel.getDataVector().removeAllElements();

        if (project != null) {

            try {
                // get task list
                ArrayList<Task> taskList = db.getAllTasksByProjectId(project.getId());

                for (Task task : taskList) {

                    String id = task.getId() + "";
                    String name = task.getName();
                    String description = task.getDescription();
                    String startDatePlanned = "";
                    String endDatePlanned = "";;
                    String startDateActual = "";;
                    String endDateActual = "";;
                    String isCompleted;
                    String inCharegePerson = "";

                    if (task.getStartDatePlanned() != null) {
                        startDatePlanned = sdf.format(task.getStartDatePlanned());
                    }
                    if (task.getEndDatePlanned() != null) {
                        endDatePlanned = sdf.format(task.getEndDatePlanned());
                    }
                    if (task.getStartDateActual() != null) {
                        startDateActual = sdf.format(task.getStartDateActual());
                    }
                    if (task.getEndDateActual() != null) {
                        endDateActual = sdf.format(task.getEndDateActual());
                    }

                    isCompleted = task.getIsCompleted() ? "YES" : "NO";

                    long inChargePersonId = task.getPersonInCharge();
                    if (inChargePersonId != 0) {
                        Team teamMember = db.getTeamMemberById(inChargePersonId);
                        if (teamMember != null) {
                            inCharegePerson = teamMember.getIdName();
                        }
                    }

                    tbModel.addRow(new Object[]{id, name, description, startDatePlanned, endDatePlanned,
                        startDateActual, endDateActual, inCharegePerson, isCompleted});
                }

            }
            catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error fetching data: " + ex.getMessage(),
                        "Database error",
                        JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }
        }

    }

    public void loadTeamMember(Project project) {
        try {
            ArrayList<Team> allAvailableResourceList = db.getAllTeamAvailabeResouces();
            ArrayList<Team> allTeamMemberList = db.getAllTeamMembers(project.getId());
            
            for (Team resource : allAvailableResourceList) {
                modelResourceList.addElement(resource);
            }
            
            for (Team member : allTeamMemberList) {
                modelMemberList.addElement(member);
            }
        }
        catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error fetching data: " + ex.getMessage(),
                        "Database error",
                        JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        pjd_taDescription = new javax.swing.JTextArea();
        pjd_tfName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        pjd_tfStartDateActual = new javax.swing.JTextField();
        pjd_tfEndDatePlanned = new javax.swing.JTextField();
        pjd_tfStartDatePlanned = new javax.swing.JTextField();
        pjd_tfEndDateActual = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        pjd_cbProjectManager = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        pjd_chkbIsCompleted = new javax.swing.JCheckBox();
        pjd_btDetailSave = new javax.swing.JButton();
        pjd_btDetailCancel = new javax.swing.JButton();
        pjd_lblProjectId = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pjd_tbTaskList = new javax.swing.JTable();
        pjd_btDeleteTask = new javax.swing.JButton();
        pjd_btUpdateTask = new javax.swing.JButton();
        pjd_btAddTask = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pjd_lstAllResourse = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        pjd_lstCurTeamMember = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        pjd_btTeamSave = new javax.swing.JButton();
        pjd_btTeamCancel = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Project Information Maintenance");
        setName("frmProjectDetails"); // NOI18N
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Project Information Maintenance");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Project Details:");

        jLabel7.setText("Project Id:");

        jLabel8.setText("Name:");

        jLabel9.setText("Description:");

        pjd_taDescription.setColumns(20);
        pjd_taDescription.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        pjd_taDescription.setRows(5);
        pjd_taDescription.setText("Project description......");
        jScrollPane4.setViewportView(pjd_taDescription);

        pjd_tfName.setText("ABC Inc. Core-System Re-Build Project");
        pjd_tfName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pjd_tfNameActionPerformed(evt);
            }
        });

        jLabel4.setText("Planned Start Date:");

        jLabel10.setText("Planned End Date:");

        jLabel11.setText("Actual Start Date:");

        jLabel13.setText("Actual End Date:");

        pjd_tfStartDateActual.setText("10-20-2019");

        pjd_tfEndDatePlanned.setText("10-20-2019");

        pjd_tfStartDatePlanned.setText("10-20-2019");

        pjd_tfEndDateActual.setText("10-20-2019");

        jLabel12.setText("Project Manager:");

        jLabel14.setText("Is Completed:");

        pjd_chkbIsCompleted.setSelected(true);

        pjd_btDetailSave.setText("Save");

        pjd_btDetailCancel.setText("Cancel");
        pjd_btDetailCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pjd_btDetailCancelActionPerformed(evt);
            }
        });

        pjd_lblProjectId.setText(":::");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pjd_cbProjectManager, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pjd_lblProjectId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pjd_tfStartDateActual, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(pjd_tfStartDatePlanned, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                        .addGap(73, 73, 73)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel10)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pjd_tfName, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                                .addComponent(jLabel9))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pjd_tfEndDatePlanned, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pjd_chkbIsCompleted)
                                    .addComponent(pjd_tfEndDateActual, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pjd_btDetailCancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pjd_btDetailSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addGap(31, 31, 31)
                        .addComponent(jScrollPane4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(pjd_tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9)
                                    .addComponent(pjd_lblProjectId))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(pjd_tfStartDatePlanned, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10)
                                    .addComponent(pjd_tfEndDatePlanned, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pjd_btDetailCancel)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(pjd_tfStartDateActual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)
                                    .addComponent(pjd_tfEndDateActual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel12)
                                        .addComponent(pjd_cbProjectManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel14))
                                    .addComponent(pjd_chkbIsCompleted)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pjd_btDetailSave)))
                        .addGap(8, 8, 8)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pjd_tbTaskList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Task ID", "Task Name", "Description", "Planned Start Date", "Planned End Date", "Actual Start Date", "Actual End Date", "Person in Charge", "Is Completed"
            }
        ));
        jScrollPane1.setViewportView(pjd_tbTaskList);

        pjd_btDeleteTask.setText("Delete Task");

        pjd_btUpdateTask.setText("Update Task");

        pjd_btAddTask.setText("Add Task");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Task List:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pjd_btDeleteTask, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pjd_btUpdateTask, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 112, Short.MAX_VALUE)
                            .addComponent(pjd_btAddTask, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {pjd_btDeleteTask, pjd_btUpdateTask});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(pjd_btAddTask)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pjd_btUpdateTask)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pjd_btDeleteTask)
                        .addGap(0, 98, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pjd_lstAllResourse.setModel(modelResourceList);
        jScrollPane2.setViewportView(pjd_lstAllResourse);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("All availabe resource:");

        pjd_lstCurTeamMember.setModel(modelMemberList);
        jScrollPane3.setViewportView(pjd_lstCurTeamMember);

        jButton1.setText(">>");

        jButton2.setText("<<");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Current team members:");

        pjd_btTeamSave.setText("Save");

        pjd_btTeamCancel.setText("Cancel");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel3))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pjd_btTeamCancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pjd_btTeamSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane2, jScrollPane3});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(76, 76, 76)
                                        .addComponent(pjd_btTeamCancel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pjd_btTeamSave)))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(38, 38, 38))
            .addGroup(layout.createSequentialGroup()
                .addGap(436, 436, 436)
                .addComponent(jLabel1)
                .addGap(78, 78, 78)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pjd_tfNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pjd_tfNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pjd_tfNameActionPerformed

    private void pjd_btDetailCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pjd_btDetailCancelActionPerformed
        this.dispose();
        //this.setVisible(false);
    }//GEN-LAST:event_pjd_btDetailCancelActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date startDateP, endDateP, startDateA, endDateA;
        try {
            startDateP = sdf.parse("2018-03-10");
            endDateP = sdf.parse("2018-03-30");
            startDateA = sdf.parse("2018-03-12");
            endDateA = sdf.parse("2018-03-28");
            Project project = new Project(10001, "ABS Inc Core-System Project", "Core-System re-build", startDateP, endDateP, startDateA, null, 2, true);
            loadTaskList(project);
        }
        catch (Exception ex) {

        }

    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton pjd_btAddTask;
    private javax.swing.JButton pjd_btDeleteTask;
    private javax.swing.JButton pjd_btDetailCancel;
    private javax.swing.JButton pjd_btDetailSave;
    private javax.swing.JButton pjd_btTeamCancel;
    private javax.swing.JButton pjd_btTeamSave;
    private javax.swing.JButton pjd_btUpdateTask;
    private javax.swing.JComboBox<String> pjd_cbProjectManager;
    private javax.swing.JCheckBox pjd_chkbIsCompleted;
    private javax.swing.JLabel pjd_lblProjectId;
    private javax.swing.JList<Team> pjd_lstAllResourse;
    private javax.swing.JList<Team> pjd_lstCurTeamMember;
    private javax.swing.JTextArea pjd_taDescription;
    private javax.swing.JTable pjd_tbTaskList;
    private javax.swing.JTextField pjd_tfEndDateActual;
    private javax.swing.JTextField pjd_tfEndDatePlanned;
    private javax.swing.JTextField pjd_tfName;
    private javax.swing.JTextField pjd_tfStartDateActual;
    private javax.swing.JTextField pjd_tfStartDatePlanned;
    // End of variables declaration//GEN-END:variables
}
