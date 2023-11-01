/*
 * 개인프로젝트 - myAdapterData.java
 * 개발자 : 컴퓨터공학과 20193037 김권수
 * 20193037@office.deu.ac.kr
 */

package deu.soft.a20193037;

public class myAdapterData {

    private int id;
    private String title;
    private String content;
    private String created_date ;
    private String last_modified_date;

    public void setId(int id){this.id = id;}
    public void setTitle(String title){this.title = title;}
    public void setContent(String content){this.content = content;}
    public void setCreated_date(String created_date){this.created_date = created_date;}
    public void  setLast_modified_date(String last_modified_date){this.last_modified_date = last_modified_date;}

    public int  getId(){return this.id;}
    public String getTitle(){return this.title;}
    public String getContent(){return this.content;}
    public String getCreated_date(){return this.created_date;}
    public String getLast_modified_date(){return this.last_modified_date;}
}
