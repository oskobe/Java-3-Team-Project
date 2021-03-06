/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IPD12.ProjectManagement;

import java.util.Date;

/**
 *
 * @author wjing
 */
public class Task {
    private long id;
    private long projectId;
    private int item;
    private String name;
    private String description;
    private Date startDatePlanned;
    private Date endDatePlanned;
    private Date startDateActual;
    private Date endDateActual;
    private long personInCharge;
    private boolean isCompleted;
    private String personInChargeName;

    // constructor
    public Task(long id, String name, String description, Date startDatePlanned, Date endDatePlanned, Date startDateActual, Date endDateActual, long personInCharge, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDatePlanned = startDatePlanned;
        this.endDatePlanned = endDatePlanned;
        this.startDateActual = startDateActual;
        this.endDateActual = endDateActual;
        this.personInCharge = personInCharge;
        this.isCompleted = isCompleted;
    }
    
    public Task(long projectId, int item, String name, String description, Date startDatePlanned, Date endDatePlanned, Date startDateActual, Date endDateActual, long personInCharge, boolean isCompleted) {
        this.projectId = projectId;
        this.item = item;
        this.name = name;
        this.description = description;
        this.startDatePlanned = startDatePlanned;
        this.endDatePlanned = endDatePlanned;
        this.startDateActual = startDateActual;
        this.endDateActual = endDateActual;
        this.personInCharge = personInCharge;
        this.isCompleted = isCompleted;
    }

    public Task(long id, long projectId, String name, String description, Date startDatePlanned, Date endDatePlanned, Date startDateActual, Date endDateActual, long personInCharge, boolean isCompleted) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.startDatePlanned = startDatePlanned;
        this.endDatePlanned = endDatePlanned;
        this.startDateActual = startDateActual;
        this.endDateActual = endDateActual;
        this.personInCharge = personInCharge;
        this.isCompleted = isCompleted;
    }
    
    
 
    public Task(long id, long projectId, String name, String description, Date startDatePlanned, Date endDatePlanned, Date startDateActual, Date endDateActual, long personInCharge, boolean isCompleted, String personInChargeName) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.startDatePlanned = startDatePlanned;
        this.endDatePlanned = endDatePlanned;
        this.startDateActual = startDateActual;
        this.endDateActual = endDateActual;
        this.personInCharge = personInCharge;
        this.isCompleted = isCompleted;
        this.personInChargeName = personInChargeName;
    }
    
    // setters and getters
    public int getItem() {
        return item;
    }

    public void setItem(int item) {    
        this.item = item;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDatePlanned() {
        return startDatePlanned;
    }

    public void setStartDatePlanned(Date startDatePlanned) {
        this.startDatePlanned = startDatePlanned;
    }

    public Date getEndDatePlanned() {
        return endDatePlanned;
    }

    public void setEndDatePlanned(Date endDatePlanned) {
        this.endDatePlanned = endDatePlanned;
    }

    public Date getStartDateActual() {
        return startDateActual;
    }

    public void setStartDateActual(Date startDateActual) {
        this.startDateActual = startDateActual;
    }

    public Date getEndDateActual() {
        return endDateActual;
    }

    public void setEndDateActual(Date endDateActual) {
        this.endDateActual = endDateActual;
    }

    public long getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(long personInCharge) {
        this.personInCharge = personInCharge;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
    
    
    
   // For Jerry
   ////////////////////////////////////////////////////////////////////
    
    public Task(long id, String name, String description, Date startDatePlanned, Date endDatePlanned, Date startDateActual, Date endDateActual, long personInCharge, boolean isCompleted, String personInChargeName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDatePlanned = startDatePlanned;
        this.endDatePlanned = endDatePlanned;
        this.startDateActual = startDateActual;
        this.endDateActual = endDateActual;
        this.personInCharge = personInCharge;
        this.isCompleted = isCompleted;
        this.personInChargeName = personInChargeName;
    }   
    
    public String getPersonInChargeName() {
        return personInChargeName;
    }

    public void setPersonInChargeName(String personInChargeName) {
        this.personInChargeName = personInChargeName;
    } 
    
   ////////////////////////////////////////////////////////////////////
    
}
