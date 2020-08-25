package com.hmtmcse.oc.test.data.ftest.project;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataBank {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++){
            System.out.println(UUID.randomUUID().toString().toUpperCase());
        }
    }

    public static List<Identity> getIdentity(){
        List<Identity> list = new ArrayList<>();
        return list;
    }

    public static List<Course> getCourse(){
        List<Course> list = new ArrayList<>();
        return list;
    }

    public static List<Department> getDepartment(){
        List<Department> list = new ArrayList<>();
        return list;
    }


//    3B970BBF-B6CE-4C7F-896D-539C1998373D
//    2C6AB840-085C-4332-8368-E27E02E34B19
//    8BC8D2D9-C5B6-4A44-9B79-295F9226E8AE
//    CC9100D2-E006-41DE-9F37-A3BCFBAED16A
//    95C7110D-3D8F-4D0F-8597-770F4BA915E7
//    B2394CCF-8D96-44BE-ABC3-80F1A1E17453
//    4A706368-A4B8-44D4-BC1F-7A803DBBE686
//    5043289B-DAF7-46B9-AF43-471E665ECD87
//    388D757E-2A1D-43EC-8428-41A344B18E58
//    0BA27F20-1516-4329-89A8-E1A7D6A900AF
//    2D0C5012-F2DC-455B-8E74-85EF13706283
//    0117C55D-7A16-4B7D-B3AB-89D5B2C7BCD9
//    94BDC08D-E51B-45E4-9781-711D99F080E5
//    5DEB30F7-DF1A-4787-A082-1374158F3EA2
//    ED36B5A5-1B58-4FE0-8160-E46AF1AD64E8
//    231D1518-D85D-46F3-8981-79FCE97B9FDD
//    122BBAD2-D8D7-4AF0-A187-488179A3E535
//    8481485B-B17D-44D3-B4BA-F08F8AB6AD3E
//    0274B3D8-A27E-4005-8002-803AF909BE22
//    147233E8-BC00-44F0-86ED-2EF5C8EE731E
//    ED538629-2CFB-47D6-9AF6-931A0F38D8DE
//    D8A49629-AD04-4D6E-853F-FBEDCC7F2B37
//    6641216F-56A5-4BC0-86E1-E60623E31399
//    C6AE0105-3C54-4E65-93E9-9944D7C18C9E
//    3A5C4FD0-A4DD-4E96-A529-17633A8813BD
//    192836B1-9614-4495-84ED-2CF70A69E083
//    6A0E900A-AEB2-49D3-9772-B9C1D22E1A5E
//    D2557DA0-1BF1-42F8-85EC-A3BB6CCAFE20
//    44D85E2C-C2B5-4DA0-A8BF-F3EA52ED3D2E
//    79AAF9DC-4420-41C8-9075-2B519B086F3C
//    3A6F0A65-B8DF-486D-A010-D7D6D3A3E3C5
//    9C243737-9C19-4C57-B40F-E1CFE837D8FD
//    FEA56B81-DB3F-4864-BF80-D643C46BE09A
//    7879E1C4-93C6-477A-8ECC-34B62C72CE6A
//    2DA8920C-2731-44D2-98C7-2BCC27618468
//    08415C33-2E36-4F60-8FDF-74C9C779DB52
//    6EE47C87-3D35-4E28-9B39-08AA8523F4F6
//    D1729B8B-C844-46DB-B56B-070CEF1F9E36
//    FB81D673-E681-4B00-B3CC-2CFB5ADABAB1
//    1B74DEE6-0FC1-42EB-9380-8A6347BC38F2
//    69EF673D-6059-491D-AA9E-51A04A220744
//    A7A24EA5-F762-4609-9D1E-275FBBF2D024
//    A98385DE-7B1D-41B1-ACA5-72E0FCF1F78B
//    4E0264D6-53F2-4EE5-B3F2-E67B36430225
//    10F55DD8-386C-4AB1-8E74-CE38EED3F7EA
//    86FC918B-B2F1-4347-9335-101C2EBA7947
//    CEBED017-BE27-4F18-8499-07850DE3CC8F
//    63688E84-4816-4281-873E-D43356F6A4F1
//    79FCB580-2318-4859-8C82-2DF1AA1F9E5F
//    FCEFA958-3CF8-45B8-B119-9788759B2395
//    DE03005A-82EE-4F93-B482-896935060D6A
//    BE0EA92E-07E5-40CC-89F5-9D067EA88365
//    EAD45EA1-AE29-47B8-B5AC-C8921154B52C
//    0B7F6249-42EF-48F7-A01F-3933AC652A09
//    32502453-1568-4B91-8244-813C92871F39
//    21E099C2-BF3E-4B06-9FDA-50EAFD21D919
//    1A91263A-394F-4C43-8B4D-EAB39496F26C
//    A675CA35-DD03-4715-B7D2-F10383FEAF14
//    04E4CB23-AC4A-45CF-86C4-580262324A08
//    4F25A1DC-5F07-4C28-B649-2C91B1BA13D6
//    1F29FB4A-5B03-4C9D-85B6-44C13AFA4D44
//    00E18EEC-0A78-4101-9835-464D92D23263
//    644444B1-061D-48A5-A79E-A9994DD0EB8F
//    32D67965-FC6B-4234-95A6-0FF095AE5DF6
//    F40BDEDE-0A9C-40CC-A6E0-408D691978EC
//    EADB6E46-6F05-4B89-AD43-9D253C53DB66
//    9CE2AB81-3414-421C-B208-A94524D3DA9E
//    4572B5D5-F312-4CC8-AFDA-EC0E9DAB26C7
//    77D043AF-D762-4123-A402-8CF3F861A827
//    1A9D2CFA-5602-4142-A14B-3EE0D5865A42
//    B19921FD-EE53-4F2E-8E2C-8C62D01376D5
//    67AB6E5F-F486-4730-ADA8-8C0F7A2CB842
//    B6DBDF82-34D9-48BF-8BBE-1078E5015B68
//    7C035513-63BF-4DDC-B203-AA452071DDE4
//    FEE9CC74-0D2A-4663-B20B-43A5A85793F4
//    524C2982-2130-4A8D-9BAC-6F4B85442F2B
//    358301F6-76B4-428D-9956-ECD66C5AFE6F
//    F92D1F88-74A5-4E7A-96BB-18F7410EDB4C
//    F6BF249E-B63B-4B86-A7A2-12B8898AF644
//    56985E94-9CBB-484D-8EFE-F335ED9930F2
//    E44C761D-665E-4BA1-9717-76FDA322DE0F
//    8A7F6E18-D121-4EBD-87A7-A39E97B01E69
//    7C24AEE1-D8D4-4CF7-A970-2F2B4742E12B
//    A42A507D-66B0-427E-839E-A4F5F26717F2
//    9B35E023-1971-4BE8-BD41-0FFF57C83A58
//    458A9C4D-BDF0-4279-A99D-F5A0BB83C463
//    A60B62F1-0E0B-463C-9AE5-F530153AF325
//    214F53E4-899B-47F2-861F-59B01ECB4895
//    4AB9CB7A-485F-462D-A6E8-856F21A4B9F6
//    3988E665-589B-47F7-90FC-B25554B061E3
//    85D23737-3971-458D-BDEE-EAA0C5796648
//    5F4D8466-F65F-46CE-A884-6C3562FFD4FD
//    1460D6F3-E3D9-4868-BE5A-CDAB1E250874
//    F28BD77B-D08E-491D-B79C-5FFBC11877E4
//    949607FB-4DA9-48C8-8921-56674C27B8C7
//    B77E078A-CEBB-452E-965C-9949958A4E41
//    4320A805-9193-4BEC-8FC6-92E72863001C
//    47B190DD-D2ED-4839-9B66-B526E5DE01F5
//    8E87742F-E34A-401F-B9D1-5676BA8F598C
//    92077102-1308-466E-B60F-F8178E9DD3BA

}