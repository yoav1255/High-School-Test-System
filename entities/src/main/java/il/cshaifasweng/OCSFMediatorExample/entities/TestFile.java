package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TestFile")
public class TestFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    @Lob
    private byte[] fileData;
    private String studentID;
    private String TestCode;

    // Constructors, getters, setters

    public TestFile(){
        this.fileData = null;
        this.fileName = null;
        this.TestCode = null;
        this.studentID = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getTestCode() {
        return TestCode;
    }

    public void setTestCode(String testCode) {
        TestCode = testCode;
    }
}



