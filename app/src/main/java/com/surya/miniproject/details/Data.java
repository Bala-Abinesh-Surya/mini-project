package com.surya.miniproject.details;

import com.surya.miniproject.models.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class Data {
    // class contains all the data, to setup the application in the event of a reset

    // Constructor
    public Data() {

    }

    private String[] STAFF_DETAILS = new String[]{
            "Mr. Vishwanath Shenoi@@@M@@@CSE",
            "Mrs. Vidhya@@@F@@@CSE",
            "Mrs. Sumitra@@@F@@@CSE",
            "Mrs. Karpagavalli@@@F@@@CSE",
            "Mrs. Jothi Lakshmi@@@F@@@CSE",
            "Mrs. Thanammal Indu@@@F@@@CSE",
            "Mrs. Sasikala@@@F@@@CSE",
            "Mrs. Rejini@@@F@@@CSE",
    };

    private String[] CLASS_DETAILS = new String[] {
            "CSE-III-A@@@CSE@@@3@@@Mrs. Vidhya",
            "CSE-III-B@@@CSE@@@3@@@Mrs. Karpagavalli"
    };

    private String[] HOD_DETAILS = new String[]{
            "Mr. Vishwanath Shenoi@@@CSE"
    };

    // contains the classes and their teaching staffs list
    private Hashtable<String, ArrayList<String>> CLASS_STAFFS_LIST = new Hashtable<String, ArrayList<String>>() {
        {
            put("CSE-III-A", new ArrayList<String>() {
                {
                    add("Mrs. Vidhya");
                    add("Mr. Vishwanath Shenoi");
                    add("Mrs. Sumitra");
                    add("Mrs. Karpagavalli");
                    add("Mrs. Jothi Lakshmi");
                    add("Mrs. Thanammal Indu");
                }
            });

            put("CSE-III-B", new ArrayList<String>() {
                {
                    add("Mrs. Vidhya");
                    add("Mr. Vishwanath Shenoi");
                    add("Mrs. Sumitra");
                    add("Mrs. Karpagavalli");
                    add("Mrs. Jothi Lakshmi");
                    add("Mrs. Thanammal Indu");
                }
            });
        }
    };

    // contains the classes and the students list
    private Hashtable<String, ArrayList<Student>> CLASS_STUDENTS_LIST = new Hashtable<String, ArrayList<Student>>() {
        {
            {
                put("CSE-III-A", new ArrayList<Student>(){
                    {
                        add(new Student("ABHAYA DEV S K", "962319104001", "CSE-III-A"));
                        add(new Student("ABINILLA V A", "962319104003", "CSE-III-A"));
                        add(new Student("ABINO AMRIY E S", "962319104004", "CSE-III-A"));
                        add(new Student("ABIRAM S P", "962319104005", "CSE-III-A"));
                        add(new Student("ADITHYAN C P", "962319104006", "CSE-III-A"));
                        add(new Student("AHAMED YOONUS ZH", "962319104007", "CSE-III-A"));
                        add(new Student("AJAY M", "962319104008", "CSE-III-A"));
                        add(new Student("AJIN R", "962319104009", "CSE-III-A"));
                        add(new Student("AJITH KANNA T", "962319104010", "CSE-III-A"));
                        add(new Student("AKASH K B", "962319104011", "CSE-III-A"));
                        add(new Student("AKHIN C S", "962319104012", "CSE-III-A"));
                        add(new Student("AKSHAYA V T", "962319104013", "CSE-III-A"));
                        add(new Student("ALDRIN MOSES A", "962319104014", "CSE-III-A"));
                        add(new Student("AMAL KRISHNAN V S", "962319104015", "CSE-III-A"));
                        add(new Student("ANAND BOOJESH R S", "962319104016", "CSE-III-A"));
                        add(new Student("ANAND MICHAEL M", "962319104017", "CSE-III-A"));
                        add(new Student("ANAND RAJ D P", "962319104018", "CSE-III-A"));
                        add(new Student("ANANTHAPADMANABHAN G", "962319104019", "CSE-III-A"));
                        add(new Student("ANAZUDEEN M S", "962319104020", "CSE-III-A"));
                        add(new Student("ANEESH R A", "962319104021", "CSE-III-A"));
                        add(new Student("ANURAAM S", "962319104022", "CSE-III-A"));
                        add(new Student("ASHIK SINHA J", "962319104023", "CSE-III-A"));
                        add(new Student("ASHRITHA H A", "962319104024", "CSE-III-A"));
                        add(new Student("ASWIN K", "962319104025", "CSE-III-A"));
                        add(new Student("ASWINI S", "962319104026", "CSE-III-A"));
                        add(new Student("ATHIRA V A", "962319104027", "CSE-III-A"));
                        add(new Student("ATSHAYAA V", "962319104028", "CSE-III-A"));
                        add(new Student("BALA ABINESH SURYA B", "962319104029", "CSE-III-A"));
                        add(new Student("BALA CHANDRAN S", "962319104030", "CSE-III-A"));
                        add(new Student("BENISHA J", "962319104031", "CSE-III-A"));
                        add(new Student("BHAIRAVI M", "962319104032", "CSE-III-A"));
                        add(new Student("BHARATHI H V", "962319104033", "CSE-III-A"));
                        add(new Student("BINOY PAUL", "962319104034", "CSE-III-A"));
                        add(new Student("CHITHAMBARA SELVII G", "962319104035", "CSE-III-A"));
                        add(new Student("DEEPAK V", "962319104036", "CSE-III-A"));
                        add(new Student("EMPIRE E", "962319104037", "CSE-III-A"));
                        add(new Student("EZHIL ARASU E", "962319104038", "CSE-III-A"));
                        add(new Student("GOHUL J P", "962319104039", "CSE-III-A"));
                        add(new Student("GOKUL B", "962319104040", "CSE-III-A"));
                        add(new Student("GOKUL S", "962319104041", "CSE-III-A"));
                        add(new Student("GOKUL KRISHNA H S", "962319104042", "CSE-III-A"));
                        add(new Student("GOWTHAM K", "962319104043", "CSE-III-A"));
                        add(new Student("HARI HARAN S V", "962319104044", "CSE-III-A"));
                        add(new Student("HARSHAVARDINI V S", "962319104045", "CSE-III-A"));
                        add(new Student("JANCY T RAJ", "962319104046", "CSE-III-A"));
                        add(new Student("JAYA HARI PRASATH M", "962319104047", "CSE-III-A"));
                        add(new Student("JOEL PAUL", "962319104048", "CSE-III-A"));
                        add(new Student("JOOHIB PRAVITHA V T", "962319104049", "CSE-III-A"));
                        add(new Student("KANEESHMA LAJ K K", "962319104050", "CSE-III-A"));
                        add(new Student("KARTHIK C", "962319104051", "CSE-III-A"));
                        add(new Student("KISHANTHINI M", "962319104052", "CSE-III-A"));
                    }
                });

                put("CSE-III-B", new ArrayList<Student>(){
                    {
                        add(new Student("KRISHNA KANTH G V", "962319104053", "CSE-III-B"));
                        add(new Student("LAKSHMAN KARTHICK K S", "962319104054", "CSE-III-B"));
                        add(new Student("MAHA LINGAM M", "962319104055", "CSE-III-B"));
                        add(new Student("MANI K V", "962319104056", "CSE-III-B"));
                        add(new Student("MEENUPRIYA S S", "962319104057", "CSE-III-B"));
                        add(new Student("MOTHIKA R", "962319104058", "CSE-III-B"));
                        add(new Student("NAGADEVI M", "962319104059", "CSE-III-B"));
                        add(new Student("NAMIT A R", "962319104060", "CSE-III-B"));
                        add(new Student("NARESH K", "962319104061", "CSE-III-B"));
                        add(new Student("NAVIN M", "962319104062", "CSE-III-B"));
                        add(new Student("NIVETHA R", "962319104063", "CSE-III-B"));
                        add(new Student("NIVETHITHA M", "962319104064", "CSE-III-B"));
                        add(new Student("PADMA PRIYA T R", "962319104065", "CSE-III-B"));
                        add(new Student("PADMESH G N", "962319104066", "CSE-III-B"));
                        add(new Student("PAVITHRA R", "962319104067", "CSE-III-B"));
                        add(new Student("PRANAV R", "962319104068", "CSE-III-B"));
                        add(new Student("PRANESH P", "962319104069", "CSE-III-B"));
                        add(new Student("PRAVEENAPTHI R K", "962319104070", "CSE-III-B"));
                        add(new Student("PRAVIN M", "962319104071", "CSE-III-B"));
                        add(new Student("RAM KISHORE J", "962319104072", "CSE-III-B"));
                        add(new Student("REBISHA S", "962319104073", "CSE-III-B"));
                        add(new Student("SABARI SHREE R", "962319104074", "CSE-III-B"));
                        add(new Student("SABARISH DEEPAK R", "962319104075", "CSE-III-B"));
                        add(new Student("SAIRAJ P", "962319104076", "CSE-III-B"));
                        add(new Student("SAJU SHAGAAR M M", "962319104077", "CSE-III-B"));
                        add(new Student("SANDHIRA SANKAY", "962319104078", "CSE-III-B"));
                        add(new Student("SARANYA R", "962319104079", "CSE-III-B"));
                        add(new Student("SHALINI V", "962319104080", "CSE-III-B"));
                        add(new Student("SHARAN B", "962319104081", "CSE-III-B"));
                        add(new Student("SHIBU A", "962319104082", "CSE-III-B"));
                        add(new Student("SHINUMON S B", "962319104083", "CSE-III-B"));
                        add(new Student("SHIVA KOWSHIK I", "962319104084", "CSE-III-B"));
                        add(new Student("SNEHA K S", "962319104085", "CSE-III-B"));
                        add(new Student("SREE LAKSHMI K J", "962319104086", "CSE-III-B"));
                        add(new Student("SREE VARSHA S V", "962319104088", "CSE-III-B"));
                        add(new Student("SRIDEVI M I", "962319104089", "CSE-III-B"));
                        add(new Student("SRUANANI C B", "962319104090", "CSE-III-B"));
                        add(new Student("SUDHAN I", "962319104091", "CSE-III-B"));
                        add(new Student("SUNIL I", "962319104092", "CSE-III-B"));
                        add(new Student("SUTHISH KUMAR S", "962319104093", "CSE-III-B"));
                        add(new Student("THANYA S S", "962319104094", "CSE-III-B"));
                        add(new Student("VAISHAG S B", "962319104095", "CSE-III-B"));
                        add(new Student("VARSHA K P", "962319104096", "CSE-III-B"));
                        add(new Student("VARUN KRISHNA K B", "962319104097", "CSE-III-B"));
                        add(new Student("VASANTHA GURU R", "962319104098", "CSE-III-B"));
                        add(new Student("VENKADESH S R", "962319104099", "CSE-III-B"));
                        add(new Student("VIBISHA K B", "9623191040100", "CSE-III-B"));
                        add(new Student("VISHAL R", "962319104101", "CSE-III-B"));
                        add(new Student("VISHNU MURUGAN", "962319104102", "CSE-III-B"));
                        add(new Student("YOGESH P", "962319104103", "CSE-III-B"));
                    }
                });
            }
        }
    };

    // developers details
    // developer_name@@@about@@@reg_no
    public String[] DEVELOPER_DETAILS = new String[]{
            "Dr. Vishwanath Shenoi@@@Are we building the right product or the product right?@@@The Head of the Department@@@Project Guide",
            "Abinilla VA@@@Remember sometimes not getting what you want is a stroke of luck!@@@962319104003@@@UI Designer",
            "Harshavardini VS@@@Cherish what you have now@@@962319104045@@@Lead Tester",
            "Abino Amriy ES@@@Be the sky with no limits...@@@962319104004@@@Scrum Master",
            "Bala Abinesh Surya B@@@```Butter Bala```@@@962319104029@@@Developer"
    };

    // getter and setter methods
    public String[] getSTAFF_DETAILS() {
        return this.STAFF_DETAILS;
    }

    public void setSTAFF_DETAILS(String[] STAFF_DETAILS) {
        this.STAFF_DETAILS = STAFF_DETAILS;
    }

    public String[] getCLASS_DETAILS() {
        return CLASS_DETAILS;
    }

    public void setCLASS_DETAILS(String[] CLASS_DETAILS) {
        this.CLASS_DETAILS = CLASS_DETAILS;
    }

    public Hashtable<String, ArrayList<String>> getCLASS_STAFFS_LIST() {
        return CLASS_STAFFS_LIST;
    }

    public void setCLASS_STAFFS_LIST(Hashtable<String, ArrayList<String>> CLASS_STAFFS_LIST) {
        this.CLASS_STAFFS_LIST = CLASS_STAFFS_LIST;
    }

    public Hashtable<String, ArrayList<Student>> getCLASS_STUDENTS_LIST() {
        return CLASS_STUDENTS_LIST;
    }

    public void setCLASS_STUDENTS_LIST(Hashtable<String, ArrayList<Student>> CLASS_STUDENTS_LIST) {
        this.CLASS_STUDENTS_LIST = CLASS_STUDENTS_LIST;
    }

    public String[] getHOD_DETAILS() {
        return HOD_DETAILS;
    }

    public void setHOD_DETAILS(String[] HOD_DETAILS) {
        this.HOD_DETAILS = HOD_DETAILS;
    }
}
